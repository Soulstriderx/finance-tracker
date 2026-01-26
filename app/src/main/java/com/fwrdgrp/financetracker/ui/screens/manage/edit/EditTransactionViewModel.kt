package com.fwrdgrp.financetracker.ui.screens.manage.edit

import androidx.lifecycle.SavedStateHandle
import com.fwrdgrp.financetracker.data.model.main.Transaction
import com.fwrdgrp.financetracker.data.repo.Repo
import com.fwrdgrp.financetracker.ui.screens.manage.base.BaseManageViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class EditTransactionViewModel @Inject constructor(
    val repo: Repo,
    savedStateHandle: SavedStateHandle
) : BaseManageViewModel() {
    val uid = savedStateHandle.get<String>("uid")

    private val _transaction = MutableStateFlow<Transaction?>(null)
    val transaction = _transaction.asStateFlow()
}