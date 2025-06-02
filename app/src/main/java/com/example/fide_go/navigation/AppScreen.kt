package com.example.fide_go.navigation

sealed class AppScreen(val route: String){
    //Definimos las pantallas de nuestra aplicacion
    //We define the screens of our application
    object LoginScreen: AppScreen("login_screen")

    object RegisterScreen: AppScreen("register_screen")

    object ForgotPasswordScreen: AppScreen("forgot_password_screen")

    object HomeScreen: AppScreen("home_screen")

    object BussinessScreen: AppScreen("bussiness_screen/{id}"){
        fun createRoute(id: String) = "bussiness_screen/$id"
    }

    object OffersScreen: AppScreen("offers_screen")

    /*

    object RegisterPhoneScreen: AppScreen("register_phone")

    object ProfileUserScreen: AppScreen("profile_user")

    object FindByEmailScreen: AppScreen("find_by_email")

    object FindByPhoneScreen: AppScreen("find_by_phone")

    object FindBySocialNetworkScreen: AppScreen("find_by_social_network")

    object ResultSearchPhoneScreen: AppScreen("result_search_phone")

    object ResultSearchEmailScreen: AppScreen("result_search_email")

    object ResultSearchSocialScreen: AppScreen("result_search_social_network")

    object EmailsScreen: AppScreen("emails_user")

    object EmailsEditScreen: AppScreen("emails_edit_user")

    object EmailsAddScreen: AppScreen("emails_add_user")

    object PhonesScreen: AppScreen("phones_user")

    object PhonesEditScreen: AppScreen("phones_edit_user")

    object PhonesAddScreen: AppScreen("phones_add_user")

    object SocialNetworksScreen: AppScreen("social_networks_user")

    object SocialNetworksEditScreen: AppScreen("social_networks_edit_user")

    object SocialNetworksAddScreen: AppScreen("social_networks_add_user")

    object ProfileUserEditScreen: AppScreen("profile_user_edit_user")

    object ProfileUserPhotoEditScreen: AppScreen("photo_edit")

    object ValidationOneSuccessScreen: AppScreen("validation_one_success")

    object ValidationTwoScreen: AppScreen("validation_two")

    object ValidationTwoSuccessScreen: AppScreen("validation_two_success")

    object InfoValidationsScreen: AppScreen("info_validations")*/

}