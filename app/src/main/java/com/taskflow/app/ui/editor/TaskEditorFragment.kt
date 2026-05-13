package com.taskflow.app.ui.editor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.taskflow.app.R
import com.taskflow.app.TaskFlowApplication
import com.taskflow.app.data.model.TaskPriority
import com.taskflow.app.databinding.FragmentTaskEditorBinding
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

class TaskEditorFragment : Fragment() {

    private var _binding: FragmentTaskEditorBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TaskEditorViewModel by viewModels {
        TaskEditorViewModelFactory(
            this,
            arguments ?: Bundle(),
            requireActivity().application,
            (requireActivity().application as TaskFlowApplication).repository
        )
    }

    private val dateFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("d MMM yyyy", Locale.getDefault())

    private var hydrated = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTaskEditorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        val taskId = arguments?.getLong("taskId") ?: 0L
        binding.toolbar.title = if (taskId > 0L) {
            getString(R.string.editor_edit_title)
        } else {
            getString(R.string.editor_new_title)
        }

        val categories = resources.getStringArray(R.array.task_categories)
        binding.categoryInput.setAdapter(
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, categories)
        )

        binding.priorityGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (!isChecked) return@addOnButtonCheckedListener
            val priority = when (checkedId) {
                R.id.btn_priority_high -> TaskPriority.HIGH
                R.id.btn_priority_medium -> TaskPriority.MEDIUM
                else -> TaskPriority.LOW
            }
            viewModel.updatePriority(priority)
        }

        binding.pickDateButton.setOnClickListener { openDatePicker() }
        binding.clearDateButton.setOnClickListener { viewModel.updateDueDate(null) }

        binding.toolbar.inflateMenu(R.menu.menu_task_editor)
        binding.toolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.action_save) {
                viewModel.save(
                    binding.titleInput.text?.toString().orEmpty(),
                    binding.descriptionInput.text?.toString().orEmpty(),
                    binding.categoryInput.text?.toString().orEmpty()
                ) { findNavController().navigateUp() }
                true
            } else {
                false
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.initialTask.collect { task ->
                        if (task == null || hydrated) return@collect
                        binding.titleInput.setText(task.title)
                        binding.descriptionInput.setText(task.description)
                        binding.categoryInput.setText(task.category)
                        hydrated = true
                    }
                }
                launch {
                    viewModel.uiState.collect { state ->
                        when (state.priority) {
                            TaskPriority.HIGH -> binding.priorityGroup.check(R.id.btn_priority_high)
                            TaskPriority.MEDIUM -> binding.priorityGroup.check(R.id.btn_priority_medium)
                            TaskPriority.LOW -> binding.priorityGroup.check(R.id.btn_priority_low)
                        }

                        binding.dateSummary.text = state.dueDateMillis?.let { millis ->
                            val date = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
                            date.format(dateFormatter)
                        } ?: getString(R.string.task_no_date)

                        binding.clearDateButton.isEnabled = state.dueDateMillis != null

                        binding.titleLayout.error = if (state.errorMessage == TaskEditorViewModel.ERROR_TITLE) {
                            getString(R.string.error_title_required)
                        } else {
                            null
                        }
                    }
                }
            }
        }
    }

    private fun openDatePicker() {
        val current = viewModel.uiState.value.dueDateMillis
        val selection = current?.let { localMillisToUtcSelection(it) } ?: MaterialDatePicker.todayInUtcMilliseconds()
        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(R.string.task_pick_date)
            .setSelection(selection)
            .build()
        picker.addOnPositiveButtonClickListener { utcMillis ->
            val pickedUtc = Instant.ofEpochMilli(utcMillis).atZone(ZoneOffset.UTC).toLocalDate()
            val localNoon = pickedUtc.atTime(12, 0).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            viewModel.updateDueDate(localNoon)
        }
        picker.show(childFragmentManager, "task_date")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun localMillisToUtcSelection(localMillis: Long): Long {
        val localDate = Instant.ofEpochMilli(localMillis).atZone(ZoneId.systemDefault()).toLocalDate()
        return localDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
    }
}
