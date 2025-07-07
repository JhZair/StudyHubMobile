package com.studyhubmobile.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.studyhubmobile.R
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.navigation.NavController
import androidx.compose.ui.graphics.Color as ComposeColor

// Modelos
import com.studyhubmobile.models.LoginRequest
import com.studyhubmobile.models.LoginResponse
import com.studyhubmobile.models.User


import com.studyhubmobile.utils.hacerLoginSimple


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf<String?>(null) }


    Scaffold(
        modifier = Modifier.background(Color(0xFF0f172a))
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "StudyHub Logo",
                modifier = Modifier.size(200.dp),
                tint = ComposeColor.Unspecified
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedButton(
                onClick = {
                    // TODO: Implementar inicio de sesión con Google
                    navController.navigate("home")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = ComposeColor(0xFF0f172a)
                )
            ) {
                Text(
                    text = "Continuar con Google",
                    color = ComposeColor(0xFF0f172a)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "O",
                color = Color.Black,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico", color = Color.White) },
                leadingIcon = { Icon(Icons.Default.Email, "email", tint = Color.White) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF1e293b),
                    unfocusedContainerColor = Color(0xFF1e293b),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña", color = Color.White) },
                leadingIcon = { Icon(Icons.Default.Lock, "lock", tint = Color.White) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF1e293b),
                    unfocusedContainerColor = Color(0xFF1e293b),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    isLoading = true
                    errorMsg = null

                    hacerLoginSimple(
                        email = email,
                        password = password,
                        onResult = { user ->
                            isLoading = false
                            println("Login exitoso: ${user?.nombre}")
                            navController.navigate("home") // o la ruta que quieras
                        },
                        onError = { error ->
                            isLoading = false
                            errorMsg = error
                            println("Error de login: $error")
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White
                    )
                } else {
                    Text("Iniciar sesión", color = Color.White)
                }
            }

            if (errorMsg != null) {
                Text(
                    text = errorMsg!!,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = {
                    // TODO: Navegar a pantalla de registro
                    navController.navigate("register")
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("¿No tienes cuenta? ", color = Color.Black)
                    Text("Regístrate", color = Color.Red)
                }
            }
        }
    }
}