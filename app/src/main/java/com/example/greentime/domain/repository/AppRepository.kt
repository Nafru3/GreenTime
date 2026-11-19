package com.example.greentime.domain.repository

import android.graphics.drawable.Drawable
import com.example.greentime.data.local.AppIconDataSource
import com.example.greentime.data.local.InstalledAppsDataSource
import com.example.greentime.di.IoDispatcher
import com.example.greentime.domain.model.InstalledApp
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

interface AppRepository {
    suspend fun getAllInstalledApps(): List<InstalledApp>
    suspend fun getAppIcon(packageName: String): Drawable?
}

class AppRepositoryImpl @Inject constructor(
    private val installedApps: InstalledAppsDataSource,
    private val iconSource: AppIconDataSource,
    @IoDispatcher private val io: CoroutineDispatcher
) : AppRepository {

    override suspend fun getAllInstalledApps(): List<InstalledApp> = withContext(io) {
        installedApps.getInstalledApps().sortedBy { it.label.lowercase() }
    }

    override suspend fun getAppIcon(packageName: String): Drawable? = withContext(io) {
        iconSource.getIcon(packageName)
    }
}