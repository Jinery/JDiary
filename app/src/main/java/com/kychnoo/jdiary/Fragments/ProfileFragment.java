package com.kychnoo.jdiary.Fragments;

import android.app.AlertDialog;
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

import com.kychnoo.jdiary.Database.DatabaseHelper;
import com.kychnoo.jdiary.R;

public class ProfileFragment extends Fragment {

    private DatabaseHelper databaseHelper;
    private String phone;


    private TextView tvDescription;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        databaseHelper = new DatabaseHelper(requireContext());

        phone = requireActivity().getIntent().getStringExtra("phone");

        TextView tvUsername = view.findViewById(R.id.tvUsername);
        TextView tvClass = view.findViewById(R.id.tvUserClass);
        tvDescription = view.findViewById(R.id.tvUserDescription);

        tvDescription.setOnClickListener(v -> showEditDesriptionVindow(phone));

        Cursor cursor = databaseHelper.getUserByPhone(phone);
        if (cursor != null && cursor.moveToFirst()) {
            String username = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USERNAME));
            String userClass = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CLASS));
            String userDescription = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION));
            tvUsername.setText(username);
            tvClass.setText("Ученик " + userClass + " класса.");
            updateUserDescription(userDescription);
            cursor.close();
        } else {
            Toast.makeText(requireContext(), "Пользователь не найден", Toast.LENGTH_SHORT).show();
        }

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
}
