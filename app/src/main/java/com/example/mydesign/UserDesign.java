package com.example.mydesign;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class UserDesign extends AppCompatActivity {
    private Button design;
    private Button send;
    private EditText file_name;
    private EditText bid;
    private EditText product_description;
    private ImageView image_preview;
    private Uri image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_design);
        design = findViewById(R.id.choose_file);
        image_preview = findViewById(R.id.image_view);
        file_name = findViewById(R.id.file_name);
        bid = findViewById(R.id.bid);
        product_description = findViewById(R.id.description);
        design.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChoose();
            }
        });
        send = findViewById(R.id.upload);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

    }
    private void uploadImage() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();
        // upload image
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final StorageReference storage = FirebaseStorage.getInstance().
                getReference().child("User Design").child(System.currentTimeMillis() + "");
        storage.putFile(image_uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            Log.d("DownloadUrl", url);
                            db.collection("User Design").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        String f_n = file_name.getText().toString();
                                        String b = bid.getText().toString();
                                        String p_d = product_description.getText().toString();
                                        DocumentReference df = db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                        df.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@androidx.annotation.NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()) {
                                                        String name = document.getString("Name");
                                                        String email = document.getString("Email");
                                                        String user_name = document.getString("User Name");
                                                        HashMap<String, Object> design_info = new HashMap<>();
                                                        design_info.put("File Name", f_n);
                                                        design_info.put("Bid", b);
                                                        design_info.put("Product Description", p_d);
                                                        design_info.put("Image URL", url);
                                                        design_info.put("User ID", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                        design_info.put("User Name", user_name);
                                                        design_info.put("Name", name);
                                                        design_info.put("Email", email);
                                                        db.collection("User Design").add(design_info);
                                                        pd.cancel();
                                                        Toast.makeText(UserDesign.this, "Design send Successfully", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(UserDesign.this, UserDesign.class));
                                                    } else {
                                                        Log.d(TAG, "No such document");
                                                    }
                                                } else {
                                                    Log.d(TAG, "get failed with ", task.getException());
                                                }
                                            }
                                        });

                                    } else {
                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                    }
                                }
                            });

                        }
                    });
                } else {
                    pd.dismiss();
                    Toast.makeText(UserDesign.this, "Upload failed", Toast.LENGTH_SHORT).show();
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
