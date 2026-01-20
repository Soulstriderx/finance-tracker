package com.fwrdgrp.financetracker.ui.screens.base

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fwrdgrp.financetracker.data.model.request.LoginReq
import com.fwrdgrp.financetracker.data.model.request.RegisterReq
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

open class BaseViewModel : ViewModel() {
    protected val _toast = MutableSharedFlow<String>()
    val toast = _toast.asSharedFlow()

    suspend fun <T> safeApiCall(func: suspend () -> T?): T? {
        return try {
            val result = withContext(Dispatchers.IO) {
                func.invoke()
            }
            result
        } catch (e: Exception) {
            null
        }
    }

    fun validateLogin(form: LoginReq): Boolean {
        if (form.email.isBlank() || form.password.isBlank()) {
            emitToast("Fields cannot be blank")
            return false
        }
        return true
    }

    fun validateRegisterFields(form: RegisterReq): Boolean {
        form.apply {
            if (!validateIncome(balance)) return false
            if (!validateEmailFormat(email)) return false
            if (!validatePassword(password, password2)) return false
        }
        return true
    }

    fun validateIncome(balance: String?): Boolean {
        if (balance == null || balance.toDouble() <= 0.0) {
            emitToast("Balance cannot be empty or less than 1")
            return false
        }
        return true
    }

    fun validateEmailFormat(email: String): Boolean {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emitToast("Invalid email format")
            return false
        }
        return true
    }

    fun validatePassword(pass: String, pass2: String): Boolean {
        if (pass.length < 8) {
            emitToast("Password must be at least 8 characters")
            return false
        }

        if (pass != pass2) {
            emitToast("Passwords do not match")
            return false
        }
        return true
    }

    private fun emitToast(msg: String) {
        viewModelScope.launch {
            _toast.emit(msg)
        }
    }
}