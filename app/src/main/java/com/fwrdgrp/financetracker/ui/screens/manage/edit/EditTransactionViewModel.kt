package com.fwrdgrp.financetracker.ui.screens.manage.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.fwrdgrp.financetracker.data.model.main.Transaction
import com.fwrdgrp.financetracker.data.model.request.TransactionReq
import com.fwrdgrp.financetracker.data.repo.Repo
import com.fwrdgrp.financetracker.ui.screens.manage.base.BaseManageViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class EditTransactionViewModel @Inject constructor(
    val repo: Repo,
    savedStateHandle: SavedStateHandle
) : BaseManageViewModel() {
    val uid = savedStateHandle.get<String>("uid")!!

    private val _transaction = MutableStateFlow<Transaction?>(null)
    val transaction = _transaction.asStateFlow()

    init {
        fetchTransactionById(uid)
        fetchCustomCategories()

    }

    fun fetchCustomCategories() {
        viewModelScope.launch {
            safeApiCall {
                repo.fetchCustomCategories().let { customCategory ->
                    _customCategories.update { customCategory }
                }
            }
        }
    }

    fun addCustomCategory(category: String) {
        viewModelScope.launch {
            safeApiCall {
                repo.addCustomCategory(category).let {
                    fetchCustomCategories()
                }
            }
        }
    }

    fun deleteCustomCategory(category: String) {
        viewModelScope.launch {
            safeApiCall {
                repo.deleteCustomCategory(category).let {
                    fetchCustomCategories()
                }
            }
        }
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

    fun updateTransaction(transactionReq: TransactionReq) {
        viewModelScope.launch {
            safeApiCall {
                validateTransaction(transactionReq, true)
                repo.editTransaction(transactionReq).let {
                    _finish.emit(Unit)
                }
            }
        }
    }
}