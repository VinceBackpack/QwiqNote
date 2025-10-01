package com.example.qwiqnote

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.qwiqnote.ui.theme.QwiqNoteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QwiqNoteTheme {
                NotesApp(this)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesApp(context: Context) {

    var noteText by remember { mutableStateOf("")}
    var notes by remember { mutableStateOf(loadNotes(context))}

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        //Title
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(50.dp)
                .height(50.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "QwiqNote!",
                style = MaterialTheme.typography.headlineMedium
            )
        }
        //Space between title and text box
        Spacer(modifier = Modifier.height(100.dp))
        //Text box for user input for notes
        OutlinedTextField(
            value = noteText,
            onValueChange = { noteText = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Type your note...") }
        )
        //space between button and text box
        Spacer(modifier = Modifier.height(8.dp))

        //Save notes button: Notes saved are added to the "notes" list
        Button(
            onClick = {
                if (noteText.isNotBlank()) {
                    notes = notes + noteText
                    saveNotes(context, notes)
                    noteText = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Note")
        }
        //Space between save note button and saved notes list
        Spacer(modifier = Modifier.height(16.dp))

        Text("Things left to do:", style = MaterialTheme.typography.titleMedium)
        //List out the saved notes
        LazyColumn {
            items(notes) { note ->
                NoteCard(
                    note = note,
                    onEdit = { newText ->
                        val updatedNotes = notes.map {
                            if (it == note) newText else it
                        }
                        notes = updatedNotes
                        saveNotes(context, updatedNotes)
                    },
                    onDelete = {
                        notes = deleteNote(context, notes, note)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteCard(note: String, onDelete: () -> Unit, onEdit:(String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        var isEditing by remember { mutableStateOf(false) }
        var editedText by remember { mutableStateOf(note) }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (isEditing) {
                OutlinedTextField(
                    value = editedText,
                    onValueChange = { editedText = it }
                )
                Button(onClick = {
                    onEdit(editedText)
                    isEditing = false
                }) {
                    Text("Save")
                }
            } else {
                Text(note)
                Row {
                    // Edit Button
                    Button(onClick = { isEditing = true }) {
                        Text("Edit")
                    }
                    //Delete Button
                    Button(onClick = { onDelete() }) {
                        Text("Delete")
                    }
                }
            }
        }
    }
}

private const val PREFS_NAME = "notes_prefs"
private const val NOTES_KEY = "notes_list"


//Save notes function: Notes saved in Shared preference so the notes won't get deleted when closing the app
fun saveNotes(context: Context, notes: List<String>) {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    prefs.edit().putStringSet(NOTES_KEY, notes.toSet()).apply()
}

//Load the list of notes from Shared preferences
fun loadNotes(context: Context): List<String> {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    return prefs.getStringSet(NOTES_KEY, emptySet())?.toList() ?: emptyList()
}

fun deleteNote(context: Context, notes: List<String>, noteToDelete: String): List<String> {
    val updatedNotes = notes - noteToDelete
    saveNotes(context, updatedNotes)
    return updatedNotes
}