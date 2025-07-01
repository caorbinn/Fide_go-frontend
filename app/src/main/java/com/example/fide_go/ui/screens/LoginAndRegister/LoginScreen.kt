package com.example.fide_go.ui.screens.LoginAndRegister

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fide_go.R
import com.example.fide_go.navigation.AppScreen
import com.example.fide_go.ui.theme.AppColors
import com.example.fide_go.ui.theme.TextSizes
import com.example.fide_go.utils.AuthManager
import com.example.fide_go.utils.AuthRes
import com.example.fide_go.utils.googleAuth.SignInState
import com.example.fide_go.viewModel.UsersViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import android.provider.Settings
import androidx.compose.material.AlertDialog
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun LoginScreen(
    navController: NavController,
    auth: AuthManager,
    state: SignInState,
    onSignInClick: () -> Unit,
    vmUsers: UsersViewModel
){
    //VARIABLES Y CONSTANTES
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    //Esta funcion muestra cuando hay un error en el inicio de sesion de google
    LaunchedEffect(key1 = state.signInError) {
        state.signInError?.let{error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    Scaffold (
        bottomBar = {
            BottomAppBar(
                contentColor = AppColors.whitePerlaFide,
                modifier = Modifier.height(44.dp),
                backgroundColor = AppColors.darkFide
            ) {
                Text(
                    text = stringResource(R.string.copyright),
                    fontSize = TextSizes.Footer,
                    fontStyle = FontStyle.Italic,
                    color= AppColors.whitePerlaFide,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                )
            }
        }
    ){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.whitePerlaFide) //Color de fondo de la aplicacion
                .padding(8.dp)
        ){
            BodyContent(navController,scope,auth,context,onSignInClick, vmUsers)
        }
    }
}

@Composable
fun BodyContent(
    navController: NavController,
    scope: CoroutineScope,
    auth: AuthManager,
    context: Context,
    onSignInClick: () -> Unit,
    vmUsers: UsersViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        FormularioLogin(Modifier.align(Alignment.Center), navController,scope,auth,context,onSignInClick, vmUsers)
    }
}


class FirstTimePreference(context: Context) {
    private val preferences = context.getSharedPreferences("first_time_pref", Context.MODE_PRIVATE)

    fun isFirstTime(): Boolean {
        return preferences.getBoolean("isFirstTime", true)
    }

    fun setFirstTime(isFirstTime: Boolean) {
        preferences.edit().putBoolean("isFirstTime", isFirstTime).apply()
    }
}



@Composable
fun FormularioLogin(
    align: Modifier,
    navController: NavController,
    scope: CoroutineScope,
    auth: AuthManager,
    context: Context,
    onSignInClick: () -> Unit,
    vmUsers: UsersViewModel
) {
    //VARIABLES Y CONSTANTES
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val firstTimePreference = remember { FirstTimePreference(context) }
    var showPermissionDialog by remember { mutableStateOf(firstTimePreference.isFirstTime())  }

    if(showPermissionDialog){
        AlertDialog(
            onDismissRequest = { showPermissionDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", context.packageName, null)
                        intent.data = uri
                        context.startActivity(intent)

                        showPermissionDialog = false
                        firstTimePreference.setFirstTime(false)
                    }
                ) {
                    Text(stringResource(R.string.ir_a_ajustes))
                }
            },
            dismissButton = {
                TextButton(onClick = { showPermissionDialog = false }) {
                    Text("cancelar")
                }
            },
            title = { Text("permisos requeridos") },
            text = { Text("los_permisos_de_c_mara_y_notificaciones_son_necesarios_para_el_correcto_funcionamiento_de_la_aplicaci_n_por_favor_habilite_estos_permisos_en_la_configuraci_n_de_la_aplicaci_n") }
        )
    }

    Column(
        modifier = align.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //Logo
        Image(
            painter = painterResource(id = R.drawable.fideblanco),
            contentDescription = "Logo Fide_go",
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp), // ajusta la altura según sea necesario
            contentScale = ContentScale.Crop // Escala de la imagen
        )

        // Campo de email - Field email
        Spacer(modifier = Modifier.height(34.dp))
        TextField(
            label = { Text(text = "correo", fontSize = TextSizes.Paragraph) },
            value = email,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            onValueChange = { email = it },
        )

        // Campo de password - Field password
        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            label = { Text(text = "password",fontSize = TextSizes.Paragraph) },
            value = password,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onValueChange = { password = it })

        // Botón de inicio de sesión - Login Button
        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)
        ) {
            Button(
                onClick = {
                    scope.launch {
                        emailPassSignIn(email, password, auth, context, navController, vmUsers)
                    }
                },
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(contentColor = AppColors.goldFide),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = "iniciar sesion",
                    fontSize = TextSizes.H3,
                    color = AppColors.whitePerlaFide)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Enlace de "¿Olvidaste tu contraseña?"
        ClickableText(
            text = AnnotatedString("has olvidado tu contraseña"),
            modifier = Modifier.padding(vertical = 8.dp),
            onClick = {
                navController.navigate(route = AppScreen.ForgotPasswordScreen.route)
            },
            style = TextStyle(
                fontSize = TextSizes.Paragraph,
                fontFamily = FontFamily.Default,
                textDecoration = TextDecoration.Underline,
                color = AppColors.secondaryFide
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Separador con texto "ó"
        Row(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Divider(
                modifier = Modifier.weight(1f),
                color = AppColors.darkFide,
                thickness = 1.dp
            )
            Text(
                text = "ó",
                modifier = Modifier.padding(horizontal = 8.dp),
                fontSize = TextSizes.Paragraph
            )
            Divider(
                modifier = Modifier.weight(1f),
                color = AppColors.darkFide,
                thickness = 1.dp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

//        // Botón de inicio de sesión con Google
//        SocialMediaButton(
//            onClick = onSignInClick,
//            text = "continuar con google",
//            icon = R.drawable.nombre_edentifica,
//            color = Color(0xFFF1F1F1)
//        )
//
//        Spacer(modifier = Modifier.height(8.dp))

        // Botón de inicio de sesión como invitado
        SocialMediaButton(
            onClick = {
                scope.launch{
                    incognitoSignIn(auth, context, navController)
                }
            },
            text = "continuar como invitado",
            icon = R.drawable.user,
            color = AppColors.grayFide
        )

        Spacer(modifier = Modifier.height(22.dp))

        // Enlace de "¿No tienes una cuenta? Regístrate aquí"
        ClickableText(
            text = AnnotatedString(stringResource(R.string.no_tiene_cuenta_registrate)),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(Alignment.CenterHorizontally),
            onClick= {
                navController.navigate(route = AppScreen.RegisterScreen.route)
            },
            style = TextStyle(
                fontSize = TextSizes.Paragraph,
                fontFamily = FontFamily.Default,
                textDecoration = TextDecoration.Underline,
                color = AppColors.secondaryFide
            )
        )
    }
}

//Function to login with email and password
private suspend fun emailPassSignIn(
    email: String,
    password: String,
    auth: AuthManager,
    context: Context,
    navController: NavController,
    vmUsers: UsersViewModel
) {
    if(email.isNotEmpty() && password.isNotEmpty()) {
        when (val result = auth.signInWithEmailandPassword(email, password)) {
            is AuthRes.Succes-> {
                navController.navigate(AppScreen.HomeScreen.route) {
                    popUpTo(AppScreen.LoginScreen.route) {
                        inclusive = true
                    }
                }

            }

            is AuthRes.Error -> {
                Toast.makeText(context, "Error Login: ${result.errorMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    } else {
        Toast.makeText(
            context,
            "There are empty fields",
            Toast.LENGTH_SHORT
        ).show()
    }
}

//Function to login how guest
private suspend fun incognitoSignIn(auth: AuthManager, context: Context, navController: NavController) {
    when(val result = auth.signInAnonymously()){
        is AuthRes.Succes ->{
            navController.navigate(AppScreen.HomeScreen.route){
                popUpTo(AppScreen.LoginScreen.route){
                    inclusive= true
                }
            }
        }
        is AuthRes.Error ->{
            Log.d("Error modo invitado","Este error se produce cuando un usuario quiere acceder como invitado en la app")
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SocialMediaButton(onClick: () -> Unit, text: String, icon: Int, color: Color) {
    var click by remember { mutableStateOf(false) }
    Surface(
        onClick = onClick,
        modifier = Modifier
            .padding(start = 40.dp, end = 40.dp)
            .clickable { click = !click },
        shape = RoundedCornerShape(50),
        border = BorderStroke(width = 1.dp, color = if(icon == R.drawable.user) color else Color.White),
        color = color
    ) {
        Row(
            modifier = Modifier
                .padding(start = 12.dp, end = 16.dp, top = 12.dp, bottom = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = icon),
                modifier = Modifier.size(24.dp),
                contentDescription = text,
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "$text", color = if(icon == R.drawable.user) Color.White else Color.White)
            click = true
        }
    }
}