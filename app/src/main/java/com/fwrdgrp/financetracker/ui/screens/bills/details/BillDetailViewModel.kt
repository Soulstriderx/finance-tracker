package com.fwrdgrp.financetracker.ui.screens.bills.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.fwrdgrp.financetracker.data.model.main.Bill
import com.fwrdgrp.financetracker.data.model.request.BillReq
import com.fwrdgrp.financetracker.data.repo.Repo
import com.fwrdgrp.financetracker.ui.screens.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class BillDetailViewModel @Inject constructor(
    val repo: Repo,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    val uid = savedStateHandle.get<String>("uid")!!

    var _finish = MutableSharedFlow<Unit>()
    val finish = _finish.asSharedFlow()
    var _bill = MutableStateFlow<Bill?>(null)
    val bill = _bill.asStateFlow()

    init {
        fetchBillById()
    }

    fun fetchBillById() {
        viewModelScope.launch {
            safeApiCall {
                repo.fetchBillById(uid).let { bill ->
                    _bill.update { bill }
                }
            }
        }
    }

    fun deleteBill(uid: String) {
        viewModelScope.launch {
            safeApiCall {
                repo.deleteBill(uid).let {
                    _finish.emit(Unit)
                }
            }
        }
    }

    fun editBill(bill: BillReq) {
        viewModelScope.launch {
            safeApiCall {
                repo.editBill(bill).let {
                    fetchBillById()
                }
            }
        }
    }

    fun deletePayment(bill: Bill, paymentUid: String) {
        viewModelScope.launch {
            safeApiCall {
                repo.deletePaymentRecord(bill, paymentUid).let {
                    fetchBillById()
                }
            }
        }

    }
}