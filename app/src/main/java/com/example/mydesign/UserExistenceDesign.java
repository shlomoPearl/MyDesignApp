package com.example.mydesign;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.storage.UploadTask;

public class UserExistenceDesign extends AppCompatActivity {
    private Button upload_btn;
    private Uri imageUri;
    private static final int IMAGE_REQUEST = 2;
    ArrayList<String> image_list;
    RecyclerView recyclerView;
    StorageReference root;
    ProgressBar progressBar;
    ImageDisplayExistence image_display;
    // clothes_uploads is the brunch that all image are upload there
    StorageReference listRef = FirebaseStorage.getInstance().getReference().child("clothes_uploads");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_existence_design);
        image_list = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerview1);
        image_display = new ImageDisplayExistence(image_list, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(null));
        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);
//        final ProgressDialog uploading = new ProgressDialog(this);
//        uploading.show();

//        upload_btn = findViewById(R.id.SelectImage1);
//
//        upload_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openImage();
//            }
//        });

        listRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference file : listResult.getItems()) {
                    file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            image_list.add(uri.toString());
                            Log.e("Item_value", uri.toString());
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            recyclerView.setAdapter(image_display);
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
    }

    /**
     * to upload image from gallery to firebase account
     */

    private void openImage() {
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
                    imageUri = data.getData();
                    uploadImage();
                }
            });


    private void uploadImage() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();
        if (imageUri != null) {                                                                                            // the name of the image is the time + type extension
            final StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("clothes_uploads").child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            fileRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            Log.d("DownloadUrl", url);
                            pd.cancel();
                            Toast.makeText(UserExistenceDesign.this, "Image upload successful", Toast.LENGTH_SHORT).show();

                            // to display the new image
                            listRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                                @Override
                                public void onSuccess(ListResult listResult) {
                                    for (StorageReference file : listResult.getItems()) {
                                        file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                // add only the new one
                                                if (!image_list.contains(uri.toString())) {
                                                    image_list.add(uri.toString());
                                                    Log.e("Item_value", uri.toString());
                                                }
                                            }
                                        }).addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                recyclerView.setAdapter(image_display);
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    });
                }
            });
        }
    }

    private String getFileExtension(Uri imageUri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }
}