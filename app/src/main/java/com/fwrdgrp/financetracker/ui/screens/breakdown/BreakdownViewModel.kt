package com.fwrdgrp.financetracker.ui.screens.breakdown

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.fwrdgrp.financetracker.data.model.main.Transaction
import com.fwrdgrp.financetracker.data.repo.Repo
import com.fwrdgrp.financetracker.ui.screens.base.BaseViewModel
import com.fwrdgrp.financetracker.ui.uiutils.toCalendar
import com.fwrdgrp.financetracker.ui.uiutils.toDateRange
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

@HiltViewModel
class BreakdownViewModel @Inject constructor(
    val repo: Repo,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    private val start = savedStateHandle.get<Long>("start")!!
    private val end = savedStateHandle.get<Long>("end")!!
    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions = _transactions.asStateFlow()
    private val _dateRange = MutableStateFlow(Timestamp(Date(start)).toCalendar().toDateRange(end))
    val dateRange = _dateRange.asStateFlow()

    init {
        fetchTransactions()
    }

    private fun fetchTransactions() {
        viewModelScope.launch {
            safeApiCall {
                repo.fetchTransactionsByRange(start, end).let { trans ->
                    _transactions.update { trans }
                }
            }
        }
    }
}