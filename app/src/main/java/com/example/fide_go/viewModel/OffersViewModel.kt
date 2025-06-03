package com.example.fide_go.viewModel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fide_go.data.model.Offers
import com.example.fide_go.data.retrofit.RetrofitApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OffersViewModel : ViewModel() {

    private val _offerInserted = MutableStateFlow<Boolean?>(null)
    val offerInserted: StateFlow<Boolean?> = _offerInserted

    private val _offerUpdated = MutableStateFlow<Boolean?>(null)
    val offerUpdated: StateFlow<Boolean?> = _offerUpdated

    private val _offerDeleted = MutableStateFlow<Boolean?>(null)
    val offerDeleted: StateFlow<Boolean?> = _offerDeleted

    private val _offerEdit = MutableStateFlow<Offers?>(null)
    val offerEdit: StateFlow<Offers?> = _offerEdit

    private val _offersByBusiness = MutableStateFlow<List<Offers>>(emptyList())
    val offersByBusiness: StateFlow<List<Offers>> = _offersByBusiness

    fun clearStates() {
        _offerInserted.value = null
        _offerUpdated.value = null
        _offerDeleted.value = null
    }

    /**
     * Inserta una nueva oferta en el backend
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun insertOfferVM(offer: Offers) {
        viewModelScope.launch {
            try {
                val response = RetrofitApi.offersService.insertOffers(offer)
                if (response.isSuccessful) {
                    _offerInserted.value = true
                    Log.i("Offers", "Oferta insertada: ${response.body()}")
                } else {
                    _offerInserted.value = false
                    Log.e("Offers", "Error al insertar oferta")
                }
            } catch (e: Exception) {
                _offerInserted.value = false
                Log.e("Exception", "insertOfferVM: ${e.localizedMessage}")
            }
        }
    }

    /**
     * Actualiza una oferta existente en el backend
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun updateOfferVM(offer: Offers) {
        viewModelScope.launch {
            try {
                val response = RetrofitApi.offersService.updateOffers(offer)
                if (response.isSuccessful) {
                    _offerUpdated.value = true
                    Log.i("Offers", "Oferta actualizada: ${response.body()}")
                } else {
                    _offerUpdated.value = false
                    Log.e("Offers", "Error al actualizar oferta")
                }
            } catch (e: Exception) {
                _offerUpdated.value = false
                Log.e("Exception", "updateOfferVM: ${e.localizedMessage}")
            }
        }
    }

    /**
     * Obtiene una oferta para edición (consulta al backend por ID)
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun saveOfferEditVM(offerId: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitApi.offersService.getOffers(offerId)
                if (response.isSuccessful) {
                    _offerEdit.value = response.body()
                    Log.i("Offers", "Oferta obtenida: ${response.body()}")
                } else {
                    Log.e("Offers", "Error al obtener oferta para editar")
                }
            } catch (e: Exception) {
                Log.e("Exception", "saveOfferEditVM: ${e.localizedMessage}")
            }
        }
    }

    /**
     * Elimina una oferta por ID en el backend
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun deleteOfferVM(id: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitApi.offersService.deleteOffers(id)
                if (response.isSuccessful) {
                    _offerDeleted.value = response.body()
                    Log.i("Offers", "Oferta eliminada: ${response.body()}")
                } else {
                    _offerDeleted.value = false
                    Log.e("Offers", "Error al eliminar oferta")
                }
            } catch (e: Exception) {
                _offerDeleted.value = false
                Log.e("Exception", "deleteOfferVM: ${e.localizedMessage}")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getOffersByBusiness(businessId: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitApi.offersService.getOffersByBusiness(businessId)
                if (response.isSuccessful) {
                    _offersByBusiness.value = response.body() ?: emptyList()
                } else {
                    Log.e("Offers", "Error al obtener ofertas por negocio")
                }
            } catch (e: Exception) {
                Log.e("Exception", "getOffersByBusiness: ${e.localizedMessage}")
            }
        }
    }
}