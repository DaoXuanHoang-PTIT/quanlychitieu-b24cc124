package com.example.quanlychitieu;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "warehouse.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 1. Tạo bảng users
        String createTableUsers = "CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL UNIQUE, " +
                "password TEXT NOT NULL)";
        db.execSQL(createTableUsers);

        // 2. Tạo bảng products
        String createTableProducts = "CREATE TABLE products (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "price INTEGER NOT NULL, " +
                "quantity INTEGER NOT NULL, " +
                "category TEXT NOT NULL, " +
                "active INTEGER NOT NULL, " +
                "description TEXT)";
        db.execSQL(createTableProducts);

        // 3. Chèn dữ liệu mẫu
        db.execSQL("INSERT INTO users (username, password) VALUES ('admin', '123456')");
        db.execSQL("INSERT INTO products (name, price, quantity, category, active, description) VALUES ('Chuột không dây', 150000, 20, 'Điện tử', 1, '')");
        db.execSQL("INSERT INTO products (name, price, quantity, category, active, description) VALUES ('Bàn phím cơ', 450000, 3, 'Điện tử', 1, 'Bàn phím cơ switch xanh, có đèn nền')");
        db.execSQL("INSERT INTO products (name, price, quantity, category, active, description) VALUES ('Sổ tay A5', 25000, 40, 'Văn phòng phẩm', 1, '')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS products");
        onCreate(db);
    }

    // Mẫu phương thức checkLogin
    public boolean checkLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username=? AND password=?", new String[]{username, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Thêm sản phẩm
    public long insertProduct(Product p) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", p.getName());
        values.put("price", p.getPrice());
        values.put("quantity", p.getQuantity());
        values.put("category", p.getCategory());
        values.put("active", p.getActive());
        values.put("description", p.getDescription());
        return db.insert("products", null, values);
    }

    // Lấy tất cả sản phẩm
    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM products", null);
        if (cursor.moveToFirst()) {
            do {
                Product p = new Product(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getInt(3),
                        cursor.getString(4),
                        cursor.getInt(5),
                        cursor.getString(6)
                );
                list.add(p);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // Lấy 1 sản phẩm theo ID
    public Product getProductById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM products WHERE id=?", new String[]{String.valueOf(id)});
        Product p = null;
        if (cursor != null && cursor.moveToFirst()) {
            p = new Product(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getInt(3),
                    cursor.getString(4),
                    cursor.getInt(5),
                    cursor.getString(6)
            );
            cursor.close();
        }
        return p;
    }

    // Cập nhật số lượng
    public boolean updateQuantity(int id, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("quantity", quantity);
        int rows = db.update("products", values, "id=?", new String[]{String.valueOf(id)});
        return rows > 0;
    }

    // Xóa sản phẩm
    public boolean deleteProduct(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete("products", "id=?", new String[]{String.valueOf(id)});
        return rows > 0;
    }

    // Thống kê tổng giá trị
    public long getTotalValue() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(price * quantity) FROM products", null);
        long total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getLong(0);
        }
        cursor.close();
        return total;
    }

    // Thống kê sắp hết hàng (< 5)
    public int countLowStock() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM products WHERE quantity < 5", null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }
}