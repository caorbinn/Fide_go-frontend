package com.example.fide_go.utils.googleAuth

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

class GoogleAuthUiClient(
    private val context: Context,
    private val oneTapClient: SignInClient
) {
    private val auth = Firebase.auth

    suspend fun signIn(): IntentSender?{
        val result= try{
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        }catch(e: Exception){
            e.printStackTrace()
            if(e is CancellationException) throw e
            null
        }

        return result?.pendingIntent?.intentSender
    }


    suspend fun signInWithIntent(intent: Intent): SignInResult {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials= GoogleAuthProvider.getCredential(googleIdToken,null)

        return try{

            val user = auth.signInWithCredential(googleCredentials).await().user
            SignInResult(
                data= user?.run{
                    email?.let {
                        UserData(
                            userId = uid,
                            username = displayName,
                            userEmail = it,
                            profilePictureUrl = photoUrl?.toString(),
                            userPhone = phoneNumber.toString()
                        )
                    }
                },
                errorMessage = null
            )

        } catch(e: Exception){
            e.printStackTrace()
            if(e is CancellationException) throw e
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
    }



//    fun getSignedInUser(): UserData?=auth.currentUser?.run{
//        UserData(
//            userId = uid,
//            username = displayName,
//            profilePictureUrl = photoUrl?.toString()
//        )
//    }


    suspend fun signOut(){
        try{
            oneTapClient.signOut().await()
            auth.signOut()
        }catch(e: Exception){
            e.printStackTrace()
            if(e is CancellationException) throw e
        }
    }

    private fun buildSignInRequest(): BeginSignInRequest{
        return BeginSignInRequest.Builder().
                setGoogleIdTokenRequestOptions(
                    GoogleIdTokenRequestOptions.builder().
                    setSupported(true).
                    setFilterByAuthorizedAccounts(false).
                    setServerClientId("1:1079461259471:android:feba2a99c423977e152fba")
                    .build()
        ).
        setAutoSelectEnabled(true).
        build()
    }
}


//class GoogleAuthUiClient(
//    private val context: Context,
//    private val oneTapClient: SignInClient
//) {
//    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
//    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
//    private val handler = Handler(context.mainLooper)
//
//    private var loginAttempts: Int
//        get() = sharedPreferences.getInt("loginAttempts", 0)
//        set(value) {
//            sharedPreferences.edit().putInt("loginAttempts", value).apply()
//        }
//
//    companion object {
//        private const val MAX_ATTEMPTS = 3
//        private const val RETRY_DELAY = 30000L // 30 segundos
//    }
//
//    suspend fun signIn(): IntentSender? {
//        if (loginAttempts >= MAX_ATTEMPTS) {
//            notifyUser()
//            handler.postDelayed({ resetAttempts() }, RETRY_DELAY)
//            return null
//        }
//
//        val result = try {
//            oneTapClient.beginSignIn(
//                buildSignInRequest()
//            ).await()
//        } catch (e: Exception) {
//            e.printStackTrace()
//            incrementAttempts()
//            if (e is CancellationException) throw e
//            null
//        }
//
//        return result?.pendingIntent?.intentSender
//    }
//
//    suspend fun signInWithIntent(intent: Intent): SignInResult {
//        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
//        val googleIdToken = credential.googleIdToken
//        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
//
//        return try {
//            val user = auth.signInWithCredential(googleCredentials).await().user
//            resetAttempts()
//            SignInResult(
//                data = user?.run {
//                    email?.let {
//                        phoneNumber?.let { it1 ->
//                            UserData(
//                                userId = uid,
//                                username = displayName,
//                                userEmail = it,
//                                profilePictureUrl = photoUrl?.toString(),
//                                userPhone = it1
//                            )
//                        }
//                    }
//                },
//                errorMessage = null
//            )
//        } catch (e: Exception) {
//            e.printStackTrace()
//            incrementAttempts()
//            if (e is CancellationException) throw e
//            SignInResult(
//                data = null,
//                errorMessage = e.message
//            )
//        }
//    }
//
//    suspend fun signOut() {
//        try {
//            oneTapClient.signOut().await()
//            auth.signOut()
//        } catch (e: Exception) {
//            e.printStackTrace()
//            if (e is CancellationException) throw e
//        }
//    }
//
//    private fun incrementAttempts() {
//        loginAttempts++
//        if (loginAttempts >= MAX_ATTEMPTS) {
//            notifyUser()
//            handler.postDelayed({ resetAttempts() }, RETRY_DELAY)
//        }
//    }
//
//    private fun resetAttempts() {
//        loginAttempts = 0
//    }
//
//    private fun notifyUser() {
//        Toast.makeText(context, "Demasiados intentos fallidos. Por favor, intenta nuevamente en unos minutos.", Toast.LENGTH_LONG).show()
//    }
//
//    private fun buildSignInRequest(): BeginSignInRequest{
//        return BeginSignInRequest.Builder().
//                setGoogleIdTokenRequestOptions(
//                    GoogleIdTokenRequestOptions.builder().
//                    setSupported(true).
//                    setFilterByAuthorizedAccounts(false).
//                    setServerClientId(context.getString(R.string.default_web_client_id))
//                    .build()
//        ).
//        setAutoSelectEnabled(true).
//        build()
//    }
//}









