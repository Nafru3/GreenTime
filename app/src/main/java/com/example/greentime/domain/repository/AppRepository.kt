package com.example.greentime.domain.repository

import android.graphics.drawable.Drawable
import com.example.greentime.data.local.AppIconDataSource
import com.example.greentime.data.local.InstalledAppsDataSource
import com.example.greentime.data.local.NetworkStatsDataSource
import com.example.greentime.data.local.UsageStatsDataSource
import com.example.greentime.di.IoDispatcher
import com.example.greentime.domain.model.AppUsage
import com.example.greentime.domain.model.AppWithUsage
import com.example.greentime.domain.model.InstalledApp
import com.example.greentime.domain.model.TimeRange
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

interface AppRepository {
    suspend fun getAllInstalledApps(): List<InstalledApp>
    suspend fun getAppIcon(packageName: String): Drawable?
    fun hasUsagePermission(): Boolean
    suspend fun getAppsWithUsage(range: TimeRange): List<AppWithUsage>
}

class AppRepositoryImpl @Inject constructor(
    private val installedApps: InstalledAppsDataSource,
    private val iconSource: AppIconDataSource,
    private val usageStats: UsageStatsDataSource,
    private val networkStats: NetworkStatsDataSource,
    @IoDispatcher private val io: CoroutineDispatcher
) : AppRepository {

    override suspend fun getAllInstalledApps(): List<InstalledApp> = withContext(io) {
        installedApps.getInstalledApps().sortedBy { it.label.lowercase() }
    }

    override suspend fun getAppIcon(packageName: String): Drawable? = withContext(io) {
        iconSource.getIcon(packageName)
    }

    override fun hasUsagePermission() = usageStats.hasPermission()

    override suspend fun getAppsWithUsage(range: TimeRange): List<AppWithUsage> =
        withContext(io) {
            val apps = installedApps.getInstalledApps()
            val timeByPkg = usageStats.getUsage(range).associateBy { it.packageName }
            val wifiByUid = networkStats.wifiBytesByUid(range)
            val mobileByUid = networkStats.mobileBytesByUid(range)

            apps
                .map { app ->
                    val time = timeByPkg[app.packageName]
                    AppWithUsage(
                        app = app,
                        usage = AppUsage(
                            packageName = app.packageName,
                            foregroundTimeMs = time?.foregroundTimeMs ?: 0L,
                            lastTimeUsed = time?.lastTimeUsed ?: 0L,
                            wifiBytes = wifiByUid[app.uid] ?: 0L,
                            mobileBytes = mobileByUid[app.uid] ?: 0L
                        )
                    )
                }
                // Une app qui télécharge en arrière-plan sans être ouverte doit apparaître
                .filter { it.usage.foregroundTimeMs > 0 || it.usage.totalBytes > 0 }
                .sortedByDescending { it.usage.totalBytes }
        }
}