package com.fwrdgrp.financetracker.data.repo


import com.fwrdgrp.financetracker.data.enum.DateFilter
import com.fwrdgrp.financetracker.data.model.main.Transaction
import com.fwrdgrp.financetracker.data.model.main.User
import com.fwrdgrp.financetracker.data.model.request.EditTransactionReq
import com.fwrdgrp.financetracker.data.model.request.LoginReq
import com.fwrdgrp.financetracker.data.model.request.RegisterReq
import com.fwrdgrp.financetracker.data.model.request.TransactionReq
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.util.Calendar

interface Repo {
    //AuthRepo
    suspend fun register(user: RegisterReq)
    suspend fun login(req: LoginReq): User

    //UserRepo
    suspend fun fetchUser(uid: String): User

    //TransactionRepo
    suspend fun addTransaction(transaction: TransactionReq)
    suspend fun fetchMyTransactions(): Flow<List<Transaction>>

}