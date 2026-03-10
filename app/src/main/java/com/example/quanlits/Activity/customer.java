package com.example.quanlits.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlits.Database.DatabaseHelper;
import com.example.quanlits.R;
import com.example.quanlits.model.Customer;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class customer extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Customer> customerList;
    private CustomerAdapter adapter;
    private EditText etName, etEmail, etPhone;
    private ImageView imgCustomer;
    private Button btnSearchCustomer, btnAddCustomer, btnDellCustomer, btnUpdateCustomer;
    private ImageButton btnRefresh;
    private DatabaseHelper dbHelper;
    private Customer selectedCustomer = null;
    private int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer);

        // Khởi tạo các thành phần giao diện
        btnRefresh = findViewById(R.id.btnRefresh);
        etName = findViewById(R.id.etStaffName);
        etEmail = findViewById(R.id.etStaffEmail);
        etPhone = findViewById(R.id.etStaffPhone);
        imgCustomer = findViewById(R.id.imgCustomer);


        btnSearchCustomer = findViewById(R.id.btnSearchCustomer);
        btnAddCustomer = findViewById(R.id.btnAddCustomer);
        btnDellCustomer = findViewById(R.id.btnDeleteCustomer);
        btnUpdateCustomer = findViewById(R.id.btnUpdateCustomer);

        recyclerView = findViewById(R.id.rvCustomerList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(this);
        dbHelper.createDatabaseIfNotExist();

        // Khởi tạo danh sách khách hàng từ cơ sở dữ liệu
        customerList = loadCustomersFromDatabase();


        // Thiết lập adapter
        adapter = new CustomerAdapter(customerList, new CustomerAdapter.CustomerClickListener() {
            @Override
            public void onCustomerClick(Customer customer, int position) {
                selectedCustomer = customer;
                selectedPosition = position;
                etName.setText(customer.getName());
                etEmail.setText(customer.getEmail());
                etPhone.setText(customer.getPhone());
            }
        });

        recyclerView.setAdapter(adapter);

        btnSearchCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String customerName = etName.getText().toString().trim();
                if (customerName.isEmpty()) {
                    Toast.makeText(customer.this, "Vui lòng nhập tên khách hàng để tìm kiếm.", Toast.LENGTH_SHORT).show();
                    return;
                }

                customerList.clear();
                customerList.addAll(searchCustomers(customerName));
                adapter.notifyDataSetChanged();

                if (customerList.isEmpty()) {
                    Toast.makeText(customer.this, "Không tìm thấy khách hàng.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(customer.this, "Tìm thấy " + customerList.size() + " khách hàng.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customerList.clear();
                customerList.addAll(loadCustomersFromDatabase());
                adapter.notifyDataSetChanged();
                clearInputFields();
                Toast.makeText(customer.this, "Danh sách đã được làm mới.", Toast.LENGTH_SHORT).show();
            }
        });

        btnAddCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();

                if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(customer.this, "Vui lòng nhập đầy đủ thông tin khách hàng.", Toast.LENGTH_SHORT).show();
                    return;
                }

                addCustomerToDatabase(new Customer(name, email, phone, R.drawable.customer));
                customerList.clear();
                customerList.addAll(loadCustomersFromDatabase());
                adapter.notifyDataSetChanged();
                clearInputFields();

                Toast.makeText(customer.this, "Đã thêm khách hàng: " + name, Toast.LENGTH_SHORT).show();
            }
        });

        btnDellCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedCustomer != null && selectedPosition != -1) {
                    deleteCustomerFromDatabase(selectedCustomer);
                    customerList.remove(selectedPosition);
                    adapter.notifyItemRemoved(selectedPosition);
                    clearInputFields();
                    Toast.makeText(customer.this, "Đã xóa khách hàng.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(customer.this, "Vui lòng chọn khách hàng để xóa.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnUpdateCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedCustomer != null && selectedPosition != -1) {
                    String updatedName = etName.getText().toString().trim();
                    String updatedEmail = etEmail.getText().toString().trim();
                    String updatedPhone = etPhone.getText().toString().trim();

                    selectedCustomer.setName(updatedName);
                    selectedCustomer.setEmail(updatedEmail);
                    selectedCustomer.setPhone(updatedPhone);

                    updateCustomerInDatabase(selectedCustomer);
                    adapter.notifyItemChanged(selectedPosition);
                    Toast.makeText(customer.this, "Đã cập nhật khách hàng: " + updatedName, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(customer.this, "Vui lòng chọn khách hàng để cập nhật.", Toast.LENGTH_SHORT).show();
                }
            }
        });



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_customer);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                Intent intent = new Intent(customer.this, home.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.nav_task) {
                Intent intent = new Intent(customer.this, TaskActivity.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.nav_storehouse) {
                Intent intent = new Intent(customer.this, StoreHouseActivity.class);
                startActivity(intent);
                return true;
            }else if (id == R.id.nav_staff) {
                Intent intent = new Intent(customer.this,StaffActivity.class);
                startActivity(intent);
                return true;
            }

            return false;
        });
    }

//    private List<Customer> loadCustomersFromDatabase() {
//        List<Customer> customers = new ArrayList<>();
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT * FROM KH", null);
//
//        while (cursor.moveToNext()) {
//            String name = cursor.getString(cursor.getColumnIndex("Name"));
//            String email = cursor.getString(cursor.getColumnIndex("Email"));
//            String phone = cursor.getString(cursor.getColumnIndex("Sdt"));
//            customers.add(new Customer(name, email, phone, R.drawable.img_8));
//        }
//
//        cursor.close();
//        return customers;
//    }

    private List<Customer> loadCustomersFromDatabase() {
        List<Customer> customers = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM KH", null);

        while (cursor.moveToNext()) {
            int nameIndex = cursor.getColumnIndex("Name");
            int emailIndex = cursor.getColumnIndex("Email");
            int phoneIndex = cursor.getColumnIndex("Sdt");

            if (nameIndex != -1 && emailIndex != -1 && phoneIndex != -1) {
                String name = cursor.getString(nameIndex);
                String email = cursor.getString(emailIndex);
                String phone = cursor.getString(phoneIndex);
                customers.add(new Customer(name, email, phone, R.drawable.customer));
            }
        }

        cursor.close();
        return customers;
    }

    private void addCustomerToDatabase(Customer customer) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Name", customer.getName());
        values.put("Email", customer.getEmail());
        values.put("Sdt", customer.getPhone());
        db.insert("KH", null, values);
    }

    private void deleteCustomerFromDatabase(Customer customer) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("KH", "Name = ?", new String[]{customer.getName()});
    }

    private void updateCustomerInDatabase(Customer customer) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Name", customer.getName()); //moi them
        values.put("Email", customer.getEmail());
        values.put("Sdt", customer.getPhone());
        db.update("KH", values, "Name = ?", new String[]{customer.getName()});
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        // Đo kích thước ảnh mà không load vào bộ nhớ
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Tính toán tỷ lệ nén
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Load ảnh đã được nén
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private List<Customer> searchCustomers(String name) {
        List<Customer> customers = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM KH WHERE Name LIKE ?", new String[]{"%" + name + "%"});

        while (cursor.moveToNext()) {
            int nameIndex = cursor.getColumnIndex("Name");
            int emailIndex = cursor.getColumnIndex("Email");
            int phoneIndex = cursor.getColumnIndex("Sdt");

            if (nameIndex != -1 && emailIndex != -1 && phoneIndex != -1) {
                String customerName = cursor.getString(nameIndex);
                String email = cursor.getString(emailIndex);
                String phone = cursor.getString(phoneIndex);
                customers.add(new Customer(customerName, email, phone, R.drawable.customer));
            }
        }

        cursor.close();
        return customers;
    }

    private void clearInputFields() {
        etName.setText("");
        etEmail.setText("");
        etPhone.setText("");
        imgCustomer.setImageResource(R.drawable.customer);
        Bitmap bitmap = decodeSampledBitmapFromResource(
                getResources(),
                R.drawable.customer,
                100, // Chiều rộng mong muốn
                100  // Chiều cao mong muốn
        );
        imgCustomer.setImageBitmap(bitmap);
    }

}
