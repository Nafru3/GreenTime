package com.example.greentime.data.local

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject

class AppIconDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun getIcon(packageName: String): Drawable? = try {
        context.packageManager.getApplicationIcon(packageName)
    } catch (e: PackageManager.NameNotFoundException) {
        null
    }
}