package com.example.mydesign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class AdminSignUp extends AppCompatActivity {

    private EditText company_name;
    private EditText address;
    private EditText phone_number;
    private EditText email;
    private EditText password;
    private Button sign_up;
    private TextView exist_admin;

    private DatabaseReference mRootRef;
    private FirebaseAuth mAuth;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_signup);
        company_name = findViewById(R.id.companyname);
        address = findViewById(R.id.address);
        phone_number = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        sign_up = findViewById(R.id.sign_up);
        exist_admin = findViewById(R.id.login_admin);
        pd = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        exist_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminSignUp.this , AdminSignIn.class));
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_company_name = company_name.getText().toString();
                String txt_address = address.getText().toString();
                String txt_phone = phone_number.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                if (TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_email)
                        || TextUtils.isEmpty(txt_address) || TextUtils.isEmpty(txt_phone)
                        || TextUtils.isEmpty(txt_company_name)){
                    Toast.makeText(AdminSignUp.this, "Empty credentials!", Toast.LENGTH_SHORT).show();
                } else if (txt_password.length() < 6){
                    Toast.makeText(AdminSignUp.this, "Password too short!", Toast.LENGTH_SHORT).show();
                } else {
                    registerAdmin(txt_company_name , txt_address , txt_phone , txt_email, txt_password);
                }
            }
        });
    }

    private void registerAdmin(String company_name, String address, String phone, String email, String password) {

        pd.setMessage("Please Wait!");
        pd.show();
        mAuth.createUserWithEmailAndPassword(email , password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                pd.cancel();
                Intent intent = new Intent(AdminSignUp.this , AdminActivity.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(AdminSignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });





    }
}