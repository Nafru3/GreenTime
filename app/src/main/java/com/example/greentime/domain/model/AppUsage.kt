package com.example.greentime.domain.model

data class AppUsage(
    val packageName: String,
    val foregroundTimeMs: Long,
    val lastTimeUsed: Long,
    val wifiBytes: Long = 0L,
    val mobileBytes: Long = 0L
) {
    val totalBytes: Long get() = wifiBytes + mobileBytes
}