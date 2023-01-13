package com.example.mydesign;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class HolderSupplierUpload extends RecyclerView.Adapter<HolderSupplierUpload.ViewHolder> {
    private ArrayList<String> image_list;
    private FirebaseFirestore store;
    private ArrayList<String[]> info_list;
    public android.content.Context context;

    public HolderSupplierUpload(ArrayList<String> imageList, ArrayList<String[]> info, Context context) {
        this.image_list = imageList;
        this.info_list = info;
        this.context = context;
        this.store = FirebaseFirestore.getInstance();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_supplier_upload,parent,false);
        return new HolderSupplierUpload.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderSupplierUpload.ViewHolder holder, int position) {
        holder.price.setText(info_list.get(position)[0]);
        holder.title.setText(info_list.get(position)[1]);
        holder.description.setText(info_list.get(position)[2]);
        Glide.with(holder.itemView.getContext()).load(image_list.get(position)).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return image_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView price;
        private TextView title;
        private TextView description;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            price = itemView.findViewById(R.id.price);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
        }
    }
}