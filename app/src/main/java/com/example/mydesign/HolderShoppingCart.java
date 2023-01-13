package com.example.mydesign;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HolderShoppingCart extends RecyclerView.Adapter<HolderShoppingCart.ViewHolder> {
    private ArrayList<String> imageList;
    private ArrayList<String[]> info;
    public Context context;

    public HolderShoppingCart(ArrayList<String> imageList, ArrayList<String[]> info, Context context) {
        this.imageList = imageList;
        this.info = info;
        this.context = context;
    }

    @NonNull
    @Override
    public HolderShoppingCart.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_cart_shopping,parent,false);
        return new HolderShoppingCart.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull HolderShoppingCart.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.supplier_name.setText(holder.supplier_name.getText().toString() + info.get(position)[0]);
        holder.phone.setText(holder.phone.getText().toString() + info.get(position)[1]);
        holder.address.setText(holder.address.getText().toString() + info.get(position)[2]);
        holder.email.setText(holder.email.getText().toString() + info.get(position)[3]);
        holder.quantity.setText(holder.quantity.getText().toString() + info.get(position)[4]);
        holder.size.setText(holder.size.getText().toString() + info.get(position)[5]);
        holder.total_price.setText(holder.total_price.getText().toString() + info.get(position)[6]);
        holder.checkBox.setChecked(info.get(position)[7].equals("true"));
        if (info.get(position)[7].equals("true")){
            holder.checkBox.setTextColor(Color.GREEN);
        }
        holder.checkBox.setEnabled(false);
        Glide.with(holder.itemView.getContext()).load(imageList.get(position)).into(holder.imageView);
        holder.phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + info.get(position)[1]));
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return imageList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView supplier_name,phone,address,email,quantity,size,total_price;
        CheckBox checkBox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            supplier_name = itemView.findViewById(R.id.order_detail);
            imageView=itemView.findViewById(R.id.image);
            email=itemView.findViewById(R.id.email);
            quantity=itemView.findViewById(R.id.quantity);
            phone=itemView.findViewById(R.id.phone);
            address=itemView.findViewById(R.id.address);
            size=itemView.findViewById(R.id.size);
            total_price=itemView.findViewById(R.id.total_price);
            checkBox=itemView.findViewById(R.id.checkbox);
        }
    }
}