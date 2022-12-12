package com.example.mydesign;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class AdminStart extends AppCompatActivity {

    private Button sign_up; // for new user
    private Button sign_in; // for exist user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_start);
        sign_up = findViewById(R.id.sign_up);
        sign_in = findViewById(R.id.sign_in);


    }
}