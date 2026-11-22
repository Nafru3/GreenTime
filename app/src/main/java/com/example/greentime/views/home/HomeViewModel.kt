package com.example.greentime.views.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greentime.domain.model.EnvironmentalImpact
import com.example.greentime.domain.model.ImpactCalculator
import com.example.greentime.domain.model.ImpactFactors
import com.example.greentime.domain.model.TimeRange
import com.example.greentime.domain.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: AppRepository,
    private val calculator: ImpactCalculator
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    private val factors = ImpactFactors()

    fun load() {
        viewModelScope.launch {
            if (!repository.hasUsagePermission()) {
                _state.update { it.copy(isLoading = false, needsPermission = true) }
                return@launch
            }
            _state.update { it.copy(isLoading = true, needsPermission = false) }

            val today = TimeRange.today()
            val yesterday = TimeRange(
                startMs = today.startMs - DAY_MS,
                endMs = today.startMs
            )

            val perApp = repository.getAppsWithUsage(today).map { item ->
                item to calculator.calculate(
                    foregroundTimeMs = item.usage.foregroundTimeMs,
                    totalBytes = item.usage.totalBytes,
                    category = item.app.category,
                    f = factors
                )
            }
            val total = perApp.fold(EnvironmentalImpact.ZERO) { acc, (_, i) -> acc + i }

            val yesterdayCo2 = repository.getAppsWithUsage(yesterday).sumOf { item ->
                calculator.calculate(
                    item.usage.foregroundTimeMs,
                    item.usage.totalBytes,
                    item.app.category,
                    factors
                ).co2Kg
            }

            // Attention : « aujourd'hui » est partiel, « hier » est complet.
            // La comparaison est donc favorable en début de journée.
            val change = if (yesterdayCo2 > 0)
                ((total.co2Kg - yesterdayCo2) / yesterdayCo2 * 100).toInt()
            else null

            val topApps = perApp
                .sortedByDescending { (_, impact) -> impact.co2Kg }
                .take(TOP_APPS_COUNT)
                .map { (item, impact) ->
                    AppImpactUi(
                        packageName = item.app.packageName,
                        label = item.app.label,
                        share = if (total.co2Kg > 0) (impact.co2Kg / total.co2Kg).toFloat() else 0f,
                        co2Kg = impact.co2Kg
                    )
                }

            _state.update {
                it.copy(
                    isLoading = false,
                    co2Kg = total.co2Kg,
                    co2ChangePercent = change,
                    waterLiters = total.waterLiters,
                    energyKwh = total.energyKwh,
                    treeDays = total.treeDays,
                    topApps = topApps
                )
            }
        }
    }

    suspend fun loadIcon(packageName: String) = repository.getAppIcon(packageName)

    private companion object {
        const val DAY_MS = 24L * 3_600_000L
        const val TOP_APPS_COUNT = 5
    }
}