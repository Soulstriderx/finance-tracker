package com.fwrdgrp.financetracker.ui.screens.profile

import androidx.lifecycle.viewModelScope
import com.fwrdgrp.financetracker.data.enum.DateFilter
import com.fwrdgrp.financetracker.data.model.main.Transaction
import com.fwrdgrp.financetracker.data.model.main.User
import com.fwrdgrp.financetracker.data.model.request.ProfileUpdateReq
import com.fwrdgrp.financetracker.data.repo.Repo
import com.fwrdgrp.financetracker.service.FirebaseAuthService
import com.fwrdgrp.financetracker.ui.screens.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val firebaseAuthService: FirebaseAuthService,
    val repo: Repo
) : BaseViewModel() {
    private var userJob: Job? = null
    private var transactionJob: Job? = null
    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transaction = _transactions.asStateFlow()

    init {
        fetchUser()
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

    fun fetchTransactions() {
        transactionJob?.cancel()

        transactionJob = viewModelScope.launch {
            repo.fetchTransactionsByDate(
                DateFilter.Monthly,
                Calendar.getInstance(),
                true
            ).collectLatest {
                _transactions.value = it
            }
        }
    }

    fun updateProfile(form: ProfileUpdateReq) {
        viewModelScope.launch {
            safeApiCall {
                repo.updateUser(form)
            }
        }
    }
}