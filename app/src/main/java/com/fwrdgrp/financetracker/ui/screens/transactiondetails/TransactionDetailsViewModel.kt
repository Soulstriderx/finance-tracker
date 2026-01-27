package com.fwrdgrp.financetracker.ui.screens.transactiondetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.fwrdgrp.financetracker.data.model.main.Transaction
import com.fwrdgrp.financetracker.data.repo.Repo
import com.fwrdgrp.financetracker.ui.screens.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class TransactionDetailsViewModel @Inject constructor(
    val repo: Repo,
    savedStateHandle: SavedStateHandle
) : BaseViewModel()  {
    val uid = savedStateHandle.get<String>("uid")!!
    private val _finish = MutableSharedFlow<Unit>()
    val finish = _finish.asSharedFlow()

    private val _transaction = MutableStateFlow<Transaction?>(null)
    val transaction = _transaction.asStateFlow()

    init {
        fetchTransactionById(uid)
    }

    fun fetchTransactionById(uid: String) {
        viewModelScope.launch {
            safeApiCall {
                repo.fetchTransactionById(uid).let {
                    _transaction.value = it
                }
            }
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            safeApiCall {
                repo.deleteTransaction(uid, transaction).let {
                    _finish.emit(Unit)
                }
            }
        }
    }
}