package com.fwrdgrp.financetracker.ui.screens.manage.add

import androidx.lifecycle.viewModelScope
import com.fwrdgrp.financetracker.data.model.request.TransactionReq
import com.fwrdgrp.financetracker.data.repo.Repo
import com.fwrdgrp.financetracker.ui.screens.manage.base.BaseManageViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    val repo: Repo
) : BaseManageViewModel() {

    init {
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

    fun addTransaction(transaction: TransactionReq) {
        viewModelScope.launch {
            safeApiCall {
                repo.addTransaction(transaction).let {
                    _finish.emit(Unit)
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
}