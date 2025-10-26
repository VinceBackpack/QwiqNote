package com.example.qwiqnote.data

import android.content.Context
import com.example.qwiqnote.model.Note
import org.json.JSONArray
import org.json.JSONObject

private const val PREFS_NAME = "notes_prefs"
private const val NOTES_KEY = "notes_list"

/**
 * Saves a list of notes into SharedPreferences as a JSON string.
 */
fun saveNotes(context: Context, notes: List<Note>) {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val jsonArray = JSONArray()
    notes.forEach { note ->
        val obj = JSONObject().apply {
            put("id", note.id)
            put("content", note.content)
        }
        jsonArray.put(obj)
    }
    prefs.edit().putString(NOTES_KEY, jsonArray.toString()).apply()
}

/**
 * Loads a list of notes from SharedPreferences.
 * Returns an empty list if none are saved.
 */
fun loadNotes(context: Context): List<Note> {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val jsonString = prefs.getString(NOTES_KEY, "[]")

    return try {
        val jsonArray = JSONArray(jsonString)
        val notes = mutableListOf<Note>()

        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            notes.add(
                Note(
                    id = obj.getLong("id"),
                    content = obj.getString("content")
                )
            )
        }
        notes
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()  // Return empty list if parsing fails
    }
}

/**
 * Deletes a note by ID and returns the updated list.
 */
fun deleteNote(context: Context, notes: List<Note>, noteToDelete: Note): List<Note> {
    val updatedNotes = notes.filter { it.id != noteToDelete.id }
    saveNotes(context, updatedNotes)
    return updatedNotes
}