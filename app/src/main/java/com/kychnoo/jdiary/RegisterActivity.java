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

                if(!password.equals(acceptPassword)) {
                    Toast.makeText(RegisterActivity.this, "Пароли не совпадают", Toast.LENGTH_SHORT).show();
                    return;
                }

                Cursor cursor = databaseHelper.getUserByUsername(username);
                if (cursor != null && cursor.getCount() > 0) {
                    Toast.makeText(RegisterActivity.this, "Юзернейм уже занят", Toast.LENGTH_SHORT).show();
                    cursor.close();
                    return;
                }
                cursor = databaseHelper.getUserByPhone(phoneNumber);
                if(cursor != null && cursor.getCount() > 0) {
                    Toast.makeText(RegisterActivity.this, "Номер уже занят", Toast.LENGTH_SHORT).show();
                    cursor.close();
                    return;
                }
                if (cursor != null) {
                    cursor.close();
                }


                long result = databaseHelper.addUser(phoneNumber, email, password, username);
                if(result != -1) {
                    Toast.makeText(RegisterActivity.this, "Регистрация прошла успешно", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, ChooseClassActivity.class);
                    intent.putExtra("phone", phoneNumber);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "Ошибка при регистрации", Toast.LENGTH_SHORT).show();
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
