package com.example.greentime.views.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.example.greentime.ui.theme.black

enum class TextType {
    Title,
    Subtitle,
    Body,
    Caption
}

@Composable
fun ThemedText(
    text: String,
    modifier: Modifier = Modifier,
    type: TextType = TextType.Body,
    color: Color = black,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) {
    // Associe chaque type à un style de la typographie Material 3
    val style = when (type) {
        TextType.Title -> MaterialTheme.typography.titleLarge
        TextType.Subtitle -> MaterialTheme.typography.titleMedium
        TextType.Body -> MaterialTheme.typography.bodyMedium
        TextType.Caption -> MaterialTheme.typography.labelSmall
    }

    Text(
        text = text,
        modifier = modifier,
        style = style,
        color = color,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = overflow
    )
}