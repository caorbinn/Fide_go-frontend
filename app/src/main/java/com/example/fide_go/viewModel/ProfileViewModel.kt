package com.example.fide_go.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fide_go.data.model.Email
import com.example.fide_go.data.model.Phone
import com.example.fide_go.data.model.Profile
import com.example.fide_go.data.model.SocialNetwork
import com.example.fide_go.data.retrofit.RetrofitApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel: ViewModel() {
    private val _emailInserted = MutableStateFlow<Boolean?>(null)
    val emailInserted: StateFlow<Boolean?> = _emailInserted

    private val _phoneInserted = MutableStateFlow<Boolean?>(null)
    val phoneInserted: StateFlow<Boolean?> = _phoneInserted

    private val _socialInserted = MutableStateFlow<Boolean?>(null)
    val socialInserted: StateFlow<Boolean?> = _socialInserted

    private val _profileEdit = MutableStateFlow<Profile?>(null)
    val profileEdit: StateFlow<Profile?> = _profileEdit

    private val _profileUpdated = MutableStateFlow<Boolean?>(false)
    val profileUpdated: StateFlow<Boolean?> = _profileUpdated



    /**
     * Esta funcion recibe un Email y lo inserta en la base de datos
     */
    fun insertEmailVm(email: Email, idProfile:String) {
        viewModelScope.launch {
            try {
                val response = RetrofitApi.profileService.insertEmail(idProfile, email)
                if (response.isSuccessful) {
                    _emailInserted.value = true
                } else {
                    _emailInserted.value = false
                    Log.e("error en profileViewModel", "insertEmail")
                }
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch profileViewModel insert email", it) }
            }
        }
    }

    /**
     * Esta funcion recibe un Email y lo inserta en la base de datos
     */
    fun insertPhoneVm(phone: Phone, idProfile:String) {
        viewModelScope.launch {
            try {
                val response = RetrofitApi.profileService.insertPhone(idProfile, phone)
                if (response.isSuccessful) {
                    _phoneInserted.value = true
                } else {
                    _phoneInserted.value = false
                    Log.e("error en profileViewModel", "insertPhone")
                }
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch profileViewModel insert phone", it) }
            }
        }
    }

    /**
     * Esta funcion recibe una red social y lo inserta en la base de datos
     */
    fun insertSocialNetworkVm(socialNetwork: SocialNetwork, idProfile:String) {
        viewModelScope.launch {
            try {
                val response = RetrofitApi.profileService.insertSocialNetwork(idProfile, socialNetwork)
                if (response.isSuccessful) {
                    _socialInserted.value = true
                } else {
                    _socialInserted.value = false
                    Log.e("error en profileViewModel", response.isSuccessful.toString())
                }
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch profileViewModel insert social", it) }
            }
        }
    }

    /**
     * Esta funcion recibe un Profile y lo guarda
     */
    fun saveProfileEdit(profile: Profile) {
        viewModelScope.launch {
            try {
                val response = profile.id?.let { RetrofitApi.profileService.getProfileById(it) }
                if (response != null) {
                    if (response.isSuccessful) {
                        _profileEdit.value = response.body()
                    } else {
                        Log.e("error en profileViewModel", "edit profile")
                    }
                }
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch profileViewModel edit", it) }
            }
        }
    }

    /**
     * Esta funcion recibe un Profile y lo actualiza
     */
    fun updateProfileVM(profile: Profile) {
        viewModelScope.launch {
            try {
                val response = RetrofitApi.profileService.updateProfile(profile)
                if (response.isSuccessful) {
                    _profileUpdated.value = response.body()
                } else {
                    Log.e("error en profileViewModel", "update profile")
                }
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch profileViewModel update", it) }
            }
        }
    }

    /**
     * Esta funcion pone a nulo el profileEdit
     */
    fun toNullProfileEdit() {
        viewModelScope.launch {
            try {
                _profileEdit.value = null
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch profileViewModel edit null", it) }
            }
        }
    }

    /**
     * Esta funcion pone a nulo el phoneInserted
     */
    fun toNullPhoneInserted() {
        viewModelScope.launch {
            try {
                _phoneInserted.value = null
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch profileViewModel phone inserted to null", it) }
            }
        }
    }

    /**
     * Esta funcion pone a nulo el emailInserted
     */
    fun toNullEmailInserted() {
        viewModelScope.launch {
            try {
                _emailInserted.value = null
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch profileViewModel email inserted to null", it) }
            }
        }
    }

    /**
     * Esta funcion pone a nulo el SocialNetworkInserted
     */
    fun toNullSocialInserted() {
        viewModelScope.launch {
            try {
                _socialInserted.value = null
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch profileViewModel social inserted to null", it) }
            }
        }
    }

}