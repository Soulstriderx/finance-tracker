package com.fwrdgrp.financetracker.data.model.main

import com.google.firebase.Timestamp

data class Bill(
    val uid: String = "",
    val name: String = "",
    val amount: String = "",
    val nextDue: Timestamp? = null,
    val day: Int = 0,
    val paymentHistory: List<Payment> = emptyList()
){
    companion object {
        fun fromMap(map: Map<String, Any>): Bill {
            return Bill(
                uid = map["uid"] as? String ?: "",
                name = map["name"] as? String ?: "",
                amount = map["amount"] as? String ?: "",
                nextDue = map["nextDue"] as? Timestamp,
                day = (map["day"] as? Number)?.toInt() ?: 0,
                paymentHistory = (map["paymentHistory"] as? List<*>)?.mapNotNull { item ->
                    (item as? Map<String, Any>)?.let { Payment.fromMap(it) }
                } ?: emptyList()
            )
        }
    }
}
