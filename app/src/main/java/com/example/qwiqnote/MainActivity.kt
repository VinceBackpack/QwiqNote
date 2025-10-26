package com.example.qwiqnote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.qwiqnote.ui.theme.QwiqNoteTheme
import com.example.qwiqnote.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QwiqNoteTheme {
                NotesApp()
            }
        }
    }
}