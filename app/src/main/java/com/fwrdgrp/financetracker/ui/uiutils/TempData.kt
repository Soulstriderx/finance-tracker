package com.fwrdgrp.financetracker.ui.uiutils

import com.fwrdgrp.financetracker.data.enum.Category
import com.fwrdgrp.financetracker.data.enum.TransactionType
import com.fwrdgrp.financetracker.data.model.main.Transaction
import com.google.firebase.Timestamp
import java.util.Calendar

val dummyTransactions = listOf(
    Transaction(
        TransactionType.EXPENSE,
        Category.FOOD.name,
        "14",
        "McDonalds",
        timestamp = firebaseTimestamp(2026, 1, 1),
        year = 2026,
        month = 1,
        day = 1,
        week = 1
    ),
    Transaction(
        TransactionType.EXPENSE,
        Category.TRANSPORTATION.name,
        "6",
        "Going home",
        timestamp = firebaseTimestamp(2026, 1, 1),
        year = 2026,
        month = 1,
        day = 1,
        week = 1
    ),
    Transaction(
        TransactionType.EXPENSE,
        Category.ENTERTAINMENT.name,
        "5",
        "Bought Steam game",
        timestamp = firebaseTimestamp(2026, 1, 1),
        year = 2026,
        month = 1,
        day = 1,
        week = 1
    ),
    Transaction(
        TransactionType.EXPENSE,
        Category.HOUSEHOLD.name,
        "15",
        "Groceries",
        timestamp = firebaseTimestamp(2026, 1, 1),
        year = 2026,
        month = 1,
        day = 1,
        week = 1
    ),
    Transaction(
        TransactionType.EXPENSE,
        Category.OTHER.name,
        "2",
        "Helped out a friend",
        timestamp = firebaseTimestamp(2026, 1, 1),
        year = 2026,
        month = 1,
        day = 1,
        week = 1
    ),

    Transaction(
        TransactionType.EXPENSE,
        Category.FOOD.name,
        "16",
        "Chicken rice",
        timestamp = firebaseTimestamp(2026, 1, 2),
        year = 2026,
        month = 1,
        day = 2,
        week = 1
    ),
    Transaction(
        TransactionType.EXPENSE,
        Category.TRANSPORTATION.name,
        "5",
        "Went home",
        timestamp = firebaseTimestamp(2026, 1, 2),
        year = 2026,
        month = 1,
        day = 2,
        week = 1
    ),
    Transaction(
        TransactionType.EXPENSE,
        Category.ENTERTAINMENT.name,
        "6",
        "Steam game",
        timestamp = firebaseTimestamp(2026, 1, 2),
        year = 2026,
        month = 1,
        day = 2,
        week = 1
    ),
    Transaction(
        TransactionType.EXPENSE,
        Category.HOUSEHOLD.name,
        "18",
        "Groceries",
        timestamp = firebaseTimestamp(2026, 1, 2),
        year = 2026,
        month = 1,
        day = 2,
        week = 1
    ),
    Transaction(
        TransactionType.EXPENSE,
        Category.OTHER.name,
        "14",
        "Bought a tiny aquarium",
        timestamp = firebaseTimestamp(2026, 1, 2),
        year = 2026,
        month = 1,
        day = 2,
        week = 1
    ),

    Transaction(
        TransactionType.EXPENSE,
        Category.FOOD.name,
        "12",
        "Tree Cafe",
        timestamp = firebaseTimestamp(2026, 1, 3),
        year = 2026,
        month = 1,
        day = 3,
        week = 1
    ),
    Transaction(
        TransactionType.EXPENSE,
        Category.TRANSPORTATION.name,
        "7",
        "Going home",
        timestamp = firebaseTimestamp(2026, 1, 3),
        year = 2026,
        month = 1,
        day = 3,
        week = 1
    ),
    Transaction(
        TransactionType.EXPENSE,
        Category.ENTERTAINMENT.name,
        "4",
        "Arcade",
        timestamp = firebaseTimestamp(2026, 1, 3),
        year = 2026,
        month = 1,
        day = 3,
        week = 1
    ),
    Transaction(
        TransactionType.EXPENSE,
        Category.HOUSEHOLD.name,
        "20",
        "Instant Noodles",
        timestamp = firebaseTimestamp(2026, 1, 3),
        year = 2026,
        month = 1,
        day = 3,
        week = 1
    ),
    Transaction(
        TransactionType.EXPENSE,
        Category.OTHER.name,
        "1",
        "No idea where it went",
        timestamp = firebaseTimestamp(2026, 1, 3),
        year = 2026,
        month = 1,
        day = 3,
        week = 1
    ),

    )

fun firebaseTimestamp(year: Int, month: Int, day: Int): Timestamp {
    val cal = Calendar.getInstance()
    cal.set(year, month - 1, day, 0, 0, 0) // months are 0-indexed
    cal.set(Calendar.MILLISECOND, 0)
    return Timestamp(cal.time)
}