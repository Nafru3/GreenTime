package com.example.greentime.views.limit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.greentime.ui.theme.dark_green
import com.example.greentime.ui.theme.light_gray
import com.example.greentime.views.components.TextType
import com.example.greentime.views.components.ThemedText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LimitScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    viewModel: LimitViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { ThemedText(text = "Gestion des Limites", type = TextType.Title) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Retour"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.openAddModal() },
                containerColor = dark_green
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Ajouter une limite", tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ThemedText(text = "Mes limites configurées", type = TextType.Subtitle)

            if (state.limits.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    ThemedText(
                        text = "Aucune limite configurée.\nAppuyez sur le bouton + pour en ajouter une.",
                        type = TextType.Body
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.limits) { limit ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.openEditModal(limit) },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = light_gray),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    ThemedText(text = limit.appName, type = TextType.Subtitle)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    val actionText = if (limit.type == LimitType.BLOCK) "Bloquer" else "Notification"
                                    val unitText = if (limit.thresholdUnit == LimitUnit.HOURS) "heures" else "g de CO2"
                                    ThemedText(
                                        text = "Action : $actionText | Seuil : ${limit.thresholdValue} $unitText",
                                        type = TextType.Caption
                                    )
                                }
                                IconButton(onClick = { viewModel.deleteLimit(limit.id) }) {
                                    Icon(
                                        imageVector = Icons.Filled.Delete,
                                        contentDescription = "Supprimer",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        if (state.isAddModalOpen) {
            AlertDialog(
                onDismissRequest = { viewModel.closeAddModal() },
                title = {
                    ThemedText(
                        text = if (state.editingLimitId != null) "Modifier la limite" else "Ajouter une limite",
                        type = TextType.Title
                    )
                },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (state.selectedApp == null) {
                            ThemedText(text = "Sélectionnez une application :", type = TextType.Body)
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                items(state.installedApps) { app ->
                                    Surface(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { viewModel.selectApp(app) },
                                        shape = RoundedCornerShape(8.dp),
                                        color = MaterialTheme.colorScheme.surfaceVariant
                                    ) {
                                        Text(
                                            text = app.label,
                                            modifier = Modifier.padding(12.dp),
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }
                        } else {
                            ThemedText(text = "Application : ${state.selectedApp?.label}", type = TextType.Subtitle)

                            ThemedText(text = "Type de limite :", type = TextType.Body)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.clickable { viewModel.setLimitType(LimitType.BLOCK) }
                                ) {
                                    RadioButton(
                                        selected = state.limitType == LimitType.BLOCK,
                                        onClick = { viewModel.setLimitType(LimitType.BLOCK) }
                                    )
                                    Text("Bloquer")
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.clickable { viewModel.setLimitType(LimitType.NOTIFY) }
                                ) {
                                    RadioButton(
                                        selected = state.limitType == LimitType.NOTIFY,
                                        onClick = { viewModel.setLimitType(LimitType.NOTIFY) }
                                    )
                                    Text("Notification")
                                }
                            }

                            ThemedText(text = "Unité et seuil :", type = TextType.Body)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.clickable { viewModel.setLimitUnit(LimitUnit.HOURS) }
                                ) {
                                    RadioButton(
                                        selected = state.limitUnit == LimitUnit.HOURS,
                                        onClick = { viewModel.setLimitUnit(LimitUnit.HOURS) }
                                    )
                                    Text("Heures")
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.clickable { viewModel.setLimitUnit(LimitUnit.GRAMS_CO2) }
                                ) {
                                    RadioButton(
                                        selected = state.limitUnit == LimitUnit.GRAMS_CO2,
                                        onClick = { viewModel.setLimitUnit(LimitUnit.GRAMS_CO2) }
                                    )
                                    Text("g de CO2")
                                }
                            }

                            OutlinedTextField(
                                value = state.thresholdInput,
                                onValueChange = { viewModel.setThreshold(it) },
                                label = { Text(if (state.limitUnit == LimitUnit.HOURS) "Nombre d'heures" else "Grammes de CO2") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                },
                confirmButton = {
                    if (state.selectedApp != null) {
                        Button(
                            onClick = { viewModel.saveLimit() },
                            colors = ButtonDefaults.buttonColors(containerColor = dark_green)
                        ) {
                            Text("Enregistrer")
                        }
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { viewModel.closeAddModal() },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                    ) {
                        Text("Annuler")
                    }
                }
            )
        }
    }
}
