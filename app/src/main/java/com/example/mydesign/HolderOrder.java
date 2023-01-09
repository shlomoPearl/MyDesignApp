package com.example.mydesign;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
    public void onBindViewHolder(@NonNull HolderOrder.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.user_name.setText(info.get(position)[0]);
        holder.email.setText(info.get(position)[1]);
        holder.quantity.setText(info.get(position)[2]);
        holder.size.setText(info.get(position)[3]);
        holder.checkBox.setChecked(info.get(position)[4].equals("true"));
        if (info.get(position)[4].equals("true")){
            holder.checkBox.setTextColor(Color.GREEN);
        }
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBox.isChecked()){
                    holder.checkBox.setTextColor(Color.GREEN);
                    store.collection("Admins").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .collection("Order").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()){
                                        for (QueryDocumentSnapshot document : task.getResult()){
                                            if (document.get("URL").toString().equals(imageList.get(position))){
                                                store.collection("Admins").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                        .collection("Order").document(document.getId()).update("Order State","true");
                                            }
                                        }


                                    }
                                }
                            });
                }else {
                    store.collection("Admins").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .collection("Order").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            if (document.get("URL").toString().equals(imageList.get(position))) {
                                                store.collection("Admins").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                        .collection("Order").document(document.getId()).update("Order State", "false");
                                            }
                                        }
                                    }
                                }
                            });
                }
            }
        });
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
    public static class ViewHolder extends RecyclerView.ViewHolder {
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