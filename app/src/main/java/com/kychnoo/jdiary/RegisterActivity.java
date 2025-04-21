package com.kychnoo.jdiary;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kychnoo.jdiary.Database.DatabaseHelper;
import com.kychnoo.jdiary.Notifications.NotificationHelper;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPhone;
    private EditText etMail;
    private EditText etPassword;
    private EditText etAcceptPassword;

    private Button btnRegister;
    private Button btnGoToLogin;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        databaseHelper = new DatabaseHelper(this);

        etUsername = findViewById(R.id.etUsername);
        etPhone = findViewById(R.id.etPhone);
        etMail = findViewById(R.id.etMail);
        etPassword = findViewById(R.id.etPassword);
        etAcceptPassword = findViewById(R.id.etAcceptPassword);

        btnRegister = findViewById(R.id.btnRegister);
        btnGoToLogin = findViewById(R.id.btnGoToLogin);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString();
                String phoneNumber = etPhone.getText().toString();
                String email = etMail.getText().toString();
                String password = etPassword.getText().toString();
                String acceptPassword = etAcceptPassword.getText().toString();

                if(TextUtils.isEmpty(username) || TextUtils.isEmpty(phoneNumber)  || TextUtils.isEmpty(email)  || TextUtils.isEmpty(password)  || TextUtils.isEmpty(acceptPassword)) {
                    NotificationHelper.show(RegisterActivity.this, "Заполните все поля", NotificationHelper.NotificationColor.INFO, 1000);
                    return;
                }

                if (!phoneNumber.trim().matches("\\+?\\d*")) {
                    NotificationHelper.show(RegisterActivity.this, "Номер телефона не может содержать символы.", NotificationHelper.NotificationColor.WARNING, 1000);
                    return;
                }

                int phoneNumberLength = phoneNumber.trim().replace("+", "").length();

                if (phoneNumberLength < 10) {
                    NotificationHelper.show(RegisterActivity.this, "Номер телефона слишком короткий. Минимальная длина: 10 цифр.", NotificationHelper.NotificationColor.WARNING, 1000);
                    return;
                }

                if (phoneNumberLength > 15) {
                    NotificationHelper.show(RegisterActivity.this, "Номер телефона слишком длинный. Максимальная длина: 15 цифр.", NotificationHelper.NotificationColor.WARNING, 1000);
                    return;
                }

                if (!email.trim().matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
                    NotificationHelper.show(RegisterActivity.this, "Введите корректный адрес электронной почты", NotificationHelper.NotificationColor.INFO, 1000);
                    return;
                }

                if(!password.equals(acceptPassword)) {
                    NotificationHelper.show(RegisterActivity.this, "Пароли не совпадают", NotificationHelper.NotificationColor.WARNING, 1000);
                    return;
                }

                Cursor cursor = databaseHelper.getUserByUsername(username);
                if (cursor != null && cursor.getCount() > 0) {
                    NotificationHelper.show(RegisterActivity.this, "Данный юзернейм занят", NotificationHelper.NotificationColor.ERROR, 1000);
                    cursor.close();
                    return;
                }
                cursor = databaseHelper.getUserByPhone(phoneNumber);
                if(cursor != null && cursor.getCount() > 0) {
                    NotificationHelper.show(RegisterActivity.this, "Данный номер занят", NotificationHelper.NotificationColor.ERROR, 1000);
                    cursor.close();
                    return;
                }
                if (cursor != null) {
                    cursor.close();
                }


                long result = databaseHelper.addUser(phoneNumber, email, password, username);
                if(result != -1) {
                    NotificationHelper.show(RegisterActivity.this, "Регистрация прошла успешно", NotificationHelper.NotificationColor.SUCCESS, 1000);
                    Intent intent = new Intent(RegisterActivity.this, ChooseClassActivity.class);
                    intent.putExtra("phone", phoneNumber);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    NotificationHelper.show(RegisterActivity.this, "Ошибка при регистрации.", NotificationHelper.NotificationColor.ERROR, 1000);
                }
            }
        });

        btnGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
