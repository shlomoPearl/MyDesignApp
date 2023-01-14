package com.example.mydesign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SupplierProfile extends AppCompatActivity {
    ImageView camera;
    TextView connect_us;
    ImageView customer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supplier_profile);
        camera= findViewById(R.id.imageView5);
        camera.setOnClickListener(v -> {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivity(intent);

        });
        connect_us = findViewById(R.id.settings);
        customer= findViewById(R.id.Settings);
        customer.setOnClickListener(v -> {
            Uri number = Uri.parse("tel:0555555555");
            Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
            startActivity(callIntent);

        });
        connect_us.setOnClickListener(v -> {
            Uri number = Uri.parse("tel:0555555555");
            Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
            startActivity(callIntent);

        });
    }
}