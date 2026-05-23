package com.example.rahul.thenotesapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.rahul.thenotesapp.data.local.entity.Note

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM notes ORDER BY timestamp DESC")
    fun getAllNotes(): LiveData<List<Note>>

    @Query("SELECT * FROM notes WHERE title LIKE :query OR description LIKE :query ORDER BY timestamp DESC")
    fun searchNotes(query: String): LiveData<List<Note>>
}