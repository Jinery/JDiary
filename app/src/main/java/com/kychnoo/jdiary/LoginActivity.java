package com.kychnoo.jdiary;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.kychnoo.jdiary.Database.DatabaseHelper;
import com.kychnoo.jdiary.Notifications.NotificationHelper;

public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_STORAGE_PERMISSION = 100;

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

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_STORAGE_PERMISSION
            );
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = etPhone.getText().toString();
                String password = etPassword.getText().toString();

                if(TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(password)) {
                    NotificationHelper.show(LoginActivity.this, "Заполните все поля", NotificationHelper.NotificationColor.INFO, 1000);
                    return;
                }

                if (!phoneNumber.trim().matches("\\+?\\d*")) {
                    NotificationHelper.show(LoginActivity.this, "Номер телефона не может содержать символы.", NotificationHelper.NotificationColor.WARNING, 1000);
                    return;
                }

                int phoneNumberLength = phoneNumber.trim().replace("+", "").length();

                if (phoneNumberLength < 10) {
                    NotificationHelper.show(LoginActivity.this, "Номер телефона слишком короткий. Минимальная длина: 10 цифр.", NotificationHelper.NotificationColor.WARNING, 1000);
                    return;
                }

                if (phoneNumberLength > 15) {
                    NotificationHelper.show(LoginActivity.this, "Номер телефона слишком длинный. Максимальная длина: 15 цифр.", NotificationHelper.NotificationColor.WARNING, 1000);
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
