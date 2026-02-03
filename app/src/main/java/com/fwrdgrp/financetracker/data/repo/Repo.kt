package com.fwrdgrp.financetracker.data.repo


import com.fwrdgrp.financetracker.data.enum.DateFilter
import com.fwrdgrp.financetracker.data.model.main.Transaction
import com.fwrdgrp.financetracker.data.model.main.User
import com.fwrdgrp.financetracker.data.model.request.LoginReq
import com.fwrdgrp.financetracker.data.model.request.ProfileUpdateReq
import com.fwrdgrp.financetracker.data.model.request.RegisterReq
import com.fwrdgrp.financetracker.data.model.request.TransactionReq
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

interface Repo {
    //AuthRepo
    suspend fun register(user: RegisterReq)
    suspend fun login(req: LoginReq): User

    //UserRepo
    suspend fun fetchUser(uid: String): User
    suspend fun updateUser(update: ProfileUpdateReq)

    //TransactionRepo
    suspend fun addTransaction(transaction: TransactionReq)
    suspend fun editTransaction(transaction: TransactionReq)
    suspend fun deleteTransaction(uid: String, transaction: Transaction)
    suspend fun fetchCustomCategories(): List<String>
    suspend fun addCustomCategory(customCat: String)
    suspend fun deleteCustomCategory(customCat: String)
    suspend fun fetchMyTransactions(): Flow<List<Transaction>>
    suspend fun fetchTransactionById(uid: String): Transaction

    suspend fun fetchTransactionsByDate(
        filter: DateFilter,
        referenceDate: Calendar,
        isStats: Boolean
    ): Flow<List<Transaction>>

    suspend fun budgetRollover(newTimestamp: Timestamp)
}