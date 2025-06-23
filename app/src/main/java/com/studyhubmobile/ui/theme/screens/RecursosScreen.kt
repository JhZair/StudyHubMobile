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
        Surface(modifier = Modifier.fillMaxSize().background(Color(0xFF0f172a))) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Recursos Disponibles",
                    color = Color.Black,
                    fontSize = 22.sp,
                    modifier = Modifier.padding(bottom = 16.dp, top = 80.dp)
                )
                ResourceCard(title = "Cálculo I", link = "https://example.com/recursos/calculo.pdf")
                ResourceCard(
                    title = "Estructuras Discretas I",
                    link = "https://example.com/recursos/estructuras.pdf"
                )
            }
        }
    }
}

@Composable
fun ResourceCard(title: String, link: String) {
    val uriHandler = LocalUriHandler.current

    val annotatedLinkString = buildAnnotatedString {
        append("📄 ")

        pushStringAnnotation(tag = "URL", annotation = link)
        withStyle(style = SpanStyle(color = Color.Cyan, textDecoration = TextDecoration.Underline)) {
            append("Ver archivo PDF")
        }
        pop()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color.DarkGray)
            .padding(16.dp)
    ) {
        Text(text = title, color = Color.White, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(4.dp))

        @Suppress("DEPRECATION")
        ClickableText(
            text = annotatedLinkString,
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
            onClick = { offset ->
                annotatedLinkString
                    .getStringAnnotations("URL", start = offset, end = offset)
                    .firstOrNull()?.let { annotation ->
                        uriHandler.openUri(annotation.item)
                    }
            }
        )
    }
}