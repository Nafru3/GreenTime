package com.example.greentime.views.stats

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greentime.domain.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _state = MutableStateFlow(StatsUiState())
    val state = _state.asStateFlow()

    fun loadStats(context: Context, days: Int = 7) = viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }
        val usagesByDay = getAppUsageForDays(context, days)
        val flattenedUsages = usagesByDay.flatten()
            .groupBy { it.packageName }
            .map { (pkg, list) ->
                AppUsage(
                    packageName = pkg,
                    appName = list.first().appName,
                    timeInMillis = list.sumOf { it.timeInMillis }
                )
            }
            .sortedByDescending { it.timeInMillis }

        _state.update { it.copy(isLoading = false, appUsages = flattenedUsages, usageDays = usagesByDay) }
    }

    fun setSelectedIndex(context: Context, index: Int) {
        _state.update { it.copy(selectedIndex = index) }
        val days = when (index) {
            0 -> 7
            1 -> 30
            2 -> 180
            else -> 7
        }
        loadStats(context, days)
    }

    suspend fun loadIcon(packageName: String): Drawable? =
        repository.getAppIcon(packageName)
}
