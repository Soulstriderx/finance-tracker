package com.fwrdgrp.financetracker.ui.uiutils

import com.fwrdgrp.financetracker.data.enum.Category
import com.fwrdgrp.financetracker.data.enum.PaymentMethod
import com.fwrdgrp.financetracker.data.enum.TransactionType
import com.fwrdgrp.financetracker.data.model.main.Transaction
import com.google.firebase.Timestamp
import java.util.Calendar

val dummyTransactions = listOf(
    Transaction(
        TransactionType.Expense,
        PaymentMethod.Cash,
        Category.Food,
        "14",
        "McDonalds",
        timestamp = firebaseTimestamp(2026, 1, 1),
        year = 2026,
        month = 1,
        day = 1,
        week = 1
    ),
    Transaction(
        TransactionType.Expense,
        PaymentMethod.Cash,
        Category.Transportation,
        "6",
        "Going home",
        timestamp = firebaseTimestamp(2026, 1, 1),
        year = 2026,
        month = 1,
        day = 1,
        week = 1
    ),
    Transaction(
        TransactionType.Expense,
        PaymentMethod.Cash,
        Category.Entertainment,
        "5",
        "Bought Steam game",
        timestamp = firebaseTimestamp(2026, 1, 1),
        year = 2026,
        month = 1,
        day = 1,
        week = 1
    ),
    Transaction(
        TransactionType.Expense,
        PaymentMethod.Cash,
        Category.Household,
        "15",
        "Groceries",
        timestamp = firebaseTimestamp(2026, 1, 1),
        year = 2026,
        month = 1,
        day = 1,
        week = 1
    ),
    Transaction(
        TransactionType.Expense,
        PaymentMethod.Cash,
        Category.Other,
        "2",
        "Helped out a friend",
        timestamp = firebaseTimestamp(2026, 1, 1),
        year = 2026,
        month = 1,
        day = 1,
        week = 1
    ),

    Transaction(
        TransactionType.Expense,
        PaymentMethod.Cash,
        Category.Food,
        "16",
        "Chicken rice",
        timestamp = firebaseTimestamp(2026, 1, 2),
        year = 2026,
        month = 1,
        day = 2,
        week = 1
    ),
    Transaction(
        TransactionType.Expense,
        PaymentMethod.Cash,
        Category.Transportation,
        "5",
        "Went home",
        timestamp = firebaseTimestamp(2026, 1, 2),
        year = 2026,
        month = 1,
        day = 2,
        week = 1
    ),
    Transaction(
        TransactionType.Expense,
        PaymentMethod.Cash,
        Category.Entertainment,
        "6",
        "Steam game",
        timestamp = firebaseTimestamp(2026, 1, 2),
        year = 2026,
        month = 1,
        day = 2,
        week = 1
    ),
    Transaction(
        TransactionType.Expense,
        PaymentMethod.Cash,
        Category.Household,
        "18",
        "Groceries",
        timestamp = firebaseTimestamp(2026, 1, 2),
        year = 2026,
        month = 1,
        day = 2,
        week = 1
    ),
    Transaction(
        TransactionType.Expense,
        PaymentMethod.Cash,
        Category.Other,
        "14",
        "Bought a tiny aquarium",
        timestamp = firebaseTimestamp(2026, 1, 2),
        year = 2026,
        month = 1,
        day = 2,
        week = 1
    ),

    Transaction(
        TransactionType.Expense,
        PaymentMethod.Cash,
        Category.Food,
        "12",
        "Tree Cafe",
        timestamp = firebaseTimestamp(2026, 1, 3),
        year = 2026,
        month = 1,
        day = 3,
        week = 1
    ),
    Transaction(
        TransactionType.Expense,
        PaymentMethod.Cash,
        Category.Transportation,
        "7",
        "Going home",
        timestamp = firebaseTimestamp(2026, 1, 3),
        year = 2026,
        month = 1,
        day = 3,
        week = 1
    ),
    Transaction(
        TransactionType.Expense,
        PaymentMethod.Cash,
        Category.Entertainment,
        "4",
        "Arcade",
        timestamp = firebaseTimestamp(2026, 1, 3),
        year = 2026,
        month = 1,
        day = 3,
        week = 1
    ),
    Transaction(
        TransactionType.Expense,
        PaymentMethod.Cash,
        Category.Household,
        "20",
        "Instant Noodles",
        timestamp = firebaseTimestamp(2026, 1, 3),
        year = 2026,
        month = 1,
        day = 3,
        week = 1
    ),
    Transaction(
        TransactionType.Expense,
        PaymentMethod.Cash,
        Category.Other,
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