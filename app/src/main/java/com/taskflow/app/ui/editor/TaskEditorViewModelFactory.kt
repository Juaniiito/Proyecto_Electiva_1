package com.taskflow.app.ui.editor

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.taskflow.app.data.TaskRepository

class TaskEditorViewModelFactory(
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle?,
    private val application: Application,
    private val repository: TaskRepository
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    override fun <T : ViewModel> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {
        if (modelClass.isAssignableFrom(TaskEditorViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskEditorViewModel(application, repository, handle) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
