package com.example.greentime.views.stats

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class StatsViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(StatsUiState())
    val state = _state.asStateFlow()

    fun loadStats(context: Context) = viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }
        val usages = getTodayAppUsage(context)
        _state.update { it.copy(isLoading = false, appUsages = usages) }
    }

    fun setSelectedIndex(index: Int) {
        _state.update { it.copy(selectedIndex = index) }
    }
}
