package com.kychnoo.jdiary.Fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kychnoo.jdiary.Adapters.TestsAdapter;
import com.kychnoo.jdiary.Database.DatabaseHelper;
import com.kychnoo.jdiary.Interfaces.ToolbarTitleSetter;
import com.kychnoo.jdiary.Notifications.NotificationHelper;
import com.kychnoo.jdiary.OtherClasses.Test;
import com.kychnoo.jdiary.R;
import com.kychnoo.jdiary.TestActivity;

import java.util.ArrayList;
import java.util.List;

public class TestsFragment extends Fragment implements TestsAdapter.OnTestClickListener {

    private RecyclerView rvTests;

    private TestsAdapter testsAdapter;

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

    @Override
    public void onResume() {
        super.onResume();
        List<Test> tests = loadTests();
        testsAdapter.updateData(tests);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tests, container, false);

        toolbarTitleSetter.setToolbarTitle("Мои тесты");

        databaseHelper = new DatabaseHelper(requireContext());
        phone = requireActivity().getIntent().getStringExtra("phone");

        rvTests = view.findViewById(R.id.testsRecyclerView);
        rvTests.setLayoutManager(new LinearLayoutManager(requireContext()));

        List<Test> tests = loadTests();

        testsAdapter = new TestsAdapter(tests, this);
        rvTests.setAdapter(testsAdapter);

        return view;
    }

    private List<Test> loadTests() {
        List<Test> testList = new ArrayList<>();

        Cursor cursor = databaseHelper.getUserByPhone(phone);
        if (cursor != null && cursor.moveToFirst()) {
            String userClass = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CLASS));
            cursor.close();

            Cursor testsCursor = databaseHelper.getTestsByClass(userClass);
            if (testsCursor != null) {
                while (testsCursor.moveToNext()) {
                    int id = testsCursor.getInt(testsCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TEST_ID));
                    String name = testsCursor.getString(testsCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TEST_NAME));
                    int questionsCount = testsCursor.getInt(testsCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TEST_QUESTIONS_COUNT));
                    int points = testsCursor.getInt(testsCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TEST_POINTS));
                    String className = testsCursor.getString(testsCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TEST_CLASS));
                    boolean isPassedTest = databaseHelper.hasUserPassedTest(phone, id);

                    testList.add(new Test(id, name, questionsCount, points, className, isPassedTest));
                }
                testsCursor.close();
            }
        }
        testList.sort((test1, test2) -> Boolean.compare(test1.isPassed(), test2.isPassed()));
        return testList;
    }


    @Override
    public void onTestClick(Test test) {
        if(databaseHelper.hasUserPassedTest(phone, test.getId())) {
            NotificationHelper.show(requireActivity(), "Вы уже выполнили этот тест", NotificationHelper.NotificationColor.INFO, 1000);
        }
        else
        {
            startTest(test);
        }
    }

    private void startTest(Test test) {
        Intent intent = new Intent(requireContext(), TestActivity.class);
        intent.putExtra("test_id", test.getId());
        intent.putExtra("user_phone", phone);
        startActivity(intent);
    }
}
