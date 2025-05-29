package com.example.fide_go.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.fide_go.R
import com.example.fide_go.data.model.*
import com.example.fide_go.navigation.AppScreen
import com.example.fide_go.utils.AuthManager
import com.example.fide_go.viewModel.BussinessViewModel
import com.example.fide_go.viewModel.UsersViewModel
import com.example.fide_go.ui.theme.AppColors
import com.example.fide_go.ui.theme.TextSizes

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navController: NavController,
    auth: AuthManager,
    onSignOutGoogle: () -> Unit,
    vmUsers: UsersViewModel,
    vmBussiness: BussinessViewModel
) {
    var showDialog by remember { mutableStateOf(false) }

    val currentUser = auth.getCurrentUser()

    LaunchedEffect(Unit) {
        val userEmail = currentUser?.email
        if (userEmail != null) {
            vmUsers.getUserByEmail(userEmail)
        }
    }

    val userState by vmUsers.user.collectAsState()
    val isLoading by vmUsers.isLoading.collectAsState()

    if (currentUser?.email != null) {
        val userToInsert = User(
            null,
            currentUser.displayName.orEmpty(),
            Phone(null, currentUser.phoneNumber.orEmpty(), false, null),
            Email(null, currentUser.email.orEmpty(), false, null),
            Profile(null, "", currentUser.photoUrl.toString(), null)
        )
        LaunchedEffect(Unit) {
            vmUsers.insertUserVm(userToInsert)
            vmUsers.getUserByEmail(currentUser.email!!)
        }
    }

    val onLogoutConfirmed: () -> Unit = {
        auth.signOut()
        onSignOutGoogle()
        navController.navigate(AppScreen.LoginScreen.route) {
            popUpTo(AppScreen.HomeScreen.route) { inclusive = true }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppColors.mainFide),
                title = {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        currentUser?.photoUrl?.toString()?.let {
                            ClickableProfileImage(navController, it) { }
                        }

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
        },
        bottomBar = {
            BottomAppBar(
                containerColor = AppColors.mainFide,
                modifier = Modifier.height(44.dp)
            ) {
                Text(
                    text = stringResource(R.string.copyright),
                    fontSize = TextSizes.Footer,
                    fontStyle = FontStyle.Italic,
                    color = AppColors.whitePerlaFide,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                )
            }
        }
    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            if (showDialog) {
                LogoutDialog(
                    onConfirmLogout = {
                        onLogoutConfirmed()
                        showDialog = false
                    },
                    onDismiss = { showDialog = false }
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.whitePerlaFide)
                .padding(24.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = AppColors.FocusFide,
                    strokeWidth = 4.dp,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(48.dp)
                )
            } else {
                BodyContentHome(navController, vmUsers, userState, vmBussiness)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BodyContentHome(
    navController: NavController,
    vmUsers: UsersViewModel,
    userState: User?,
    vmBusiness: BussinessViewModel
) {
    val businesses by vmBusiness.listBussiness.collectAsState()

    LaunchedEffect(Unit) {
        vmBusiness.getListBussiness()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.fide),
            contentDescription = "imagen de bienvenida",
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            contentScale = ContentScale.Crop
        )

        Text(
            text = if (userState != null)
                stringResource(R.string.navega_por_nuestros_negocios_y_descubre_ofertas_incre_bles_pensadas_para_ti)
            else
                stringResource(R.string.fidego),
            fontSize = TextSizes.H2,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif,
            modifier = Modifier.padding(top=24.dp,bottom = 16.dp),
            color = AppColors.mainFide
        )

        Spacer(modifier = Modifier.height(4.dp)) // más chico

        if (businesses.isNotEmpty()) {
            businesses.forEach { business ->
                BusinessCard(business = business) {
                    // navController.navigate("businessDetail/${business.id}")
                }
                Spacer(modifier = Modifier.height(24.dp)) // también más chico
            }
        } else {
            CircularProgressIndicator()
        }

        Spacer(modifier = Modifier.height(16.dp)) // opcionalmente más pequeño
    }

}


@Composable
fun BusinessCard(business: Bussiness, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.whitePerlaFide)
    ) {
        Column {
//            // Imagen estática local
//            Image(
//                painter = painterResource(id = R.drawable.barberia),
//                contentDescription = "Imagen del negocio",
//                contentScale = ContentScale.Crop,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(180.dp)
//                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
//            )

            // Imagen dinámica desde URL con Coil
            AsyncImage(
                model = business.urlImageBussiness,
                contentDescription = "Imagen del negocio",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            )


            // Información del negocio
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = business.bussinessName.takeIf { it.isNotBlank() } ?: "",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = AppColors.mainFide,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = business.bussinessAddress?.takeIf { it.isNotBlank() } ?: "",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}




@Composable
fun ClickableProfileImage(
    navController: NavController,
    imageUrl: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .size(40.dp)
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl.ifBlank { R.drawable.nombre_edentifica })
                .crossfade(true)
                .build(),
            contentDescription = "Imagen",
            placeholder = painterResource(id = R.drawable.nombre_edentifica),
            contentScale = ContentScale.Crop,
            modifier = Modifier.clip(CircleShape)
        )
    }
}

@Composable
fun LogoutDialog(
    onConfirmLogout: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        containerColor = AppColors.whitePerlaFide,
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.cerrar_sesi_n), color = AppColors.mainFide) },
        text = { Text(stringResource(R.string.est_s_seguro_que_deseas_cerrar_sesi_n), color = AppColors.mainFide) },
        confirmButton = {
            Button(
                onClick = onConfirmLogout,
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.FocusFide)
            ) {
                Text(stringResource(R.string.aceptar), color = AppColors.whitePerlaFide)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                border = BorderStroke(1.dp, AppColors.FocusFide),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = AppColors.FocusFide)
            ) {
                Text(stringResource(R.string.cancelar))
            }
        }
    )
}
