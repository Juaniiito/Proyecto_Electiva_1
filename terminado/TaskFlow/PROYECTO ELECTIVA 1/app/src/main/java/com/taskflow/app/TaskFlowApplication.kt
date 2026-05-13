package com.taskflow.app

import android.app.Application
import com.taskflow.app.data.TaskRepository
import com.taskflow.app.data.local.TaskDatabase
import com.taskflow.app.data.local.TaskEntity
import com.taskflow.app.data.model.TaskPriority
import com.taskflow.app.notification.NotificationHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class TaskFlowApplication : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    lateinit var repository: TaskRepository
        private set

    override fun onCreate() {
        super.onCreate()
        val database = TaskDatabase.build(this)
        repository = TaskRepository(database.taskDao())
        NotificationHelper.ensureChannel(this)
        
        // Agregar tareas de ejemplo para que la app no se vea vacía al iniciar
        applicationScope.launch(Dispatchers.IO) {
            val dao = database.taskDao()
            // Verificamos si la base de datos está vacía de forma sencilla
            if (repository.getTask(1L) == null) {
                dao.insert(TaskEntity(
                    title = "Bienvenido a TaskFlow",
                    description = "Esta es tu primera tarea. ¡Puedes completarla o eliminarla!",
                    dueDateMillis = System.currentTimeMillis(),
                    priority = TaskPriority.HIGH.storageName,
                    category = "General"
                ))
                dao.insert(TaskEntity(
                    title = "Organiza tu día",
                    description = "Usa las categorías para separar tus tareas personales y de trabajo.",
                    dueDateMillis = System.currentTimeMillis() + 86400000,
                    priority = TaskPriority.MEDIUM.storageName,
                    category = "Personal"
                ))
                dao.insert(TaskEntity(
                    title = "Tarea sin fecha",
                    description = "Esta tarea aparecerá en la sección 'Algún día'.",
                    dueDateMillis = null,
                    priority = TaskPriority.LOW.storageName,
                    category = "Estudio"
                ))
            }
        }
    }
}
