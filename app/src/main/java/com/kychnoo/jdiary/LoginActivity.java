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

                if(phoneNumber.trim().isEmpty() || password.trim().isEmpty()) {
                    NotificationHelper.show(LoginActivity.this, "Заполните все поля", NotificationHelper.NotificationColor.INFO, 1000);
                    return;
                }

                if (!phoneNumber.trim().matches("\\d+")) {
                    NotificationHelper.show(LoginActivity.this, "Номер телефона должен содержать только цифры", NotificationHelper.NotificationColor.WARNING, 1000);
                    return;
                }

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
                                NotificationHelper.show(LoginActivity.this, "Вход выполнен", NotificationHelper.NotificationColor.SUCCESS, 1000);
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("phone", phoneNumber);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            NotificationHelper.show(LoginActivity.this, "Неверный пароль.", NotificationHelper.NotificationColor.WARNING, 1000);
                        }
                    } else {
                        NotificationHelper.show(LoginActivity.this, "Пользователь не найден", NotificationHelper.NotificationColor.WARNING, 2000);
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
