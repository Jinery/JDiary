package com.kychnoo.jdiary.Fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kychnoo.jdiary.Adapters.StudentsAdapter;
import com.kychnoo.jdiary.Database.DatabaseHelper;
import com.kychnoo.jdiary.Interfaces.ToolbarTitleSetter;
import com.kychnoo.jdiary.OtherClasses.Student;
import com.kychnoo.jdiary.R;

import java.util.ArrayList;
import java.util.List;

public class ClassFragment extends Fragment {

    private RecyclerView recyclerView;

    private StudentsAdapter studentsAdapter;
    private List<Student> studentList;

    private DatabaseHelper databaseHelper;
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
        View view = inflater.inflate(R.layout.fragment_class, container, false);

        toolbarTitleSetter.setToolbarTitle("Мой лкасс");

        databaseHelper = new DatabaseHelper(requireContext());
        recyclerView = view.findViewById(R.id.classRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        studentList = new ArrayList<>();
        studentsAdapter = new StudentsAdapter(studentList);
        recyclerView.setAdapter(studentsAdapter);

        phone = requireActivity().getIntent().getStringExtra("phone");

        TextView tvMyClass = view.findViewById(R.id.tvStudentsInClass);

        Cursor cursor = databaseHelper.getUserByPhone(phone);

        if(cursor != null && cursor.moveToFirst()) {
            String userClass = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CLASS));
            int studentsCount = databaseHelper.getStudentCountInClass(userClass);
            tvMyClass.setText("Всего в классе: " + studentsCount + " человек.");

            cursor = databaseHelper.getStudentsInClass(userClass);
            if(cursor != null && cursor.moveToFirst()) {
                do {
                    String username = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USERNAME));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION));
                    studentList.add(new Student(username, description));
                }
                while(cursor.moveToNext());
                cursor.close();
            }
            studentsAdapter.notifyDataSetChanged();
        }
        return view;
    }
}
