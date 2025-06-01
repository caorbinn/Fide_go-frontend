package com.example.fide_go.ui.screens.Offers

// OffersScreen.kt

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import com.example.fide_go.R
import com.example.fide_go.data.model.Offers
import com.example.fide_go.data.model.Bussiness
import com.example.fide_go.utils.AuthManager
import com.example.fide_go.navigation.AppScreen
import com.example.fide_go.viewModel.OffersViewModel
import com.example.fide_go.viewModel.UsersViewModel
import com.example.fide_go.viewModel.BussinessViewModel
import com.example.fide_go.ui.theme.AppColors
import com.example.fide_go.ui.theme.TextSizes
import com.example.fide_go.ui.screens.LogoutDialog
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OffersScreen(
    navController: NavController,
    auth: AuthManager,
    onSignOutGoogle: () -> Unit,
    vmUsers: UsersViewModel,
    vmOffers: OffersViewModel = viewModel(),
    vmBussiness: BussinessViewModel,
    isEditMode: Boolean = false
) {
    // IMPORTANTE: utilizamos "initial = null" para que el compilador sepa el valor inicial
    val offerToEdit by vmOffers.offerEdit.collectAsState(initial = null)
    val insertedState by vmOffers.offerInserted.collectAsState(initial = null)
    val updatedState by vmOffers.offerUpdated.collectAsState(initial = null)

    val businessList by vmBussiness.listBussiness.collectAsState()
    var selectedBusiness by remember { mutableStateOf<Bussiness?>(null) }
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        vmBussiness.getListBussiness()
    }

    // Estados locales para los campos
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var terms by remember { mutableStateOf("") }
    var pointsText by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current

    // Si estamos en modo edición y ya llegó "offerToEdit", precargamos los campos
    LaunchedEffect(offerToEdit) {
        offerToEdit?.let { offer ->
            title = offer.title
            description = offer.description.toString()
            terms = offer.termsAndConditions.toString()
            pointsText = offer.points.toString()
        }
    }

    // Validación sencilla: todos los campos con algo y puntos >= 0
    val isFormValid = title.isNotBlank()
            && description.isNotBlank()
            && terms.isNotBlank()
            && pointsText.toIntOrNull()?.let { it >= 0 } == true
            && selectedBusiness != null

    var showDialog by remember { mutableStateOf(false) }

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
                    Text(
                        text = if (isEditMode) "Editar Oferta" else "Nueva Oferta",
                        color = AppColors.whitePerlaFide
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(AppScreen.HomeScreen.route) }) {
                        Icon(
                            Icons.Outlined.Home,
                            contentDescription = "Home",
                            tint = AppColors.whitePerlaFide
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showDialog = true }) {
                        Icon(
                            Icons.Outlined.ExitToApp,
                            contentDescription = "Cerrar sesión",
                            tint = AppColors.whitePerlaFide
                        )
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
    ) { innerPadding ->
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
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título de la oferta") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5
            )

            OutlinedTextField(
                value = terms,
                onValueChange = { terms = it },
                label = { Text("Términos y condiciones") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5
            )

            OutlinedTextField(
                value = pointsText,
                onValueChange = { newText ->
                    if (newText.all { it.isDigit() }) {
                        pointsText = newText
                    }
                },
                label = { Text("Puntos necesarios") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                OutlinedTextField(
                    readOnly = true,
                    value = selectedBusiness?.bussinessName ?: "Selecciona negocio",
                    onValueChange = {},
                    label = { Text("Negocio") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    businessList.forEach { buss ->
                        DropdownMenuItem(
                            text = { Text(buss.bussinessName) },
                            onClick = {
                                selectedBusiness = buss
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    // Creamos la instancia de Offers con todos sus parámetros:
                    val oferta = Offers(
                        id = offerToEdit?.id
                            ?: "123456",    // Si estamos editando, mantiene el ID; si no, pone "123456"
                        title = title.trim(),
                        description = description.trim(),
                        termsAndConditions = terms.trim(),
                        points = pointsText.toIntOrNull() ?: 0,
                        bussinessId = selectedBusiness?.id
                    )

                    if (isEditMode) {
                        vmOffers.updateOfferVM(oferta)
                    } else {
                        vmOffers.insertOfferVM(oferta)
                    }

                    // Limpiamos campos y quitamos foco
                    title = ""
                    description = ""
                    terms = ""
                    pointsText = ""
                    selectedBusiness = null
                    focusManager.clearFocus()
                },
                enabled = isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(text = if (isEditMode) "Actualizar oferta" else "Publicar oferta")
            }


            // Mensajes de éxito/fracaso (opcional)
            insertedState?.let { success ->
                if (success) {
                    Text(
                        text = "Oferta publicada correctamente",
                        color = MaterialTheme.colorScheme.primary
                    )
                } else {
                    Text(
                        text = "Error al publicar la oferta",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            updatedState?.let { success ->
                if (success) {
                    Text(
                        text = "Oferta actualizada correctamente",
                        color = MaterialTheme.colorScheme.primary
                    )
                } else {
                    Text(
                        text = "Error al actualizar la oferta",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
