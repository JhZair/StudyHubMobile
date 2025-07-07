package com.studyhubmobile.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.studyhubmobile.R
import com.studyhubmobile.session.SessionManager.logout
import com.studyhubmobile.models.User
import com.studyhubmobile.MainActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, currentUser: User?) {
    var isMenuExpanded by remember { mutableStateOf(false) }
    val menuItems = listOf(
        "Home" to { navController.navigate("home") },
        "Recursos" to { navController.navigate("recursos") },
        "Simulacro" to { navController.navigate("simulacro") },
        "Perfil" to { navController.navigate("perfil") }
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding(),
        containerColor = Color.White,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0f172a))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { isMenuExpanded = !isMenuExpanded }
                        ) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú", tint = Color.White)
                        }
                        Text(
                            text = "StudyHub",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (currentUser != null) {
                            Text(
                                text = "Hola, ${currentUser.nombre}",
                                color = Color.White,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(end = 16.dp)
                            )
                        } else {
                            TextButton(
                                onClick = { navController.navigate("login") },
                                modifier = Modifier.padding(end = 8.dp)
                            ) {
                                Text(
                                    text = "Login",
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                            }
                            TextButton(
                                onClick = { navController.navigate("register") },
                                modifier = Modifier.padding(end = 8.dp)
                            ) {
                                Text(
                                    text = "Sign Up",
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }

                DropdownMenu(
                    expanded = isMenuExpanded,
                    onDismissRequest = { isMenuExpanded = false },
                    modifier = Modifier
                        .width(200.dp)
                        .background(Color(0xFF0f172a))
                ) {
                    menuItems.forEach { (text, action) ->
                        DropdownMenuItem(
                            text = { Text(text, color = Color.White) },
                            onClick = {
                                action()
                                isMenuExpanded = false
                            }
                        )
                    }
                    
                    if (currentUser == null) {
                        DropdownMenuItem(
                            text = { Text("Iniciar Sesión", color = Color.White) },
                            onClick = {
                                navController.navigate("login")
                                isMenuExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Crear Cuenta", color = Color.White) },
                            onClick = {
                                navController.navigate("register")
                                isMenuExpanded = false
                            }
                        )
                    } else {
                        DropdownMenuItem(
                            text = { Text("Cerrar Sesión", color = Color.White) },
                            onClick = {
                                try {
                                    val activity = navController.context as MainActivity
                                    activity.authRepository.currentUser = null
                                    navController.navigate("login") {
                                        popUpTo("home") { inclusive = true }
                                    }
                                    isMenuExpanded = false
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "Logo de StudyHub",
                modifier = Modifier
                    .size(200.dp)
                    .padding(24.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Bienvenido a StudyHub",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Inspiramos a estudiantes a dominar sus exámenes con un repositorio " +
                        "colaborativo que ofrece recursos prácticos, una comunidad vibrante y " +
                        "herramientas innovadoras como búsqueda inteligente, simulaciones " +
                        "interactivas y organización eficiente.",
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}