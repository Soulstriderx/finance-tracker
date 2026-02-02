package com.fwrdgrp.financetracker.data.model.request

import com.google.firebase.Timestamp

data class BudgetReq(
    val food: String? = null,
    val transportation: String? = null,
    val entertainment: String? = null,
    val household: String? = null,
    val refresh: Timestamp? = null,
    val day: Int? = null
) {
    companion object {
        fun fromMap(map: Map<String, Any>): BudgetReq {
            return BudgetReq(
                food = (map["food"] as? String) ?: "0",
                transportation = (map["transportation"] as? String) ?: "0",
                entertainment = (map["entertainment"] as? String) ?: "0",
                household = (map["household"] as? String) ?: "0",
                refresh = map["refresh"] as? Timestamp,
                day = (map["day"] as? Int) ?: 0,
            )
        }
    }

    fun toMap(): Map<String, Any> {
        val map = mutableMapOf<String, Any>()
        food?.let { map["food"] = it }
        transportation?.let { map["transportation"] = it }
        entertainment?.let { map["entertainment"] = it }
        household?.let { map["household"] = it }
        refresh?.let { map["refresh"] = it }
        day?.let { map["day"] = it }
        return map
    }
}