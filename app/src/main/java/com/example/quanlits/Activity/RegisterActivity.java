package com.example.quanlits.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlits.Database.DatabaseHelper;
import com.example.quanlits.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameText, passworldText, confirmPasswordText;
    private Button btnRegister, btnBackToLogin;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        // Khởi tạo DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Ánh xạ các view
        usernameText = findViewById(R.id.usernameText);
        passworldText = findViewById(R.id.passworldText);
        confirmPasswordText = findViewById(R.id.confirmPasswordText);
        btnRegister = findViewById(R.id.btnRegister);
        btnBackToLogin = findViewById(R.id.btnBackToLogin);

        // Xử lý sự kiện cho nút Đăng Ký
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameText.getText().toString().trim();
                String password = passworldText.getText().toString().trim();
                String confirmPassword = confirmPasswordText.getText().toString().trim();

                // Kiểm tra điều kiện hợp lệ
                if (username.isEmpty()) {
                    usernameText.setError("Tên tài khoản không được để trống");
                } else if (password.isEmpty()) {
                    passworldText.setError("Mật khẩu không được để trống");
                } else if (!password.equals(confirmPassword)) {
                    confirmPasswordText.setError("Mật khẩu xác nhận không khớp");
                } else {
                    // Lưu tài khoản vào cơ sở dữ liệu
                    dbHelper.insertUser(username, password);

                    // Hiển thị thông báo và chuyển đến màn hình đăng nhập
                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    finish();  // Đóng màn hình đăng ký
                }
            }
        });

        // Xử lý sự kiện cho nút Quay lại đăng nhập
        btnBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Quay lại màn hình đăng nhập
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                finish();  // Đóng màn hình đăng ký
            }
        });
    }
}

