package com.example.rahul.thenotesapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

import java.io.Serializable

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val timestamp: Long,
    val color: Int
) : Serializable