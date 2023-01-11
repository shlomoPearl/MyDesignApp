package com.example.mydesign;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ShowOrderActivity extends AppCompatActivity {
    private ArrayList<String> image_list;
    private ArrayList<String[]> info;
    private RecyclerView recyclerView;
    private HolderOrder image_display;
    private FirebaseFirestore store;
    private TextView count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_order);
        image_list = new ArrayList<>();
        info = new ArrayList<>();
        count = findViewById(R.id.order);
        recyclerView = findViewById(R.id.recyclerview);
        image_display = new HolderOrder(image_list,info , this);
        recyclerView.setLayoutManager(new LinearLayoutManager(null));
        store = FirebaseFirestore.getInstance();
        // display user detail from Orders in firestore
        store.collection("Admins").document(FirebaseAuth.getInstance()
                .getCurrentUser().getUid()).collection("Order").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> list = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        System.out.println(document.getId() + " => " + document.getData());
                        System.out.println(document.get("URL").toString());
                        if (document.contains("Bid")){
                            String bid = document.get("Bid").toString();
                            String description = document.get("Description").toString();
                            String user_name = document.get("User Name").toString();
                            String email = document.get("Email").toString();
                            String url = document.get("URL").toString();
                            String order_state = document.get("Order State").toString();
                            String[] user_details = {user_name, email, bid, description, order_state};
                            image_list.add(url);
                            info.add(user_details);
                        }else {
                            String quantity = document.get("Quantity").toString();
                            String size = document.get("SIZE").toString();
                            String user_name = document.get("User Name").toString();
                            String email = document.get("User Email").toString();
                            String url = document.get("URL").toString();
                            String order_state = document.get("Order State").toString();
                            String[] user_details = {"User Name - " + user_name, "Email - " + email,
                                    "Quantity - " + quantity, "Size - " + size, order_state};
                            image_list.add(url);
                            info.add(user_details);
                        }


                    }
                    count.setText("Total Order - " + image_list.size());
                    Log.d(TAG, list.toString());
                    recyclerView.setAdapter(image_display);
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }
}
