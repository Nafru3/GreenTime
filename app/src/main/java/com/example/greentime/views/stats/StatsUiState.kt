package com.example.greentime.views.stats

data class StatsUiState(
    val selectedIndex: Int = 0,
    val appUsages: List<AppUsage> = emptyList(),
    val isLoading: Boolean = false
)
