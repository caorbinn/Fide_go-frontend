package com.example.fide_go.ui.screens.LoginAndRegister

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.example.fide_go.data.model.Email
import com.example.fide_go.data.model.Phone
import com.example.fide_go.data.model.Profile
import com.example.fide_go.data.model.User
import com.example.fide_go.ui.theme.AppColors
import com.example.fide_go.ui.theme.TextSizes
import com.example.fide_go.utils.AuthManager
import com.example.fide_go.utils.AuthRes
import com.example.fide_go.viewModel.UsersViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RegisterScreen(
    navController: NavController,
    auth: AuthManager,
    vmUsers: UsersViewModel
){
    //VARIABLES Y CONSTANTES
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val isLoading by vmUsers.isLoadingRegister.collectAsState()
    var enable = true

    val scope = rememberCoroutineScope()


    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppColors.mainFide),
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector= Icons.Default.ArrowBack,
                            contentDescription="ArrowBack",
                            tint = AppColors.whitePerlaFide
                        )
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
                .padding(24.dp)
        ){
            if (isLoading==true) {
                enable = false
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(18.dp))
                //Logo
                Image(
                    painter = painterResource(id = R.drawable.nombre_edentifica),
                    contentDescription = "Logo eDentifica",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp), // ajusta la altura según sea necesario
                    contentScale = ContentScale.Crop // Escala de la imagen
                )
                Spacer(modifier = Modifier.height(8.dp))

                //title
                Text(
                    text = stringResource(R.string.crear_una_cuenta),
                    color = AppColors.mainFide,
                    fontSize = TextSizes.H1
                )

                //field name
                Spacer(modifier = Modifier.height(40.dp))
                TextField(
                    label = {
                        Text(
                            text = stringResource(R.string.nombre),
                            fontSize = TextSizes.Paragraph
                            )
                        },
                    value = username,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    onValueChange = { username = it }
                )

                //field phone
                Spacer(modifier = Modifier.height(20.dp))
                TextField(
                    label = {
                        Text(
                            text = stringResource(R.string.n_mero_de_tel_fono),
                            fontSize = TextSizes.Paragraph
                        )
                    },
                    value = phone,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    onValueChange = { phone = it },
                    placeholder = {Text("34xxxxxxxxx")}
                )

                //field email
                Spacer(modifier = Modifier.height(20.dp))
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

                //field password
                Spacer(modifier = Modifier.height(20.dp))
                TextField(
                    label = {
                        Text(
                            text = stringResource(R.string.contrase_a_m_n_6_caracteres),
                            fontSize = TextSizes.Paragraph
                        )
                    },
                    value = password,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    onValueChange = { password = it }
                )

                //button signUp
                Spacer(modifier = Modifier.height(30.dp))
                Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                    Button(
                        onClick = {
                            scope.launch {

                                signUp(username, phone, email, password, auth, context, navController,vmUsers)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AppColors.FocusFide),
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        enabled = enable
                    ) {
                        Text(text = stringResource(R.string.registrate))
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                ClickableText(
                    text = AnnotatedString(stringResource(R.string.ya_tienes_una_cuenta_inicia_sesion)),
                    onClick = {
                        navController.popBackStack()
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
    }

}

private suspend fun signUp(
    username: String,
    phone: String,
    email: String,
    password: String,
    auth: AuthManager,
    context: Context,
    navController: NavController,
    vmUsers: UsersViewModel
) {
    if(username.isNotEmpty() && phone.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()){
        val userToInsert: User = User(
            null,
            username,
            Phone(null,phone,null,null),
            Email(null,email,null,null),
            Profile(null,"","",null)
        )




        when(val result = auth.createUserWithEmailandPassword(email, password)){
            // agregar la linea de retrofit para insertar el user en la base de datos
            is AuthRes.Succes ->{
                // Llama a la función del ViewModel para insertar el usuario y espera a que se complete
                val isUserInserted = coroutineScope {
                    async { vmUsers.insertUserVm(userToInsert) }
                }.await()

                Toast.makeText(context,
                    context.getString(R.string.registro_con_exito), Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            }
            is AuthRes.Error ->{
                Toast.makeText(context, "Error SignUp: ${result.errorMessage}", Toast.LENGTH_SHORT).show()
            }
        }

    }else{
        Toast.makeText(context, context.getString(R.string.hay_campos_vacios), Toast.LENGTH_SHORT).show()
    }
}
