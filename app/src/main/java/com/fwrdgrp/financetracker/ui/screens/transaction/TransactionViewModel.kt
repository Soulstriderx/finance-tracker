package com.fwrdgrp.financetracker.ui.screens.transaction

import androidx.lifecycle.viewModelScope
import com.fwrdgrp.financetracker.data.enum.DateFilter
import com.fwrdgrp.financetracker.data.model.main.Transaction
import com.fwrdgrp.financetracker.data.repo.Repo
import com.fwrdgrp.financetracker.ui.screens.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar

@HiltViewModel
class TransactionViewModel @Inject constructor(
    val repo: Repo
) : BaseViewModel() {
    private var fetchJob: Job? = null
    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions = _transactions.asStateFlow()

    private val _dateFilter = MutableStateFlow(DateFilter.Daily)
    val dateFilter = _dateFilter.asStateFlow()

    init {
        fetchTransactions()
    }

    fun fetchTransactions(calendar: Calendar = Calendar.getInstance()) {
        fetchJob?.cancel()

        fetchJob = viewModelScope.launch {
            safeApiCall {
                repo.fetchTransactionsByDate(_dateFilter.value, calendar, false)
                    .collectLatest { trans ->
                    _transactions.update { trans }
                }
            }
        }
    }

    fun onDateFilterSelect(filter: DateFilter, calendar: Calendar) {
        _dateFilter.update { filter }
        fetchTransactions(calendar)
    }
}