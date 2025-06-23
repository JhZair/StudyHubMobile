package com.studyhubmobile.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.studyhubmobile.ui.theme.components.TopBar
import androidx.compose.material3.MaterialTheme

@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = { TopBar(navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Bienvenido a StudyHub", style = MaterialTheme.typography.headlineSmall)
            }

    }
}