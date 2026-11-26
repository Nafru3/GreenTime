package com.example.greentime.views.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.greentime.ui.theme.dark_green
import com.example.greentime.views.components.ItemApplication
import com.example.greentime.views.components.PeriodTabBar
import com.example.greentime.views.components.TextType
import com.example.greentime.views.components.ThemedText
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollSpec
import com.patrykandpatrick.vico.compose.component.lineComponent
import com.patrykandpatrick.vico.core.entry.entryModelOf

@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
fun StatScreen(
    modifier: Modifier = Modifier,
    viewModel: StatsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadStats(context, 7)
    }

    val options = listOf("7 jours", "30 jours", "6 mois")

    val data = when (state.selectedIndex) {
        0 -> listOf(3f, 5f, 2f, 8f, 6f, 4f, 7f)
        1 -> listOf(4f, 6f, 5f, 7f, 8f, 6f, 9f, 5f, 4f, 7f)
        else -> listOf(12f, 15f, 18f, 14f, 20f, 22f)
    }

    val totalUsage = state.appUsages.sumOf { it.timeInMillis }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ThemedText("Statistiques", type = TextType.Title)
            }

            item {
                PeriodTabBar(
                    options = options,
                    selectedIndex = state.selectedIndex,
                    onSelect = { index ->
                        viewModel.setSelectedIndex(context, index)
                    }
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Column {
                            ThemedText(text = "Emission de CO2", type = TextType.Subtitle)
                            Spacer(modifier = Modifier.height(4.dp))
                            ThemedText(text = "8,4 Kg", type = TextType.Title)
                        }

                        Chart(
                            chart = columnChart(
                                columns = listOf(
                                    lineComponent(
                                        color = dark_green,
                                        thickness = 22.dp,
                                        shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                                    )
                                )
                            ),
                            model = entryModelOf(*data.toTypedArray()),
                            startAxis = rememberStartAxis(),
                            bottomAxis = rememberBottomAxis(),
                            chartScrollSpec = rememberChartScrollSpec(isScrollEnabled = false),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                        )
                    }
                }
            }

            item {
                ThemedText(text = "Utilisation des Applications", type = TextType.Subtitle)
            }

            items(state.appUsages) { appUsage ->
                val percent = if (totalUsage > 0) (appUsage.timeInMillis.toFloat() / totalUsage * 100).toInt() else 0
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
                ) {
                    ItemApplication(
                        name = appUsage.appName,
                        packageName = appUsage.packageName,
                        valuePercent = percent,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }
    }
}
