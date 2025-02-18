package com.kychnoo.jdiary.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kychnoo.jdiary.Database.DatabaseHelper;
import com.kychnoo.jdiary.MainActivity;
import com.kychnoo.jdiary.R;

public class ProfileFragment extends Fragment {

    private DatabaseHelper databaseHelper;
    private String phone;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        databaseHelper = new DatabaseHelper(requireContext());

        phone = requireActivity().getIntent().getStringExtra("phone");

        TextView tvUsername = view.findViewById(R.id.tvUsername);
        TextView tvClass = view.findViewById(R.id.tvUserClass);

        Cursor cursor = databaseHelper.getUserByPhone(phone);
        if (cursor != null && cursor.moveToFirst()) {
            String username = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USERNAME));
            String userClass = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CLASS));
            tvUsername.setText(username);
            tvClass.setText("Ученик " + userClass + " класса.");
            cursor.close();
        } else {
            Toast.makeText(requireContext(), "Пользователь не найден", Toast.LENGTH_SHORT).show();
        }

        return view;
    }
}
