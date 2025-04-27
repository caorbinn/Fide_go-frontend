package com.example.fide_go.utils


import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.example.fide_go.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

/**
 * Esta clase se encarga de administrar la autenticacion e ingreso a nuestra aplicacion.
 *
 * This class is responsible for managing authentication and login to our application.
 */

//Aqui vamos a contener los casos de exitos o de error
//Here we will contain the success or error cases
sealed class AuthRes<out T>{
    data class Succes<T>(val data: T ): AuthRes<T>();
    data class Error(val errorMessage: String ): AuthRes<Nothing>();
}



class AuthManager() {
    private val auth: FirebaseAuth by lazy { Firebase.auth }

    //Function to login guest
    suspend fun signInAnonymously(): AuthRes<FirebaseUser>{
        return try {
            val result = auth.signInAnonymously().await()
            AuthRes.Succes(result.user ?: throw Exception("Error logging in"))
        }catch (e: Exception){
            AuthRes.Error(e.message ?: "Error logging in how guest")
        }
    }


    //Funcion para crear usuarios, esto es temporal, se debe de usar retrofit para insertar el usuario
    suspend fun createUserWithEmailandPassword(email:String, password:String): AuthRes<FirebaseUser?>{
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            AuthRes.Succes(authResult.user)
        }catch (e: Exception){
            AuthRes.Error(e.message ?: "Error to create user")
        }
    }


    //Function to login with email and password
    suspend fun signInWithEmailandPassword(email:String, password:String): AuthRes<FirebaseUser?>{
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            AuthRes.Succes(authResult.user)
        }catch (e: Exception){
            AuthRes.Error(e.message ?: "Error loggin in with email and password")
        }
    }


    suspend fun resetPassword(email: String): AuthRes<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            AuthRes.Succes(Unit)
        }catch (e: Exception){
            AuthRes.Error(e.message?: "Error to reset password")
        }
    }


    fun signOut(){
        auth.signOut();
    }

    fun getCurrentUser(): FirebaseUser?{
        return auth.currentUser
    }
}