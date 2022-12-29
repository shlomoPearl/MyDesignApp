package com.example.mydesign;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class ColorSize extends AppCompatActivity {


    private ArrayList<String> image_list;
    private ArrayList<String[]> info;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private AdminOrderDisplay image_display;
    private FirebaseFirestore store;

ImageView iv1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.admin_order);
//            image_list = new ArrayList<>();
//            info = new ArrayList<>();
//            recyclerView = findViewById(R.id.recyclerview);
//            image_display = new AdminOrderDisplay(image_list,info , this);
//            recyclerView.setLayoutManager(new LinearLayoutManager(null));
//            progressBar = findViewById(R.id.progress);
//            progressBar.setVisibility(View.VISIBLE);
//            store = FirebaseFirestore.getInstance();
//            // display user detail from Orders in firestore
//            store.collection("Orders").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.color_size);
        // the refernce to the image view that holds the image that the user will see
        iv1 = (ImageView)findViewById(R.id.imageView);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            int itemId = bundle.getInt("itemId");
            iv1.setImageResource(itemId);


}
    }}