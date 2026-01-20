package com.fwrdgrp.financetracker.ui.screens.auth.login

import androidx.lifecycle.viewModelScope
import com.fwrdgrp.financetracker.data.model.request.LoginReq
import com.fwrdgrp.financetracker.data.repo.Repo
import com.fwrdgrp.financetracker.ui.screens.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    val repo: Repo
): BaseViewModel() {
    private val _finish = MutableSharedFlow<Unit>()
    val finish = _finish.asSharedFlow()

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