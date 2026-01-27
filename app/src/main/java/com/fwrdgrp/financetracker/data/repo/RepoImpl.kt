package com.fwrdgrp.financetracker.data.repo

import com.fwrdgrp.financetracker.data.datautils.calculateDateRange
import com.fwrdgrp.financetracker.data.datautils.calculateStatsDateRange
import com.fwrdgrp.financetracker.data.datautils.getNewBalance
import com.fwrdgrp.financetracker.data.enum.DateFilter
import com.fwrdgrp.financetracker.data.enum.TransactionType
import com.fwrdgrp.financetracker.data.model.main.Transaction
import com.fwrdgrp.financetracker.data.model.main.User
import com.fwrdgrp.financetracker.data.model.request.LoginReq
import com.fwrdgrp.financetracker.data.model.request.RegisterReq
import com.fwrdgrp.financetracker.data.model.request.TransactionReq
import com.fwrdgrp.financetracker.service.FirebaseAuthService
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import jakarta.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import java.util.Date

class RepoImpl @Inject constructor(
    private val authService: FirebaseAuthService,
    firestore: FirebaseFirestore
) : Repo {
    private val dbRef = firestore.collection("users")

    private fun requireUser(): User {
        return authService.getCurrentUser() ?: throw IllegalStateException("User not logged in")
    }

    private fun userDoc(): DocumentReference {
        return dbRef.document(requireUser().uid)
    }

    override suspend fun register(user: RegisterReq) {
        val userUid = authService.register(user.email, user.password)
        val userWithUid = user.copy(uid = userUid)
        dbRef.document(userUid).set(userWithUid.toMap()).await()
    }

    override suspend fun login(req: LoginReq): User {
        return authService.login(req.email, req.password).let { user ->
            dbRef.document(user.uid)
                .get()
                .await()
                .toObject(User::class.java)
                ?: throw IllegalStateException("User doesn't exist")
        }
    }

    override suspend fun fetchUser(uid: String): User {
        val snapshot = dbRef.document(uid).get().await()

        return snapshot.toObject(User::class.java)
            ?: throw IllegalStateException("User doesn't exist")
    }

    override suspend fun addTransaction(transaction: TransactionReq) {
        val user = requireUser()
        val newBalance = if (transaction.type == TransactionType.Expense) {
            (user.balance.toDouble() - transaction.amount.toDouble()).toString()
        } else {
            (user.balance.toDouble() + transaction.amount.toDouble()).toString()
        }
        val transactionRef = userDoc()
            .collection("transactions")
            .document()

        val transactionWithId = transaction.copy(
            uid = transactionRef.id
        )

        userDoc().update("balance", newBalance)
        transactionRef.set(transactionWithId.toMap()).await()
    }

    override suspend fun editTransaction(transaction: TransactionReq) {
        val user = requireUser()

        val oldAmount = transaction.amount.toDouble()
        val newAmount = transaction.newAmount.toDouble()

        val updatedAmount = getNewBalance(
            oldAmount,
            newAmount,
            transaction.type,
            transaction.newType
        )
        val newBalance = user.balance.toDouble() + updatedAmount

        userDoc().update("balance", newBalance.toString())
        userDoc().collection("transactions").document(transaction.uid)
            .update(transaction.toEditMap())
    }

    override suspend fun deleteTransaction(uid: String, transaction: Transaction) {
        val user = requireUser()
        val transactionRef = userDoc().collection("transactions")
        if (transaction.type == TransactionType.Expense) {
            val balance = (user.balance.toDouble() + transaction.amount.toDouble()).toString()
            userDoc().update("balance", balance).await()
        } else {
            val balance = (user.balance.toDouble() - transaction.amount.toDouble()).toString()
            userDoc().update("balance", balance).await()
        }

        transactionRef.document(uid).delete().await()
    }

    override suspend fun fetchCustomCategories(): List<String> {
        val categoriesDoc = userDoc().collection("custom_category").document("categories")
        val snapshot = categoriesDoc.get().await()
        return snapshot.get("categories") as? List<String> ?: emptyList()
    }

    override suspend fun addCustomCategory(customCat: String) {
        val categoriesDoc = userDoc().collection("custom_category").document("categories")
        categoriesDoc.set(
            mapOf("categories" to FieldValue.arrayUnion(customCat)),
            SetOptions.merge()
        ).await()
    }

    override suspend fun deleteCustomCategory(customCat: String) {
        val categoriesDoc = userDoc().collection("custom_category").document("categories")

        categoriesDoc.update(
            "categories",
            FieldValue.arrayRemove(customCat)
        ).await()

        val query = userDoc().collection("transactions")
            .whereEqualTo("customCategory", customCat)

        query.get().await().forEach { doc ->
            doc.reference.update("customCategory", null)
        }
    }

    override suspend fun fetchMyTransactions(): Flow<List<Transaction>> = callbackFlow {
        val snapshot = userDoc()
            .collection("transactions")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val transactions = snapshot?.documents?.mapNotNull {
                    it.toObject(Transaction::class.java)
                } ?: emptyList()

                trySend(transactions)
            }

        awaitClose { snapshot.remove() }
    }

    override suspend fun fetchTransactionById(uid: String): Transaction {
        val snapshot = userDoc()
            .collection("transactions")
            .document(uid).get().await()

        return snapshot.toObject(Transaction::class.java)
            ?: throw IllegalStateException("Transaction doesn't exist")
    }

    override suspend fun fetchTransactionsByDate(
        filter: DateFilter,
        referenceDate: Calendar,
        isStats: Boolean
    ): Flow<List<Transaction>> = callbackFlow {
        val (startMillis, endMillis) = if (isStats) {
            calculateStatsDateRange(filter, referenceDate)
        } else {
            calculateDateRange(filter, referenceDate)
        }
        val startTimestamp = Timestamp(Date(startMillis))
        val endTimestamp = Timestamp(Date(endMillis))

        val snapshot = userDoc()
            .collection("transactions")
            .whereGreaterThanOrEqualTo("timestamp", startTimestamp)
            .whereLessThan("timestamp", endTimestamp)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val transactions = snapshot?.documents?.mapNotNull {
                    it.toObject(Transaction::class.java)
                } ?: emptyList()
                trySend(transactions)
            }
        awaitClose { snapshot.remove() }
    }
}