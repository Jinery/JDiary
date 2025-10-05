package com.kychnoo.jdiary.Fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.kychnoo.jdiary.Adapters.DateAdapter;
import com.kychnoo.jdiary.Adapters.LessonsAdapter;
import com.kychnoo.jdiary.Database.DatabaseHelper;
import com.kychnoo.jdiary.Interfaces.DateSelectedListener;
import com.kychnoo.jdiary.Interfaces.ToolbarTitleSetter;
import com.kychnoo.jdiary.Managers.CenterZoomLayoutManager;
import com.kychnoo.jdiary.OtherClasses.Lesson;
import com.kychnoo.jdiary.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ScheduleFragment extends Fragment implements DateSelectedListener {

    private RecyclerView recyclerView;
    private RecyclerView horizontalDateRecycler;
    private LessonsAdapter lessonsAdapter;
    private DateAdapter dateAdapter;
    private List<Lesson> lessonsList;
    private DatabaseHelper databaseHelper;
    private LinearSnapHelper snapHelper;

    private ToolbarTitleSetter toolbarTitleSetter;
    private String phone;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        toolbarTitleSetter.setToolbarTitle("Расписание");
        phone = requireActivity().getIntent().getStringExtra("phone");

        recyclerView = view.findViewById(R.id.rvSchedules);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        databaseHelper = new DatabaseHelper(requireContext());
        lessonsList = new ArrayList<>();

        lessonsAdapter = new LessonsAdapter(requireContext(), lessonsList);
        recyclerView.setAdapter(lessonsAdapter);

        initializeDateSelector(view);

        loadLessonsForLastAvailableDate();

        return view;
    }

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

    private void initializeDateSelector(View view) {
        View dateSelectorLayout = view.findViewById(R.id.dateSelectorLayout);
        horizontalDateRecycler = dateSelectorLayout.findViewById(R.id.recyclerViewDates);
        ImageButton btnPrevious = dateSelectorLayout.findViewById(R.id.btnPrevious);
        ImageButton btnNext = dateSelectorLayout.findViewById(R.id.btnNext);

        List<LocalDate> datesList = loadDatesFromDatabase();

        int initialPosition = findInitialPosition(datesList);

        dateAdapter = new DateAdapter(datesList, initialPosition, this);
        dateAdapter.attachToRecyclerView(horizontalDateRecycler);

        CenterZoomLayoutManager layoutManager = new CenterZoomLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
        );
        horizontalDateRecycler.setLayoutManager(layoutManager);
        horizontalDateRecycler.setAdapter(dateAdapter);
        horizontalDateRecycler.scrollToPosition(initialPosition);

        btnPrevious.setOnClickListener(v -> dateAdapter.selectPreviousDate());
        btnNext.setOnClickListener(v -> dateAdapter.selectNextDate());

        snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(horizontalDateRecycler);
    }

    private List<LocalDate> loadDatesFromDatabase() {
        List<LocalDate> dates = new ArrayList<>();

        String userClass = null;
        Cursor userCursor = databaseHelper.getUserByPhone(phone);
        if (userCursor != null && userCursor.moveToFirst()) {
            userClass = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CLASS));
            userCursor.close();
        }

        if (userClass == null) return dates;

        Cursor scheduleCursor = databaseHelper.getSchedulesByClass(userClass);
        if (scheduleCursor != null && scheduleCursor.moveToFirst()) {
            do {
                String dateString = scheduleCursor.getString(
                        scheduleCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCHEDULE_DATE)
                );

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    try {
                        LocalDate date = LocalDate.parse(dateString,
                                DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                        dates.add(date);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } while (scheduleCursor.moveToNext());
            scheduleCursor.close();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dates.sort(LocalDate::compareTo);
        }

        return dates;
    }

    private int findInitialPosition(List<LocalDate> dates) {
        if (dates == null || dates.isEmpty()) {
            return 0;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate today = LocalDate.now();

            for (int i = 0; i < dates.size(); i++) {
                if (dates.get(i).equals(today)) {
                    return i;
                }
            }
            for (int i = 0; i < dates.size(); i++) {
                if (!dates.get(i).isBefore(today)) {
                    return i;
                }
            }
        }

        return dates.size() - 1;
    }

    private void loadLessonsForLastAvailableDate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            List<LocalDate> availableDates = loadDatesFromDatabase();

            if (!availableDates.isEmpty()) {
                LocalDate lastDate = availableDates.get(availableDates.size() - 1);
                loadLessonsForDate(lastDate);

                if (dateAdapter != null) {
                    int lastPosition = availableDates.size() - 1;
                    dateAdapter.setSelectedPosition(lastPosition);
                    horizontalDateRecycler.scrollToPosition(lastPosition);
                }
            } else {
                lessonsList.clear();
                lessonsAdapter.updateLessons(lessonsList);
            }
        }
    }

    private void loadLessonsForDate(LocalDate date) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String dateString = date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

            lessonsList.clear();

            String userClass = null;
            Cursor userCursor = databaseHelper.getUserByPhone(phone);
            if (userCursor != null && userCursor.moveToFirst()) {
                userClass = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CLASS));
                userCursor.close();
            }

            if (userClass != null) {
                Cursor scheduleCursor = databaseHelper.getSchedulesByClass(userClass);
                if (scheduleCursor != null && scheduleCursor.moveToFirst()) {
                    do {
                        String scheduleDate = scheduleCursor.getString(
                                scheduleCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCHEDULE_DATE)
                        );

                        if (scheduleDate.equals(dateString)) {
                            long scheduleId = scheduleCursor.getLong(
                                    scheduleCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCHEDULE_ID)
                            );

                            loadLessonsFromDatabase(scheduleId);
                            break;
                        }
                    } while (scheduleCursor.moveToNext());
                    scheduleCursor.close();
                }
            }

            lessonsAdapter.updateLessons(lessonsList);
        }
    }

    private void loadLessonsFromDatabase(long scheduleId) {
        Cursor lessonsCursor = databaseHelper.getLessonsByScheduleId(scheduleId);
        if (lessonsCursor != null && lessonsCursor.moveToFirst()) {
            do {
                String subject = lessonsCursor.getString(
                        lessonsCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LESSON_SUBJECT)
                );
                String homework = lessonsCursor.getString(
                        lessonsCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LESSON_HOMEWORK)
                );

                lessonsList.add(new Lesson(subject, homework));
            } while (lessonsCursor.moveToNext());
            lessonsCursor.close();
        }
    }

    @Override
    public void onDateSelected(LocalDate selectedDate) {
        loadLessonsForDate(selectedDate);
    }

}
