package com.example.fide_go.navigation

import android.os.Build
import android.provider.ContactsContract.Contacts.Photo
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fide_go.ui.screens.HomeScreen
import com.example.fide_go.ui.screens.LoginAndRegister.ForgotPasswordScreen
import com.example.fide_go.ui.screens.LoginAndRegister.LoginScreen
import com.example.fide_go.ui.screens.LoginAndRegister.RegisterScreen
/*
import com.example.fide_go.ui.screens.Search.FindByEmailScreen
import com.example.fide_go.ui.screens.Search.FindByPhoneScreen
import com.example.fide_go.ui.screens.LoginAndRegister.ForgotPasswordScreen
import com.example.fide_go.ui.screens.HomeScreen
import com.example.fide_go.ui.screens.LoginAndRegister.LoginScreen
import com.example.fide_go.ui.screens.ProfileUser.ProfileScreen
import com.example.fide_go.ui.screens.LoginAndRegister.RegisterPhoneScreen
import com.example.fide_go.ui.screens.LoginAndRegister.RegisterScreen
import com.example.fide_go.ui.screens.ProfileUser.EmailsScreen
import com.example.fide_go.ui.screens.ProfileUser.PhonesScreen
import com.example.fide_go.ui.screens.ProfileUser.SocialNetworksScreen
import com.example.fide_go.ui.screens.ProfileUser.add.EmailsAddScreen
import com.example.fide_go.ui.screens.ProfileUser.add.PhonesAddScreen
import com.example.fide_go.ui.screens.ProfileUser.add.SocialNetworksAddScreen
import com.example.fide_go.ui.screens.ProfileUser.edit.EditPhoto
import com.example.fide_go.ui.screens.ProfileUser.edit.EmailsEditScreen
import com.example.fide_go.ui.screens.ProfileUser.edit.PhonesEditScreen
import com.example.fide_go.ui.screens.ProfileUser.edit.ProfileUserEditScreen
import com.example.fide_go.ui.screens.ProfileUser.edit.SocialNetworksEditScreen
import com.example.fide_go.ui.screens.Results.ResultSearchEmailScreen
import com.example.fide_go.ui.screens.Results.ResultSearchPhoneScreen
import com.example.fide_go.ui.screens.Results.ResultSearchSocialScreen
import com.example.fide_go.ui.screens.Search.FindBySocialNetworkScreen
import com.example.fide_go.ui.screens.Validations.InfoValidationsScreen
import com.example.fide_go.ui.screens.Validations.ValidationOneCheckScreen
import com.example.fide_go.ui.screens.Validations.ValidationOneScreen
import com.example.fide_go.ui.screens.Validations.ValidationOneSuccessScreen
import com.example.fide_go.ui.screens.Validations.ValidationTwoScreen
import com.example.fide_go.ui.screens.Validations.ValidationTwoSuccessScreen
*/
import com.example.fide_go.viewModel.UsersViewModel
import com.example.fide_go.utils.AuthManager
import com.google.firebase.auth.FirebaseUser
import com.example.fide_go.utils.googleAuth.SignInState
import com.example.fide_go.viewModel.BussinessViewModel
import com.example.fide_go.viewModel.EmailViewModel
import com.example.fide_go.viewModel.PhonesViewModel
import com.example.fide_go.viewModel.ProfileViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(
    state: SignInState,
    onSignInClickGoogle: () -> Unit,
    onSignOutGoogle: () -> Unit,
    vmUsers: UsersViewModel,
    vmPhones: PhonesViewModel,
    vmEmails: EmailViewModel,
    vmProfiles: ProfileViewModel,
    vmBussiness: BussinessViewModel
) {
    //Aqui se maneja toda la navegacion entre nuestras pantallas
    //This handles all the navigation between our screens.
    val navController = rememberNavController()
    //Aqui instancio Auth manager para el ingreso a la aplicacion como anonimo
    //Here I instantiate Auth manager to login to the application as anonymous
    val authManager: AuthManager = AuthManager()

    //Aqui recojo al usuario actual que todavia no ha cerrado sesion o que en su defecto es null si no hay usuario con la sesion abierta.
    //Here I get the current user who is not logged out yet or null if there is no user logged in.
    val user: FirebaseUser? = authManager.getCurrentUser()

    NavHost(
        navController = navController,
        startDestination = if(user==null) AppScreen.LoginScreen.route else AppScreen.HomeScreen.route // here we have a conditional, this redirect to screen start depends of user exist or not
    ){
        composable(route=AppScreen.LoginScreen.route){
            LoginScreen(
                navController = navController,
                auth= authManager,
                state = state,
                onSignInClick = onSignInClickGoogle,
                vmUsers=vmUsers
            )
        }

        composable(route=AppScreen.RegisterScreen.route){
            RegisterScreen(
                navController = navController,
                auth = authManager,
                   vmUsers=vmUsers
            )
        }

        composable(route=AppScreen.ForgotPasswordScreen.route){
            ForgotPasswordScreen(
                navController = navController,
                auth = authManager
                )
        }


        composable(route= AppScreen.HomeScreen.route){
            HomeScreen(
                navController = navController,
                auth= authManager,
                onSignOutGoogle= onSignOutGoogle,
                vmUsers= vmUsers,
                vmBussiness =  vmBussiness
            )
        }
        /*
        composable(route=AppScreen.InfoValidationsScreen.route){
            InfoValidationsScreen(
                navController = navController,
                auth= authManager,
                onSignOutGoogle= onSignOutGoogle,
                vmUsers=vmUsers
            )
        }

        composable(route=AppScreen.ValidationOneScreen.route){
            ValidationOneScreen(
                navController = navController,
                auth= authManager,
                onSignOutGoogle= onSignOutGoogle,
                vmUsers=vmUsers
            )
        }


        composable(route=AppScreen.ValidationTwoScreen.route){
            ValidationTwoScreen(
                navController = navController,
                auth= authManager,
                onSignOutGoogle= onSignOutGoogle,
                vmUsers=vmUsers
            )
        }


        composable(route=AppScreen.ValidationOneCheckScreen.route){
            ValidationOneCheckScreen(
                navController = navController,
                auth= authManager,
                vmUsers=vmUsers
            )
        }

        composable(route=AppScreen.ValidationOneSuccessScreen.route){
            ValidationOneSuccessScreen(
                navController = navController,
                auth= authManager,
                vmUsers=vmUsers
            )
        }

        composable(route=AppScreen.ValidationTwoSuccessScreen.route){
            ValidationTwoSuccessScreen(
                navController = navController,
                auth= authManager,
                vmUsers=vmUsers
            )
        }

        composable(route=AppScreen.RegisterPhoneScreen.route){
            RegisterPhoneScreen(
                navController = navController,
                auth= authManager,
                vmUsers=vmUsers,
                vmPhones=vmPhones
            )
        }

        composable(route=AppScreen.ProfileUserScreen.route){
            ProfileScreen(
                navController = navController,
                auth= authManager,
                onSignOutGoogle= onSignOutGoogle,
                vmUsers= vmUsers,
                vmProfiles=vmProfiles
            )
        }

        //GENERAL
        composable(route=AppScreen.FindByEmailScreen.route){
            FindByEmailScreen(
                navController = navController,
                auth= authManager,
                onSignOutGoogle= onSignOutGoogle,
                vmUsers= vmUsers
            )
        }

        composable(route=AppScreen.FindByPhoneScreen.route){
            FindByPhoneScreen(
                navController = navController,
                auth= authManager,
                onSignOutGoogle= onSignOutGoogle,
                vmUsers= vmUsers
            )
        }

        composable(route=AppScreen.FindBySocialNetworkScreen.route){
            FindBySocialNetworkScreen(
                navController = navController,
                auth= authManager,
                onSignOutGoogle= onSignOutGoogle,
                vmUsers= vmUsers
            )
        }

        composable(route=AppScreen.ResultSearchPhoneScreen.route){
            ResultSearchPhoneScreen(
                navController = navController,
                auth= authManager,
                onSignOutGoogle= onSignOutGoogle,
                vmUsers= vmUsers
            )
        }

        composable(route=AppScreen.ResultSearchEmailScreen.route){
            ResultSearchEmailScreen(
                navController = navController,
                auth= authManager,
                onSignOutGoogle= onSignOutGoogle,
                vmUsers= vmUsers
            )
        }

        composable(route=AppScreen.ResultSearchSocialScreen.route){
            ResultSearchSocialScreen(
                navController = navController,
                auth = authManager,
                onSignOutGoogle = onSignOutGoogle,
                vmUsers = vmUsers
            )
        }

        //PROFILE
        composable(route=AppScreen.ProfileUserEditScreen.route){
            ProfileUserEditScreen(
                navController = navController,
                auth = authManager,
                onSignOutGoogle = onSignOutGoogle,
                vmUsers = vmUsers,
                vmProfile=vmProfiles
            )
        }

            //EMAIL-PROFILE
        composable(route=AppScreen.EmailsScreen.route){
            EmailsScreen(
                navController = navController,
                auth = authManager,
                onSignOutGoogle = onSignOutGoogle,
                vmUsers = vmUsers,
                vmEmails=vmEmails
            )
        }

        composable(route=AppScreen.EmailsEditScreen.route){
            EmailsEditScreen(
                navController = navController,
                auth = authManager,
                onSignOutGoogle = onSignOutGoogle,
                vmUsers = vmUsers,
                vmEmails=vmEmails
            )
        }

        composable(route=AppScreen.EmailsAddScreen.route){
            EmailsAddScreen(
                navController = navController,
                auth = authManager,
                onSignOutGoogle = onSignOutGoogle,
                vmUsers = vmUsers,
                vmEmails=vmEmails,
                vmProfiles=vmProfiles
            )
        }

            //PHONE-PROFILE
        composable(route=AppScreen.PhonesScreen.route){
            PhonesScreen(
                navController = navController,
                auth = authManager,
                onSignOutGoogle = onSignOutGoogle,
                vmUsers = vmUsers,
                vmPhones=vmPhones
            )
        }

        composable(route=AppScreen.PhonesEditScreen.route){
            PhonesEditScreen(
                navController = navController,
                auth = authManager,
                onSignOutGoogle = onSignOutGoogle,
                vmUsers = vmUsers,
                vmPhones=vmPhones
            )
        }

        composable(route=AppScreen.PhonesAddScreen.route){
            PhonesAddScreen(
                navController = navController,
                auth = authManager,
                onSignOutGoogle = onSignOutGoogle,
                vmUsers = vmUsers,
                vmPhones=vmPhones,
                vmProfiles=vmProfiles
            )
        }

            //SOCIAL NETWORKS-PROFILE
        composable(route=AppScreen.SocialNetworksScreen.route){
            SocialNetworksScreen(
                navController = navController,
                auth = authManager,
                onSignOutGoogle = onSignOutGoogle,
                vmUsers = vmUsers,
                vmSocial=vmSocialNetworks
            )
        }

        composable(route=AppScreen.SocialNetworksEditScreen.route){
            SocialNetworksEditScreen(
                navController = navController,
                auth = authManager,
                onSignOutGoogle = onSignOutGoogle,
                vmUsers = vmUsers,
                vmSocial=vmSocialNetworks
            )
        }

        composable(route=AppScreen.SocialNetworksAddScreen.route){
            SocialNetworksAddScreen(
                navController = navController,
                auth = authManager,
                onSignOutGoogle = onSignOutGoogle,
                vmUsers = vmUsers,
                vmSocial=vmSocialNetworks,
                vmProfiles=vmProfiles
            )
        }

        composable(route=AppScreen.ProfileUserPhotoEditScreen.route){
            EditPhoto(
                navController = navController,
                auth = authManager,
                onSignOutGoogle = onSignOutGoogle,
                vmUsers = vmUsers,
                vmProfiles=vmProfiles
            )
        }*/

    }
}