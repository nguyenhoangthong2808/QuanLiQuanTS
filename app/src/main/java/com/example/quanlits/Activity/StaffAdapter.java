package com.example.quanlits.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlits.R;

import java.util.ArrayList;

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.ViewHolder> {

    private final ArrayList<String> staffList;

    public StaffAdapter(ArrayList<String> staffList) {
        this.staffList = staffList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_staff, parent, false); // Tệp XML: item_staff.xml
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Phân tích chuỗi thông tin từ danh sách nhân viên
        String[] staffInfo = staffList.get(position).split(", ");
        String name = staffInfo[0].replace("Name: ", "");
        String gender = staffInfo[1].replace("Gender: ", "");
        String phone = staffInfo[2].replace("Phone: ", "");

        // Hiển thị thông tin vào các thành phần giao diện
        holder.tvStaffName.setText(name);
        holder.tvStaffGT.setText(gender);
        holder.tvStaffPhone.setText(phone);
    }

    @Override
    public int getItemCount() {
        return staffList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageStaff;
        TextView tvStaffName, tvStaffGT, tvStaffPhone;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageStaff = itemView.findViewById(R.id.imageStaff); // Ảnh nhân viên
            tvStaffName = itemView.findViewById(R.id.tvStaffName); // Tên nhân viên
            tvStaffGT = itemView.findViewById(R.id.tvStaffGT); // Giới tính
            tvStaffPhone = itemView.findViewById(R.id.tvStaffPhone); // Số điện thoại
        }
    }
}
