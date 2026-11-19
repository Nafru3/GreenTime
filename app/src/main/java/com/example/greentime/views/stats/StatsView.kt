package com.example.greentime.views.stats

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.greentime.views.components.TextType
import com.example.greentime.views.components.ThemedText
import com.example.greentime.views.components.ItemApplication
import com.example.greentime.ui.theme.green
import com.example.greentime.ui.theme.light_gray

@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
fun StatScreen(modifier: Modifier = Modifier) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val options = listOf("7 jours", "30 jours", "6 mois")

    val data = when (selectedIndex) {
        0 -> listOf(3f, 5f, 2f, 8f, 6f, 4f, 7f)
        1 -> listOf(4f, 6f, 5f, 7f, 8f, 6f, 9f, 5f, 4f, 7f)
        else -> listOf(12f, 15f, 18f, 14f, 20f, 22f)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ThemedText("Statistiques", type = TextType.Title)

        SingleChoiceSegmentedButtonRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            options.forEachIndexed { index, label ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = options.size
                    ),
                    onClick = { selectedIndex = index },
                    selected = index == selectedIndex,
                    label = { Text(label) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            colors = CardDefaults.cardColors(containerColor = light_gray)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {

//                ThemedText(
//                    text = when (selectedIndex) {
//                        0 -> "Activité des 7 derniers jours"
//                        1 -> "Activité des 30 derniers jours"
//                        else -> "Activité des 6 derniers mois"
//                    },
//                    type = TextType.Subtitle
//                )
                ThemedText(text = "Emission de CO2", type = TextType.Subtitle)
                ThemedText(text = "8,4Kg", type = TextType.Title)


                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                ) {
                    val maxVal = (data.maxOrNull() ?: 1f).coerceAtLeast(1f)
                    val barWidth = size.width / (data.size * 2f)

                    data.forEachIndexed { index, value ->
                        val barHeight = (value / maxVal) * size.height
                        val left = index * (barWidth * 2) + barWidth / 2
                        val top = size.height - barHeight

                        drawRect(
                            color = green,
                            topLeft = Offset(left, top),
                            size = Size(barWidth, barHeight)
                        )
                    }
                }


            }

        }
        ThemedText(text = "Utilisation des Applications", type = TextType.Subtitle)
        ItemApplication()
        ItemApplication()
        ItemApplication()
        ItemApplication()
        ItemApplication()
    }
}
