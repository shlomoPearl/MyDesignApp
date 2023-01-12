package com.example.mydesign;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class SupplierMainActivity extends AppCompatActivity {
    private static final int CHOOSE_IMAGE = 1;
    private Button choose_file;
    private Button upload;
    private Button logout;
    private TextView show_order;
    private TextView show_user_design;
    private TextView show_upload;
    private EditText file_name;
    private EditText price;
    private EditText product_description;
    private ImageView image_preview;
    private Uri image_uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supplier_activity_main);
        choose_file = findViewById(R.id.choose_file);
        upload = findViewById(R.id.upload);
        logout = findViewById(R.id.logout);
        show_order = findViewById(R.id.show_order);
        show_user_design = findViewById(R.id.show_user_design);
        show_upload = findViewById(R.id.show_uploads);
        file_name = findViewById(R.id.edit_text_file_name);
        price = findViewById(R.id.edit_text_price);
        product_description = findViewById(R.id.description);
        image_preview = findViewById(R.id.image_view);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(SupplierMainActivity.this, OpenScreen.class));
                finish();
            }
        });
        choose_file.setOnClickListener(view -> showFileChoose());
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (file_name.getText().toString().isEmpty() || image_uri == null ||
                        price.getText().toString().isEmpty() ||
                        product_description.getText().toString().isEmpty()) {
                    Toast.makeText(SupplierMainActivity.this,
                            "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                }else {
                    uploadImage();
                }
            }
        });
        show_order.setOnClickListener(view -> {
            Intent intent = new Intent(SupplierMainActivity.this, ShowOrderActivity.class);
            startActivity(intent);
        });
        show_user_design.setOnClickListener(view -> {
            Intent intent = new Intent(SupplierMainActivity.this, ShowUserDesignActivity.class);
            startActivity(intent);
        });
        show_upload.setOnClickListener(view -> {
            Intent intent = new Intent(SupplierMainActivity.this, ShowUploadActivity.class);
            startActivity(intent);
        });
    }

    private void uploadImage() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();
        // upload image
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final StorageReference storage = FirebaseStorage.getInstance().
            getReference().child("Catalog Uploads").child(System.currentTimeMillis() + "");
            storage.putFile(image_uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            Log.d("DownloadUrl", url);
                            db.collection("Supplier Uploads").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        String f_n = file_name.getText().toString();
                                        String p = price.getText().toString();
                                        String p_d = product_description.getText().toString();
                                        HashMap<String, Object> catalog = new HashMap<>();
                                        Task<DocumentSnapshot> document = db.collection("Admins")
                                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .get();
                                        if (document.isSuccessful()) {
                                            String company_name = document.getResult().getString("Company Name");
                                            catalog.put("Company Name", company_name);
                                        }
                                        catalog.put("File Name", f_n);
                                        catalog.put("Price", p);
                                        catalog.put("Product Description", p_d);
                                        catalog.put("Image URL", url);
                                        catalog.put("Supplier ID", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                        db.collection("Supplier Uploads").add(catalog);
                                        pd.cancel();
                                        Toast.makeText(SupplierMainActivity.this, "Upload Successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SupplierMainActivity.this, SupplierMainActivity.class));
                                    } else {
                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                    }
                                }
                            });

                        }
                    });
                } else {
                    pd.dismiss();
                    Toast.makeText(SupplierMainActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void showFileChoose() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        launchSomeActivity.launch(intent);
    }
    ActivityResultLauncher<Intent> launchSomeActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    image_uri = data.getData();
                    Picasso.get().load(image_uri).into(image_preview);
                }
            });
}