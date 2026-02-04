package com.fwrdgrp.financetracker.ui.screens.base

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fwrdgrp.financetracker.data.enum.Category
import com.fwrdgrp.financetracker.data.model.request.BillReq
import com.fwrdgrp.financetracker.data.model.request.LoginReq
import com.fwrdgrp.financetracker.data.model.request.RegisterReq
import com.fwrdgrp.financetracker.data.model.request.TransactionReq
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

open class BaseViewModel : ViewModel() {
    protected val _showIncomePrompt = MutableStateFlow(false)
    val showIncomePrompt = _showIncomePrompt.asStateFlow()
    protected val _toast = MutableSharedFlow<String>(replay = 1)
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

    fun dismissPrompt() {
        _showIncomePrompt.update { false }
    }

    fun showPrompt() {
        _showIncomePrompt.update { true }
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

    fun validateTransaction(form: TransactionReq, isEditing: Boolean): Boolean {
        val amount = if (isEditing) form.newAmount else form.amount
        val timestamp = if (isEditing) form.newTimestamp else form.timestamp
        val category = if (isEditing) form.newCategory else form.category

        if (amount.isBlank()) {
            emitToast("Amount cannot be empty")
            return false
        }

        val amountValue = amount.toDoubleOrNull()
        if (amountValue == null || amountValue <= 0.0) {
            emitToast("Please enter a valid amount greater than 0")
            return false
        }

        if (timestamp == null) {
            emitToast("Please select a date")
            return false
        }

        if (category == Category.Other && form.customCategory.isNullOrBlank()) {
            emitToast("Please select a custom category")
            return false
        }

        return true
    }

    fun validateBill(form: BillReq, isEdit: Boolean): Boolean {
        val name = if (isEdit) form.newName else form.name
        val amount = if (isEdit) form.newAmount else form.amount
        val nextDue = if (isEdit) form.newNextDue else form.nextDue

        if (name.isBlank()) {
            emitToast("Bill name cannot be empty")
            return false
        }

        if (amount.isBlank()) {
            emitToast("Amount cannot be empty")
            return false
        }

        val amountValue = amount.toDoubleOrNull()
        if (amountValue == null || amountValue <= 0.0) {
            emitToast("Please enter a valid amount greater than 0")
            return false
        }

        if (nextDue == null) {
            emitToast("Please select a recurring date")
            return false
        }
        return true
    }

    fun validateCustomCat(category: String): Boolean {
        if (category.isBlank()) {
            emitToast("Category name cannot be empty")
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