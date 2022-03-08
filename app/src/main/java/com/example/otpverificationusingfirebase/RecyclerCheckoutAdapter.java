package com.example.otpverificationusingfirebase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class RecyclerCheckoutAdapter extends RecyclerView.Adapter<RecyclerCheckoutAdapter.CheckoutViewHolder> {

    Context context;
    ArrayList<CheckoutModel> checkoutModels;
    ReChAdapter reChAdapter;
    RecyclerProductAdapter productAdapter;

    public interface ReChAdapter{
        void OnRemove(int pos, Double nm);
    }

    public RecyclerCheckoutAdapter(Context context, ArrayList<CheckoutModel> checkoutModels, ReChAdapter reChAdapter) {

        this.context = context;
        this.checkoutModels = checkoutModels;
        this.reChAdapter = reChAdapter;
    }

    @NonNull
    @Override
    public CheckoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.checkout_list, parent, false);
        return new CheckoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckoutViewHolder holder, int position) {
        CheckoutModel ch = checkoutModels.get(position);
        holder.itName.setText(ch.getName());
        holder.itPrice.setText(ch.getPrice());
        holder.itQuan.setText(ch.getQuantity());
        String ttl = "₹ " + ch.getTotal();
        holder.totalPr.setText(ttl);

        holder.chDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nm = ch.getName();
                SharedPreferences sp = context.getSharedPreferences("Item details", Context.MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = sp.edit();
                prefsEditor.remove(nm);
                prefsEditor.apply();

                reChAdapter.OnRemove(holder.getAdapterPosition(),Double.parseDouble(ch.getTotal()));


            }
        });
    }

    @Override
    public int getItemCount() {
        return checkoutModels.size();
    }

    public static class CheckoutViewHolder extends RecyclerView.ViewHolder {

        TextView itName, itPrice, itQuan, totalPr;
        Button chDelete;

        public CheckoutViewHolder(@NonNull View itemView) {
            super(itemView);

            itName = itemView.findViewById(R.id.item_name);
            itPrice = itemView.findViewById(R.id.item_rate);
            itQuan = itemView.findViewById(R.id.item_quantity);
            chDelete = itemView.findViewById(R.id.chDelete);
            totalPr = itemView.findViewById(R.id.item_total);

        }
    }


}


