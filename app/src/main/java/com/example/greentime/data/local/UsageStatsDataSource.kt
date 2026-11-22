package com.example.greentime.data.local

import android.annotation.SuppressLint
import android.app.AppOpsManager
import android.app.usage.UsageStatsManager
import android.content.Context
import android.os.Build
import com.example.greentime.domain.model.AppUsage
import com.example.greentime.domain.model.TimeRange
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject

class UsageStatsDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    @SuppressLint("ServiceCast")
    private val usm = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

    @SuppressLint("ServiceCast")
    fun hasPermission(): Boolean {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOps.unsafeCheckOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(),
                context.packageName
            )
        } else {
            @Suppress("DEPRECATION")
            appOps.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(),
                context.packageName
            )
        }
        return mode == AppOpsManager.MODE_ALLOWED
    }

    fun getUsage(range: TimeRange): List<AppUsage> =
        usm.queryAndAggregateUsageStats(range.startMs, range.endMs)
            .values
            .filter { it.totalTimeInForeground > 0 }
            .map {
                AppUsage(
                    packageName = it.packageName,
                    foregroundTimeMs = it.totalTimeInForeground,
                    lastTimeUsed = it.lastTimeUsed
                )
            }
}