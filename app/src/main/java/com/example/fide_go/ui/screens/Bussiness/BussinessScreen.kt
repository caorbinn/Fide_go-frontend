package com.example.fide_go.ui.screens.Bussiness

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.*
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.fide_go.R
import com.example.fide_go.data.model.Bussiness
import com.example.fide_go.data.model.Offers
import com.example.fide_go.data.model.Email
import com.example.fide_go.data.model.Phone
import com.example.fide_go.data.model.Profile
import com.example.fide_go.data.model.User
import com.example.fide_go.navigation.AppScreen
import com.example.fide_go.ui.screens.ClickableProfileImage
import com.example.fide_go.ui.theme.AppColors
import com.example.fide_go.ui.theme.TextSizes
import com.example.fide_go.utils.AuthManager
import com.example.fide_go.viewModel.BussinessViewModel
import com.example.fide_go.viewModel.OffersViewModel
import com.example.fide_go.viewModel.UsersViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BussinessScreen(
    navController: NavController,
    auth: AuthManager,
    onSignOutGoogle: () -> Unit,
    vmUsers: UsersViewModel,
    vmBussiness: BussinessViewModel,
    vmOffers: OffersViewModel,
    businessId: String
) {
    var showDialog by remember { mutableStateOf(false) }
    // Estado para controlar la oferta seleccionada para borrado
    var showDeleteDialogForOfferId by remember { mutableStateOf<String?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val currentUser = auth.getCurrentUser()

    LaunchedEffect(Unit) {
        val userEmail = currentUser?.email
        if (userEmail != null) {
            vmUsers.getUserByEmail(userEmail)
        }
    }

    val userState by vmUsers.user.collectAsState()
    val isLoadingUser by vmUsers.isLoading.collectAsState()

    LaunchedEffect(businessId) {
        vmBussiness.getBussinessById(businessId)
        vmOffers.getOffersByBusiness(businessId)
    }

    val business by vmBussiness.currentBussiness.collectAsState()
    val offers by vmOffers.offersByBusiness.collectAsState()

    // ─── Construcción de User (pasando argumentos por posición) ───
    if (currentUser?.email != null) {
        val userToInsert = User(
            /*   id    */      null,
            /*username*/      currentUser.displayName.orEmpty(),

            /*  Phone(id: String?, phone: String, verified: Boolean, userId: String?)  */
            Phone(
                null,
                currentUser.phoneNumber.orEmpty(),
                false,
                null
            ),

            /*  Email(id: String?, email: String, verified: Boolean, userId: String?)  */
            Email(
                null,
                currentUser.email.orEmpty(),
                false,
                null
            ),

            /*
             *  Profile(
             *    id: String?,
             *    description: String,
             *    urlImageProfile: String,
             *    dateBirth: LocalDate?
             *  )
             *
             *  Solo cuatro parámetros, en ese orden exacto.
             */
            Profile(
                null,
                "",
                currentUser.photoUrl.toString(),
                null
            ),

            /* businessId */   null
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
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppColors.mainFide),
                title = {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        currentUser?.photoUrl?.toString()?.let { photoUrl ->
                            ClickableProfileImage(navController, photoUrl) { }
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
                    IconButton(onClick = { navController.navigate(AppScreen.HomeScreen.route) }) {
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
            if (showDeleteDialogForOfferId != null) {
                DeleteOfferDialog(
                    onConfirm = {
                        showDeleteDialogForOfferId?.let { id ->
                            vmOffers.deleteOfferVM(id)
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Cupón eliminado correctamente")
                            }
                        }
                        showDeleteDialogForOfferId = null
                    },
                    onDismiss = { showDeleteDialogForOfferId = null }
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.whitePerlaFide)
                .padding(24.dp)
        ) {
            if (isLoadingUser) {
                CircularProgressIndicator(
                    color = AppColors.FocusFide,
                    strokeWidth = 4.dp,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(48.dp)
                )
            } else {
                BodyContentBusiness(
                    navController = navController,
                    userState = userState,
                    business = business,
                    offers = offers,
                    vmOffers = vmOffers,
                    // ─── Pasamos estado y callback ───
                    showDeleteDialogForOfferId = showDeleteDialogForOfferId,
                    onDeleteClick = { offerId ->
                        showDeleteDialogForOfferId = offerId
                    }
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BodyContentBusiness(
    navController: NavController,
    userState: User?,
    business: Bussiness?,
    offers: List<Offers>,
    vmOffers: OffersViewModel,

    // ─── Nuevos parámetros que recibe este composable ───
    showDeleteDialogForOfferId: String?,
    onDeleteClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        // Encabezado del negocio
        Text(
            text = business?.bussinessName ?: "",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = AppColors.mainFide
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = business?.bussinessDescription ?: "",
            fontSize = 16.sp,
            color = Color.DarkGray
        )
        Spacer(modifier = Modifier.height(24.dp))

        if (offers.isNotEmpty()) {
            Text(
                text = stringResource(R.string.available_offers),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.mainFide
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(offers) { offer ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                // Navegar al detalle de la oferta si es necesario
                                // navController.navigate("offerDetail/${offer.id}")
                            },
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Imagen de la oferta
                            AsyncImage(
                                model = offer.urlImageOffer,
                                contentDescription = "Imagen de la oferta",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                // Título de la oferta
                                Text(
                                    text = offer.title,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                // Puntos en negrita y tamaño mayor
                                Text(
                                    text = "${offer.points ?: 0} puntos",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                // Descripción breve opcional
                                offer.description?.let { desc ->
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = desc,
                                        fontSize = 14.sp,
                                        color = Color.Gray,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                if (userState?.admin == true) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Button(
                                            onClick = {
                                                offer.id?.let { vmOffers.saveOfferEditVM(it) }
                                                offer.id?.let {
                                                    navController.navigate(
                                                        AppScreen.EditOfferScreen.createRoute(it)
                                                    )
                                                }
                                            },
                                            modifier = Modifier.weight(1f),
                                            contentPadding = PaddingValues(4.dp)
                                        ) {
                                            Text("Editar")
                                        }
                                        Button(
                                            onClick = {
                                                // Cuando el usuario pulsa “Eliminar”,
                                                // invocamos la lambda que viene de BussinessScreen
                                                offer.id?.let { onDeleteClick(it) }
                                            },
                                            modifier = Modifier.weight(1f),
                                            contentPadding = PaddingValues(4.dp)
                                        ) {
                                            Text("Eliminar")
                                        }
                                    }
                                } else {
                                    Button(
                                        onClick = {
                                            offer.id?.let {
                                                navController.navigate(
                                                    AppScreen.RedeemOfferScreen.createRoute(it)
                                                )
                                            }
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        contentPadding = PaddingValues(4.dp)
                                    ) {
                                        Text("Canjear")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            Text(
                text = stringResource(R.string.no_offers),
                fontSize = 18.sp,
                color = Color.Gray
            )
        }
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

@Composable
fun DeleteOfferDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        containerColor = AppColors.whitePerlaFide,
        onDismissRequest = onDismiss,
        title = { Text("¿Está seguro de eliminar cupón?", color = AppColors.mainFide) },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.FocusFide)
            ) {
                Text("Sí", color = AppColors.whitePerlaFide)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                border = BorderStroke(1.dp, AppColors.FocusFide),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = AppColors.FocusFide)
            ) {
                Text("No")
            }
        }
    )
}
