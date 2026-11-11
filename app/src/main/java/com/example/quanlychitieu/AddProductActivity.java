package com.example.quanlychitieu;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity để thêm khoản chi / sản phẩm mới.
 */
public class AddProductActivity extends AppCompatActivity {
    private EditText etName, etPrice, etQuantity, etDesc;
    private Spinner spCategory;
    private Switch swActive;
    private Button btnSave;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        dbHelper = new DatabaseHelper(this);

        etName = findViewById(R.id.etName);
        etPrice = findViewById(R.id.etPrice);
        etQuantity = findViewById(R.id.etQuantity);
        etDesc = findViewById(R.id.etDesc);
        spCategory = findViewById(R.id.spCategory);
        swActive = findViewById(R.id.swActive);
        btnSave = findViewById(R.id.btnSave);

        // Set Adapter cho Spinner
        String[] categories = {"Điện tử", "Thực phẩm", "Văn phòng phẩm", "Gia dụng", "Khác"};
        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(catAdapter);

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String priceStr = etPrice.getText().toString().trim();
            String qtyStr = etQuantity.getText().toString().trim();
            String desc = etDesc.getText().toString().trim();
            String category = spCategory.getSelectedItem().toString();
            int active = swActive.isChecked() ? 1 : 0;

            if (name.isEmpty()) {
                etName.setError("Tên không được để trống");
                return;
            }
            if (priceStr.isEmpty() || Integer.parseInt(priceStr) <= 0) {
                etPrice.setError("Giá phải lớn hơn 0");
                return;
            }
            if (qtyStr.isEmpty() || Integer.parseInt(qtyStr) < 0) {
                etQuantity.setError("Số lượng không hợp lệ");
                return;
            }

            int price = Integer.parseInt(priceStr);
            int quantity = Integer.parseInt(qtyStr);

            Product p = new Product(name, price, quantity, category, active, desc);
            dbHelper.insertProduct(p);

            Toast.makeText(this, "Đã thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
