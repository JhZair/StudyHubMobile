package com.studyhubmobile.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.foundation.text.ClickableText
import androidx.navigation.NavController
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.studyhubmobile.R
import com.studyhubmobile.data.api.RecursoNetworkModule
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import com.studyhubmobile.ui.components.ResourceCard
import com.studyhubmobile.models.Recurso

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecursosScreen(navController: NavController) {
    var isMenuExpanded by remember { mutableStateOf(false) }
    val menuItems = listOf(
        "Home" to { navController.navigate("home") },
        "Recursos" to { navController.navigate("recursos") },
        "Simulacro" to { navController.navigate("simulacro") },
        "Perfil" to { navController.navigate("perfil") }
    )
    val scope = rememberCoroutineScope()
    var recursos by remember { mutableStateOf<List<Recurso>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        isLoading = true
        try {
            val data = RecursoNetworkModule.apiService.getRecursos()
            recursos = data
        } catch (e: Exception) {
            // Manejar error
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
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
                    }
                }
            }
        }
    ) { padding ->
        Surface(modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0f172a))
            .padding(padding)) {

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Recursos Disponibles",
                    color = Color.White,
                    fontSize = 22.sp,
                    modifier = Modifier.padding(bottom = 16.dp, top = 80.dp)
                )

                if (isLoading) {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    if (recursos.isEmpty()) {
                        Text(
                            text = "No hay recursos disponibles.",
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        recursos.forEach { recurso ->
                            ResourceCard(
                                title = recurso.titulo,
                                link = recurso.archivo
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}
