package com.kychnoo.jdiary.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kychnoo.jdiary.OtherClasses.Note;
import com.kychnoo.jdiary.R;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private List<Note> notes;
    private OnNoteClickListener listener;
    private OnNoteEditListener editListener;
    private OnNoteDeleteListener deleteListener;

    public interface OnNoteClickListener {
        void onNoteClick(Note note);
    }

    public interface  OnNoteEditListener {
        void onNoteEdit(Note note);
    }

    public interface OnNoteDeleteListener {
        void onNoteDelete(Note note);
    }

    public NotesAdapter(List<Note> notes, OnNoteClickListener listener, OnNoteEditListener editListener, OnNoteDeleteListener deleteListener) {
        this.notes = notes;
        this.listener = listener;
        this.editListener = editListener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.tvTitle.setText(note.getTitle());
        holder.tvDate.setText(note.getDate());

        holder.itemView.setOnClickListener(v -> listener.onNoteClick(note));
        holder.ivEdit.setOnClickListener(v -> editListener.onNoteEdit(note));
        holder.ivDelete.setOnClickListener(v -> deleteListener.onNoteDelete(note));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvDate;

        ImageView ivEdit;
        ImageView ivDelete;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDate = itemView.findViewById(R.id.tvDate);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            ivDelete = itemView.findViewById(R.id.ivDelete);
        }
    }
}
