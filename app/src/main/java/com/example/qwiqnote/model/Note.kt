
package com.example.qwiqnote.model

data class Note(
    val id: Long = System.currentTimeMillis(),
    val content: String
)