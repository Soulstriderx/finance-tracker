package com.fwrdgrp.financetracker.ui.screens.manage.base

import com.fwrdgrp.financetracker.ui.screens.base.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseManageViewModel : BaseViewModel() {
    protected val _finish = MutableSharedFlow<Unit>()
    val finish = _finish.asSharedFlow()
    protected val _customCategories = MutableStateFlow<List<String>>(emptyList())
    val customCategories = _customCategories.asStateFlow()

}