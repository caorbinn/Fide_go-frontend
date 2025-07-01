package com.example.fide_go.viewModel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fide_go.data.model.QrPurchase
import com.example.fide_go.data.retrofit.RetrofitApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PurchaseViewModel : ViewModel() {
    private val _purchaseSaved = MutableStateFlow<Boolean?>(null)
    val purchaseSaved: StateFlow<Boolean?> = _purchaseSaved

    @RequiresApi(Build.VERSION_CODES.O)
    fun registerPurchase(userId: String, purchase: QrPurchase) {
        viewModelScope.launch {
            try {
                val response = RetrofitApi.purchaseService.registerPurchase(userId, purchase)
                _purchaseSaved.value = response.isSuccessful
            } catch (e: Exception) {
                _purchaseSaved.value = false
                Log.e("PurchaseViewModel", e.message ?: "error")
            }
        }
    }

    fun resetState() {
        _purchaseSaved.value = null
    }
}