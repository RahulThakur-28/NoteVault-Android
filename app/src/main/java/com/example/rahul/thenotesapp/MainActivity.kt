package com.example.rahul.thenotesapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.rahul.thenotesapp.databinding.ActivityMainBinding
import com.example.rahul.thenotesapp.ui.auth.LoginActivity
import com.example.rahul.thenotesapp.ui.notes.AddEditNoteActivity
import com.example.rahul.thenotesapp.ui.notes.adapter.NoteAdapter
import com.example.rahul.thenotesapp.utils.PreferenceManager
import com.example.rahul.thenotesapp.viewmodel.NoteViewModel
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: NoteViewModel by viewModels()
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferenceManager = PreferenceManager(this)

        setupRecyclerView()
        observeNotes()
        setupSearchView()
        setupClickListeners()
        setupSwipeToDelete()
    }

    // 🔹 RecyclerView Setup
    private fun setupRecyclerView() {
        noteAdapter = NoteAdapter(
            onNoteClick = { note ->
                val intent = Intent(this, AddEditNoteActivity::class.java)
                intent.putExtra("EXTRA_NOTE", note)
                startActivity(intent)
            },
            onNoteLongClick = { note ->
                showDeleteDialog(note)
            }
        )

        binding.rvNotes.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = noteAdapter
        }
    }

    // 🔹 Observe Notes
    private fun observeNotes() {
        viewModel.allNotes.observe(this) { notes ->
            noteAdapter.submitList(notes)
            binding.tvEmpty.visibility =
                if (notes.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    // 🔹 Search Notes
    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    viewModel.searchNotes(it).observe(this@MainActivity) { notes ->
                        noteAdapter.submitList(notes)
                    }
                }
                return true
            }
        })
    }

    // 🔹 Click Listeners
    private fun setupClickListeners() {
        binding.fabAddNote.setOnClickListener {
            startActivity(Intent(this, AddEditNoteActivity::class.java))
        }

        binding.ivLogout.setOnClickListener {
            showLogoutDialog()
        }
    }

    // 🔹 Swipe to Delete
    private fun setupSwipeToDelete() {
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val note = noteAdapter.currentList.getOrNull(position) ?: return

                viewModel.delete(note)

                Snackbar.make(binding.root, "Note deleted", Snackbar.LENGTH_LONG)
                    .setAction("Undo") {
                        viewModel.insert(note)
                    }
                    .show()
            }
        }

        ItemTouchHelper(callback).attachToRecyclerView(binding.rvNotes)
    }

    // 🔹 Delete Dialog
    private fun showDeleteDialog(note: com.example.rahul.thenotesapp.data.local.entity.Note) {
        AlertDialog.Builder(this)
            .setTitle("Delete Note")
            .setMessage("Are you sure you want to delete this note?")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.delete(note)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    // 🔹 Logout Dialog
    private fun showLogoutDialog() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Logout") { _, _ ->
                preferenceManager.logout()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}