package com.example.mydesign;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mydesign.menu.UserMenu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UserExistenceDesign extends UserMenu {
    private ArrayList<String> image_list;
    private ArrayList<String[]> info;
//    private ArrayList<Integer> price_list;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private HolderExistenceDesign image_display;
    private FirebaseFirestore db;
    private TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycleview);
        image_list = new ArrayList<>();
        info = new ArrayList<>();
//        price_list = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerview);
        progressBar = findViewById(R.id.progress);
        message = findViewById(R.id.show_text);
        image_display = new HolderExistenceDesign(image_list, info,  this);
        recyclerView.setLayoutManager(new LinearLayoutManager(null));
        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);
        db = FirebaseFirestore.getInstance();
        db.collection("Supplier Uploads").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().size() == 0) {
                        message.setText("No Products Available");
                        progressBar.setVisibility(View.GONE);
                        return;
                    }
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        System.out.println(document.getData());
                        image_list.add(document.getString("Image URL"));
                        String id = document.getString("Supplier ID");
                        String company = document.getString("Company Name");
                        String name = document.getString("File Name");
                        String price = document.getString("Price");
                        String description = document.getString("Product Description");

                        String[] item_info = {id, company, name, description, price};
                        message.setText("Let's Start Shopping");
                        message.setTextColor(Color.parseColor("#B8139D"));
                        info.add(item_info);

                    }
                    recyclerView.setAdapter(image_display);
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.GONE);
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });
    }
}