package com.example.quanlits.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlits.Database.DatabaseHelper;
import com.example.quanlits.R;
import com.example.quanlits.Service.MusicService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText username, password;
    Button LoginButton, btnRegister;
    ImageView imgLoa;
    DatabaseHelper dbHelper;
    ArrayList<String> mylist;
    private boolean isPlaying = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo DatabaseHelper
        dbHelper = new DatabaseHelper(this);
        processCopy(); // Copy CSDL nếu chưa có

        // Ánh xạ ImageView
        imgLoa = findViewById(R.id.imgloa);

        // Ánh xạ các view cho phần đăng nhập
        username = findViewById(R.id.usernameText);
        password = findViewById(R.id.passworldText);
        LoginButton = findViewById(R.id.btnlogin);
        btnRegister = findViewById(R.id.btnRegister);  // Nút Đăng Ký

        dbHelper.createDatabaseIfNotExist();
        mylist = new ArrayList<>();
        dbHelper.getDataFromTable("TK", mylist);

        // Khởi động MusicService để phát nhạc liên tục
        Intent musicIntent = new Intent(MainActivity.this, MusicService.class);
        startService(musicIntent);
        isPlaying = true; // Khởi động Service phát nhạc

        // Sự kiện click cho imgLoa để phát và tắt nhạc
        imgLoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPlaying) {
                    // Nhạc đang phát, nhấn để tắt
                    stopService(new Intent(MainActivity.this, MusicService.class));
                    Toast.makeText(MainActivity.this, "Nhạc đã tắt", Toast.LENGTH_SHORT).show();
                    isPlaying = false;
                    // Bạn cũng có thể đổi icon của nút loa ở đây nếu muốn
                    // imgLoa.setImageResource(R.drawable.ic_volume_off);
                } else {
                    // Nhạc đang tắt, nhấn để bật
                    startService(new Intent(MainActivity.this, MusicService.class));
                    Toast.makeText(MainActivity.this, "Nhạc đang phát", Toast.LENGTH_SHORT).show();
                    isPlaying = true;
                    // Thay icon nếu muốn
                    // imgLoa.setImageResource(R.drawable.ic_volume_on);
                }
            }
        });

        // Xử lý sự kiện khi nhấn nút Đăng nhập
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString().trim();
                String pass = password.getText().toString().trim();

                if (user.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                } else {
                    // Kiểm tra thông tin đăng nhập
                    boolean loginSuccess = dbHelper.checkUser(user, pass);
                    if (loginSuccess) {
                        Toast.makeText(MainActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        // Chuyển đến màn hình khác sau khi đăng nhập thành công
                        startActivity(new Intent(MainActivity.this, home.class));
                    } else {
                        Toast.makeText(MainActivity.this, "Tên đăng nhập hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Xử lý sự kiện khi nhấn nút Đăng Ký
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến màn hình Đăng Ký (RegisterActivity)
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });
    }

    private void processCopy() {
        // Kiểm tra xem cơ sở dữ liệu đã có chưa, nếu chưa thì copy từ assets vào
        File dbFile = getDatabasePath("nl.db");
        if (!dbFile.exists()) {
            try {
                CopyDataBaseFromAsset();
                Toast.makeText(this, "Copying success", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private String getDatabasePath() {
        return getApplicationInfo().dataDir + "/databases/nl.db";
    }

    public void CopyDataBaseFromAsset() {
        try {
            InputStream myInput;
            myInput = getAssets().open("nl.db");
            String outFileName = getDatabasePath();
            File f = new File(getApplicationInfo().dataDir + "/databases/");
            if (!f.exists()) f.mkdir();
            OutputStream myOutput = new FileOutputStream(outFileName);
            int size = myInput.available();
            byte[] buffer = new byte[size];
            myInput.read(buffer);
            myOutput.write(buffer);
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Giải phóng tài nguyên khi Activity bị hủy
        stopService(new Intent(MainActivity.this, MusicService.class)); // Dừng MusicService khi không cần thiết
    }
}
