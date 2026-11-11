package com.example.quanlychitieu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

/**
 * Adapter tùy chỉnh cho ListView hiển thị danh sách sản phẩm / khoản chi.
 */
public class ProductAdapter extends BaseAdapter {
    private final Context context;
    private final List<Product> list;

    public ProductAdapter(Context context, List<Product> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        }

        Product p = list.get(position);

        TextView tvName = convertView.findViewById(R.id.tvName);
        TextView tvCategory = convertView.findViewById(R.id.tvCategory);
        TextView tvPrice = convertView.findViewById(R.id.tvPrice);
        TextView tvQuantity = convertView.findViewById(R.id.tvQuantity);

        if (tvName != null) tvName.setText(p.getName());
        if (tvCategory != null) tvCategory.setText(p.getCategory());
        if (tvPrice != null) tvPrice.setText(String.format(Locale.getDefault(), "%,d đ", p.getPrice()));
        if (tvQuantity != null) tvQuantity.setText("SL: " + p.getQuantity());

        return convertView;
    }
}
