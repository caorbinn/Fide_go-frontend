package com.example.fide_go.ui.screens.Offers

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fide_go.R
import com.example.fide_go.data.model.User
import com.example.fide_go.navigation.AppScreen
import com.example.fide_go.ui.screens.LogoutDialog
import com.example.fide_go.ui.theme.AppColors
import com.example.fide_go.ui.theme.TextSizes
import com.example.fide_go.utils.AuthManager
import com.example.fide_go.viewModel.OffersViewModel
import com.example.fide_go.viewModel.UsersViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RedeemOfferScreen(
    navController: NavController,
    auth: AuthManager,
    onSignOutGoogle: () -> Unit,
    vmUsers: UsersViewModel,
    vmOffers: OffersViewModel,
    offerId: String
) {
    var showDialog by remember { mutableStateOf(false) }

    val currentUser = auth.getCurrentUser()

    // Fetch or insert user if needed
    LaunchedEffect(currentUser?.email) {
        currentUser?.email?.let { vmUsers.getUserByEmail(it) }
    }

    val userState by vmUsers.user.collectAsState()

    val onLogoutConfirmed: () -> Unit = {
        auth.signOut()
        onSignOutGoogle()
        navController.navigate(AppScreen.LoginScreen.route) {
            popUpTo(AppScreen.HomeScreen.route) { inclusive = true }
        }
    }

    LaunchedEffect(offerId) {
        vmOffers.saveOfferEditVM(offerId)
    }
    val offerState by vmOffers.offerEdit.collectAsState()
    val redeemCode by vmUsers.redeemCode.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppColors.mainFide),
                title = {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Uncomment to show profile image if desired
                        // currentUser?.photoUrl?.toString()?.let {
                        //     ClickableProfileImage(navController, it) { }
                        // }

                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = userState?.username?.let { stringResource(R.string.hola, it) }
                                    ?: stringResource(R.string.bienvenid),
                                fontSize = TextSizes.H3,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = AppColors.whitePerlaFide
                            )
                            Text(
                                text = userState?.email?.email ?: stringResource(R.string.usuario_anonimo),
                                fontSize = TextSizes.Footer,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = AppColors.whitePerlaFide
                            )
                        }

                        Spacer(modifier = Modifier.width(100.dp))

                        Text(
                            "Puntos: ${userState?.profile?.pointsUser ?: 0}",
                            fontSize = TextSizes.Footer,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = AppColors.whitePerlaFide
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(AppScreen.HomeScreen.route)
                    }) {
                        Icon(Icons.Outlined.Home, contentDescription = "Home", tint = AppColors.whitePerlaFide)
                    }
                    IconButton(onClick = { showDialog = true }) {
                        Icon(Icons.Outlined.ExitToApp, contentDescription = "Cerrar sesión", tint = AppColors.whitePerlaFide)
                    }
                }
            )
        }
    ) { padding ->
        if (showDialog) {
            LogoutDialog(
                onConfirmLogout = {
                    onLogoutConfirmed()
                    showDialog = false
                },
                onDismiss = { showDialog = false }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            offerState?.let { offer ->
                Text(offer.title)
                Spacer(modifier = Modifier.height(8.dp))
                Text(offer.description ?: "")
                Spacer(modifier = Modifier.height(8.dp))
                Text("Puntos necesarios: ${offer.points ?: 0}")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    userState?.id?.let { vmUsers.redeemOfferVM(it, offer) }
                }) {
                    Text("Canjear")
                }
                redeemCode?.let {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Código: $it")
                }
            }
        }
    }
}
