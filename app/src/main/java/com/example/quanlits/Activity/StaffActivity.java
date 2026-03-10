package com.example.quanlits.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlits.Activity.StaffAdapter;
import com.example.quanlits.Database.DatabaseHelper;
import com.example.quanlits.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class StaffActivity extends AppCompatActivity {

    private EditText etName, etGender, etPhone;
    private Button btnAdd, btnUpdate, btnDelete, btnSearch;
    private RecyclerView recyclerView;
    private DatabaseHelper dbHelper;
    private StaffAdapter adapter;
    private ArrayList<String> staffList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff);

        // Ánh xạ các thành phần giao diện
        etName = findViewById(R.id.etStaffName);
        etGender = findViewById(R.id.etStaffGT);
        etPhone = findViewById(R.id.etStaffPhone);

        btnAdd = findViewById(R.id.btnAddStaff);
        btnUpdate = findViewById(R.id.btnUpdateStaff);
        btnDelete = findViewById(R.id.btnDeleteStaff);
        btnSearch = findViewById(R.id.btnSearchStaff);

        recyclerView = findViewById(R.id.rvEmployeeList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo DatabaseHelper
        dbHelper = new DatabaseHelper(this);
        dbHelper.createDatabaseIfNotExist();
        dbHelper.openDatabase();


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_staff);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                Intent intent = new Intent(StaffActivity.this, home.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.nav_task) {
                Intent intent = new Intent(StaffActivity.this, TaskActivity.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.nav_storehouse) {
                Intent intent = new Intent(StaffActivity.this, StoreHouseActivity.class);
                startActivity(intent);
                return true;
            }else if (id == R.id.nav_customer) {
                Intent intent = new Intent(StaffActivity.this,customer.class);
                startActivity(intent);
                return true;
            }

            return false;
        });

        // Lấy danh sách nhân viên ban đầu
        refreshList();

        // Thêm nhân viên
        btnAdd.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String gender = etGender.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();

            if (name.isEmpty() || gender.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            String query = "INSERT INTO STAFF (Name, GT, SDT) VALUES ('" + name + "', '" + gender + "', '" + phone + "')";
            dbHelper.mDatabase.execSQL(query);

            Toast.makeText(this, "Thêm nhân viên thành công!", Toast.LENGTH_SHORT).show();
            refreshList();
        });

        // Sửa thông tin nhân viên
        btnUpdate.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String gender = etGender.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();

            if (name.isEmpty() || gender.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            String query = "UPDATE STAFF SET GT = '" + gender + "', SDT = '" + phone + "' WHERE Name = '" + name + "'";
            dbHelper.mDatabase.execSQL(query);

            Toast.makeText(this, "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show();
            refreshList();
        });

        // Xóa nhân viên
        btnDelete.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tên nhân viên để xóa!", Toast.LENGTH_SHORT).show();
                return;
            }

            String query = "DELETE FROM STAFF WHERE Name = '" + name + "'";
            dbHelper.mDatabase.execSQL(query);

            Toast.makeText(this, "Xóa nhân viên thành công!", Toast.LENGTH_SHORT).show();
            refreshList();
        });

        // Tìm kiếm nhân viên
        btnSearch.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tên nhân viên để tìm kiếm!", Toast.LENGTH_SHORT).show();
                return;
            }

            staffList = new ArrayList<>();
            String query = "SELECT * FROM STAFF WHERE Name LIKE '%" + name + "%'";
            var cursor = dbHelper.mDatabase.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    String foundName = cursor.getString(cursor.getColumnIndexOrThrow("Name"));
                    String gender = cursor.getString(cursor.getColumnIndexOrThrow("GT"));
                    String phone = cursor.getString(cursor.getColumnIndexOrThrow("SDT"));
                    staffList.add("Name: " + foundName + ", Gender: " + gender + ", Phone: " + phone);
                } while (cursor.moveToNext());
            }
            cursor.close();

            if (staffList.isEmpty()) {
                Toast.makeText(this, "Không tìm thấy nhân viên!", Toast.LENGTH_SHORT).show();
            } else {
                adapter = new StaffAdapter(staffList);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    // Cập nhật danh sách nhân viên
    private void refreshList() {
        staffList = new ArrayList<>();
        String query = "SELECT * FROM STAFF";
        var cursor = dbHelper.mDatabase.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("Name"));
                String gender = cursor.getString(cursor.getColumnIndexOrThrow("GT"));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow("SDT"));
                staffList.add("Name: " + name + ", Gender: " + gender + ", Phone: " + phone);
            } while (cursor.moveToNext());
        }
        cursor.close();

        adapter = new StaffAdapter(staffList);
        recyclerView.setAdapter(adapter);
    }
}
