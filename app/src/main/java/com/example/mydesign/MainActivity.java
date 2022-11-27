package com.example.mydesign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button sign_in =  findViewById(R.id.signinbutton);
    Button sign_up = findViewById(R.id.signupbutton);
    Button skip = findViewById(R.id.skipbutton);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onSigninBtn(View view){
        sign_in.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), registresion.class);
                startActivity(myIntent);
            }
        });
    }

    public void onSignupBtn(View view){
        sign_up.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent1 = new Intent(view.getContext(), sign_in.class);
                startActivity(myIntent1);
            }
        });

    }

    public void onSkipBtn(View view){
        skip.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent2 = new Intent(view.getContext(), skip.class);
                startActivity(myIntent2);
            }
        });

    }
}