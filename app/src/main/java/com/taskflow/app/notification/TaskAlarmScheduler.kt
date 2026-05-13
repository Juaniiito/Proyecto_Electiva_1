package com.taskflow.app.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.AlarmManagerCompat
import com.taskflow.app.data.local.TaskEntity
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

object TaskAlarmScheduler {

    private const val ACTION = "com.taskflow.app.ACTION_TASK_REMINDER"

    fun schedule(context: Context, task: TaskEntity) {
        cancel(context, task.id)
        if (task.completed) return
        val dueMillis = task.dueDateMillis ?: return
        val zone = ZoneId.systemDefault()
        val triggerAt = nextNineAmOnDueDay(dueMillis, zone) ?: return
        if (triggerAt <= System.currentTimeMillis()) return

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pi = pendingIntent(context, task.id)
        AlarmManagerCompat.setExactAndAllowWhileIdle(
            alarmManager,
            AlarmManager.RTC_WAKEUP,
            triggerAt,
            pi
        )
    }

    fun cancel(context: Context, taskId: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent(context, taskId))
    }

    private fun pendingIntent(context: Context, taskId: Long): PendingIntent {
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            action = ACTION
            putExtra(ReminderReceiver.EXTRA_TASK_ID, taskId)
        }
        val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        return PendingIntent.getBroadcast(context, taskId.toInt(), intent, flags)
    }

    private fun nextNineAmOnDueDay(dueMillis: Long, zone: ZoneId): Long? {
        val dueZdt = ZonedDateTime.ofInstant(Instant.ofEpochMilli(dueMillis), zone)
        val targetDay = dueZdt.toLocalDate()
        val atNine = ZonedDateTime.of(targetDay, LocalTime.of(9, 0), zone)
        return atNine.toInstant().toEpochMilli()
    }
}
