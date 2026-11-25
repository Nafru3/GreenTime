package com.example.greentime.views.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.greentime.ui.theme.background
import com.example.greentime.ui.theme.black
import com.example.greentime.ui.theme.dark_green


@Composable
fun PeriodTabBar(
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(34.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(background),
        verticalAlignment = Alignment.CenterVertically
    ) {
        options.forEachIndexed { index, label ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(
                        RoundedCornerShape(
                            topStart = if (index == 0) 28.dp else 0.dp,
                            bottomStart = if (index == 0) 28.dp else 0.dp,
                            topEnd = if (index == options.lastIndex) 28.dp else 0.dp,
                            bottomEnd = if (index == options.lastIndex) 28.dp else 0.dp
                        )
                    )
                    .background(
                        if (index == selectedIndex)
                            dark_green
                        else
                            Color.Transparent
                    )
                    .clickable { onSelect(index) },
                contentAlignment = Alignment.Center
            ) {
                if (index == selectedIndex)
                    ThemedText(
                        text = label,
                        type = TextType.Body,
                        color = background
                    )
                else
                    ThemedText(
                        text = label,
                        type = TextType.Body,
                        color = black
                    )
            }
        }
    }
}
