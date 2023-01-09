//package com.example.mydesign;
//
//import static android.content.ContentValues.TAG;
//
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.ProgressBar;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.QuerySnapshot;
//import java.util.ArrayList;
//import java.util.List;
//
//public class SupplierOrder extends AppCompatActivity {
//
//    private ArrayList<String> image_list;
//    private ArrayList<String[]> info;
//    private RecyclerView recyclerView;
//    private ProgressBar progressBar;
//    private SupplierOrderDisplay image_display;
//    private FirebaseFirestore store;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.supplier_order);
//        image_list = new ArrayList<>();
//        info = new ArrayList<>();
//        recyclerView = findViewById(R.id.recyclerview);
//        image_display = new SupplierOrderDisplay(image_list,info , this);
//        recyclerView.setLayoutManager(new LinearLayoutManager(null));
//        progressBar = findViewById(R.id.progress);
//        progressBar.setVisibility(View.VISIBLE);
//        store = FirebaseFirestore.getInstance();
//        // display user detail from Orders in firestore
//        store.collection("Orders").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    List<String> list = new ArrayList<>();
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        String email = document.get("Email").toString();
//                        String name = document.get("Name").toString();
//                        String user_name = document.get("User Name").toString();
//                        String url = document.get("URL").toString();
//                        String size = document.get("SIZE").toString();
//                        String order_state = document.get("Order State").toString();
//                        String[] user_details = {"Email - "+email,"Name - "+name,
//                                "User Name - "+user_name, "Size - "+size, order_state};
//                        image_list.add(url);
//                        info.add(user_details);
//
//                    }
//                    Log.d(TAG, list.toString());
//                    recyclerView.setAdapter(image_display);
//                    progressBar.setVisibility(View.GONE);
//                } else {
//                    Log.d(TAG, "Error getting documents: ", task.getException());
//                }
//            }
//        });
//    }
//
//}
