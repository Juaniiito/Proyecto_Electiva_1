package com.taskflow.app.ui.editor

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.taskflow.app.data.TaskRepository
import com.taskflow.app.data.local.TaskEntity
import com.taskflow.app.data.model.TaskPriority
import com.taskflow.app.notification.TaskAlarmScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TaskEditorUiState(
    val dueDateMillis: Long? = null,
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val isSaving: Boolean = false,
    val errorMessage: String? = null
)

class TaskEditorViewModel(
    application: Application,
    private val repository: TaskRepository,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val taskId: Long = savedStateHandle.get<Long>("taskId") ?: 0L

    private val _uiState = MutableStateFlow(TaskEditorUiState())
    val uiState: StateFlow<TaskEditorUiState> = _uiState.asStateFlow()

    private val _initialTask = MutableStateFlow<TaskEntity?>(null)
    val initialTask: StateFlow<TaskEntity?> = _initialTask.asStateFlow()

    init {
        if (taskId > 0L) {
            viewModelScope.launch {
                val existing = repository.getTask(taskId) ?: return@launch
                _initialTask.value = existing
                _uiState.value = TaskEditorUiState(
                    dueDateMillis = existing.dueDateMillis,
                    priority = TaskPriority.fromStorage(existing.priority)
                )
            }
        }
    }

    fun updateDueDate(millis: Long?) = _uiState.update { it.copy(dueDateMillis = millis, errorMessage = null) }

    fun updatePriority(priority: TaskPriority) = _uiState.update { it.copy(priority = priority, errorMessage = null) }

    fun save(
        title: String,
        description: String,
        category: String,
        onDone: () -> Unit
    ) {
        val trimmedTitle = title.trim()
        if (trimmedTitle.isBlank()) {
            _uiState.update { it.copy(errorMessage = ERROR_TITLE) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null) }
            val snapshot = _uiState.value
            val trimmedCategory = category.trim().ifBlank { "General" }

            val entity = if (taskId > 0L) {
                val existing = repository.getTask(taskId) ?: return@launch
                existing.copy(
                    title = trimmedTitle,
                    description = description.trim(),
                    dueDateMillis = snapshot.dueDateMillis,
                    priority = snapshot.priority.storageName,
                    category = trimmedCategory
                )
            } else {
                TaskEntity(
                    title = trimmedTitle,
                    description = description.trim(),
                    dueDateMillis = snapshot.dueDateMillis,
                    priority = snapshot.priority.storageName,
                    category = trimmedCategory
                )
            }

            val id = repository.upsert(entity)
            val persisted = repository.getTask(id) ?: entity.copy(id = id)
            TaskAlarmScheduler.cancel(getApplication(), id)
            TaskAlarmScheduler.schedule(getApplication(), persisted)
            _uiState.update { it.copy(isSaving = false) }
            onDone()
        }
    }

    companion object {
        const val ERROR_TITLE = "TITLE_REQUIRED"
    }
}
