package com.studyhubmobile.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.ClickableText
import androidx.compose.ui.text.AnnotatedString.Range
import androidx.compose.ui.text.AnnotatedString

@Composable
fun ResourceCard(title: String, link: String) {
    val uriHandler = LocalUriHandler.current

    val annotatedLinkString = buildAnnotatedString {
        append("📄 $title\n\n")

        pushStringAnnotation(tag = "URL", annotation = link)
        withStyle(style = SpanStyle(color = Color.Cyan, textDecoration = TextDecoration.Underline)) {
            append("Ver archivo PDF")
        }
        pop()
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1e293b))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            ClickableText(
                text = annotatedLinkString,
                style = LocalTextStyle.current.copy(color = Color.White),
                onClick = { offset ->
                    annotatedLinkString
                        .getStringAnnotations(tag = "URL", start = offset, end = offset)
                        .firstOrNull()?.let { annotation ->
                            uriHandler.openUri(annotation.item)
                        }
                }
            )
        }
    }
}
