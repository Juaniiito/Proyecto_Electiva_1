package com.taskflow.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.taskflow.app.ui.TaskScreen
import com.taskflow.app.ui.theme.TaskFlowTheme
import com.taskflow.app.viewmodel.TaskViewModel
import com.taskflow.app.viewmodel.TaskViewModelFactory

class MainActivity : ComponentActivity() {
    private val viewModel: TaskViewModel by viewModels {
        val app = application as TaskFlowApplication
        TaskViewModelFactory(app, app.repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskFlowTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TaskScreen(viewModel)
                }
            }
        }
    }
}
