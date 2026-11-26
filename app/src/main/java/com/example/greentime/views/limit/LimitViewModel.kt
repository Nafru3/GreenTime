package com.example.greentime.views.limit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greentime.domain.model.AppCategory
import com.example.greentime.domain.model.InstalledApp
import com.example.greentime.domain.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class LimitType { BLOCK, NOTIFY }
enum class LimitUnit { HOURS, GRAMS_CO2 }

data class AppLimit(
    val id: String = java.util.UUID.randomUUID().toString(),
    val packageName: String,
    val appName: String,
    val type: LimitType,
    val thresholdValue: Float,
    val thresholdUnit: LimitUnit
)

data class LimitUiState(
    val limits: List<AppLimit> = emptyList(),
    val installedApps: List<InstalledApp> = emptyList(),
    val isAddModalOpen: Boolean = false,
    val editingLimitId: String? = null,
    val selectedApp: InstalledApp? = null,
    val limitType: LimitType = LimitType.BLOCK,
    val limitUnit: LimitUnit = LimitUnit.HOURS,
    val thresholdInput: String = "2"
)

@HiltViewModel
class LimitViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LimitUiState())
    val state: StateFlow<LimitUiState> = _state.asStateFlow()

    init {
        loadInstalledApps()
    }

    private fun loadInstalledApps() {
        viewModelScope.launch {
            val apps = repository.getAllInstalledApps()
            _state.update { it.copy(installedApps = apps) }
        }
    }

    fun openAddModal() {
        _state.update {
            it.copy(
                isAddModalOpen = true,
                editingLimitId = null,
                selectedApp = null,
                limitType = LimitType.BLOCK,
                limitUnit = LimitUnit.HOURS,
                thresholdInput = "2"
            )
        }
    }

    fun openEditModal(limit: AppLimit) {
        val app = _state.value.installedApps.find { it.packageName == limit.packageName }
            ?: InstalledApp(packageName = limit.packageName, label = limit.appName, uid = 0, isSystemApp = false, category = AppCategory.OTHER)
        _state.update {
            it.copy(
                isAddModalOpen = true,
                editingLimitId = limit.id,
                selectedApp = app,
                limitType = limit.type,
                limitUnit = limit.thresholdUnit,
                thresholdInput = limit.thresholdValue.toString()
            )
        }
    }

    fun closeAddModal() {
        _state.update { it.copy(isAddModalOpen = false, editingLimitId = null) }
    }

    fun selectApp(app: InstalledApp) {
        _state.update { it.copy(selectedApp = app) }
    }

    fun setLimitType(type: LimitType) {
        _state.update { it.copy(limitType = type) }
    }

    fun setLimitUnit(unit: LimitUnit) {
        _state.update { it.copy(limitUnit = unit) }
    }

    fun setThreshold(input: String) {
        _state.update { it.copy(thresholdInput = input) }
    }

    fun saveLimit() {
        val app = _state.value.selectedApp ?: return
        val value = _state.value.thresholdInput.toFloatOrNull() ?: 1f
        val editingId = _state.value.editingLimitId

        if (editingId != null) {
            _state.update {
                it.copy(
                    limits = it.limits.map { l ->
                        if (l.id == editingId) {
                            l.copy(
                                packageName = app.packageName,
                                appName = app.label,
                                type = it.limitType,
                                thresholdValue = value,
                                thresholdUnit = it.limitUnit
                            )
                        } else l
                    },
                    isAddModalOpen = false,
                    editingLimitId = null
                )
            }
        } else {
            val newLimit = AppLimit(
                packageName = app.packageName,
                appName = app.label,
                type = _state.value.limitType,
                thresholdValue = value,
                thresholdUnit = _state.value.limitUnit
            )
            _state.update {
                it.copy(
                    limits = it.limits + newLimit,
                    isAddModalOpen = false
                )
            }
        }
    }

        fun deleteLimit(limitId: String) {
        _state.update {
            it.copy(limits = it.limits.filter { l -> l.id != limitId })
        }
    }
}
