package com.example.greentime.views.stats

import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.PackageManager
import java.util.Calendar

data class AppUsage(
    val packageName: String,
    val appName: String,
    val timeInMillis: Long
)

fun getAppLabel(context: Context, packageName: String): String {
    return try {
        val pm = context.packageManager
        val appInfo = pm.getApplicationInfo(packageName, 0)
        pm.getApplicationLabel(appInfo).toString()
    } catch (e: PackageManager.NameNotFoundException) {
        packageName
    }
}

fun getTodayAppUsage(context: Context): List<AppUsage> {
    val usageStatsManager =
        context.getSystemService(Context.USAGE_STATS_SERVICE)
                as UsageStatsManager

    // Début de la journée : minuit
    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    val startTime = calendar.timeInMillis
    val endTime = System.currentTimeMillis()

    val stats = usageStatsManager.queryUsageStats(
        UsageStatsManager.INTERVAL_DAILY,
        startTime,
        endTime
    )

    return stats
        .filter { it.totalTimeInForeground > 0 }
        .map {
            AppUsage(
                packageName = it.packageName,
                appName = getAppLabel(context, it.packageName),
                timeInMillis = it.totalTimeInForeground
            )
        }
        .sortedByDescending { it.timeInMillis }
}

// Convertir les millisecondes en heures et minutes
fun formatDuration(timeInMillis: Long): String {
    val totalMinutes = timeInMillis / 60_000
    val hours = totalMinutes / 60
    val minutes = totalMinutes % 60

    return "${hours} h ${minutes} min"
}
