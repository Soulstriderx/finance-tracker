package com.fwrdgrp.financetracker.ui.screens.bills

import androidx.lifecycle.viewModelScope
import com.fwrdgrp.financetracker.data.model.main.Bill
import com.fwrdgrp.financetracker.data.model.request.BillReq
import com.fwrdgrp.financetracker.data.repo.Repo
import com.fwrdgrp.financetracker.ui.screens.base.BaseViewModel
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class BillsViewModel @Inject constructor(
    val repo: Repo
): BaseViewModel() {
    private var _bills = MutableStateFlow<List<Bill>>(emptyList())
    val bills = _bills.asStateFlow()

    init {
        fetchBills()
    }

    fun fetchBills() {
        viewModelScope.launch {
            safeApiCall {
                repo.fetchBills().let { bills ->
                    _bills.update { bills }
                }
            }
        }
    }

    fun addBill(bill: BillReq) {
        viewModelScope.launch {
            safeApiCall {
                repo.addBill(bill).let {
                    fetchBills()
                }
            }
        }
    }

    fun payBill(bill: Bill, newTimestamp: Timestamp) {
        viewModelScope.launch {
            safeApiCall {
                repo.payBill(bill, newTimestamp).let {
                    fetchBills()
                }
            }
        }
    }
}