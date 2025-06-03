package com.example.fide_go.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fide_go.data.model.Email
import com.example.fide_go.data.model.Phone
import com.example.fide_go.navigation.AppScreen
import com.example.fide_go.ui.theme.AppColors
import com.example.fide_go.ui.theme.TextSizes
import com.example.fide_go.utils.AuthManager
import com.example.fide_go.viewModel.EmailViewModel
import com.example.fide_go.viewModel.PhonesViewModel
import com.example.fide_go.viewModel.UsersViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    auth: AuthManager,
    vmUsers: UsersViewModel,
    vmPhones: PhonesViewModel,
    vmEmails: EmailViewModel
) {
    val userState by vmUsers.user.collectAsState()

    var username by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    LaunchedEffect(userState) {
        userState?.let {
            username = it.username ?: ""
            phone = it.phone.phoneNumber
            email = it.email.email
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppColors.mainFide),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = AppColors.whitePerlaFide)
                    }
                },
                title = { Text("Editar Perfil", color = AppColors.whitePerlaFide, fontSize = TextSizes.H3) }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = AppColors.mainFide,
                modifier = Modifier.height(44.dp)
            ) {
                Text(
                    text = "\u00A9 Fide-Go",
                    fontSize = TextSizes.Footer,
                    color = AppColors.whitePerlaFide,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.whitePerlaFide)
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            TextField(
                label = { Text("Nombre") },
                value = username,
                onValueChange = { username = it },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                label = { Text("Correo") },
                value = email,
                onValueChange = { email = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                label = { Text("Teléfono") },
                value = phone,
                onValueChange = { phone = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    userState?.let {
                        val updatedPhone = Phone(it.phone.id, phone, it.phone.isVerified, it.phone.idProfileUser)
                        val updatedEmail = Email(it.email.id, email, it.email.isVerified, it.email.idProfileUser)
                        vmPhones.updatePhoneVM(updatedPhone)
                        vmEmails.updateEmailVM(updatedEmail)
                        val updatedUser = it.copy(username = username)
                        vmUsers.updateUserVM(updatedUser)
                        vmUsers.getUserByEmail(updatedEmail.email)
                    }
                    navController.navigate(AppScreen.HomeScreen.route)
                },
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.FocusFide),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Guardar", color = AppColors.whitePerlaFide)
            }
        }
    }
}