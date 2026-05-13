package com.taskflow.app.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.taskflow.app.data.TaskRepository
import com.taskflow.app.data.local.TaskEntity
import com.taskflow.app.data.model.TaskPriority
import com.taskflow.app.data.model.TaskSection
import com.taskflow.app.data.model.TaskSectionMapper
import com.taskflow.app.notification.TaskAlarmScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    application: Application,
    private val repository: TaskRepository
) : AndroidViewModel(application) {

    private val searchQuery = MutableStateFlow("")
    private val priorityFilter = MutableStateFlow<TaskPriority?>(null)

    val items: StateFlow<List<MainListItem>> = combine(
        repository.observeTasks(),
        searchQuery,
        priorityFilter
    ) { tasks, query, priority ->
        val filtered = tasks.asSequence()
            .filter { matchesSearch(it, query) }
            .filter { matchesPriority(it, priority) }
            .toList()
        buildSectionedList(filtered)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun setPriorityFilter(priority: TaskPriority?) {
        priorityFilter.value = priority
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            TaskAlarmScheduler.cancel(getApplication(), task.id)
            repository.delete(task)
        }
    }

    fun setCompleted(task: TaskEntity, completed: Boolean) {
        viewModelScope.launch {
            val updated = task.copy(completed = completed)
            repository.upsert(updated)
            if (completed) {
                TaskAlarmScheduler.cancel(getApplication(), task.id)
            } else {
                TaskAlarmScheduler.schedule(getApplication(), updated)
            }
        }
    }

    private fun matchesSearch(task: TaskEntity, query: String): Boolean {
        if (query.isBlank()) return true
        val q = query.trim().lowercase()
        return task.title.lowercase().contains(q) ||
            task.description.lowercase().contains(q) ||
            task.category.lowercase().contains(q)
    }

    private fun matchesPriority(task: TaskEntity, priority: TaskPriority?): Boolean {
        if (priority == null) return true
        return TaskPriority.fromStorage(task.priority) == priority
    }

    private fun buildSectionedList(tasks: List<TaskEntity>): List<MainListItem> {
        val incomplete = tasks.filter { !it.completed }
        val complete = tasks.filter { it.completed }

        val result = mutableListOf<MainListItem>()
        val sectionOrder = listOf(TaskSection.TODAY, TaskSection.UPCOMING, TaskSection.SOMEDAY)

        sectionOrder.forEach { section ->
            val inSection = incomplete
                .filter { TaskSectionMapper.sectionFor(it) == section }
                .sortedWith(sectionComparator(section))
            if (inSection.isEmpty()) return@forEach
            result += MainListItem.Header(section)
            inSection.forEach { result += MainListItem.Row(it) }
        }

        if (complete.isNotEmpty()) {
            result += MainListItem.Header(TaskSection.COMPLETED)
            complete
                .sortedByDescending { it.createdAtMillis }
                .forEach { result += MainListItem.Row(it) }
        }

        return result
    }

    private fun sectionComparator(section: TaskSection): Comparator<TaskEntity> = when (section) {
        TaskSection.TODAY,
        TaskSection.UPCOMING -> compareBy<TaskEntity> { it.dueDateMillis ?: Long.MAX_VALUE }
            .thenBy { it.title.lowercase() }

        TaskSection.SOMEDAY -> compareByDescending<TaskEntity> { it.createdAtMillis }
            .thenBy { it.title.lowercase() }

        else -> compareBy { it.title.lowercase() }
    }
}
