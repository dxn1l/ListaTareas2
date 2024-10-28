package com.example.listatareas.Screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.listatareas.BaseDeDatos.TaskDatabaseHelper
import com.example.listatareas2.R
import com.example.listatareas2.ui.theme.azulClarito
import com.example.listatareas2.ui.theme.naranja

@Composable
fun TaskDetailsScreen(context: Context, taskId: Int, navigateBack: () -> Unit) {
    val dbHelper = TaskDatabaseHelper(context)
    val task = dbHelper.getAllTasks().find { it.id == taskId }

    val priorityColor = when (task?.priority) {
        "Baja" -> Color.Green
        "Media" -> naranja
        "Alta" -> Color.Red
        else -> Color.Black
    }

    Column(modifier = Modifier.padding(16.dp)) {
        task?.let {
            Text(text = stringResource(R.string.task) + ": " + it.title)
            Text(text = stringResource(R.string.description) + ": " + it.description)
            Text(text = stringResource(R.string.date) + ": " + it.date)
            Row {
                Text(text = stringResource(R.string.priority) + ": ")
                Text(text = it.priority, color = priorityColor)
            }
            Text(text = stringResource(R.string.cost) + ": " + it.cost + "" + stringResource(R.string.currency))
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