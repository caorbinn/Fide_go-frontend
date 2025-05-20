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
                    modifier = Modifier.fillMaxWidth().wrapContentSize(Alignment.Center)
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
                    modifier = Modifier.align(Alignment.Center).size(48.dp)
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
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (userState != null)
                stringResource(R.string.bienvenido_a_fidego)
            else
                stringResource(R.string.fidego),
            fontSize = TextSizes.title,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Cursive,
            modifier = Modifier.padding(bottom = 16.dp),
            color = AppColors.mainFide
        )

        Image(
            painter = painterResource(id = R.drawable.nombre_edentifica),
            contentDescription = "imagen de bienvenida",
            modifier = Modifier.fillMaxWidth().scale(0.7f),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(10.dp))

        if (businesses.isNotEmpty()) {
            businesses.forEach { business ->
                BusinessCard(business = business) {
                    // navController.navigate("businessDetail/${business.id}")
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        } else {
            CircularProgressIndicator()
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { /* navController.navigate(AppScreen.FindByEmailScreen.route) */ },
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.FocusFide),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier.weight(1f).height(50.dp)
            ) {
                Text("Buscar correo")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = { /* navController.navigate(AppScreen.FindByPhoneScreen.route) */ },
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.FocusFide),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier.weight(1f).height(50.dp)
            ) {
                Text("Buscar teléfono")
            }
        }
    }
}

@Composable
fun BusinessCard(business: Bussiness, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = business.bussinessName.orEmpty(), fontWeight = FontWeight.Bold, fontSize = 20.sp)
            if (!business.bussinessDescription.isNullOrBlank()) {
                Text(text = business.bussinessDescription.orEmpty(), fontSize = 14.sp, color = Color.Gray)
            }
            if (!business.bussinessAddress.isNullOrBlank()) {
                Text(text = business.bussinessAddress.orEmpty(), fontSize = 14.sp)
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
