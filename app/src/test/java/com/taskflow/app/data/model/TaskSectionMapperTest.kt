package com.taskflow.app.data.model

import com.taskflow.app.data.local.TaskEntity
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId

class TaskSectionMapperTest {

    private val zone: ZoneId = ZoneId.of("America/Bogota")

    @Test
    fun highPriorityGoesToTodayEvenWithoutDate() {
        val task = TaskEntity(
            title = "Urgente",
            description = "",
            dueDateMillis = null,
            priority = TaskPriority.HIGH.storageName,
            category = "Test",
            completed = false
        )
        assertEquals(TaskSection.TODAY, TaskSectionMapper.sectionFor(task, zone))
    }

    @Test
    fun futureDueDateGoesToUpcoming() {
        val future = LocalDate.now(zone).plusDays(3)
        val millis = future.atTime(12, 0).atZone(zone).toInstant().toEpochMilli()
        val task = TaskEntity(
            title = "Futura",
            description = "",
            dueDateMillis = millis,
            priority = TaskPriority.MEDIUM.storageName,
            category = "Test",
            completed = false
        )
        assertEquals(TaskSection.UPCOMING, TaskSectionMapper.sectionFor(task, zone))
    }

    @Test
    fun noDateMediumGoesToSomeday() {
        val task = TaskEntity(
            title = "Algún día",
            description = "",
            dueDateMillis = null,
            priority = TaskPriority.MEDIUM.storageName,
            category = "Test",
            completed = false
        )
        assertEquals(TaskSection.SOMEDAY, TaskSectionMapper.sectionFor(task, zone))
    }
}
