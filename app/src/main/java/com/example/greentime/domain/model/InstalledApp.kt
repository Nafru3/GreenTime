package com.example.greentime.domain.model

data class InstalledApp(
    val packageName: String,
    val label: String,
    val uid: Int,
    val isSystemApp: Boolean,
    val category: AppCategory
)