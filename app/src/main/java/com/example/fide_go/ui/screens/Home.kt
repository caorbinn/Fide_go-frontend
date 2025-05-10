package com.example.fide_go.ui.screens

import  android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.getValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.fide_go.R
import com.example.fide_go.data.model.Email
import com.example.fide_go.data.model.Phone
import com.example.fide_go.data.model.Profile
import com.example.fide_go.navigation.AppScreen
import com.example.fide_go.viewModel.UsersViewModel
import com.example.fide_go.utils.AuthManager
import com.example.fide_go.data.model.User
//------import com.example.fide_go.ui.screens.Validations.BodyContentValidationOne
import com.example.fide_go.ui.theme.AppColors
import com.example.fide_go.ui.theme.TextSizes
import com.google.firebase.auth.FirebaseUser
import kotlin.coroutines.suspendCoroutine


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navController: NavController,
    auth: AuthManager,
    onSignOutGoogle: () -> Unit,
    vmUsers: UsersViewModel,
) {
    // VARIABLES Y CONSTANTES
    //para mostrar el dialogo de cerrar Sesion
    var showDialog by remember { mutableStateOf(false) }

    //recojo al user Actual
    val currentUser = auth.getCurrentUser()

    // Llama a getUserByEmail cuando se inicia HomeScreen
    LaunchedEffect(Unit) {
        val userEmail = auth.getCurrentUser()?.email
        if (userEmail != null) {
            vmUsers.getUserByEmail(userEmail)
        }
    }

    // Observa el flujo de usuario en el ViewModel
    val userState by vmUsers.user.collectAsState()
    val isLoading by vmUsers.isLoading.collectAsState()

    //si el user es existe le pregunto si ya esta validado
    if(userState != null){

    /*    //Si no tiene ninguna validacion lo envio a las validaciones
        if(userState?.validations?.get(0)?.isValidated==false || userState?.validations?.get(1)?.isValidated==false) { // importante modificacion en home

            navController.navigate(AppScreen.InfoValidationsScreen.route) {
                popUpTo(AppScreen.HomeScreen.route) {
                    inclusive = true
                }
            }
        }
*/
    }else {
        if(currentUser?.email != null) {// si el usuario existe en firebase y no existe en la base de datos lo inserto en la base de datos
            val userToInsert: User = User(
                null,
                null,
                auth.getCurrentUser()?.displayName.toString(),
                "",
                Phone(null,auth.getCurrentUser()?.phoneNumber.toString(),false,null),
                Email(null,auth.getCurrentUser()?.email.toString(),false,null),
                Profile(null,"",auth.getCurrentUser()?.photoUrl.toString(),null),
                null,
                null
            )
            // Llama a la función del ViewModel para insertar el usuario y espera a que se complete
            LaunchedEffect (Unit) {
                vmUsers.insertUserVm(userToInsert)
                vmUsers.getUserByEmail(auth.getCurrentUser()?.email.toString())
            }
        }

    }


    val onLogoutConfirmed:()->Unit = {
        auth.signOut()
        onSignOutGoogle()

        navController.navigate(AppScreen.LoginScreen.route){
            popUpTo(AppScreen.HomeScreen.route){
                inclusive= true
            }
        }
    }

    //Estructura de la pantalla
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppColors.mainFide),
                title = {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                            if(auth.getCurrentUser()?.email!=null && userState?.validations?.get(0)?.isValidated ==true ){
                                userState?.profile?.urlImageProfile?.let {
                                    ClickableProfileImage(
                                        navController = navController,
                                        imageUrl = it
                                    ) {
                                        //----navController.navigate(AppScreen.ProfileUserScreen.route)
                                    }
                                }
                            }

                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Spacer(modifier = Modifier.width(8.dp))
                            (if(!currentUser?.displayName.isNullOrEmpty() || userState!=null) userState?.name?.let {
                                stringResource(
                                    R.string.hola, it
                                )
                            } else stringResource(R.string.bienvenid))?.let {
                                Text(
                                    text = it,//welcomeMessage,
                                    fontSize = TextSizes.H3,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = AppColors.whitePerlaFide
                                )
                            }
                            (if(!currentUser?.email.isNullOrEmpty()|| userState!=null) userState?.email?.email else stringResource(
                                R.string.usuario_anonimo
                            ))?.let {
                                Text(
                                    text = it,
                                    fontSize = TextSizes.Footer,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = AppColors.whitePerlaFide
                                )
                            }
                        }

                    }
                },
                actions = {
                    //Botton Home
                    IconButton(
                        onClick = {
                            navController.navigate(AppScreen.HomeScreen.route)
                        }
                    ) {
                        Icon(
                            Icons.Outlined.Home,
                            contentDescription = "Home",
                            tint = AppColors.whitePerlaFide
                        )
                    }
                    //boton de accion para salir cerrar sesion
                    IconButton(
                        onClick = {
                            showDialog = true
                        }
                    ) {
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
                    fontStyle = FontStyle.Italic,
                    color = AppColors.whitePerlaFide,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                )
            }
        }
    ) {
        //funcion para mostrar un pop up preguntando si quiere cerrar la sesion
        contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            if (showDialog) {
                LogoutDialog(
                    onConfirmLogout = {
                        onLogoutConfirmed()
                        showDialog = false
                    },
                    onDismiss = { showDialog = false })
            }

        }

        //funcion composable que pinta el contenido de home
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.whitePerlaFide) //Color de fondo de la aplicacion
                .padding(24.dp)
        ){
            if (isLoading) {
                // Mostrar CircularProgressIndicator mientras carga
                CircularProgressIndicator(
                    color = AppColors.FocusFide,
                    strokeWidth = 4.dp,
                    modifier = Modifier.align(Alignment.Center).size(48.dp)
                )
            } else {
                // Mostrar el contenido de Home cuando la carga finaliza
                BodyContentHome(navController, vmUsers, userState)
            }
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BodyContentHome(
    navController: NavController,
    vmUsers: UsersViewModel,
    userState: User?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //Title
        if(userState != null){
            Text(
                text = stringResource(R.string.bienvenido_a_fidego),
                fontSize = TextSizes.title,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold, // Aumenta el grosor del texto
                fontFamily = FontFamily.Cursive, // Aplica la fuente personalizada
                modifier = Modifier.padding(bottom = 16.dp),
                color = AppColors.mainFide
            )
        }else{
            Text(
                text = stringResource(R.string.fidego),
                fontSize = TextSizes.title,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold, // Aumenta el grosor del texto
                fontFamily = FontFamily.Cursive, // Aplica la fuente personalizada
                modifier = Modifier.padding(bottom = 16.dp),
                color = AppColors.mainFide
            )
        }


        //Image
        Image(
            painter = painterResource(id = R.drawable.nombre_edentifica),
            contentDescription = "search",
            modifier = Modifier
                .fillMaxWidth()
                .scale(0.7f)
                .padding(0.dp), // ajusta la altura según sea necesario
            contentScale = ContentScale.Crop // Escala de la imagen
        )

        Spacer(modifier = Modifier.height(10.dp))
        //Title
        Text(
            text = "Texto de prueba 1",
            fontSize = TextSizes.H2,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )


        //Button correo
        Spacer(modifier = Modifier.height(10.dp))
        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
            Button(
                onClick = {
//                    navController.navigate(AppScreen.FindByEmailScreen.route)
                },
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.FocusFide),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
//                Text(text = stringResource(R.string.buscar_correo))
                Text("buscar correo")
            }
        }

        //Button telefono
        Spacer(modifier = Modifier.height(34.dp))
        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
            Button(
                onClick = {
//                    navController.navigate(AppScreen.FindByPhoneScreen.route)
                },
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.FocusFide),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
//                Text(text = stringResource(R.string.buscar_telefono))
                Text("buscar telefono")

            }
        }


    }
}

/**
 * Imagen de perfil clikeable
 */
@Composable
fun ClickableProfileImage(
    navController: NavController,  // Assuming you're using NavController for navigation
    imageUrl: String,
    onClick: () -> Unit  // Define your click action
) {
    if(!imageUrl.equals("")){
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .size(40.dp)
                .clickable { onClick() }
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Imagen",
                placeholder = painterResource(id = R.drawable.nombre_edentifica),
                contentScale = ContentScale.Crop,
                modifier = Modifier.clip(CircleShape)
            )
        }
    }else{
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .size(40.dp)
                .clickable { onClick() }
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(R.drawable.nombre_edentifica)
                    .crossfade(true)
                    .build(),
                contentDescription = "Imagen",
                placeholder = painterResource(id = R.drawable.nombre_edentifica),
                contentScale = ContentScale.Crop,
                modifier = Modifier.clip(CircleShape)
            )
        }
    }


}

/**
 * Funcion composable que se encarga de mostrar un alert para preguntar al
 * usuario si quiere continuar o cerrar sesion
 */
@Composable
fun LogoutDialog(
    onConfirmLogout: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        containerColor = AppColors.whitePerlaFide,
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.cerrar_sesi_n), color = AppColors.mainFide) },
        text = { Text(stringResource(R.string.est_s_seguro_que_deseas_cerrar_sesi_n),color = AppColors.mainFide) },
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
