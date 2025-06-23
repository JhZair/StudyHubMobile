package com.studyhubmobile.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@Composable
fun LoadingScreen(
    navController: NavController,
    onLoadingComplete: () -> Unit
) {
    var isLoading by remember { mutableStateOf(true) }
    
    LaunchedEffect(Unit) {
        delay(2000) // Espera 2 segundos
        isLoading = false
        onLoadingComplete()
    }

    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Logo
                Image(
                    painter = painterResource(id = com.studyhubmobile.R.drawable.logo),
                    contentDescription = "Logo StudyHub",
                    modifier = Modifier.size(120.dp)
                )
                
                // Indicador de progreso
                CircularProgressIndicator(
                    modifier = Modifier.size(40.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
