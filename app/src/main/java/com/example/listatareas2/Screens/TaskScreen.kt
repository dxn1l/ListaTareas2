package com.example.listatareas.Screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
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
fun TaskScreen(context: Context, navigateToPending: () -> Unit, navigateToCompleted: () -> Unit, navigateToAddTask: () -> Unit, navigateToTaskDetails: (Int) -> Unit) {
    val dbHelper = TaskDatabaseHelper(context)
    var tasks by remember { mutableStateOf(dbHelper.getAllTasks()) }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Button(
            onClick = navigateToAddTask,
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
                                IconButton(
                                    onClick = { navigateToTaskDetails(task.id) },
                                    colors = IconButtonDefaults.iconButtonColors(
                                        contentColor = azulClarito
                                    )
                                ) {
                                    Icon(Icons.Filled.Info, contentDescription = stringResource(R.string.details))
                                }
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
                                IconButton(
                                    onClick = {
                                        dbHelper.deleteTask(task.id)
                                        tasks = dbHelper.getAllTasks()
                                    },
                                    colors = IconButtonDefaults.iconButtonColors(
                                        contentColor = azulClarito
                                    )
                                ) {
                                    Icon(Icons.Filled.Delete, contentDescription = stringResource(R.string.delete))
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
}