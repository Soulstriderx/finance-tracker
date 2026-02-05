package com.fwrdgrp.financetracker.ui.screens.auth.login

import androidx.lifecycle.viewModelScope
import com.fwrdgrp.financetracker.data.model.main.User
import com.fwrdgrp.financetracker.data.model.request.LoginReq
import com.fwrdgrp.financetracker.data.repo.Repo
import com.fwrdgrp.financetracker.service.FirebaseAuthService
import com.fwrdgrp.financetracker.ui.screens.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    val repo: Repo,
    val firebaseAuthService: FirebaseAuthService
): BaseViewModel() {
    private val _finish = MutableSharedFlow<Unit>()
    val finish = _finish.asSharedFlow()

    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    init {
        fetchUser()
    }

    fun fetchUser() {
        viewModelScope.launch {
            firebaseAuthService.user.collect {
                _user.value = it
            }
        }
    }

    fun login(req: LoginReq) {
        if(!validateLogin(req)) return
        viewModelScope.launch {
            safeApiCall {
                repo.login(req).let {
                    _finish.emit(Unit)
                }
            }
        }
    }
}