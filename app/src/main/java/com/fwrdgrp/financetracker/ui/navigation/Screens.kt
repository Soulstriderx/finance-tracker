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
    @Serializable
    data class Edit(val uid: String): Screen()
    @Serializable
    object Transaction: Screen()
    @Serializable
    data class TranDetails(val uid: String): Screen()
    @Serializable
    object Stats: Screen()
}