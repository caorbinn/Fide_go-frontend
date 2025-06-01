package com.example.fide_go

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fide_go.navigation.AppNavigation
import com.example.fide_go.ui.theme.Fide_goTheme
import com.example.fide_go.utils.googleAuth.GoogleAuthUiClient
import com.example.fide_go.utils.googleAuth.SignInViewModel
import com.example.fide_go.viewModel.BussinessViewModel
import com.example.fide_go.viewModel.EmailViewModel
import com.example.fide_go.viewModel.OffersViewModel
import com.example.fide_go.viewModel.PhonesViewModel
import com.example.fide_go.viewModel.ProfileViewModel
import com.example.fide_go.viewModel.UsersViewModel
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Proyecto Fide-go:
 * Aplicación que consiste en tener nuestros negocios reagrupados en una app que nos sirva para
 * la fidelización de nuestros clientes, y captar muchos otros.
 *
 * @version 1.0
 * @author Caleb Espinoza Valencia
 */
class MainActivity : ComponentActivity() {

    // ViewModels
    private val vmUsers by viewModels<UsersViewModel>()
    private val vmPhones by viewModels<PhonesViewModel>()
    private val vmEmails by viewModels<EmailViewModel>()
    private val vmProfiles by viewModels<ProfileViewModel>()
    private val vmBussiness by viewModels<BussinessViewModel>()
    private val vmOffers by viewModels<OffersViewModel>()


    // Cliente de One-Tap / Google Identity
    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Fide_goTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val viewModel = viewModel<SignInViewModel>()
                    val state by viewModel.state.collectAsStateWithLifecycle()
                    val context = LocalContext.current

                    val launcher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.StartIntentSenderForResult(),
                        onResult = { result ->
                            if (result.resultCode == RESULT_OK) {
                                lifecycleScope.launch {
                                    val signInResult = googleAuthUiClient.signInWithIntent(
                                        intent = result.data ?: return@launch
                                    )
                                    viewModel.onSignInResult(signInResult, context)
                                }
                            }
                        }
                    )

                    LaunchedEffect(state.isSignInSuccessful) {
                        if (state.isSignInSuccessful) {
                            Toast.makeText(
                                applicationContext,
                                "Sign in successful",
                                Toast.LENGTH_SHORT
                            ).show()
                            viewModel.resetState()
                        }
                    }

                    AppNavigation(
                        state = state,
                        onSignInClickGoogle = {
                            lifecycleScope.launch {
                                val signInIntentSender = googleAuthUiClient.signIn()
                                launcher.launch(
                                    IntentSenderRequest.Builder(
                                        signInIntentSender ?: return@launch
                                    ).build()
                                )
                            }
                        },
                        onSignOutGoogle = {
                            lifecycleScope.launch {
                                googleAuthUiClient.signOut()
                                Toast.makeText(
                                    applicationContext,
                                    "Signed out",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent(context, MainActivity::class.java)
                                intent.addFlags(
                                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                                            Intent.FLAG_ACTIVITY_NEW_TASK
                                )
                                startActivity(intent)
                                finish()
                            }
                        },
                        vmUsers = vmUsers,
                        vmPhones = vmPhones,
                        vmEmails = vmEmails,
                        vmProfiles = vmProfiles,
                        vmBussiness = vmBussiness,
                        vmOffers = vmOffers
                    )
                }
            }
        }
    }
}
