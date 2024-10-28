package com.example.listatareas.Screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.listatareas.BaseDeDatos.TaskDatabaseHelper
import com.example.listatareas2.R
import com.example.listatareas2.ui.theme.azulClarito

@Composable
fun CompletedTasksScreen(context: Context, navigateBack: () -> Unit) {
    val dbHelper = TaskDatabaseHelper(context)
    var tasks by remember { mutableStateOf(dbHelper.getAllTasks().filter { it.done }) }

    Column(modifier = Modifier.padding(16.dp)) {
        Box(modifier = Modifier.weight(1f)) {
            LazyColumn {
                items(tasks) { task ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = task.title ?: "")
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = navigateBack,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = azulClarito
            )
        ) {
            Text(stringResource(R.string.back_to_task_list))
        }
    }
}