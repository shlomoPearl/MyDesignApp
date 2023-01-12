package com.example.mydesign;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mydesign.menu.SupplierMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ShowUploadActivity extends SupplierMenu {
    private ArrayList<String> image_list;
    private ArrayList<String[]> info;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView count;
    private HolderSupplierUpload image_display;
    private FirebaseFirestore store;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycleview);
        image_list = new ArrayList<>();
        info = new ArrayList<>();
        count = findViewById(R.id.show_text);
        recyclerView = findViewById(R.id.recyclerview);
        progressBar = findViewById(R.id.progress);
        image_display = new HolderSupplierUpload(image_list,info , this);
        recyclerView.setLayoutManager(new LinearLayoutManager(null));
        store = FirebaseFirestore.getInstance();

        // display user detail from Orders in firestore
        store.collection("Supplier Uploads").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    if (document.exists() && document.get("Supplier ID").equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        String title = document.get("File Name").toString();
                        String price = document.get("Price").toString();
                        String description = document.get("Product Description").toString();
                        String url = document.get("Image URL").toString();
                        String[] upload_details = {title,"price - " + price + " $",description};
                        image_list.add(url);
                        info.add(upload_details);
                    }
                }
            }
            count.setText("Total Item In Catalog - " + image_list.size());
            recyclerView.setAdapter(image_display);
            progressBar.setVisibility(View.INVISIBLE);
        });
    }
}
