package com.studyhubmobile.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.foundation.text.ClickableText
import com.studyhubmobile.ui.theme.components.TopBar
import androidx.compose.material3.Scaffold
import androidx.navigation.NavController

@Composable
fun RecursosScreen(navController: NavController) {
    Scaffold(
        topBar = { TopBar(navController) }
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