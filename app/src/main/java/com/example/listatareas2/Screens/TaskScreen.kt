package com.example.listatareas.Screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.listatareas.BaseDeDatos.TaskDatabaseHelper
import com.example.listatareas2.R
import com.example.listatareas2.ui.theme.azulClarito

@Composable
fun TaskScreen(context: Context, navigateToPending: () -> Unit, navigateToCompleted: () -> Unit) {
    val dbHelper = TaskDatabaseHelper(context)
    var tasks by remember { mutableStateOf(dbHelper.getAllTasks()) }
    var showDialog by remember { mutableStateOf(false) }
    var taskTitle by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Button(
            onClick = { showDialog = true },
            modifier = Modifier.align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(
                containerColor = azulClarito
            )
        ) {
            Text(stringResource(R.string.add_task))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Box(modifier = Modifier.weight(1f)) {
            LazyColumn {
                items(tasks) { task ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(task.title ?: "")
                            Row {
                                Checkbox(
                                    checked = task.done,
                                    onCheckedChange = {
                                        dbHelper.markTaskAsDone(task.id, !task.done)
                                        tasks = dbHelper.getAllTasks()
                                    },
                                    colors = CheckboxDefaults.colors(
                                        checkmarkColor = Color.White,
                                        uncheckedColor = azulClarito,
                                        checkedColor = azulClarito
                                    )
                                )
                                Button(
                                    onClick = {
                                        dbHelper.deleteTask(task.id)
                                        tasks = dbHelper.getAllTasks()
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = azulClarito
                                    )
                                ) {
                                    Text(stringResource(R.string.delete))
                                }
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = navigateToPending,
                colors = ButtonDefaults.buttonColors(
                    containerColor = azulClarito
                )
            ) {
                Text(stringResource(R.string.pending_tasks))
            }
            Button(
                onClick = navigateToCompleted,
                colors = ButtonDefaults.buttonColors(
                    containerColor = azulClarito
                )
            ) {
                Text(stringResource(R.string.completed_tasks))
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(stringResource(R.string.add_task_title)) },
            text = {
                Column {
                    BasicTextField(
                        value = taskTitle,
                        onValueChange = { taskTitle = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        decorationBox = { innerTextField ->
                            if (taskTitle.isEmpty()) {
                                Text(stringResource(R.string.task))
                            }
                            innerTextField()
                        }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        dbHelper.addTask(taskTitle)
                        tasks = dbHelper.getAllTasks()
                        taskTitle = ""
                        showDialog = false
                    },
                    enabled = taskTitle.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = azulClarito
                    )
                ) {
                    Text(stringResource(R.string.add))
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = azulClarito
                    )
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}