package com.taskflow.app.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.taskflow.app.TaskFlowApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val pendingResult = goAsync()
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        scope.launch {
            try {
                val app = context.applicationContext as? TaskFlowApplication ?: return@launch
                val taskId = intent?.getLongExtra(EXTRA_TASK_ID, -1L) ?: -1L
                if (taskId <= 0L) return@launch
                val task = app.repository.getTask(taskId) ?: return@launch
                if (!task.completed) {
                    NotificationHelper.showDueReminder(context, task)
                }
            } finally {
                pendingResult.finish()
            }
        }
    }

    companion object {
        const val EXTRA_TASK_ID = "extra_task_id"
    }
}
