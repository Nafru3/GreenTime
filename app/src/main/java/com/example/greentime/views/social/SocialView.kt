package com.example.greentime.views.social

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.greentime.ui.theme.dark_green
import com.example.greentime.views.components.TextType
import com.example.greentime.views.components.ThemedText

@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
fun SocialView(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Filled.Groups,
            contentDescription = "Social",
            tint = dark_green,
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        ThemedText(
            text = "Bientôt disponible",
            type = TextType.Title,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        ThemedText(
            text = "La partie sociale de GreenTime arrive très prochainement.",
            type = TextType.Body,
            textAlign = TextAlign.Center
        )
    }
}
