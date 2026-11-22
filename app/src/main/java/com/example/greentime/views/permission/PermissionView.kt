package com.example.greentime.views.permission

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.greentime.ui.theme.green
import com.example.greentime.views.components.TextType
import com.example.greentime.views.components.ThemedText

@Composable
fun PermissionView(
    modifier: Modifier = Modifier,
    viewModel: PermissionViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ThemedText("Autorisation requise", type = TextType.Title)

        Spacer(modifier = Modifier.height(16.dp))

        ThemedText(
            text = "1. Cherchez et cliquez sur GreenTime dans la liste.\n2. Activez l'accès à l'utilisation.",
            type = TextType.Body,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { viewModel.openUsageSettings(context) },
            colors = ButtonDefaults.buttonColors(containerColor = green),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ouvrir les paramètres")
        }
    }
}
