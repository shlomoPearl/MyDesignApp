package com.example.mydesign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


public class UserMainActivity extends AppCompatActivity {

    private Button logout;
    private Button your_own_d;
    private Button existing_d;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_main_activity);
        logout = findViewById(R.id.logout);
        your_own_d = findViewById(R.id.your_own_btn);
        existing_d = findViewById(R.id.existing_btn);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(UserMainActivity.this, "Logged Out!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UserMainActivity.this, OpenScreen.class));
            }
        });
        existing_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserMainActivity.this, UserExistenceDesign.class));
            }
        });
        your_own_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserMainActivity.this, UserDesign.class));
            }
        });

    }

}