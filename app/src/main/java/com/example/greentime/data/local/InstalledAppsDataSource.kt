package com.example.greentime.data.local

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import com.example.greentime.domain.model.AppCategory
import com.example.greentime.domain.model.InstalledApp
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject

class InstalledAppsDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun getInstalledApps(): List<InstalledApp> {
        val pm = context.packageManager
        return pm.getInstalledApplications(PackageManager.GET_META_DATA).map {
            InstalledApp(
                packageName = it.packageName,
                label = pm.getApplicationLabel(it).toString(),
                uid = it.uid,
                isSystemApp = (it.flags and ApplicationInfo.FLAG_SYSTEM) != 0,
                category = it.toAppCategory()
            )
        }
    }

    private fun ApplicationInfo.toAppCategory(): AppCategory {
        // Avant Android 8, la catégorie n'existe pas
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            // Avant Android 8, les jeux se détectaient avec un flag (déprécié)
            @Suppress("DEPRECATION")
            return if ((flags and ApplicationInfo.FLAG_IS_GAME) != 0) AppCategory.GAME
            else AppCategory.OTHER
        }

        return when (category) {
            ApplicationInfo.CATEGORY_GAME -> AppCategory.GAME
            ApplicationInfo.CATEGORY_AUDIO -> AppCategory.AUDIO
            ApplicationInfo.CATEGORY_VIDEO -> AppCategory.VIDEO
            ApplicationInfo.CATEGORY_IMAGE -> AppCategory.IMAGE
            ApplicationInfo.CATEGORY_SOCIAL -> AppCategory.SOCIAL
            ApplicationInfo.CATEGORY_NEWS -> AppCategory.NEWS
            ApplicationInfo.CATEGORY_MAPS -> AppCategory.MAPS
            ApplicationInfo.CATEGORY_PRODUCTIVITY -> AppCategory.PRODUCTIVITY
            else -> AppCategory.OTHER
        }
    }
}