package com.fwrdgrp.financetracker.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    object Login: Screen()
    @Serializable
    object Register: Screen()
    @Serializable
    object Home: Screen()
    @Serializable
    object Add: Screen()
}