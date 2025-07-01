package com.example.fide_go.ui.screens.LoginAndRegister

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fide_go.R
import com.example.fide_go.navigation.AppScreen
import com.example.fide_go.ui.theme.AppColors
import com.example.fide_go.ui.theme.TextSizes
import com.example.fide_go.utils.AuthManager
import com.example.fide_go.utils.AuthRes
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ForgotPasswordScreen(navController: NavController, auth: AuthManager /*altaUsuarioViewModel: AltaUsuarioViewModel*/){

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppColors.mainFide),
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(imageVector= Icons.Default.ArrowBack, contentDescription="ArrowBack", tint = AppColors.whitePerlaFide)
                    }
                },
                title = {
                    Text(
                        text = stringResource(R.string.inicio_de_sesion),
                        fontSize = TextSizes.H3,
                        color= AppColors.whitePerlaFide
                    )
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
                    color= AppColors.whitePerlaFide,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                )
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.whitePerlaFide) //Color de fondo de la aplicacion
                .padding(8.dp)
        ){
            BodyContentForgotPassword(navController, auth)
        }
    }
}

@Composable
fun BodyContentForgotPassword(navController: NavController, auth: AuthManager) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        FormularioForgotPassword(Modifier.align(Alignment.Center), navController, auth)
    }
}

@Composable
fun FormularioForgotPassword(
    align: Modifier,
    navController: NavController,
    auth: AuthManager
) {
    //VARIABLES
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column(
        modifier = align.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(102.dp))
        //Logo
        Image(
            painter = painterResource(id = R.drawable.fideblanco),
            contentDescription = "Logo eDentifica",
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp), // ajusta la altura según sea necesario
            contentScale = ContentScale.Crop // Escala de la imagen
        )
        Spacer(modifier = Modifier.height(60.dp))

        //Title
        Text(
            text = stringResource(R.string.has_olvidado_tu_contrasena),
            color = AppColors.mainFide,
            fontSize = TextSizes.H1,
            textAlign = TextAlign.Center,
        )

        //field email
        Spacer(modifier = Modifier.height(50.dp))
        TextField(
            label = {
                Text(
                    text = stringResource(R.string.correo),
                    fontSize = TextSizes.Paragraph
                )
            },
            value = email,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            onValueChange = { email = it },
            placeholder = {Text(stringResource(R.string.ejemplo_gmail_com))}
        )

        //button send email
        Spacer(modifier = Modifier.height(30.dp))
        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
            Button(
                onClick = {
                    scope.launch {
                        when(val res = auth.resetPassword(email)) {
                            is AuthRes.Succes -> {
                                Toast.makeText(context,
                                    context.getString(R.string.se_ha_enviado_un_correo_para_restablecer_la_contrasena), Toast.LENGTH_SHORT).show()
                                navController.navigate(AppScreen.LoginScreen.route)
                            }
                            is AuthRes.Error -> {
                                Toast.makeText(context,
                                    context.getString(R.string.error_al_enviar_el_correo), Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.FocusFide),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = stringResource(R.string.recuperar_contrasena),
                    fontSize = TextSizes.H3,
                    color = AppColors.whitePerlaFide
                )
            }
        }
    }
}