package com.fwrdgrp.financetracker.data.model.main

import com.google.firebase.Timestamp

data class Budget(
    val food: String? = null,
    val foodUsed: String? = null,
    val transportation: String? = null,
    val transportationUsed: String? = null,
    val entertainment: String? = null,
    val entertainmentUsed: String? = null,
    val household: String? = null,
    val householdUsed: String? = null,
    val refresh: Timestamp? = null,
    val day: Int? = null
) {
    companion object {
        fun fromMap(map: Map<String, Any>): Budget {
            return Budget(
                food = map["food"]?.toString() ?: "0",
                foodUsed = map["foodUsed"]?.toString() ?: "0",
                transportation = map["transportation"]?.toString() ?: "0",
                transportationUsed = map["transportationUsed"]?.toString() ?: "0",
                entertainment = map["entertainment"]?.toString() ?: "0",
                entertainmentUsed = map["entertainmentUsed"]?.toString() ?: "0",
                household = map["household"]?.toString() ?: "0",
                householdUsed = map["householdUsed"]?.toString() ?: "0",
                refresh = map["refresh"] as? Timestamp,
                day = (map["day"] as? Number)?.toInt() ?: 0,
            )
        }
    }

    fun toMap(): Map<String, Any> {
        return mutableMapOf<String, Any>(
            "food" to (food ?: 0),
            "transportation" to (transportation ?: 0),
            "entertainment" to (entertainment ?: 0),
            "household" to (household ?: 0),
            "day" to (day ?: 0),
            "refresh" to (refresh ?: 0)
        )
    }
}
