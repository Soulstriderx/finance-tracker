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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
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
        val user = authService.getCurrentUser() ?: throw Exception("User doesn't exist")
        val newBalance = if (transaction.type == TransactionType.Expense) {
            (user.balance.toDouble() - transaction.amount.toDouble()).toString()
        } else {
            (user.balance.toDouble() + transaction.amount.toDouble()).toString()
        }
        val userDoc = dbRef.document(user.uid)
        val transactionRef = userDoc
            .collection("transactions")
            .document()

        val transactionWithId = transaction.copy(
            uid = transactionRef.id
        )

        userDoc.update("balance", newBalance)
        transactionRef.set(transactionWithId.toMap()).await()
    }

    override suspend fun editTransaction(transaction: TransactionReq) {
        val user = authService.getCurrentUser() ?: throw Exception("User doesn't exist")

        val oldAmount = transaction.amount.toDouble()
        val newAmount = transaction.newAmount.toDouble()

        val userDoc = dbRef.document(user.uid)
        val updatedAmount = getNewBalance(
            oldAmount,
            newAmount,
            transaction.type,
            transaction.newType
        )
        val newBalance = user.balance.toDouble() + updatedAmount



        userDoc.update("balance", newBalance.toString())
        userDoc.collection("transactions").document(transaction.uid)
            .update(transaction.toEditMap())
    }

    override suspend fun deleteTransaction(uid: String) {
        val user = authService.getCurrentUser() ?: throw Exception("User doesn't exist")
        val transactionRef = dbRef
            .document(user.uid)
            .collection("transactions")

        transactionRef.document(uid).delete().await()
    }


    override suspend fun fetchMyTransactions(): Flow<List<Transaction>> = callbackFlow {
        val user = authService.getCurrentUser() ?: throw Exception("User doesn't exist")
        val snapshot = dbRef
            .document(user.uid)
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
        val user = authService.getCurrentUser() ?: throw Exception("User doesn't exist")
        val snapshot = dbRef
            .document(user.uid)
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
        val user = authService.getCurrentUser() ?: throw Exception("User doesn't exist")

        val (startMillis, endMillis) = if (isStats) {
            calculateStatsDateRange(filter, referenceDate)
        } else {
            calculateDateRange(filter, referenceDate)
        }
        val startTimestamp = Timestamp(Date(startMillis))
        val endTimestamp = Timestamp(Date(endMillis))

        val snapshot = dbRef
            .document(user.uid)
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