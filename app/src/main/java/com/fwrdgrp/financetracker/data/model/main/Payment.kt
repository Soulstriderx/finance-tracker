package com.fwrdgrp.financetracker.data.model.main

import com.google.firebase.Timestamp

data class Payment(
    val uid: String = "",
    val paidDate: Timestamp? = null,
    val monthYear: String = ""
) {
    companion object {
        fun fromMap(map: Map<String, Any>): Payment {
            return Payment(
                uid = map["uid"] as? String ?: "",
                paidDate = map["paidDate"] as? Timestamp,
                monthYear = map["monthYear"] as? String ?: ""
            )
        }
    }

    fun toMap(): Map<String, Any> {
        val map = mutableMapOf(
            "uid" to uid,
            "paidDate" to (paidDate ?: 0),
            "monthYear" to monthYear
        )
        return map
    }
}
