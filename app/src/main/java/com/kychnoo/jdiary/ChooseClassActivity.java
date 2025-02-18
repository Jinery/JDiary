package com.kychnoo.jdiary;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;


import com.kychnoo.jdiary.Database.DatabaseHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChooseClassActivity extends AppCompatActivity {

    private Spinner spinnerClasses;

    private Button btnChooseClass;

    private DatabaseHelper databaseHelper;
    private String phoneNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_class);

        databaseHelper = new DatabaseHelper(this);
        phoneNumber = getIntent().getStringExtra("phone");

        spinnerClasses = findViewById(R.id.spinnerClasses);
        btnChooseClass = findViewById(R.id.btnChooseClasses);

        loadClasses();

        btnChooseClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedClassName = spinnerClasses.getSelectedItem().toString();
                int result = databaseHelper.updateUserClass(phoneNumber, selectedClassName);
                if(result > 0) {
                    Toast.makeText(ChooseClassActivity.this, "Успешно выбран класс: " + selectedClassName, Toast.LENGTH_SHORT);
                    Intent intent = new Intent(ChooseClassActivity.this, MainActivity.class);
                    intent.putExtra("phone", phoneNumber);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(ChooseClassActivity.this, "Не удалось выбрать класс: " + selectedClassName, Toast.LENGTH_SHORT);
                }
            }
        });
    }

    private void loadClasses() {
        Cursor cursor = databaseHelper.getAllClasses();
        List<String> classNames = new ArrayList<>();

        if(cursor != null)
        {
            while (cursor.moveToNext()) {
                String className = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CLASS_NAME));
                classNames.add(className);
            }
            cursor.close();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, classNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClasses.setAdapter(adapter);
    }
}
