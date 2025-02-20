package com.kychnoo.jdiary.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kychnoo.jdiary.Adapters.GradesAdapter;
import com.kychnoo.jdiary.Database.DatabaseHelper;
import com.kychnoo.jdiary.OthetClasses.Grade;
import com.kychnoo.jdiary.R;

import java.util.ArrayList;
import java.util.List;

public class DiaryFragment extends Fragment {

    private RecyclerView rvGrades;

    private GradesAdapter gradesAdapter;

    private DatabaseHelper databaseHelper;

    private String phone;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_diary, container, false);

        databaseHelper = new DatabaseHelper(requireContext());

        phone = requireActivity().getIntent().getStringExtra("phone");

        rvGrades = view.findViewById(R.id.rvGrades);
        rvGrades.setLayoutManager(new LinearLayoutManager(requireContext()));

        loadGrades();

        return view;
    }

    private void loadGrades() {

        Cursor cursor = databaseHelper.getGradesByUser(phone);
        if(cursor != null) {
            List<Grade> gradeList = new ArrayList<>();
            while(cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_GRADE_ID));
                String userPhone = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_GRADE_USER_PHONE));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_GRADE_DATE));
                int score = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_GRADE_SCORE));
                gradeList.add(new Grade(id, userPhone, date, score));
            }
            cursor.close();
            gradesAdapter = new GradesAdapter(gradeList);
            rvGrades.setAdapter(gradesAdapter);
        }

    }
}
