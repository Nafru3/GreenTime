package com.example.greentime.domain.model

data class EnvironmentalImpact(
    val energyKwh: Double,
    val co2Kg: Double,
    val waterLiters: Double,
    val treeDays: Double // jours d'absorption d'un arbre nécessaires pour compenser
) {
    operator fun plus(o: EnvironmentalImpact) = EnvironmentalImpact(
        energyKwh + o.energyKwh,
        co2Kg + o.co2Kg,
        waterLiters + o.waterLiters,
        treeDays + o.treeDays
    )

    companion object {
        val ZERO = EnvironmentalImpact(0.0, 0.0, 0.0, 0.0)
    }
}