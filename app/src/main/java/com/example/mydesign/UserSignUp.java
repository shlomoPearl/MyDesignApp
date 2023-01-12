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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserSignUp extends AppCompatActivity {

    private EditText username;
    private EditText name;
    private EditText email;
    private EditText password;
    private Button sign_up;
    private TextView exist_user;

    private FirebaseFirestore store;
    private FirebaseAuth mAuth;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_signup);

        username = findViewById(R.id.username);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        sign_up = findViewById(R.id.sign_up);
        exist_user = findViewById(R.id.login_user);
        store = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        pd = new ProgressDialog(this);

        exist_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserSignUp.this , UserSignIn.class));
                finish();
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtUsername = username.getText().toString();
                String txtName = name.getText().toString();
                String txtEmail = email.getText().toString();
                String txtPassword = password.getText().toString();

                if (TextUtils.isEmpty(txtUsername) || TextUtils.isEmpty(txtName)
                        || TextUtils.isEmpty(txtEmail) || TextUtils.isEmpty(txtPassword)){
                    Toast.makeText(UserSignUp.this, "Empty credentials!", Toast.LENGTH_SHORT).show();
                } else if (txtPassword.length() < 6){
                    Toast.makeText(UserSignUp.this, "Password too short!", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(txtUsername , txtName , txtEmail , txtPassword);
                }
            }
        });
    }

    private void registerUser(final String username, final String name, final String email, String password) {

        pd.setMessage("Please Wait!");
        pd.show();

        mAuth.createUserWithEmailAndPassword(email , password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser user = mAuth.getCurrentUser();
                store.collection("Users").document(user.getUid())
                        .collection("Orders").document();
                store.collection("Users").document(user.getUid())
                        .collection("Design").document();
                DocumentReference df = store.collection("Users").document(user.getUid());
                Map<String, Object> user_info = new HashMap<>();
                user_info.put("User Name",username);
                user_info.put("Name",name);
                user_info.put("Email",email);
//                user_info.put("Is User?",true);
                df.set(user_info);
                pd.cancel();
                Toast.makeText(UserSignUp.this,"Create account successfully!",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UserSignUp.this , UserMainActivity.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(UserSignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}