package com.example.mydesign;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.storage.UploadTask;

public class UserExistenceDesign extends AppCompatActivity {
    private Uri image_uri;
    private ArrayList<String> image_list;
    private ArrayList<String> id_list;
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
        recyclerView = findViewById(R.id.recyclerview1);
        image_display = new ImageDisplayExistence(image_list, id_list, this);
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