package com.fwrdgrp.financetracker.ui.screens.home

import androidx.lifecycle.viewModelScope
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

@HiltViewModel
class HomeViewModel @Inject constructor(
    val repo: Repo,
    val firebaseAuthService: FirebaseAuthService
) : BaseViewModel() {
    private var fetchJob: Job? = null
    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions = _transactions.asStateFlow()

    init {
        fetchUser()
        fetchTransactions()
    }
    fun fetchUser() {
        viewModelScope.launch {
            firebaseAuthService.user.collect {
                _user.value = it
            }
        }
    }
    fun fetchTransactions() {
        fetchJob?.cancel()

        fetchJob = viewModelScope.launch {
            safeApiCall {
                repo.fetchMyTransactions().collectLatest { trans ->
                    _transactions.update { trans }
                }
            }
        }
    }
}