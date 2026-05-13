package com.taskflow.app.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.taskflow.app.data.TaskRepository
import com.taskflow.app.data.local.TaskEntity
import com.taskflow.app.data.model.TaskPriority
import com.taskflow.app.notification.NotificationHelper
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class TaskViewModel(
    application: Application,
    private val repository: TaskRepository
) : AndroidViewModel(application) {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val allTasks: StateFlow<List<TaskEntity>> = _searchQuery
        .flatMapLatest { query ->
            repository.observeTasks().map { tasks ->
                if (query.isEmpty()) tasks
                else tasks.filter { it.title.contains(query, ignoreCase = true) || it.description.contains(query, ignoreCase = true) }
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun insert(title: String, description: String, date: Long?, priority: TaskPriority, category: String) {
        viewModelScope.launch {
            val task = TaskEntity(
                title = title,
                description = description,
                dueDateMillis = date,
                priority = priority.storageName,
                category = category
            )
            repository.upsert(task)
        }
    }

    fun update(task: TaskEntity) {
        viewModelScope.launch {
            repository.upsert(task)
        }
    }

    fun delete(task: TaskEntity) {
        viewModelScope.launch {
            repository.delete(task)
        }
    }

    fun toggleCompleted(task: TaskEntity) {
        viewModelScope.launch {
            repository.upsert(task.copy(completed = !task.completed))
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun groupTasks(tasks: List<TaskEntity>): Map<String, List<TaskEntity>> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val todayStart = calendar.timeInMillis
        
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val tomorrowStart = calendar.timeInMillis

        val groups = linkedMapOf<String, MutableList<TaskEntity>>()
        groups["Hoy"] = mutableListOf()
        groups["Próximos días"] = mutableListOf()
        groups["Algún día"] = mutableListOf()

        tasks.forEach { task ->
            if (task.completed) return@forEach
            
            when {
                task.dueDateMillis == null -> groups["Algún día"]?.add(task)
                task.priority == TaskPriority.HIGH.storageName || (task.dueDateMillis >= todayStart && task.dueDateMillis < tomorrowStart) -> groups["Hoy"]?.add(task)
                task.dueDateMillis >= tomorrowStart -> groups["Próximos días"]?.add(task)
                else -> groups["Hoy"]?.add(task) // Past tasks in Hoy
            }
        }
        return groups
    }
}

class TaskViewModelFactory(
    private val application: Application,
    private val repository: TaskRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
