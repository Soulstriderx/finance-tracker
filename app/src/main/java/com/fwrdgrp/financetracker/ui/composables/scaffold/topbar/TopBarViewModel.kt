package com.fwrdgrp.financetracker.ui.composables.scaffold.topbar

import com.fwrdgrp.financetracker.data.repo.Repo
import com.fwrdgrp.financetracker.service.FirebaseAuthService
import com.fwrdgrp.financetracker.ui.screens.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class TopBarViewModel @Inject constructor(
    val repo: Repo,
    val authService: FirebaseAuthService,
): BaseViewModel() {

    fun signOut(){
        repo.removeAllListeners()
        authService.signout()
    }
}