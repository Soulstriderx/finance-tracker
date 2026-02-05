package com.fwrdgrp.financetracker.ui.screens.auth.register

import androidx.lifecycle.viewModelScope
import com.fwrdgrp.financetracker.data.model.main.MonthlyIncome
import com.fwrdgrp.financetracker.data.model.request.RegisterReq
import com.fwrdgrp.financetracker.data.repo.Repo
import com.fwrdgrp.financetracker.ui.screens.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class RegisterViewModel @Inject constructor(
    val repo: Repo
) : BaseViewModel() {
    private var _finish = MutableSharedFlow<Unit>()
    val finish = _finish.asSharedFlow()

    fun register(userReq: RegisterReq, income: MonthlyIncome, checked: Boolean) {
        if (!validateRegisterFields(userReq)) return
        viewModelScope.launch {
            val success = safeApiCall {
                if (checked) {
                    val userReqWithIncome = userReq.copy(monthlyIncome = income)
                    repo.register(userReqWithIncome)
                } else {
                    repo.register(userReq)
                }
            }
            if (success != null) {
                _toast.emit("Registration Successful")
                _finish.emit(Unit)
            }
        }
    }
}