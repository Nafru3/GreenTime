package com.example.greentime.data.local

import android.annotation.SuppressLint
import android.app.usage.NetworkStats
import android.app.usage.NetworkStatsManager
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.example.greentime.domain.model.TimeRange
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject

class NetworkStatsDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    @SuppressLint("ServiceCast")
    private val nsm =
        context.getSystemService(Context.NETWORK_STATS_SERVICE) as NetworkStatsManager

    /** Retourne uid -> octets (reçus + envoyés) pour un type de réseau. */
    fun bytesByUid(networkType: Int, range: TimeRange): Map<Int, Long> {
        val result = mutableMapOf<Int, Long>()

        val stats = try {
            nsm.querySummary(networkType, null, range.startMs, range.endMs)
        } catch (e: Exception) {
            // Permission absente, réseau indisponible, ou abonné introuvable (API < 29)
            Log.w("NetworkStats", "querySummary a échoué pour le type $networkType", e)
            return emptyMap()
        }

        try {
            val bucket = NetworkStats.Bucket()
            while (stats.hasNextBucket()) {
                stats.getNextBucket(bucket)
                // Ignorer les UID spéciaux (apps désinstallées, tethering)
                if (bucket.uid < 0) continue
                result.merge(bucket.uid, bucket.rxBytes + bucket.txBytes, Long::plus)
            }
        } finally {
            stats.close()
        }
        return result
    }

    fun wifiBytesByUid(range: TimeRange) =
        bytesByUid(ConnectivityManager.TYPE_WIFI, range)

    fun mobileBytesByUid(range: TimeRange) =
        bytesByUid(ConnectivityManager.TYPE_MOBILE, range)
}