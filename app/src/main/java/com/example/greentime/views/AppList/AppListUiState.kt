package com.example.greentime.views.AppList

import com.example.greentime.domain.model.InstalledApp

data class AppListUiState(
    val isLoading: Boolean = false,
    val apps: List<InstalledApp> = emptyList()
)
