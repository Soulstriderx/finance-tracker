package com.fwrdgrp.financetracker.ui.screens.manage.add

import androidx.lifecycle.viewModelScope
import com.fwrdgrp.financetracker.data.model.request.TransactionReq
import com.fwrdgrp.financetracker.data.repo.Repo
import com.fwrdgrp.financetracker.ui.screens.manage.base.BaseManageViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    val repo: Repo
) : BaseManageViewModel() {
    fun addTransaction(transaction: TransactionReq) {
        viewModelScope.launch {
            safeApiCall {
                repo.addTransaction(transaction).let {
                    _finish.emit(Unit)
                }
            }
        }
    }
}