package com.example.greentime.data.local

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
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
                isSystemApp = (it.flags and ApplicationInfo.FLAG_SYSTEM) != 0
            )
        }
    }
}