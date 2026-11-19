package com.example.greentime.views.components

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap

@Composable
fun AppIcon(
    packageName: String,
    loadIcon: suspend (String) -> Drawable?,
    modifier: Modifier = Modifier
) {
    val bitmap by produceState<ImageBitmap?>(initialValue = null, packageName) {
        value = loadIcon(packageName)?.toBitmap()?.asImageBitmap()
    }

    bitmap?.let {
        Image(bitmap = it, contentDescription = null, modifier = modifier)
    } ?: Box(modifier) // placeholder pendant le chargement
}