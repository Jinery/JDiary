package com.kychnoo.jdiary.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kychnoo.jdiary.Adapters.AchievementsAdapter;
import com.kychnoo.jdiary.Database.DatabaseHelper;
import com.kychnoo.jdiary.Interfaces.ToolbarTitleSetter;
import com.kychnoo.jdiary.OtherClasses.Achievement;
import com.kychnoo.jdiary.R;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private DatabaseHelper databaseHelper;
    private String phone;


    private TextView tvDescription;
    private TextView tvUserPoints;

    private ToolbarTitleSetter toolbarTitleSetter;

    private RecyclerView rvAchievements;

    private AchievementsAdapter achievementsAdapter;

    private List<Achievement> achievementsList;


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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        toolbarTitleSetter.setToolbarTitle("Профиль");

        databaseHelper = new DatabaseHelper(requireContext());

        phone = requireActivity().getIntent().getStringExtra("phone");

        TextView tvUsername = view.findViewById(R.id.tvUsername);
        TextView tvClass = view.findViewById(R.id.tvUserClass);
        tvDescription = view.findViewById(R.id.tvUserDescription);
        tvUserPoints = view.findViewById(R.id.tvUserPoints);

        rvAchievements = view.findViewById(R.id.rvAchievements);
        rvAchievements.setLayoutManager(new LinearLayoutManager(requireContext()));

        achievementsList = new ArrayList<>();

        achievementsAdapter = new AchievementsAdapter(achievementsList);
        rvAchievements.setAdapter(achievementsAdapter);

        tvDescription.setOnClickListener(v -> showEditDesriptionVindow(phone));

        Cursor cursor = databaseHelper.getUserByPhone(phone);
        if (cursor != null && cursor.moveToFirst()) {
            String username = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USERNAME));
            String userClass = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CLASS));
            String userDescription = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION));
            String userPoints = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EXPERIENCE_POINTS));
            tvUsername.setText(username);
            tvClass.setText("Ученик " + userClass + " класса.");
            tvUserPoints.setText("У вас " + userPoints + " очков.");
            updateUserDescription(userDescription);
            cursor.close();
        } else {
            Toast.makeText(requireContext(), "Пользователь не найден", Toast.LENGTH_SHORT).show();
        }

        loadAchievements();

        return view;
    }

    private void showEditDesriptionVindow(String phone) {
        Cursor cursor = databaseHelper.getUserByPhone(phone);
        if(cursor != null && cursor.moveToFirst()) {
            String currentDescription = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION));
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_discription, null);
            builder.setView(dialogView);

            EditText etDescription = dialogView.findViewById(R.id.etDescription);
            TextView tvAvialableSymvols = dialogView.findViewById(R.id.tvAvialableSymvols);

            etDescription.setText(currentDescription);
            updateCharCount(etDescription.getText().length(), tvAvialableSymvols);

            etDescription.setFilters(new InputFilter[] { new InputFilter.LengthFilter(35)});

            etDescription.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    updateCharCount(s.length(), tvAvialableSymvols);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            builder.setPositiveButton("Сохранить", (dialog, which) -> {
                String newDescription = etDescription.getText().toString();
                databaseHelper.updateUserDescription(phone, newDescription);
                updateUserDescription(newDescription);
            });

            builder.setNegativeButton("Отмена", (dialog, which) -> dialog.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else
        {
            Toast.makeText(requireContext(), "Не удаётся загрузить данные об описании.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateCharCount(int length,  TextView tvAvialableSymvols) {
        int remainingChars = 35 - length;
        tvAvialableSymvols.setText(remainingChars + " символов осталось");
    }

    private void updateUserDescription(String newDescription) {
        if(newDescription == null || newDescription.isEmpty())
            tvDescription.setText("Описание отсутствует.");
        else
            tvDescription.setText(newDescription);
    }

    private void loadAchievements() {
        Cursor cursor = databaseHelper.getUserAchievements(phone);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ACHIEVEMENT_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ACHIEVEMENT_TITLE));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ACHIEVEMENT_CONTENT));
                achievementsList.add(new Achievement(id, title, description));
            } while (cursor.moveToNext());
            cursor.close();
        }
    }
}
