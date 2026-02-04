package com.fwrdgrp.financetracker.data.repo

import com.fwrdgrp.financetracker.data.datautils.calculateBudgetUpdates
import com.fwrdgrp.financetracker.data.datautils.calculateBudgetUpdatesForEdit
import com.fwrdgrp.financetracker.data.datautils.calculateDateRange
import com.fwrdgrp.financetracker.data.datautils.calculateMetricChanges
import com.fwrdgrp.financetracker.data.datautils.calculateStatsDateRange
import com.fwrdgrp.financetracker.data.datautils.createBillTransaction
import com.fwrdgrp.financetracker.data.datautils.createIncomeTransaction
import com.fwrdgrp.financetracker.data.datautils.createPaymentRecord
import com.fwrdgrp.financetracker.data.datautils.plus
import com.fwrdgrp.financetracker.data.datautils.updateUserMetricsAndBudget
import com.fwrdgrp.financetracker.data.enum.DateFilter
import com.fwrdgrp.financetracker.data.model.main.Bill
import com.fwrdgrp.financetracker.data.model.main.Transaction
import com.fwrdgrp.financetracker.data.model.main.User
import com.fwrdgrp.financetracker.data.model.request.BillReq
import com.fwrdgrp.financetracker.data.model.request.LoginReq
import com.fwrdgrp.financetracker.data.model.request.ProfileUpdateReq
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

    override suspend fun updateUser(update: ProfileUpdateReq) {
        userDoc().update(update.toMap())
    }

    override suspend fun addTransaction(transaction: TransactionReq) {
        val user = requireUser()

        val metricChanges = transaction.type.calculateMetricChanges(
            amount = transaction.amount.toDouble()
        )

        val budgetUpdates = transaction.category.calculateBudgetUpdates(
            amount = transaction.amount.toDouble(),
            type = transaction.type,
            timestamp = transaction.timestamp ?: Timestamp.now(),
            budget = user.budget,
            isAdding = true
        )

        val transactionRef = if (transaction.uid.isEmpty()) {
            userDoc().collection("transactions").document()
        } else {
            userDoc().collection("transactions").document(transaction.uid)
        }

        val transactionWithId = transaction.copy(uid = transactionRef.id)

        userDoc().updateUserMetricsAndBudget(
            metricChanges = metricChanges,
            budgetUpdates = budgetUpdates,
            currentBalance = user.balance.toDouble(),
            currentLifetimeSpend = user.lifetimeSpend.toDouble(),
            currentLifetimeIncome = user.lifetimeIncome.toDouble()
        )

        transactionRef.set(transactionWithId.toMap()).await()
    }

    override suspend fun editTransaction(transaction: TransactionReq) {
        val user = requireUser()

        val oldAmount = transaction.amount.toDouble()
        val newAmount = transaction.newAmount.toDouble()

        val oldMetricChanges = transaction.type.calculateMetricChanges(
            amount = oldAmount,
            isAdding = false
        )

        val newMetricChanges = transaction.newType.calculateMetricChanges(
            amount = newAmount,
            isAdding = true
        )

        val totalMetricChanges = oldMetricChanges + newMetricChanges

        val budgetUpdates = transaction.category.calculateBudgetUpdatesForEdit(
            oldAmount = oldAmount,
            oldType = transaction.type,
            oldTimestamp = transaction.timestamp ?: Timestamp.now(),
            newCategory = transaction.newCategory,
            newAmount = newAmount,
            newType = transaction.newType,
            newTimestamp = transaction.newTimestamp ?: transaction.timestamp ?: Timestamp.now(),
            budget = user.budget
        )

        userDoc().updateUserMetricsAndBudget(
            metricChanges = totalMetricChanges,
            budgetUpdates = budgetUpdates,
            currentBalance = user.balance.toDouble(),
            currentLifetimeSpend = user.lifetimeSpend.toDouble(),
            currentLifetimeIncome = user.lifetimeIncome.toDouble()
        )
        val transactionRef = userDoc()
            .collection("transactions")
            .document(transaction.uid)

        transactionRef.update(transaction.toEditMap()).await()
    }

    override suspend fun deleteTransaction(uid: String, transaction: Transaction) {
        val user = requireUser()
        val transactionRef = userDoc().collection("transactions")
        val metricChanges = transaction.type.calculateMetricChanges(
            amount = transaction.amount.toDouble(),
            isAdding = false
        )

        val budgetUpdates = transaction.category.calculateBudgetUpdates(
            amount = transaction.amount.toDouble(),
            type = transaction.type,
            timestamp = transaction.timestamp ?: Timestamp.now(),
            budget = user.budget,
            isAdding = false
        )
        userDoc().updateUserMetricsAndBudget(
            metricChanges = metricChanges,
            budgetUpdates = budgetUpdates,
            currentBalance = user.balance.toDouble(),
            currentLifetimeSpend = user.lifetimeSpend.toDouble(),
            currentLifetimeIncome = user.lifetimeIncome.toDouble()
        )

        transactionRef.document(uid).delete().await()
    }

    override suspend fun fetchCustomCategories(): List<String> {
        val categoriesDoc = userDoc()
            .collection("custom_category")
            .document("categories")
        val snapshot = categoriesDoc.get().await()
        return snapshot.get("categories") as? List<String> ?: emptyList()
    }

    override suspend fun addCustomCategory(customCat: String) {
        val categoriesDoc = userDoc()
            .collection("custom_category")
            .document("categories")
        categoriesDoc.set(
            mapOf("categories" to FieldValue.arrayUnion(customCat)),
            SetOptions.merge()
        ).await()
    }

    override suspend fun deleteCustomCategory(customCat: String) {
        val categoriesDoc = userDoc()
            .collection("custom_category")
            .document("categories")

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

    override suspend fun fetchTransactionsByRange(
        start: Long,
        end: Long
    ): List<Transaction> {
        val snapshot = userDoc().collection("transactions")
            .whereGreaterThanOrEqualTo("timestamp", Timestamp(Date(start)))
            .whereLessThan("timestamp", Timestamp(Date(end)))
            .orderBy("timestamp", Query.Direction.DESCENDING).get().await()

        return snapshot.documents.mapNotNull {
            it.toObject(Transaction::class.java)
        }
    }

    override suspend fun budgetRollover(newTimestamp: Timestamp) {
        userDoc().update(
            mapOf(
                "budget.foodUsed" to "0",
                "budget.transportationUsed" to "0",
                "budget.entertainmentUsed" to "0",
                "budget.householdUsed" to "0",
                "budget.refresh" to newTimestamp
            )
        ).await()
    }

    override suspend fun incomeRollover(newTimestamp: Timestamp) {
        val user = requireUser()

        addTransaction(createIncomeTransaction(user))
        userDoc().update(mapOf("monthlyIncome.payday" to newTimestamp))
    }

    override suspend fun fetchBills(): List<Bill> {
        val snapshot = userDoc().collection("bills").get().await()
        return snapshot.documents.mapNotNull {
            it.toObject(Bill::class.java)
        }
    }

    override suspend fun fetchBillById(uid: String): Bill {
        val snapshot = userDoc().collection("bills")
            .document(uid).get().await()

        return snapshot.toObject(Bill::class.java)
            ?: throw Exception("Bill doesn't exist")
    }

    override suspend fun addBill(bill: BillReq) {
        val billRef = userDoc()
            .collection("bills")
            .document()

        val billWithId = bill.copy(uid = billRef.id)

        billRef.set(billWithId.toMap()).await()
    }

    override suspend fun payBill(bill: Bill, newTimestamp: Timestamp) {
        val docUid = userDoc().collection("transactions").document()
        val transaction = createBillTransaction(bill, docUid.id)
        addTransaction(transaction)
        val record = createPaymentRecord(docUid.id, bill)
        userDoc().collection("bills").document(bill.uid)
            .update(
                mapOf(
                    "paymentHistory" to FieldValue.arrayUnion(record.toMap()),
                    "nextDue" to newTimestamp
                )
            )
    }

    override suspend fun deleteBill(uid: String) {
        userDoc().collection("bills").document(uid).delete().await()
    }

    override suspend fun editBill(bill: BillReq) {
        userDoc()
            .collection("bills")
            .document(bill.uid)
            .update(bill.toEditMap()).await()
    }

    override suspend fun deletePaymentRecord(
        bill: Bill,
        paymentUid: String
    ) {
        val payment = bill.paymentHistory.find { it.uid == paymentUid }
            ?: throw Exception("Payment record not found")

        val transactionSnapshot = userDoc().collection("transactions")
            .document(paymentUid).get().await()

        val transaction = transactionSnapshot.toObject(Transaction::class.java)
            ?: throw Exception("Transaction not found")

        deleteTransaction(paymentUid, transaction)

        val updatedHistory = bill.paymentHistory.filter { it.uid != paymentUid }
        userDoc().collection("bills").document(bill.uid)
            .update("paymentHistory", updatedHistory.map { it.toMap() }).await()
    }
}