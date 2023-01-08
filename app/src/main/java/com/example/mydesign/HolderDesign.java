package com.example.mydesign;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class HolderDesign extends RecyclerView.Adapter<HolderDesign.ViewHolder> {
    private ArrayList<String> imageList;
    private FirebaseFirestore store;
    private ArrayList<String[]> info;
    public Context context;

    public HolderDesign(ArrayList<String> imageList, ArrayList<String[]> info, Context context) {
        this.imageList = imageList;
        this.info = info;
        this.context = context;
        this.store = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public HolderDesign.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_show_design,parent,false);
        return new HolderDesign.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderDesign.ViewHolder holder, int position) {
        holder.user_name.setText(info.get(position)[0]);
        holder.email.setText(info.get(position)[1]);
        holder.bid.setText(info.get(position)[2]);
        holder.file_name.setText(info.get(position)[3]);
        holder.description.setText(info.get(position)[4]);
        Glide.with(holder.itemView.getContext()).load(imageList.get(position)).into(holder.imageView);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//              //
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
        TextView description;
        TextView file_name;
        TextView user_name;
        TextView bid;
        CheckBox checkBox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            file_name = itemView.findViewById(R.id.file_name);
            imageView=itemView.findViewById(R.id.image);
            email=itemView.findViewById(R.id.email);
            description =itemView.findViewById(R.id.product_description);
            user_name=itemView.findViewById(R.id.username);
            bid =itemView.findViewById(R.id.bid);
            checkBox=itemView.findViewById(R.id.checkbox);
        }
    }
}
