package com.example.rahul.thenotesapp.data.repository

import androidx.lifecycle.LiveData
import com.example.rahul.thenotesapp.data.local.dao.NoteDao
import com.example.rahul.thenotesapp.data.local.entity.Note

class NoteRepository(private val noteDao: NoteDao) {
    val allNotes: LiveData<List<Note>> = noteDao.getAllNotes()

    suspend fun insert(note: Note) {
        noteDao.insertNote(note)
    }

    suspend fun update(note: Note) {
        noteDao.updateNote(note)
    }

    suspend fun delete(note: Note) {
        noteDao.deleteNote(note)
    }

    fun searchNotes(query: String): LiveData<List<Note>> {
        return noteDao.searchNotes("%$query%")
    }
}