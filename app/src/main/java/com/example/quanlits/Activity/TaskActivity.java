package com.example.quanlits.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlits.Database.DatabaseHelper;
import com.example.quanlits.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class TaskActivity extends AppCompatActivity {

    private RecyclerView recyclerViewTasks;
    private TaskAdapter taskAdapter;
    private List<String> taskList;
    private EditText editTextNewTask;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task);

        // Khởi tạo các thành phần giao diện
        recyclerViewTasks = findViewById(R.id.recyclerViewTasks);
        Button buttonAddTask = findViewById(R.id.buttonAddTask);
        editTextNewTask = findViewById(R.id.editTextNewTask);

        // Khởi tạo DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Tải danh sách nhiệm vụ từ cơ sở dữ liệu
        taskList = databaseHelper.getAllTasks();

        // Cài đặt RecyclerView và Adapter
        taskAdapter = new TaskAdapter(taskList);
        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTasks.setAdapter(taskAdapter);

        // Xử lý sự kiện thêm nhiệm vụ mới
        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTask = editTextNewTask.getText().toString().trim();
                if (!newTask.isEmpty()) {
                    // Lưu nhiệm vụ vào cơ sở dữ liệu
                    databaseHelper.insertTask(newTask);

                    // Cập nhật lại danh sách nhiệm vụ
                    taskList.add(newTask);
                    taskAdapter.notifyItemInserted(taskList.size() - 1);
                    editTextNewTask.setText("");
                }
            }
        });

        // Cài đặt BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_task);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                Intent intent = new Intent(TaskActivity.this, home.class);
                startActivity(intent);
                finish();
                return true;
            } else if (id == R.id.nav_task) {
                return true;
            } else if (id == R.id.nav_customer) {
                Intent intent = new Intent(TaskActivity.this, customer.class);
                startActivity(intent);
                finish();
                return true;
            } else if (id == R.id.nav_storehouse) {
                Intent intent = new Intent(TaskActivity.this, StoreHouseActivity.class);
                startActivity(intent);
                finish();
                return true;
            } else if (id == R.id.nav_staff) {
                Intent intent = new Intent(TaskActivity.this, StoreHouseActivity.class);
                startActivity(intent);
                finish();
                return true;
            }

            return false;
        });

    }
}
