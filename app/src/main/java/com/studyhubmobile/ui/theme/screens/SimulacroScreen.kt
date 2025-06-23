package com.studyhubmobile.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.studyhubmobile.ui.theme.components.TopBar


@Composable
fun SimulacroScreen(navController: NavController) {
    Scaffold(
        topBar = { TopBar(navController) }
    ) { padding ->
    Surface(modifier = Modifier.fillMaxSize().background(Color(0xFF0f172a))) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Simulacros",
                color = Color.Black,
                fontSize = 22.sp,
                modifier = Modifier.padding(bottom = 16.dp, top = 80.dp)
            )
            ExamItem("Simulacro de Cálculo I")
            ExamItem("Simulacro de Programación de Videojuegos")
            }
        }
    }
}

@Composable
fun ExamItem(title: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color.DarkGray)
            .clickable { }
            .padding(16.dp)
    ) {
        Text(text = title, color = Color.White, fontSize = 18.sp)
    }
}
