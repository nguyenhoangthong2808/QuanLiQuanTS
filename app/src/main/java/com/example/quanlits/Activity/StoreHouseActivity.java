package com.example.quanlits.Activity;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlits.Database.DatabaseHelper;
import com.example.quanlits.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StoreHouseActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseHelper dbHelper;
    private EditText editTextName, editTextType, editTextQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storehouse);

        recyclerView = findViewById(R.id.recyclerViewStoreHouse);
        editTextName = findViewById(R.id.edtTextName);
        editTextType = findViewById(R.id.editTextType);
        editTextQuantity = findViewById(R.id.editTextQuantity);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Button btnAdd = findViewById(R.id.btnThemVatLieu);
        Button btnEdit = findViewById(R.id.btnSuaVatLieu);
        Button btnDelete = findViewById(R.id.btnXoaVatLieu);

        dbHelper = new DatabaseHelper(this);
        dbHelper.createDatabaseIfNotExist();

        // Mở cơ sở dữ liệu và hiển thị dữ liệu ban đầu
        refreshData();

        // Thêm chức năng cho các nút
        btnAdd.setOnClickListener(v -> addItem());
        btnEdit.setOnClickListener(v -> editItem());
        btnDelete.setOnClickListener(v -> deleteItem());



        // Khởi tạo và thiết lập BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_storehouse);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                Intent intent = new Intent(StoreHouseActivity.this, home.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.nav_task) {
                Intent intent = new Intent(StoreHouseActivity.this, TaskActivity.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.nav_customer) {
                Intent intent = new Intent(StoreHouseActivity.this, customer.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.nav_storehouse) {

                return true;
            } else if (id == R.id.nav_staff) {
             Intent intent = new Intent(StoreHouseActivity.this, StaffActivity.class);
            startActivity(intent);
                return true;
            }

            return false;
        });
    }


    private void addItem() {
        String name = editTextName.getText().toString().trim();
        String type = editTextType.getText().toString().trim();
        String quantityStr = editTextQuantity.getText().toString().trim();

        if (name.isEmpty() || type.isEmpty() || quantityStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityStr);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.execSQL("INSERT INTO nl (NAME, TYPE, QUANTITY) VALUES (?, ?, ?)", new Object[]{name, type, quantity});
            Toast.makeText(this, "Thêm vật liệu thành công!", Toast.LENGTH_SHORT).show();
            refreshData();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Số lượng phải là số!", Toast.LENGTH_SHORT).show();
        } catch (SQLiteException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi thêm vật liệu!", Toast.LENGTH_SHORT).show();
        }
    }

    private void editItem() {
        String name = editTextName.getText().toString().trim();
        String type = editTextType.getText().toString().trim();
        String quantityStr = editTextQuantity.getText().toString().trim();

        if (name.isEmpty() || type.isEmpty() || quantityStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityStr);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.execSQL("UPDATE nl SET TYPE = ?, QUANTITY = ? WHERE NAME = ?", new Object[]{type, quantity, name});
            Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
            refreshData();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Số lượng phải là số!", Toast.LENGTH_SHORT).show();
        } catch (SQLiteException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi cập nhật vật liệu!", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteItem() {
        String name = editTextName.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên vật liệu để xóa!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.execSQL("DELETE FROM nl WHERE NAME = ?", new Object[]{name});
            Toast.makeText(this, "Xóa vật liệu thành công!", Toast.LENGTH_SHORT).show();
            refreshData();
        } catch (SQLiteException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi xóa vật liệu!", Toast.LENGTH_SHORT).show();
        }
    }

    private void refreshData() {
        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM nl", null);
            StoreHouseAdapter adapter = (StoreHouseAdapter) recyclerView.getAdapter();
            if (adapter != null) {
                adapter.swapCursor(cursor);
            } else {
                StoreHouseAdapter newAdapter = new StoreHouseAdapter(this, cursor);
                recyclerView.setAdapter(newAdapter);
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi tải lại dữ liệu!", Toast.LENGTH_SHORT).show();
        }
    }
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        // Đầu tiên, chỉ đo kích thước ảnh mà không tải nó vào bộ nhớ
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Tính toán inSampleSize (tỷ lệ nén)
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Bây giờ, giải mã ảnh với inSampleSize để giảm kích thước
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Kích thước ban đầu của ảnh
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            // Tính toán tỷ lệ nén gần nhất theo yêu cầu
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Tăng inSampleSize đến khi kích thước ảnh nhỏ hơn yêu cầu
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


}
