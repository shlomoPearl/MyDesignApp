package com.example.mydesign;

import static com.android.volley.VolleyLog.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mydesign.menu.UserMenu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UserCartShopping extends UserMenu {
    private ArrayList<String> image_list;
    private ArrayList<String[]> info;
    private RecyclerView recyclerView;
    private HolderShoppingCart image_display;
    private FirebaseFirestore store;
    private ProgressBar progressBar;
    private TextView count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycleview);
        image_list = new ArrayList<>();
        info = new ArrayList<>();
        count = findViewById(R.id.show_text);
        progressBar = findViewById(R.id.progress);
        recyclerView = findViewById(R.id.recyclerview);
        image_display = new HolderShoppingCart(image_list,info , this);
        recyclerView.setLayoutManager(new LinearLayoutManager(null));
        store = FirebaseFirestore.getInstance();
        progressBar.setVisibility(ProgressBar.VISIBLE);

        store.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("Orders").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    count.setText("You have " + task.getResult().getDocuments().size()+ " items in your cart");
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String supplier_name = document.get("Supplier Name").toString();
                        String phone = document.get("Supplier Phone").toString();
                        String address = document.get("Supplier Address").toString();
                        String email = document.get("Supplier Email").toString();
                        String quantity = document.get("Quantity").toString();
                        String size = document.get("SIZE").toString();
                        String total_price = document.get("Total Price").toString();
                        String order_state = document.get("Order State").toString();
                        String url = document.get("URL").toString();
                        String[] user_details = {supplier_name,phone, address ,email, quantity,
                                                size, total_price, order_state};
                        image_list.add(url);
                        info.add(user_details);
//                        count.setText("You have " + image_list.size() + " orders");
                        recyclerView.setAdapter(image_display);
                        progressBar.setVisibility(ProgressBar.INVISIBLE);
                    }
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                } else {
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }
}