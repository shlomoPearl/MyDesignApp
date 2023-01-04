package com.example.mydesign;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class HolderOrder extends RecyclerView.Adapter<HolderOrder.ViewHolder> {
    private ArrayList<String> imageList;
    private FirebaseFirestore store;
    private ArrayList<String[]> info;
    public Context context;

    public HolderOrder(ArrayList<String> imageList, ArrayList<String[]> info, Context context) {
        this.imageList = imageList;
        this.info = info;
        this.context = context;
        this.store = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public HolderOrder.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_order,parent,false);
        return new HolderOrder.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderOrder.ViewHolder holder, int position) {
        holder.user_name.setText(info.get(position)[0]);
        holder.email.setText(info.get(position)[1]);
        holder.quantity.setText(info.get(position)[2]);
        holder.size.setText(info.get(position)[3]);
        Glide.with(holder.itemView.getContext()).load(imageList.get(position)).into(holder.imageView);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
    }


    @Override
    public int getItemCount() {
        return imageList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView email;
        TextView quantity;
        TextView user_name;
        TextView size;
        CheckBox checkBox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.image);
            email=itemView.findViewById(R.id.email);
            quantity=itemView.findViewById(R.id.quantity);
            user_name=itemView.findViewById(R.id.username);
            size=itemView.findViewById(R.id.size);
            checkBox=itemView.findViewById(R.id.checkbox);
        }
    }
}