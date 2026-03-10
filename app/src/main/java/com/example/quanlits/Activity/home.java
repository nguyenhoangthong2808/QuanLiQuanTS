package com.example.quanlits.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlits.Activity.foodAdapter;
import com.example.quanlits.Activity.food;
import com.example.quanlits.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class home extends AppCompatActivity {
    private RecyclerView rcvFood;
    private Button btnCafe, btnTraSua, btnBanh;
    private EditText editTextSearch;
    private foodAdapter adapter;
    private List<food> foodList;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        // Initialize views
        editTextSearch = findViewById(R.id.editTextText);
        btnCafe = findViewById(R.id.btn_cf);
        btnTraSua = findViewById(R.id.btn_ts);
        btnBanh = findViewById(R.id.btn_banh);
        rcvFood = findViewById(R.id.rcv_food);

        // Set up RecyclerView
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rcvFood.setLayoutManager(gridLayoutManager);

        // Initialize data and adapter
        foodList = getListFood();
        adapter = new foodAdapter(foodList);
        rcvFood.setAdapter(adapter);

        // Handle search input
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                filterSearch(query);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Handle button clicks
        btnCafe.setOnClickListener(v -> filterFoodList(food.TYPE_CAFE));
        btnTraSua.setOnClickListener(v -> filterFoodList(food.TYPE_TRASUA));
        btnBanh.setOnClickListener(v -> filterFoodList(food.TYPE_BANH));

        // Set up BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                startActivity(new Intent(home.this, home.class));
                return true;
            } else if (id == R.id.nav_task) {
                startActivity(new Intent(home.this, TaskActivity.class));
                return true;
            } else if (id == R.id.nav_customer) {
                startActivity(new Intent(home.this, customer.class));
                return true;
            } else if (id == R.id.nav_storehouse) {
                startActivity(new Intent(home.this, StoreHouseActivity.class));
                return true;
            } else if (id == R.id.nav_staff) {
                startActivity(new Intent(home.this, StaffActivity.class));
                return true;
            }
            return false;
        });
    }

    // Filter the food list by search query
    private void filterSearch(String query) {
        List<food> filteredList = new ArrayList<>();
        for (food item : foodList) {
            if (item.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(item);
            }
        }



        adapter.updateList(filteredList);
    }

    // Filter the food list by type
    private void filterFoodList(int foodType) {
        List<food> filteredList = new ArrayList<>();
        for (food item : foodList) {
            if (item.getType() == foodType) {
                filteredList.add(item);
            }
        }

        adapter.updateList(filteredList);
    }

    // Generate sample food list
    private List<food> getListFood() {
        List<food> list = new ArrayList<>();
        list.add(new food(R.drawable.img, "Trà Sữa Truyền Thống", food.TYPE_TRASUA));
        list.add(new food(R.drawable.img_1, "Trà Sữa Matcha", food.TYPE_TRASUA));
        list.add(new food(R.drawable.img_2, "Trà Sữa Socola", food.TYPE_TRASUA));
        list.add(new food(R.drawable.img_3, "Trà Sữa chân trâu", food.TYPE_TRASUA));
        list.add(new food(R.drawable.cf1, "Cà Phê Đen", food.TYPE_CAFE));
        list.add(new food(R.drawable.cf2, "Trà Chanh", food.TYPE_CAFE));
        list.add(new food(R.drawable.cf3, "Latte", food.TYPE_CAFE));
        list.add(new food(R.drawable.cake1_0, "Bánh Mì Bơ", food.TYPE_BANH));
        list.add(new food(R.drawable.cake1_1, "Bánh Kem", food.TYPE_BANH));
        return list;
    }
}
