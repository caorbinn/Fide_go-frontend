package com.example.fide_go.ui.screens.Scan

import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fide_go.data.model.QrPurchase
import com.example.fide_go.navigation.AppScreen
import com.example.fide_go.utils.AuthManager
import com.example.fide_go.viewModel.PurchaseViewModel
import com.example.fide_go.viewModel.UsersViewModel
import com.google.gson.Gson
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScanQrScreen(
    navController: NavController,
    auth: AuthManager,
    vmUsers: UsersViewModel,
    vmPurchase: PurchaseViewModel
) {
    val context = LocalContext.current
    val userState by vmUsers.user.collectAsState()

    LaunchedEffect(Unit) {
        auth.getCurrentUser()?.email?.let { vmUsers.getUserByEmail(it) }
    }

    val launcher = rememberLauncherForActivityResult(ScanContract()) { result ->
        if (result.contents != null) {
            try {
                val qr = Gson().fromJson(result.contents, QrPurchase::class.java)
                userState?.id?.let { vmPurchase.registerPurchase(it, qr) }
            } catch (e: Exception) {
                Toast.makeText(context, "QR no válido", Toast.LENGTH_LONG).show()
            }
        }
    }

    val saved by vmPurchase.purchaseSaved.collectAsState()
    if (saved == true) {
        Toast.makeText(context, "Compra registrada", Toast.LENGTH_LONG).show()
        vmPurchase.resetState()
        navController.navigate(AppScreen.HomeScreen.route) { popUpTo(AppScreen.HomeScreen.route) { inclusive = true } }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { launcher.launch(ScanOptions()) }, modifier = Modifier.padding(16.dp)) {
            Text(text = "Escanear QR")
        }
    }
}