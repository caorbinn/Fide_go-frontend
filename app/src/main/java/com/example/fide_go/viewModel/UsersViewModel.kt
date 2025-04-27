package com.example.fide_go.viewModel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fide_go.data.model.Profile
import com.example.fide_go.data.model.User
import com.example.fide_go.data.model.dto.UserDto
import com.example.fide_go.data.retrofit.RetrofitApi
import com.example.fide_go.data.retrofit.RetrofitApi.userService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class UsersViewModel : ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _userEmailSearch = MutableStateFlow<UserDto?>(null)
    val userEmailSearch: StateFlow<UserDto?> = _userEmailSearch

    private val _userPhoneSearch = MutableStateFlow<UserDto?>(null)
    val userPhoneSearch: StateFlow<UserDto?> = _userPhoneSearch

    private val _userSocialSearch = MutableStateFlow<UserDto?>(null)
    val userSocialSearch: StateFlow<UserDto?> = _userSocialSearch

    private val _userInserted = MutableStateFlow<Boolean?>(false)
    val userInserted: StateFlow<Boolean?> = _userInserted

    private val _validationOne = MutableStateFlow<Boolean>(false)
    val validationOne: StateFlow<Boolean> = _validationOne

    private val _answerValidation = MutableStateFlow<Boolean?>(null)
    val answerValidation: StateFlow<Boolean?> = _answerValidation

    private val _userUpdate = MutableStateFlow<Boolean?>(false)
    val userUpdate: StateFlow<Boolean?> = _userUpdate

    private val _userEdit = MutableStateFlow<User?>(null)
    val userEdit: StateFlow<User?> = _userEdit

    private val _findString=MutableStateFlow<String?>(null)
    val findString: StateFlow<String?> = _findString

    // Variable que representa el estado de carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Variable que representa el estado de carga
    private val _isLoadingCall = MutableStateFlow(false)
    val isLoadingCall: StateFlow<Boolean> = _isLoadingCall

    // Variable que representa el estado de carga
    private val _isLoadingRegister = MutableStateFlow(false)
    val isLoadingRegister: StateFlow<Boolean> = _isLoadingRegister


    //CRUD USER
    /**
     * Esta funcion recibe un User y lo inserta en la base de datos
     */

    suspend fun insertUserVm(user:User): Boolean {
        _isLoadingRegister.value= true
        return try {
            val response = userService.insertUser(user)
            if (response.isSuccessful) {
                _userInserted.value = true
                _isLoadingRegister.value = false
                true
            } else {
                Log.e("error en userViewModel", "insertUser")
                _isLoadingRegister.value = false
                false
            }
        } catch (e: Exception) {
            // Manejar errores de red u otros errores
            e.message?.let { Log.e("error catch userViewModel insert User", it) }
            false
        }
    }

    /**
     * Esta funcion recibe un email y nos devuelve al usuario encontrado
     */
    fun getUserByEmail(email: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = userService.getByEmail(email)
                if (response.isSuccessful) {
                    _user.value = response.body()
                    _isLoading.value = false
                } else {
                    Log.e("error en userViewModel", "getUserByEmail")
                    //_isLoading.value = false
                }
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch userViewModel getUserByEmail", it) }
            }
        }
    }

    //BUSQUEDAS
    /**
     * Esta funcion recibe un email y nos devuelve al usuario encontrado, esta la utilizamos para las busquedas de correo
     */
    fun getUserByEmailSearch(email: String) {
        viewModelScope.launch {
            try {
                val response = userService.getDtoByEmail(email)
                if (response.isSuccessful) {
                    _userEmailSearch.value = response.body()
                } else {
                    Log.e("error en userViewModel", "getUserByEmailSearch")
                }
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch userViewModel getUserByEmailSearch", it) }
            }
        }
    }


    /**
     * Esta funcion pone en nulo la respuesta de buscar por email
     */
    fun putEmailResultNull() {
        viewModelScope.launch {
            try {
                _userEmailSearch.value=null
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch userViewModel EmailResultNull", it) }
            }
        }
    }



    /**
     * Esta funcion recibe un phone y nos devuelve al usuario encontrado, esta la utilizamos para las busquedas de telefonos
     */
    fun getUserByPhoneSearch(phone: String) {
        viewModelScope.launch {
            try {
                val response = userService.getDtoByPhone(phone)
                if (response.isSuccessful) {
                    _userPhoneSearch.value = response.body()
                } else {
                    Log.e("error en userViewModel", "getUserByPhoneSearch")
                }
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch userViewModel getUserByPhoneSearch", it) }
            }
        }
    }

    /**
     * Esta funcion pone en nulo la respuesta de buscar por telefono
     */
    fun putPhoneResultNull() {
        viewModelScope.launch {
            try {
                _userPhoneSearch.value=null
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch userViewModel PhoneResultNull", it) }
            }
        }
    }



    /**
     * Esta funcion recibe un phone y nos devuelve al usuario encontrado, esta la utilizamos para las busquedas de telefonos
     */
    fun getUserBySocialSearch(type: String, socialname: String) {
        viewModelScope.launch {
            try {
                val response = userService.getDtoBySocialNetwork(type,socialname)
                Log.e("error en userViewModel", ""+ response)
                if (response.isSuccessful) {
                    _userSocialSearch.value = response.body()
                } else {
                    Log.e("error en userViewModel", "getUserBySocialSearch" + response.body())
                }
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch userViewModel getUserBySocialSearch", it) }
            }
        }
    }

    /**
     * Esta funcion pone en nulo la respuesta de buscar por telefono
     */
    fun putSocialNetworkResultNull() {
        viewModelScope.launch {
            try {
                _userSocialSearch.value=null
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch userViewModel SocialNetworkResultNull", it) }
            }
        }
    }



    //FUNCIONES VALIDACIONES

    /**
     * Esta funcion recibe un usuario para realizar la llamada de la validacion one
     */
    fun toDoCallByUser(user: User) {
        _isLoadingCall.value = true
        viewModelScope.launch {
            try {
                val response = userService.toDoCall(user)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _validationOne.value = responseBody
                        Log.e("validacion", "bien")
                        _isLoadingCall.value = false
                    } else {
                        _isLoadingCall.value = false
                        Log.e("error en userViewModel", "toDoCallByUser if")
                    }
                } else {
                    _isLoadingCall.value = false
                    Log.e("error en userViewModel", "toDoCallByUser else")
                }
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch userViewModel toDoCallByUser try", it) }
            }
        }
    }


    /**
     * Esta funcion recibe la respuesta matematica y actualiza su variable si es correcta
     */
    fun answerMathByUser(answer: Int,user: User) {
        viewModelScope.launch {
            try {
                val response = userService.answerMathChallenge(answer,user)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody==true) {
                        _answerValidation.value = responseBody
                        //si la respuesta es true, actualizao al usuario desde la base de datos
                        getUserByEmail(user.email.email)
                        Log.e("respuesta validacion", "bien")
                    } else {
                        _answerValidation.value = responseBody
                        //si la respuesta es true, actualizao al usuario desde la base de datos
                        getUserByEmail(user.email.email)
                        Log.e("error en userViewModel", "la respuesta no es correcta")
                    }
                } else {
                    Log.e("error en userViewModel", "answerMathByUser")
                }
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch userViewModel answerMathByUser try", it) }
            }
        }
    }


    /**
     * Esta funcion niega la validacion 1
     */
    fun validationOneNegative() {
        viewModelScope.launch {
            try {
                _validationOne.value=false
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch userViewModel validationOneNegative", it) }
            }
        }
    }

    /**
     * Esta funcion pone en nulo la respuesta del reto matematico del usuario
     */
    fun validationOneCheckNegative() {
        viewModelScope.launch {
            try {
                _answerValidation.value=null
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch userViewModel validationOneCheckNegative", it) }
            }
        }
    }


    /**
     * Esta funcion recibe un User y lo actualiza
     */
    fun updateUserVM(user:User) {
        viewModelScope.launch {
            try {
                val response = userService.updateUser(user)
                if (response.isSuccessful) {
                    _userUpdate.value = response.body()
                } else {
                    Log.e("error en userViewModel", "update user")
                }
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch userViewModel update", it) }
            }
        }
    }



    /**
     * Esta funcion recibe un User y lo guarda
     */
    fun saveUserEdit(user: User) {
        viewModelScope.launch {
            try {
                val response = userService.getByEmail(user.email.email)
                if (response.isSuccessful) {
                    _userEdit.value = response.body()
                } else {
                    Log.e("error en userViewModel", "edit user")
                }
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch userViewModel edit", it) }
            }
        }
    }


    /**
     * Esta funcion pone a nulo el userEdit
     */
    fun toNullUserEdit() {
        viewModelScope.launch {
            try {
                _userEdit.value = null
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch userViewModel edit null", it) }
            }
        }
    }

    /**
     * Esta funcion establece el parametro de busqueda
     */
    fun saveFindString(find: String) {
        viewModelScope.launch {
            try {
                _findString.value = find
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch save find string", it) }
            }
        }
    }

    /**
     * Esta funcion pone a nulo el userEdit
     */
    fun toNullfindString() {
        viewModelScope.launch {
            try {
                _findString.value = null
            } catch (e: Exception) {
                // Manejar errores de red u otros errores
                e.message?.let { Log.e("error catch to null find string", it) }
            }
        }
    }

}