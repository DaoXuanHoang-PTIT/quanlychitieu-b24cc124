package com.example.quanlychitieu;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

/**
 * Activity hiển thị chi tiết sản phẩm / khoản chi và quản lý số lượng, xóa sản phẩm.
 */
public class ProductDetailActivity extends AppCompatActivity {
    private TextView tvName, tvCategory, tvPrice, tvQuantity, tvTotalValue, tvLowStockWarning, tvDesc;
    private Button btnAddOne, btnSellOne, btnBack, btnDelete;
    private DatabaseHelper dbHelper;
    private int productId;
    private Product currentProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        dbHelper = new DatabaseHelper(this);
        productId = getIntent().getIntExtra("PRODUCT_ID", -1);

        tvName = findViewById(R.id.tvName);
        tvCategory = findViewById(R.id.tvCategory);
        tvPrice = findViewById(R.id.tvPrice);
        tvQuantity = findViewById(R.id.tvQuantity);
        tvTotalValue = findViewById(R.id.tvTotalValue);
        tvLowStockWarning = findViewById(R.id.tvLowStockWarning);
        tvDesc = findViewById(R.id.tvDesc);

        btnAddOne = findViewById(R.id.btnAddOne);
        btnSellOne = findViewById(R.id.btnSellOne);
        btnBack = findViewById(R.id.btnBack);
        btnDelete = findViewById(R.id.btnDelete);

        loadProductData();

        btnAddOne.setOnClickListener(v -> {
            if (currentProduct != null) {
                int newQty = currentProduct.getQuantity() + 1;
                dbHelper.updateQuantity(productId, newQty);
                loadProductData();
            }
        });

        btnSellOne.setOnClickListener(v -> {
            if (currentProduct != null) {
                if (currentProduct.getQuantity() <= 0) {
                    Toast.makeText(this, "Hết hàng", Toast.LENGTH_SHORT).show();
                } else {
                    int newQty = currentProduct.getQuantity() - 1;
                    dbHelper.updateQuantity(productId, newQty);
                    loadProductData();
                }
            }
        });

        btnBack.setOnClickListener(v -> finish());

        btnDelete.setOnClickListener(v -> {
            dbHelper.deleteProduct(productId);
            Toast.makeText(this, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void loadProductData() {
        currentProduct = dbHelper.getProductById(productId);
        if (currentProduct != null) {
            tvName.setText(currentProduct.getName());
            tvCategory.setText(currentProduct.getCategory());
            tvPrice.setText(String.format(Locale.getDefault(), "%,d đ", currentProduct.getPrice()));
            tvQuantity.setText(String.valueOf(currentProduct.getQuantity()));
            tvTotalValue.setText(String.format(Locale.getDefault(), "%,d đ", (long) currentProduct.getPrice() * currentProduct.getQuantity()));
            tvDesc.setText(currentProduct.getDescription());

            if (currentProduct.getQuantity() < 5) {
                tvLowStockWarning.setVisibility(View.VISIBLE);
            } else {
                tvLowStockWarning.setVisibility(View.GONE);
            }
        }
    }
}
