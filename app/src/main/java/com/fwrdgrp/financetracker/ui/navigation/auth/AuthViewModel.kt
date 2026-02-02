package com.fwrdgrp.financetracker.ui.navigation.auth

import com.fwrdgrp.financetracker.service.FirebaseAuthService
import com.fwrdgrp.financetracker.ui.screens.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    val authService: FirebaseAuthService
) : BaseViewModel()