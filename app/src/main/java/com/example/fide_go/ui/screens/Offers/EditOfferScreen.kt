package com.example.fide_go.ui.screens.Offers

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.NavController
import com.example.fide_go.utils.AuthManager
import com.example.fide_go.viewModel.BussinessViewModel
import com.example.fide_go.viewModel.OffersViewModel
import com.example.fide_go.viewModel.UsersViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditOfferScreen(
    navController: NavController,
    auth: AuthManager,
    onSignOutGoogle: () -> Unit,
    vmUsers: UsersViewModel,
    vmOffers: OffersViewModel,
    vmBussiness: BussinessViewModel,
    offerId: String
) {
    LaunchedEffect(offerId) {
        vmOffers.saveOfferEditVM(offerId)
    }
    OffersScreen(
        navController = navController,
        auth = auth,
        onSignOutGoogle = onSignOutGoogle,
        vmUsers = vmUsers,
        vmOffers = vmOffers,
        vmBussiness = vmBussiness,
        isEditMode = true
    )
}
