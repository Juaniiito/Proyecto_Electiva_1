package com.taskflow.app.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.taskflow.app.data.model.TaskPriority

@Entity(
    tableName = "tasks",
    indices = [Index(value = ["completed"]), Index(value = ["dueDateMillis"])]
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val dueDateMillis: Long?,
    val priority: String = TaskPriority.MEDIUM.storageName,
    val category: String,
    val completed: Boolean = false,
    val createdAtMillis: Long = System.currentTimeMillis()
)
