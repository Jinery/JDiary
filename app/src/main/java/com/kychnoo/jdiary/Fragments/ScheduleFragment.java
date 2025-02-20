package com.kychnoo.jdiary.Fragments;

import android.content.Context;
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

import com.kychnoo.jdiary.Adapters.ScheduleAdapter;
import com.kychnoo.jdiary.Database.DatabaseHelper;
import com.kychnoo.jdiary.Interfaces.ToolbarTitleSetter;
import com.kychnoo.jdiary.OtherClasses.Schedule;
import com.kychnoo.jdiary.R;

import java.util.ArrayList;
import java.util.List;

public class ScheduleFragment extends Fragment {

    private RecyclerView recyclerView;
    private ScheduleAdapter adapter;
    private List<Schedule> scheduleItems;
    private DatabaseHelper databaseHelper;

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

        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        toolbarTitleSetter.setToolbarTitle("Расписание");

        recyclerView = view.findViewById(R.id.rvSchedules);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        databaseHelper = new DatabaseHelper(requireContext());
        scheduleItems = new ArrayList<>();

        loadSchedules();
        return view;
    }

    private void loadSchedules() {

        Cursor cursor = databaseHelper.getAllSchedules();
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCHEDULE_ID));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCHEDULE_DATE));
                int lessonsCount = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCHEDULE_LESSONS_COUNT));
                String className = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCHEDULE_CLASS));
                scheduleItems.add(new Schedule(id, date, lessonsCount, className));
            } while (cursor.moveToNext());
        }
        cursor.close();

        adapter = new ScheduleAdapter(requireContext(), scheduleItems);
        recyclerView.setAdapter(adapter);

    }


}
