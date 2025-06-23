package com.studyhubmobile.ui.theme.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.clickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavController) {
    var expanded by remember { mutableStateOf(false) }
    val currentRoute = remember { mutableStateOf<String?>(null) }
    
    LaunchedEffect(navController.currentBackStackEntry) {
        currentRoute.value = navController.currentBackStackEntry?.destination?.route
    }

    val menuItems = when (currentRoute.value) {
        "home" -> listOf(
            "Recursos" to { navController.navigate("recursos") },
            "Simulacros" to { navController.navigate("simulacro") }
        )
        "recursos" -> listOf(
            "Home" to { navController.navigate("home") },
            "Simulacros" to { navController.navigate("simulacro") }
        )
        "simulacro" -> listOf(
            "Home" to { navController.navigate("home") },
            "Recursos" to { navController.navigate("recursos") }
        )
        "configuracion" -> listOf(
            "Configuración" to { /* TODO: Implementar configuración */ },
            "Cerrar sesión" to { /* TODO: Implementar logout */ }
        )
        else -> listOf(
            "Home" to { navController.navigate("home") },
            "Recursos" to { navController.navigate("recursos") },
            "Simulacros" to { navController.navigate("simulacro") }
        )
    }

    TopAppBar(
        title = {
            Text(
                text = "StudyHub",
                color = Color.White,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clickable { navController.navigate("home") }
            )
        },
        navigationIcon = {
            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
                }
                
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    menuItems.forEach { (text, action) ->
                        DropdownMenuItem(
                            text = { Text(text) },
                            onClick = {
                                expanded = false
                                action()
                            }
                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF1E293B) // slate-900
        )
    )
}