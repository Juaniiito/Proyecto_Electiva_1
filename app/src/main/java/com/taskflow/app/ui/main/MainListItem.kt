package com.taskflow.app.ui.main

import com.taskflow.app.data.local.TaskEntity
import com.taskflow.app.data.model.TaskSection

sealed interface MainListItem {
    data class Header(val section: TaskSection) : MainListItem
    data class Row(val task: TaskEntity) : MainListItem
}
