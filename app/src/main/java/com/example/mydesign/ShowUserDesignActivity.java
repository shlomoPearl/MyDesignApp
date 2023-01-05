package com.example.mydesign;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ShowUserDesignActivity extends AppCompatActivity {
    private ArrayList<String> image_list;
    private ArrayList<String[]> info;
    private RecyclerView recyclerView;
    private HolderDesign image_display;
    private FirebaseFirestore store;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.show_user_design);
            image_list = new ArrayList<>();
            info = new ArrayList<>();
            recyclerView = findViewById(R.id.recyclerview);
            image_display = new HolderDesign(image_list,info , this);
            recyclerView.setLayoutManager(new LinearLayoutManager(null));
            store = FirebaseFirestore.getInstance();
            store.collection("User Design").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        List<String> list = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String user_name = document.get("User Name").toString();
                            String email = document.get("Email").toString();
                            String bid = document.get("Bid").toString();
                            String url = document.get("Image URL").toString();
                            String file_name = document.get("File Name").toString();
                            String description = document.get("Product Description").toString();
                            String[] user_details = {"User Name - "+user_name, "Email - "+email
                                    ,"Bid - " + bid, "Product Name - "+file_name, "Product Description - "+description};
                            image_list.add(url);
                            info.add(user_details);

                        }
                        Log.d(TAG, list.toString());
                        recyclerView.setAdapter(image_display);
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
        }
    }

