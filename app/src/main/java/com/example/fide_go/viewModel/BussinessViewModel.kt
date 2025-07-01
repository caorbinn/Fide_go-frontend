package com.example.fide_go.viewModel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fide_go.data.model.Bussiness
import com.example.fide_go.data.retrofit.RetrofitApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BussinessViewModel : ViewModel() {

    private val _bussinessInserted = MutableStateFlow<Boolean?>(null)
    val bussinessInserted: StateFlow<Boolean?> = _bussinessInserted

    private val _bussinessUpdated = MutableStateFlow<Boolean?>(null)
    val bussinessUpdated: StateFlow<Boolean?> = _bussinessUpdated

    private val _bussinessDeleted = MutableStateFlow<Boolean?>(null)
    val bussinessDeleted: StateFlow<Boolean?> = _bussinessDeleted

    private val _bussinessEdit = MutableStateFlow<Bussiness?>(null)
    val bussinessEdit: StateFlow<Bussiness?> = _bussinessEdit

    private val _bussinessAll = MutableStateFlow<List<Bussiness>>(emptyList())
    val listBussiness: StateFlow<List<Bussiness>> = _bussinessAll

    private val _currentBussiness = MutableStateFlow<Bussiness?>(null)
    val currentBussiness: StateFlow<Bussiness?> = _currentBussiness

    /**
     * Inserta un nuevo negocio
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun insertBussinessVM(bussiness: Bussiness) {
        viewModelScope.launch {
            try {
                val response = RetrofitApi.bussinessService.insertBussiness(bussiness)
                _bussinessInserted.value = response.isSuccessful && (response.body() == true)
            } catch (e: Exception) {
                _bussinessInserted.value = false
                e.message?.let { Log.e("Exception", it) }
            }
        }
    }

    /**
     * Actualiza un negocio
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun updateBussinessVM(bussiness: Bussiness) {
        viewModelScope.launch {
            try {
                val response = RetrofitApi.bussinessService.updateBussiness(bussiness)
                if (response.isSuccessful) {
                    _bussinessUpdated.value = true
                    Log.e("Bussiness Updated", response.body().toString())
                } else {
                    _bussinessUpdated.value = false
                    Log.e("Error", "update Bussiness")
                }
            } catch (e: Exception) {
                e.message?.let { Log.e("Exception", it) }
            }
        }
    }

    /**
     * Guarda un negocio para edición
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun saveBussinessEdit(bussiness: Bussiness) {
        viewModelScope.launch {
            try {
                val response = bussiness.id?.let { RetrofitApi.bussinessService.getBussiness(it) }
                if (response != null) {
                    if (response.isSuccessful) {
                        _bussinessEdit.value = response.body()
                    } else {
                        Log.e("Error", "edit Bussiness")
                    }
                }
            } catch (e: Exception) {
                e.message?.let { Log.e("Exception", it) }
            }
        }
    }

    /**
     * Elimina un negocio por ID
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun deleteBussinessVM(id: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitApi.bussinessService.deleteBussiness(id)
                if (response.isSuccessful) {
                    _bussinessDeleted.value = response.body()
                } else {
                    _bussinessDeleted.value = false
                    Log.e("Error", "delete Bussiness")
                }
            } catch (e: Exception) {
                e.message?.let { Log.e("Exception", it) }
            }
        }
    }

    /**
     * Obtiene la lista de todos los negocios
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun getListBussiness() {
        viewModelScope.launch {
            try {
                val response = RetrofitApi.bussinessService.getAllBussiness()
                if (response.isSuccessful) {
                    _bussinessAll.value = response.body() ?: emptyList()
                } else {
                    Log.e("Error", "list Bussiness")
                }
            } catch (e: Exception) {
                e.message?.let { Log.e("Exception", it) }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getBussinessById(id: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitApi.bussinessService.getBussiness(id)
                if (response.isSuccessful) {
                    _currentBussiness.value = response.body()
                } else {
                    Log.e("Error", "get Bussiness")
                }
            } catch (e: Exception) {
                e.message?.let { Log.e("Exception", it) }
            }
        }
    }
}