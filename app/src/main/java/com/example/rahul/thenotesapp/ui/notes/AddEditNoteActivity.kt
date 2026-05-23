package com.example.rahul.thenotesapp.ui.notes

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.rahul.thenotesapp.R
import com.example.rahul.thenotesapp.data.local.entity.Note
import com.example.rahul.thenotesapp.databinding.ActivityAddEditNoteBinding
import com.example.rahul.thenotesapp.viewmodel.NoteViewModel

class AddEditNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditNoteBinding
    private val viewModel: NoteViewModel by viewModels()

    private var selectedColor: Int = 0
    private var existingNote: Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddEditNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        selectedColor = ContextCompat.getColor(this, R.color.white)

        // ✅ Safe cast
        existingNote = intent.getSerializableExtra("EXTRA_NOTE") as? Note

        if (existingNote != null) {
            binding.etNoteTitle.setText(existingNote?.title)
            binding.etNoteDesc.setText(existingNote?.description)
            selectedColor = existingNote?.color ?: selectedColor
            binding.addEditNoteRoot.setBackgroundColor(selectedColor)

            supportActionBar?.title = "Edit Note"
        } else {
            supportActionBar?.title = "Add Note"
        }

        setupColorPicker()

        binding.btnSaveNote.setOnClickListener {
            saveNote()
        }
    }

    private fun setupColorPicker() {
        binding.viewYellow.setOnClickListener { updateColor(R.color.note_yellow) }
        binding.viewBlue.setOnClickListener { updateColor(R.color.note_blue) }
        binding.viewGreen.setOnClickListener { updateColor(R.color.note_green) }
        binding.viewPink.setOnClickListener { updateColor(R.color.note_pink) }
        binding.viewPurple.setOnClickListener { updateColor(R.color.note_purple) }
        binding.viewWhite.setOnClickListener { updateColor(R.color.white) }
    }

    private fun updateColor(colorResId: Int) {
        selectedColor = ContextCompat.getColor(this, colorResId)
        binding.addEditNoteRoot.setBackgroundColor(selectedColor)
    }

    private fun saveNote() {
        val title = binding.etNoteTitle.text.toString().trim()
        val description = binding.etNoteDesc.text.toString().trim()

        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show()
            return
        }

        val timestamp = System.currentTimeMillis()

        if (existingNote == null) {
            val note = Note(
                title = title,
                description = description,
                timestamp = timestamp,
                color = selectedColor
            )
            viewModel.insert(note)
            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show()
        } else {
            val updatedNote = existingNote?.copy(
                title = title,
                description = description,
                timestamp = timestamp,
                color = selectedColor
            )
            updatedNote?.let { viewModel.update(it) }
            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show()
        }

        finish()
    }
}