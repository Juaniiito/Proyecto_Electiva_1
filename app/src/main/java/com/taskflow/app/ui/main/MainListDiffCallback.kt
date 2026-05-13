package com.taskflow.app.ui.main

import androidx.recyclerview.widget.DiffUtil

class MainListDiffCallback : DiffUtil.ItemCallback<MainListItem>() {
    override fun areItemsTheSame(oldItem: MainListItem, newItem: MainListItem): Boolean {
        return when {
            oldItem is MainListItem.Header && newItem is MainListItem.Header ->
                oldItem.section == newItem.section

            oldItem is MainListItem.Row && newItem is MainListItem.Row ->
                oldItem.task.id == newItem.task.id

            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: MainListItem, newItem: MainListItem): Boolean {
        return oldItem == newItem
    }
}
