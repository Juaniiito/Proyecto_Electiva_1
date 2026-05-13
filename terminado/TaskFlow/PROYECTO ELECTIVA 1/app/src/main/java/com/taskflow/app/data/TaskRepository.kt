package com.taskflow.app.data

import com.taskflow.app.data.local.TaskDao
import com.taskflow.app.data.local.TaskEntity
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val dao: TaskDao) {

    fun observeTasks(): Flow<List<TaskEntity>> = dao.observeAll()

    suspend fun getTask(id: Long): TaskEntity? = dao.getById(id)

    suspend fun upsert(task: TaskEntity): Long {
        return if (task.id == 0L) dao.insert(task) else {
            dao.update(task)
            task.id
        }
    }

    suspend fun delete(task: TaskEntity) = dao.delete(task)
}
