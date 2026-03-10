

package com.example.quanlits.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlits.R;
import com.example.quanlits.model.Customer;

import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {

    public interface CustomerClickListener {
        void onCustomerClick(Customer customer, int position);
    }

    private List<Customer> customerList;
    private CustomerClickListener listener;

    public CustomerAdapter(List<Customer> customerList, CustomerClickListener listener) {
        this.customerList = customerList;
        this.listener = listener;
    }

    @Override
    public CustomerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_customer, parent, false);
        return new CustomerViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(CustomerViewHolder holder, int position) {
        Customer customer = customerList.get(position);
        holder.bind(customer, position);
    }


    @Override
    public int getItemCount() {
        return customerList.size();
    }

    class CustomerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvName, tvEmail, tvPhone;
        ImageView imgCustomer;
        CustomerClickListener listener;
        int position;

        public CustomerViewHolder(View itemView, CustomerClickListener listener) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvCustomerName);
            tvEmail = itemView.findViewById(R.id.tvCustomerEmail);
            tvPhone = itemView.findViewById(R.id.tvCustomerPhone);
            imgCustomer = itemView.findViewById(R.id.imgCustomer);
            this.listener = listener;
            itemView.setOnClickListener(this);
        }

        public void bind(Customer customer, int position) {
            this.position = position;
            tvName.setText(customer.getName());
            tvEmail.setText(customer.getEmail());
            tvPhone.setText(customer.getPhone());
            imgCustomer.setImageResource(customer.getImageResource());
        }

        @Override
        public void onClick(View v) {
            listener.onCustomerClick(customerList.get(position), position);
        }
    }


}
