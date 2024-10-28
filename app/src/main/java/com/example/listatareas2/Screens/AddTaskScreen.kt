package com.example.listatareas.Screens

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.listatareas.BaseDeDatos.TaskDatabaseHelper
import com.example.listatareas2.R
import com.example.listatareas2.ui.theme.azulClarito

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(context: Context, navigateBack: () -> Unit) {
    val dbHelper = TaskDatabaseHelper(context)
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf("Baja") }
    var cost by remember { mutableStateOf("") }
    val priorities = listOf("Baja", "Media", "Alta")
    var expanded by remember { mutableStateOf(false) }
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text(stringResource(R.string.task)) },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text(stringResource(R.string.description)) },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )
        OutlinedTextField(
            value = date,
            onValueChange = { date = it },
            label = { Text(stringResource(R.string.date)) },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = priority,
                onValueChange = { },
                label = { Text(stringResource(R.string.priority)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .menuAnchor(),
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                priorities.forEach { label ->
                    DropdownMenuItem(
                        text = { Text(label) },
                        onClick = {
                            priority = label
                            expanded = false
                        }
                    )
                }
            }
        }
        OutlinedTextField(
            value = cost,
            onValueChange = { cost = it },
            label = { Text(stringResource(R.string.cost)) },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                when {
                    title.isBlank() || date.isBlank() || cost.isBlank() || description.isBlank() -> {
                        snackbarMessage = "Por favor, completa todos los campos."
                        showSnackbar = true
                    }
                    cost.toDoubleOrNull() == null -> {
                        snackbarMessage = "El campo coste no puede tener letras, solo números y un punto para decimales."
                        showSnackbar = true
                    }
                    else -> {
                        dbHelper.addTask(title, description, date, priority, cost.toDoubleOrNull() ?: 0.0)
                        navigateBack()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = azulClarito
            )
        ) {
            Text(stringResource(R.string.add))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = navigateBack,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = azulClarito
            )
        ) {
            Text(stringResource(R.string.cancel))
        }
    }

    if (showSnackbar) {
        Snackbar(
            action = {
                TextButton(onClick = { showSnackbar = false }) {
                    Text("OK")
                }
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(snackbarMessage)
        }
    }
}