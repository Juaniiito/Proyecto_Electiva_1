package com.taskflow.app.data.model

import com.taskflow.app.data.local.TaskEntity
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

object TaskSectionMapper {

    fun sectionFor(entity: TaskEntity, zoneId: ZoneId = ZoneId.systemDefault()): TaskSection {
        if (entity.completed) return TaskSection.COMPLETED

        if (entity.priority == TaskPriority.HIGH.storageName) {
            return TaskSection.TODAY
        }

        val dueMillis = entity.dueDateMillis ?: return TaskSection.SOMEDAY
        val today = LocalDate.now(zoneId)
        val dueDate = Instant.ofEpochMilli(dueMillis).atZone(zoneId).toLocalDate()

        return when {
            dueDate.isAfter(today) -> TaskSection.UPCOMING
            else -> TaskSection.TODAY
        }
    }
}
