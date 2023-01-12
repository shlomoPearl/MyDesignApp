package com.example.mydesign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class OpenScreen extends AppCompatActivity {

    private Button admin;
    private Button user;


//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
//            System.out.println(FirebaseAuth.getInstance().getCurrentUser().getUid());
//
//            FirebaseFirestore store = FirebaseFirestore.getInstance();
//            store.collection("Admins").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                    if (task.isSuccessful()) {
//                        System.out.println(FirebaseAuth.getInstance().getCurrentUser().getUid() + "********");
//                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            if (document.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
//                                System.out.println(FirebaseAuth.getInstance().getCurrentUser().getUid() +"%%%%%%%%%%%");
//                                startActivity(new Intent(OpenScreen.this, SupplierMainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
//                                finish();
//                            }
//                        }
//                        startActivity(new Intent(OpenScreen.this, UserMainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
//                        finish();
//                    }
//                }
//            });
//        }
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_screen);
        admin = findViewById(R.id.admin_btn);
        user= findViewById(R.id.user_btn);
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OpenScreen.this, SupplierStart.class));
            }
        });
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent (OpenScreen.this , UserStart.class));
            }
        });

    }

}