package com.example.fide_go.ui.screens.Bussiness

import android.net.Uri
import android.os.Build
import android.webkit.MimeTypeMap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import com.example.fide_go.data.model.Bussiness
import com.example.fide_go.data.retrofit.RetrofitApi
import com.example.fide_go.navigation.AppScreen
import com.example.fide_go.ui.screens.LogoutDialog
import com.example.fide_go.ui.theme.AppColors
import com.example.fide_go.ui.theme.TextSizes
import com.example.fide_go.utils.AuthManager
import com.example.fide_go.viewModel.BussinessViewModel
import com.example.fide_go.viewModel.UsersViewModel
import kotlinx.coroutines.launch
import java.util.UUID

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBussinessScreen(
    navController: NavController,
    auth: AuthManager,
    onSignOutGoogle: () -> Unit,
    vmUsers: UsersViewModel,
    vmBussiness: BussinessViewModel
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    val insertedState by vmBussiness.bussinessInserted.collectAsState()

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        imageUri = uri
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
                title = { Text("Nuevo Negocio", color = AppColors.whitePerlaFide) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(AppScreen.HomeScreen.route) }) {
                        Icon(Icons.Outlined.Home, contentDescription = "Home", tint = AppColors.whitePerlaFide)
                    }
                },
                actions = {
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
                    text = "© Fide-Go",
                    fontSize = TextSizes.Footer,
                    color = AppColors.whitePerlaFide,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                )
            }
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre del negocio") },
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
                value = address,
                onValueChange = { address = it },
                label = { Text("Dirección") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Button(onClick = { launcher.launch("image/*") }) {
                Text("Seleccionar imagen")
            }

            imageUri?.let { uri ->
                AsyncImage(
                    model = uri,
                    contentDescription = "Imagen seleccionada",
                    modifier = Modifier.height(150.dp)
                )
            }

            val isFormValid = name.isNotBlank() && description.isNotBlank() && address.isNotBlank() && imageUri != null

            Button(
                onClick = {
                    coroutineScope.launch {
                        var uploadedUrl: String? = null
                        imageUri?.let { uri ->
                            val inputStream = context.contentResolver.openInputStream(uri)
                            val bytes = inputStream?.readBytes() ?: ByteArray(0)
                            val mimeType = context.contentResolver.getType(uri)
                            val extension = mimeType?.let { MimeTypeMap.getSingleton().getExtensionFromMimeType(it) } ?: "jpg"
                            val uniqueFilename = "${UUID.randomUUID()}.$extension"
                            val requestBody = bytes.toRequestBody("image/*".toMediaTypeOrNull())
                            val body = MultipartBody.Part.createFormData("file", uniqueFilename, requestBody)
                            val response = RetrofitApi.imageService.uploadImage(body)
                            if (response.isSuccessful) {
                                uploadedUrl = response.body()
                            }
                        }

                        val bussiness = Bussiness(
                            id = UUID.randomUUID().toString(),
                            bussinessName = name.trim(),
                            bussinessDescription = description.trim(),
                            bussinessAddress = address.trim(),
                            urlImageBussiness = "https://dkhmzp0m-8000.uks1.devtunnels.ms" + uploadedUrl,
                            offers = emptyList()
                        )
                        vmBussiness.insertBussinessVM(bussiness)
                        name = ""
                        description = ""
                        address = ""
                        imageUri = null
                    }
                },
                enabled = isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Agregar negocio")
            }

            insertedState?.let { success ->
                if (success) {
                    Text(text = "Negocio agregado correctamente", color = MaterialTheme.colorScheme.primary)
                } else {
                    Text(text = "Error al agregar negocio", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}