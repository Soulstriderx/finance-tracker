package com.fwrdgrp.financetracker.data.model.request

import com.google.firebase.Timestamp

data class BillReq(
    val uid: String = "",
    val name: String = "",
    val newName: String = "",
    val amount: String = "",
    val newAmount: String = "",
    val nextDue: Timestamp? = null,
    val newNextDue: Timestamp? = null,
    val day: Int = 0,
    val newDay: Int = 0,
) {
    fun toMap(): Map<String, Any> {
        val map: MutableMap<String, Any> = mutableMapOf(
            "uid" to uid,
            "name" to name,
            "amount" to amount,
            "day" to day,
        )
        nextDue?.let { map["nextDue"] = it }
        return map
    }

    fun toEditMap(): Map<String, Any> {
        val map: MutableMap<String, Any> = mutableMapOf(
            "uid" to uid,
            "name" to newName,
            "amount" to newAmount,
            "day" to newDay,
        )
        newNextDue?.let { map["nextDue"] = it }
        return map
    }
}
