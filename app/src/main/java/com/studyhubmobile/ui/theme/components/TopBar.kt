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
            IconButton(onClick = { expanded = true }) {
                Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Recursos") },
                    onClick = {
                        expanded = false
                        navController.navigate("recursos")
                    }
                )
                DropdownMenuItem(
                    text = { Text("Simulacros") },
                    onClick = {
                        expanded = false
                        navController.navigate("simulacro")
                    }
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF1E293B) // slate-900
        )
    )
}