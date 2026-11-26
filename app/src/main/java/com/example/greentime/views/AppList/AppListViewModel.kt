package com.example.greentime.views.AppList

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
class AppListViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AppListUiState())
    val state = _state.asStateFlow()

    init { loadApps() }

    private fun loadApps() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }
        val apps = repository.getAllInstalledApps()
        _state.update { it.copy(isLoading = false, apps = apps) }
    }

    suspend fun loadIcon(packageName: String): Drawable? =
        repository.getAppIcon(packageName)
}