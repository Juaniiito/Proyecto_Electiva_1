package com.taskflow.app.data.model

enum class TaskPriority(val storageName: String, val displayName: String) {
    HIGH("HIGH", "Alta"),
    MEDIUM("MEDIUM", "Media"),
    LOW("LOW", "Baja");

    companion object {
        fun fromStorage(value: String): TaskPriority =
            entries.firstOrNull { it.storageName == value } ?: MEDIUM
    }
}
