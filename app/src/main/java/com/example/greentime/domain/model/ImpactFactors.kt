package com.example.greentime.domain.model

import jakarta.inject.Inject

/* ⚠️ VALEURS PROVISOIRES, inventées pour que l'écran affiche quelque chose.
* À remplacer par des valeurs sourcées (Hydro-Québec, IEA/Ember, Sustainable Web Design,
* études WUE...). Note la source et la date en commentaire à côté de chaque valeur.
*/
data class ImpactFactors(
    val networkKwhPerGb: Double = 0.1,          // TODO source
    val localGridGCo2PerKwh: Double = 30.0,     // TODO source (Québec)
    val globalGridGCo2PerKwh: Double = 450.0,   // TODO source (moyenne mondiale)
    val waterLitersPerKwh: Double = 1.8,        // TODO source
    val treeAbsorptionKgCo2PerYear: Double = 25.0 // TODO source
) {
    /** Puissance moyenne du téléphone en watts selon la catégorie. TODO source. */
    fun devicePowerWatts(category: AppCategory): Double = when (category) {
        AppCategory.GAME -> 2.5
        AppCategory.VIDEO -> 2.0
        AppCategory.SOCIAL -> 1.5
        AppCategory.AUDIO -> 0.8
        else -> 1.2
    }
}

class ImpactCalculator @Inject constructor() {

    fun calculate(
        foregroundTimeMs: Long,
        totalBytes: Long,
        category: AppCategory,
        f: ImpactFactors
    ): EnvironmentalImpact {
        val hours = foregroundTimeMs / 3_600_000.0
        val deviceKwh = hours * f.devicePowerWatts(category) / 1000.0

        val gigabytes = totalBytes / 1_073_741_824.0
        val networkKwh = gigabytes * f.networkKwhPerGb

        // Téléphone : réseau électrique local. Réseau + datacenters : moyenne mondiale.
        val co2Kg = (deviceKwh * f.localGridGCo2PerKwh +
                networkKwh * f.globalGridGCo2PerKwh) / 1000.0

        val totalKwh = deviceKwh + networkKwh
        val waterLiters = totalKwh * f.waterLitersPerKwh
        val treeDays = co2Kg / (f.treeAbsorptionKgCo2PerYear / 365.0)

        return EnvironmentalImpact(totalKwh, co2Kg, waterLiters, treeDays)
    }
}