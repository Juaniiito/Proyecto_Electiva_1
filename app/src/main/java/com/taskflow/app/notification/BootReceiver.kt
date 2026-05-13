package com.taskflow.app.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.taskflow.app.TaskFlowApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action != Intent.ACTION_BOOT_COMPLETED) return
        val pendingResult = goAsync()
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        scope.launch {
            try {
                val app = context.applicationContext as? TaskFlowApplication ?: return@launch
                val tasks = app.repository.observeTasks().first()
                tasks.filter { !it.completed && it.dueDateMillis != null }
                    .forEach { TaskAlarmScheduler.schedule(context, it) }
            } finally {
                pendingResult.finish()
            }
        }
    }
}
