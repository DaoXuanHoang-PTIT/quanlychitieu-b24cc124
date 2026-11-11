package com.example.quanlychitieu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Locale;

/**
 * Activity chính hiển thị danh sách các khoản chi và bảng thống kê tổng quan.
 */
public class MainActivity extends AppCompatActivity {
    private TextView tvTotalProducts, tvTotalValue, tvLowStock;
    private ListView lvProducts;
    private Button btnAddProduct;
    private DatabaseHelper dbHelper;
    private ProductAdapter adapter;
    private List<Product> productList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        tvTotalProducts = findViewById(R.id.tvTotalProducts);
        tvTotalValue = findViewById(R.id.tvTotalValue);
        tvLowStock = findViewById(R.id.tvLowStock);
        lvProducts = findViewById(R.id.lvProducts);
        btnAddProduct = findViewById(R.id.btnAddProduct);

        btnAddProduct.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AddProductActivity.class)));

        lvProducts.setOnItemClickListener((parent, view, position, id) -> {
            Product selected = productList.get(position);
            Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
            intent.putExtra("PRODUCT_ID", selected.getId());
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        productList = dbHelper.getAllProducts();
        adapter = new ProductAdapter(this, productList);
        lvProducts.setAdapter(adapter);

        // Cập nhật khối thống kê
        tvTotalProducts.setText(String.valueOf(productList.size()));
        tvTotalValue.setText(String.format(Locale.getDefault(), "%,d đ", dbHelper.getTotalValue()));
        tvLowStock.setText(String.valueOf(dbHelper.countLowStock()));
    }
}
