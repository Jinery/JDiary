package com.kychnoo.jdiary.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kychnoo.jdiary.Adapters.NotesAdapter;
import com.kychnoo.jdiary.Database.DatabaseHelper;
import com.kychnoo.jdiary.Interfaces.ToolbarTitleSetter;
import com.kychnoo.jdiary.OtherClasses.Note;
import com.kychnoo.jdiary.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotesFragment extends Fragment implements NotesAdapter.OnNoteClickListener, NotesAdapter.OnNoteEditListener,NotesAdapter.OnNoteDeleteListener{

    private RecyclerView rvNotes;

    private Button btnAddNote;

    private NotesAdapter adapter;
    private List<Note> notes;

    private DatabaseHelper databaseHelper;

    private AlarmManager alarmManager;

    private PendingIntent pendingIntent;

    private String phone;

    private ToolbarTitleSetter toolbarTitleSetter;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ToolbarTitleSetter)
            toolbarTitleSetter = (ToolbarTitleSetter) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        toolbarTitleSetter = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        phone = requireActivity().getIntent().getStringExtra("phone");

        toolbarTitleSetter.setToolbarTitle("Заметки");

        databaseHelper = new DatabaseHelper(requireContext());
        alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);

        rvNotes = view.findViewById(R.id.rvNotes);
        btnAddNote = view.findViewById(R.id.btnAddNote);

        rvNotes.setLayoutManager(new LinearLayoutManager(requireContext()));
        notes = new ArrayList<>();
        adapter = new NotesAdapter(notes, (NotesAdapter.OnNoteClickListener)this, (NotesAdapter.OnNoteEditListener) this, (NotesAdapter.OnNoteDeleteListener)this);
        rvNotes.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        rvNotes.setAdapter(adapter);

        loadNotes();

        btnAddNote.setOnClickListener(v -> showAddNoteDialog(null));

        return view;
    }

    private void loadNotes() {
        notes.clear();
        Cursor cursor = databaseHelper.getNotesByUser(phone);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NOTE_ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NOTE_TITLE));
            String content = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NOTE_CONTENT));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NOTE_DATE));

            notes.add(new Note(id, title, content, date));
        }
        adapter.notifyDataSetChanged();
        cursor.close();
    }

    private void showAddNoteDialog(Note note) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(note == null ? "Добавить заметку" : "Редактировать заметку");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_note, null);
        EditText editTextTitle = dialogView.findViewById(R.id.edit_text_title);
        EditText editTextContent = dialogView.findViewById(R.id.edit_text_content);

        if (note != null) {
            editTextTitle.setText(note.getTitle());
            editTextContent.setText(note.getContent());
        }


        builder.setView(dialogView)
                .setPositiveButton("Сохранить", (dialog, which) -> {
                    String title = editTextTitle.getText().toString().trim();
                    String content = editTextContent.getText().toString().trim();
                    String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

                    if (note == null) {
                        databaseHelper.addNote(phone, title, content, date);
                    } else {
                        databaseHelper.updateNote(note.getId(), title, content, date);
                    }
                    loadNotes();
                })
                .setNegativeButton("Отмена", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    private void showCheckNoteDialog(Note note) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_check_note, null);
        TextView tvNoteTitle = dialogView.findViewById(R.id.tvNoteTitle);
        TextView tvNoteContent = dialogView.findViewById(R.id.tvNoteContent);

        if (note != null) {
            tvNoteTitle.setText(note.getTitle());
            tvNoteContent.setText(note.getContent());
        }

        builder.setView(dialogView)
                .setNegativeButton("Закрыть", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    @Override
    public void onNoteClick(Note note) {
        showCheckNoteDialog(note);
    }

    @Override
    public void onNoteDelete(Note note) {
        databaseHelper.deleteNote(note.getId());
        loadNotes();
    }

    @Override
    public void onNoteEdit(Note note) {
        showAddNoteDialog(note);
    }
}
