package com.example.rahul.thenotesapp.ui.notes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.rahul.thenotesapp.data.local.entity.Note
import com.example.rahul.thenotesapp.databinding.ItemNoteBinding
import java.text.SimpleDateFormat
import java.util.*

class NoteAdapter(
    private val onNoteClick: (Note) -> Unit,
    private val onNoteLongClick: (Note) -> Unit
) : ListAdapter<Note, NoteAdapter.NoteViewHolder>(NoteDiffCallback()) {

    private val sdf = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class NoteViewHolder(private val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(note: Note) {
            binding.tvNoteTitle.text = note.title
            binding.tvNoteDesc.text = note.description
            binding.tvNoteDate.text = sdf.format(Date(note.timestamp))

            // 🎨 Background
            binding.cardNote.setCardBackgroundColor(note.color)

            // 🔥 Auto text color contrast
            val darkness = 1 - (0.299 * android.graphics.Color.red(note.color) +
                    0.587 * android.graphics.Color.green(note.color) +
                    0.114 * android.graphics.Color.blue(note.color)) / 255

            val textColor = if (darkness < 0.5)
                android.graphics.Color.BLACK
            else
                android.graphics.Color.WHITE

            binding.tvNoteTitle.setTextColor(textColor)
            binding.tvNoteDesc.setTextColor(textColor)
            binding.tvNoteDate.setTextColor(textColor)

            // ✨ Animation
            binding.root.alpha = 0f
            binding.root.animate().alpha(1f).setDuration(300).start()

            // Click
            binding.root.setOnClickListener {
                onNoteClick(note)
            }

            // Long click
            binding.root.setOnLongClickListener {
                onNoteLongClick(note)
                true
            }
        }
    }

    class NoteDiffCallback : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Note, newItem: Note) = oldItem == newItem
    }
}