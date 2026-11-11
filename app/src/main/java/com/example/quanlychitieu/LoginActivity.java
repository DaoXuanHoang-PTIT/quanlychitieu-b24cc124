package com.example.quanlychitieu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity xử lý đăng nhập hệ thống Quản lý chi tiêu.
 */
public class LoginActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private TextView tvError;
    private Button btnLogin;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        tvError = findViewById(R.id.tvError);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            String u = etUsername.getText().toString().trim();
            String p = etPassword.getText().toString().trim();

            if (u.isEmpty() || p.isEmpty()) {
                tvError.setText("Vui lòng nhập đầy đủ thông tin!");
                tvError.setVisibility(View.VISIBLE);
                return;
            }

            if (dbHelper.checkLogin(u, p)) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Đóng LoginActivity để nút Back không quay lại được
            } else {
                tvError.setText("Sai tên đăng nhập hoặc mật khẩu");
                tvError.setVisibility(View.VISIBLE);
            }
        });
    }
}
