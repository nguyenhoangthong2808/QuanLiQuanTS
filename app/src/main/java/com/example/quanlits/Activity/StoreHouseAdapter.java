package com.example.quanlits.Activity;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlits.R;

public class StoreHouseAdapter extends RecyclerView.Adapter<StoreHouseAdapter.StoreHouseViewHolder> {

    private static final String TAG = "StoreHouseAdapter"; // Để log dễ dàng hơn
    private Context context;
    private Cursor cursor;

    private int nameColumnIndex;
    private int typeColumnIndex;  // Chỉ mục cột TYPE
    private int quantityColumnIndex;

    public StoreHouseAdapter(Context context, Cursor cursor) {
        this.context = context;
        setCursor(cursor);  // Gọi hàm setCursor để đảm bảo cột được xác định ngay khi khởi tạo
    }

    @Override
    public StoreHouseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_storehouse, parent, false);
        return new StoreHouseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StoreHouseViewHolder holder, int position) {
        if (cursor != null && cursor.moveToPosition(position)) {
            // Kiểm tra vị trí hợp lệ và di chuyển con trỏ
            String name = cursor.getString(nameColumnIndex);
            String type = cursor.getString(typeColumnIndex);  // Lấy giá trị cột TYPE
            int quantity = cursor.getInt(quantityColumnIndex);

            holder.nameTextView.setText(name);
            holder.typeTextView.setText(type);  // Hiển thị thông tin TYPE
            holder.quantityTextView.setText(String.valueOf(quantity));
        }
    }

    @Override
    public int getItemCount() {
        return (cursor != null) ? cursor.getCount() : 0;
    }

    public void swapCursor(Cursor newCursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        setCursor(newCursor); // Gọi hàm setCursor để tái sử dụng logic thiết lập cột
        notifyDataSetChanged(); // Cập nhật lại adapter khi dữ liệu thay đổi
    }

    private void setCursor(Cursor cursor) {
        this.cursor = cursor;

        if (cursor != null) {
            // Lấy index các cột chỉ một lần khi cursor thay đổi
            nameColumnIndex = cursor.getColumnIndex("NAME");
            typeColumnIndex = cursor.getColumnIndex("TYPE");  // Lấy chỉ mục của cột TYPE
            quantityColumnIndex = cursor.getColumnIndex("QUANTITY");

            if (nameColumnIndex == -1 || typeColumnIndex == -1 || quantityColumnIndex == -1) {
                Log.e(TAG, "Column not found in cursor! Ensure your SQL query includes 'NAME', 'TYPE', and 'QUANTITY'");
            }
        }
    }


    public class StoreHouseViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView typeTextView;  // Thêm TextView để hiển thị TYPE
        TextView quantityTextView;

        public StoreHouseViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tvName);
            typeTextView = itemView.findViewById(R.id.tvType);  // Khởi tạo TextView cho TYPE
            quantityTextView = itemView.findViewById(R.id.tvQuantity);
        }
    }
}
