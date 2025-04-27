package com.example.fide_go.utils.googleAuth

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SignInViewModel: ViewModel() {
    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    // New status to manage the user's e-mail
    // Nuevo estado para manejar el correo electrónico del usuario
    private val _email = MutableStateFlow<String?>(null)
    val email: StateFlow<String?> = _email

    fun onSignInResult(result: SignInResult, context: Context){

        _state.update{it.copy(
            isSignInSuccessful = result.data !=null,
            signInError = result.errorMessage
        )}

        // If the login was successful, obtain the user's email address.
        // Si el inicio de sesión fue exitoso, obtener el correo electrónico del usuario
        if (result.data != null) {
            val account = result.data
            val email = account.userEmail
            _email.value = email
        }
    }

    fun resetState(){
        _state.update {SignInState()}
    }

}