package com.example.mydesign;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.mydesign.menu.UserMenu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UserExistenceDesign extends UserMenu {
    private Uri image_uri;
    private ArrayList<String> image_list;
    private ArrayList<String> id_list;
    private ArrayList<Integer> price_list;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ImageDisplayExistence image_display;
    private FirebaseFirestore db;
    // clothes_uploads is the brunch that all image are upload there
//    StorageReference listRef = FirebaseStorage.getInstance().getReference().child("clothes_uploads");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_existence_design);
        image_list = new ArrayList<>();
        id_list = new ArrayList<>();
        price_list = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerview1);
        image_display = new ImageDisplayExistence(image_list, id_list,price_list,  this);
        recyclerView.setLayoutManager(new LinearLayoutManager(null));
        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);
        db = FirebaseFirestore.getInstance();
        db.collection("Supplier Uploads").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        System.out.println(document.getData());
                        image_list.add(document.getString("Image URL"));
                        id_list.add(document.getString("Supplier ID"));
                        price_list.add(Integer.valueOf(document.getString("Price")));
                    }
                    image_display.notifyDataSetChanged();
                    recyclerView.setAdapter(image_display);
                    progressBar.setVisibility(View.GONE);
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });
    }

}