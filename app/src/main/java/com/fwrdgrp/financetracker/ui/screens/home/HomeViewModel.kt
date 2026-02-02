package com.fwrdgrp.financetracker.ui.screens.home

import androidx.lifecycle.viewModelScope
import com.fwrdgrp.financetracker.data.enum.DateFilter
import com.fwrdgrp.financetracker.data.model.main.Transaction
import com.fwrdgrp.financetracker.data.model.main.User
import com.fwrdgrp.financetracker.data.repo.Repo
import com.fwrdgrp.financetracker.service.FirebaseAuthService
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
class HomeViewModel @Inject constructor(
    val repo: Repo,
    val firebaseAuthService: FirebaseAuthService
) : BaseViewModel() {
    private var recentTransactionJob: Job? = null
    private var transactionsJob: Job? = null
    private var userJob: Job? = null
    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    private val _dateFilter = MutableStateFlow(DateFilter.Monthly)
    val dateFilter = _dateFilter.asStateFlow()

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions = _transactions.asStateFlow()
    private val _recentTransactions = MutableStateFlow<List<Transaction>>(emptyList())
    val recentTransactions = _recentTransactions.asStateFlow()

    init {
        fetchUser()
        fetchRecentTransactions()
        fetchTransactions()
    }

    fun fetchUser() {
        userJob?.cancel()

        userJob = viewModelScope.launch {
            firebaseAuthService.user.collectLatest {
                _user.value = it
            }
        }
    }

    fun fetchRecentTransactions() {
        recentTransactionJob?.cancel()

        recentTransactionJob = viewModelScope.launch {
            safeApiCall {
                repo.fetchMyTransactions().collectLatest { trans ->
                    _recentTransactions.update { trans }
                    fetchTransactions()
                }
            }
        }
    }

    fun fetchTransactions(calendar: Calendar = Calendar.getInstance()) {
        transactionsJob?.cancel()

        transactionsJob = viewModelScope.launch {
            safeApiCall {
                repo.fetchTransactionsByDate(
                    _dateFilter.value,
                    calendar,
                    false
                ).collectLatest { trans ->
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