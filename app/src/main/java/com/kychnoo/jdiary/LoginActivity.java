package com.kychnoo.jdiary;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kychnoo.jdiary.Database.DatabaseHelper;
import com.kychnoo.jdiary.Notifications.NotificationHelper;

public class LoginActivity extends AppCompatActivity {

    private EditText etPhone;
    private EditText etPassword;

    private Button btnLogin;
    private Button btnGoToRegister;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        databaseHelper = new DatabaseHelper(this);

        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);

        btnLogin = findViewById(R.id.btnLogin);
        btnGoToRegister = findViewById(R.id.btnGoToRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = etPhone.getText().toString();
                String password = etPassword.getText().toString();

                Cursor cursor = databaseHelper.getUserByPhone(phoneNumber);
                try {
                    if (cursor != null && cursor.moveToFirst()) {
                        String storedPassword = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PASSWORD));
                        if (storedPassword.equals(password)) {
                            String userClass = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CLASS));
                            if(userClass == null)
                            {
                                Intent intent = new Intent(LoginActivity.this, ChooseClassActivity.class);
                                intent.putExtra("phone", phoneNumber);
                                startActivity(intent);
                                finish();
                            }else {
                                Toast.makeText(LoginActivity.this, "Вход выполнен", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("phone", phoneNumber);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Неверный пароль.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Пользователь не найден", Toast.LENGTH_SHORT).show();
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }
        });

        btnGoToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
