package com.example.quanlits.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlits.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class foodAdapter extends RecyclerView.Adapter<foodAdapter.ViewHolder> {
    private List<food> foodList;

    public foodAdapter(List<food> foodList) {
        this.foodList = foodList;
    }
    public void updateList(List<food> newList) {
        foodList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        food item = foodList.get(position);
        holder.foodName.setText(item.getName());
        Glide.with(holder.itemView.getContext())
                .load(item.getImageResId())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView foodName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_1);
            foodName = itemView.findViewById(R.id.tv_name_food);
        }
    }
}
