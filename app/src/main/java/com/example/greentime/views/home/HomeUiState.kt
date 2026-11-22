package com.example.greentime.views.home

data class AppImpactUi(
    val packageName: String,
    val label: String,
    val share: Float,   // 0f..1f, part du CO₂ total
    val co2Kg: Double
)

data class HomeUiState(
    val isLoading: Boolean = true,
    val needsPermission: Boolean = false,
    val userName: String = "Lucas",       // TODO : à lire depuis DataStore / profil
    val co2Kg: Double = 0.0,
    val co2ChangePercent: Int? = null,    // négatif = baisse par rapport à hier
    val waterLiters: Double = 0.0,
    val energyKwh: Double = 0.0,
    val treeDays: Double = 0.0,
    val topApps: List<AppImpactUi> = emptyList()
)