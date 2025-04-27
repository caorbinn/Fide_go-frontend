package com.example.fide_go.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fide_go.data.model.Email
import com.example.fide_go.data.model.Phone
import com.example.fide_go.data.model.User
import com.example.fide_go.data.retrofit.RetrofitApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EmailViewModel: ViewModel()  {
    private val _emailUpdated = MutableStateFlow<Boolean?>(null)
    val emailUpdated: StateFlow<Boolean?> = _emailUpdated

    private val _emaildeleted = MutableStateFlow<Boolean?>(null)
    val emailDeleted: StateFlow<Boolean?> = _emaildeleted

    private val _emailEdit = MutableStateFlow<Email?>(null)
    val emailEdit: StateFlow<Email?> = _emailEdit


    private val _listEmails = MutableStateFlow<Set<Email>?>(null)
    val listEmail: StateFlow<Set<Email>?> = _listEmails




    /**
     * Esta funcion recibe un Email y lo actualiza
     */
    fun updateEmailVM(email: Email) {
        viewModelScope.launch {
            try {
                val response = RetrofitApi.emailService.updateEmail(email)
                if (response.isSuccessful) {
                    _emailUpdated.value = true
                    Log.e("correo actualizado", response.body().toString())
                } else {
                    _emailUpdated.value = false
                    Log.e("error en emailViewModel", "update Email")
                }
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch emailViewModel update", it) }
            }
        }
    }

    /**
     * Esta funcion recibe un Email y lo guarda
     */
    fun saveEmailEdit(email: Email) {
        viewModelScope.launch {
            try {
                val response = email.id?.let { RetrofitApi.emailService.getEmail(it) }
                if (response != null) {
                    if (response.isSuccessful) {
                        _emailEdit.value = response.body()
                    } else {
                        Log.e("error en emailViewModel", "edit Email")
                    }
                }
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch emailViewModel edit", it) }
            }
        }
    }


    /**
     * Esta funcion recibe un Email y lo elimina
     */
    fun deleteEmailVM(id:String) {
        viewModelScope.launch {
            try {
                val response = RetrofitApi.emailService.deleteEmail(id)
                if (response.isSuccessful) {
                    _emaildeleted.value = response.body()
                } else {
                    _emaildeleted.value = false
                    Log.e("error en emailViewModel", "delete Email")
                }
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch emailViewModel delete", it) }
            }
        }
    }



    /**
     * Esta funcion recibe un idProfile y devuelve la lista de emails
     */
    fun getListEmails(idProfile: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitApi.emailService.listEmailsUser(idProfile)
                if (response.isSuccessful) {
                    _listEmails.value = response.body()
                } else {
                    Log.e("error en emailViewModel", "list Email")
                }
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch emailViewModel list", it) }
            }
        }
    }

    /**
     * Esta funcion pone a nulo el emailedit
     */
    fun toNullEmailEdit() {
        viewModelScope.launch {
            try {
               _emailEdit.value = null
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch emailViewModel edit null", it) }
            }
        }
    }

    /**
     * Esta funcion pone a nulo el emailUpdated
     */
    fun toNullEmailUpdated() {
        viewModelScope.launch {
            try {
                _emailUpdated.value = null
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch emailViewModel emailupdate to null", it) }
            }
        }
    }

    /**
     * Esta funcion pone a nulo el emailDeleted
     */
    fun toNullEmailDeleted() {
        viewModelScope.launch {
            try {
                _emaildeleted.value = null
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch emailViewModel emailDeleted to null", it) }
            }
        }
    }


}