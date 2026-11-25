package com.example.greentime.views.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.greentime.ui.theme.dark_green
import com.example.greentime.ui.theme.gray
import com.example.greentime.ui.theme.light_gray
import com.example.greentime.views.stats.StatsViewModel

@Preview(showBackground = true, widthDp = 412, heightDp = 100)
@Composable
fun ItemApplication(
    modifier: Modifier = Modifier,
    name: String = "Youtube",
    packageName: String = "com.google.android.youtube",
    valuePercent: Int = 50,
    valueCarbon: Double = 5.5
) {
    val viewModel: StatsViewModel = hiltViewModel()
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AppIcon(
            packageName = packageName,
            loadIcon = viewModel::loadIcon,
            modifier = Modifier.size(40.dp)
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            ThemedText(name, type = TextType.Body)
            
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
            ) {
                val greenWidth = (valuePercent.toFloat() / 100) * size.width
                drawRoundRect(
                    color = light_gray,
                    topLeft = Offset(0.0f, 0.0f),
                    size = size,
                    cornerRadius = CornerRadius(120f, 120f)
                )
                drawRoundRect(
                    color = dark_green,
                    topLeft = Offset(0.0f, 0.0f),
                    size = Size(greenWidth, size.height),
                    cornerRadius = CornerRadius(120f, 120f)
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.End
        ) {
            ThemedText(text = "$valuePercent%")
            ThemedText(text = "$valueCarbon kg CO2", color = gray, type = TextType.Caption)
        }
    }
}
