package com.example.quanlits.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "nl.db";
    private static String DB_PATH = "";
    private Context mContext;
    public SQLiteDatabase mDatabase;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.mContext = context;
        DB_PATH = mContext.getApplicationInfo().dataDir + "/databases/";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Không cần vì CSDL đã có sẵn trong assets
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Nâng cấp CSDL nếu cần
    }


    public void createDatabaseIfNotExist() {
        File dbFile = mContext.getDatabasePath(DB_NAME);
        if (!dbFile.exists()) {
            copyDatabaseFromAssets();
        }
    }

    private void copyDatabaseFromAssets() {
        try {
            InputStream myInput = mContext.getAssets().open(DB_NAME);
            File f = new File(DB_PATH);
            if (!f.exists()) {
                f.mkdir();
            }
            String outFileName = DB_PATH + DB_NAME;
            FileOutputStream myOutput = new FileOutputStream(outFileName);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            myOutput.flush();
            myOutput.close();
            myInput.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openDatabase() {
        if (mDatabase == null || !mDatabase.isOpen()) {
            String path = DB_PATH + DB_NAME;
            mDatabase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
        }
    }

    @Override
    public synchronized void close() {
        if (mDatabase != null && mDatabase.isOpen())
            mDatabase.close();
        super.close();
    }


    public void getDataFromTable(String tableName, java.util.ArrayList<String> mylist) {
        openDatabase();
        Cursor c = mDatabase.query(tableName, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                String name = c.getString(c.getColumnIndexOrThrow("Name"));
                String password = c.getString(c.getColumnIndexOrThrow("Password"));
                mylist.add("Name: " + name + ", Password: " + password);
                c.moveToNext();
            }
        }
        c.close();
    }


    // Hàm kiểm tra đăng nhập
    public boolean checkUser(String username, String password) {
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM TK WHERE Name=? AND Password=?",
                new String[]{username, password});
        boolean exists = false;
        if (cursor != null) {
            exists = (cursor.getCount() > 0);
            cursor.close();
        }
        return exists;
    }

    public ArrayList<String> getAllItemsFromNl() {
        ArrayList<String> items = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM nl", null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("NAME"));
                String type = cursor.getString(cursor.getColumnIndexOrThrow("TYPE"));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("QUANTITY"));
                items.add("Name: " + name + ", Type: " + type + ", Quantity: " + quantity);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return items;
    }
    public ArrayList<String> getAllCustomers() {
        ArrayList<String> customers = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM KH", null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("Name"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("Email"));
                int sdt = cursor.getInt(cursor.getColumnIndexOrThrow("Sdt"));
                customers.add("Name: " + name + ", Email: " + email + ", Sdt: " + sdt);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return customers;
    }
    public ArrayList<String> getAllTasks() {
        ArrayList<String> tasks = new ArrayList<>();
        Cursor cursor = null;

        try {
            openDatabase(); // Đảm bảo cơ sở dữ liệu được mở
            cursor = mDatabase.rawQuery("SELECT * FROM NV", null);

            if (cursor.moveToFirst()) {
                int taskColumnIndex = cursor.getColumnIndex("Task");
                if (taskColumnIndex != -1) { // Kiểm tra cột "Task" tồn tại
                    do {
                        String task = cursor.getString(taskColumnIndex);
                        tasks.add(task);
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); 
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close(); 
            }
        }

        return tasks;
    }

    public void insertTask(String task) {
        openDatabase(); 
        try {
            String query = "INSERT INTO NV (Task) VALUES (?)";
            mDatabase.execSQL(query, new Object[]{task});
        } catch (Exception e) {
            e.printStackTrace(); 
        }
    }

    public ArrayList<String> getAllStaff() {
        ArrayList<String> staffList = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM STAFF", null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("Name"));
                String gender = cursor.getString(cursor.getColumnIndexOrThrow("GT"));
                int phone = cursor.getInt(cursor.getColumnIndexOrThrow("SDT"));
                staffList.add("Name: " + name + ", Gender: " + gender + ", Phone: " + phone);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return staffList;
    }
    // Phương thức chèn tài khoản vào bảng TK
    public void insertUser(String username, String password) {
        openDatabase(); 
        try {
          
            String query = "INSERT INTO TK (Name, Password) VALUES (?, ?)";
            mDatabase.execSQL(query, new Object[]{username, password});
        } catch (Exception e) {
            e.printStackTrace(); 
        }
    }



}
