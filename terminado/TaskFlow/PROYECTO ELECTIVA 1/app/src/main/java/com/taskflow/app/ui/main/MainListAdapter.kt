package com.taskflow.app.ui.main

import android.content.res.ColorStateList
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.taskflow.app.R
import com.taskflow.app.data.local.TaskEntity
import com.taskflow.app.data.model.TaskPriority
import com.taskflow.app.data.model.TaskSection
import com.taskflow.app.databinding.ItemSectionHeaderBinding
import com.taskflow.app.databinding.ItemTaskRowBinding
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class MainListAdapter(
    private val onToggleComplete: (TaskEntity, Boolean) -> Unit,
    private val onOpen: (TaskEntity) -> Unit
) : ListAdapter<MainListItem, RecyclerView.ViewHolder>(MainListDiffCallback()) {

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is MainListItem.Header -> HEADER_VIEW_TYPE
        is MainListItem.Row -> ROW_VIEW_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            HEADER_VIEW_TYPE -> HeaderViewHolder(
                ItemSectionHeaderBinding.inflate(inflater, parent, false)
            )

            ROW_VIEW_TYPE -> TaskViewHolder(
                ItemTaskRowBinding.inflate(inflater, parent, false),
                onToggleComplete,
                onOpen
            )

            else -> error("Unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is MainListItem.Header -> (holder as HeaderViewHolder).bind(item.section)
            is MainListItem.Row -> (holder as TaskViewHolder).bind(item.task)
        }
    }

    private class HeaderViewHolder(
        private val binding: ItemSectionHeaderBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(section: TaskSection) {
            val res = binding.root.context
            val title = when (section) {
                TaskSection.TODAY -> res.getString(R.string.section_today)
                TaskSection.UPCOMING -> res.getString(R.string.section_upcoming)
                TaskSection.SOMEDAY -> res.getString(R.string.section_someday)
                TaskSection.COMPLETED -> res.getString(R.string.section_completed)
            }
            binding.sectionTitle.text = title
        }
    }

    private class TaskViewHolder(
        private val binding: ItemTaskRowBinding,
        private val onToggleComplete: (TaskEntity, Boolean) -> Unit,
        private val onOpen: (TaskEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private val dateFormatter: DateTimeFormatter =
            DateTimeFormatter.ofPattern("d MMM yyyy", Locale.getDefault())

        fun bind(task: TaskEntity) {
            binding.taskTitle.text = task.title
            binding.taskDescription.text = task.description
            binding.taskCategory.text = task.category

            val due = task.dueDateMillis?.let {
                Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate().format(dateFormatter)
            }
            binding.taskDue.text = due ?: binding.root.context.getString(R.string.task_no_date)
            binding.taskDue.alpha = if (task.dueDateMillis == null) 0.65f else 1f

            val priority = TaskPriority.fromStorage(task.priority)
            val context = binding.root.context
            val (bg, fg) = when (priority) {
                TaskPriority.HIGH -> R.color.priority_high_bg to R.color.priority_high_fg
                TaskPriority.MEDIUM -> R.color.priority_medium_bg to R.color.priority_medium_fg
                TaskPriority.LOW -> R.color.priority_low_bg to R.color.priority_low_fg
            }
            binding.priorityChip.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(context, bg))
            binding.priorityChip.setTextColor(ContextCompat.getColor(context, fg))
            binding.priorityChip.text = when (priority) {
                TaskPriority.HIGH -> context.getString(R.string.priority_high)
                TaskPriority.MEDIUM -> context.getString(R.string.priority_medium)
                TaskPriority.LOW -> context.getString(R.string.priority_low)
            }

            binding.taskCheckbox.isChecked = task.completed
            binding.taskTitle.paintFlags = if (task.completed) {
                binding.taskTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                binding.taskTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }

            binding.taskCheckbox.setOnCheckedChangeListener(null)
            binding.taskCheckbox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked != task.completed) {
                    onToggleComplete(task, isChecked)
                }
            }

            binding.root.setOnClickListener { onOpen(task) }
            binding.card.strokeColor = ContextCompat.getColor(
                binding.root.context,
                if (task.completed) R.color.md_outline_variant else R.color.md_outline
            )
        }
    }

    companion object {
        const val HEADER_VIEW_TYPE = 0
        const val ROW_VIEW_TYPE = 1
    }
}
