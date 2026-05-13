package com.taskflow.app.ui.main

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.taskflow.app.R
import com.taskflow.app.TaskFlowApplication
import com.taskflow.app.data.model.TaskPriority
import com.taskflow.app.databinding.FragmentMainBinding
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(
            requireActivity().application,
            (requireActivity().application as TaskFlowApplication).repository
        )
    }

    private lateinit var listAdapter: MainListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.title = getString(R.string.app_name)
        binding.toolbar.setupWithNavController(findNavController())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        listAdapter = MainListAdapter(
            onToggleComplete = { task, completed -> viewModel.setCompleted(task, completed) },
            onOpen = { task ->
                findNavController().navigate(
                    R.id.action_mainFragment_to_taskEditorFragment,
                    bundleOf("taskId" to task.id)
                )
            }
        )

        binding.taskRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator().apply {
                supportsChangeAnimations = true
                addDuration = 180
                changeDuration = 180
                moveDuration = 180
                removeDuration = 180
            }
        }

        ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean = false

                override fun getSwipeDirs(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
                    if (viewHolder.itemViewType != MainListAdapter.ROW_VIEW_TYPE) return 0
                    return super.getSwipeDirs(recyclerView, viewHolder)
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.bindingAdapterPosition
                    if (position == RecyclerView.NO_POSITION) return
                    val item = listAdapter.currentList.getOrNull(position) as? MainListItem.Row ?: return
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(R.string.delete_task_title)
                        .setMessage(R.string.delete_task_message)
                        .setNegativeButton(android.R.string.cancel) { _, _ ->
                            listAdapter.notifyItemChanged(position)
                        }
                        .setPositiveButton(R.string.action_delete) { _, _ ->
                            viewModel.deleteTask(item.task)
                        }
                        .show()
                }
            }
        ).attachToRecyclerView(binding.taskRecycler)

        binding.searchInput.doAfterTextChanged { editable ->
            viewModel.setSearchQuery(editable?.toString().orEmpty())
        }

        binding.filterGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            val id = checkedIds.firstOrNull() ?: return@setOnCheckedStateChangeListener
            when (id) {
                R.id.chip_all -> viewModel.setPriorityFilter(null)
                R.id.chip_high -> viewModel.setPriorityFilter(TaskPriority.HIGH)
                R.id.chip_medium -> viewModel.setPriorityFilter(TaskPriority.MEDIUM)
                R.id.chip_low -> viewModel.setPriorityFilter(TaskPriority.LOW)
            }
        }

        binding.fabAdd.setOnClickListener {
            findNavController().navigate(
                R.id.action_mainFragment_to_taskEditorFragment,
                bundleOf("taskId" to 0L)
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.items.collect { items ->
                    listAdapter.submitList(items) {
                        binding.emptyState.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
