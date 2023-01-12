package com.example.mydesign;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mydesign.menu.UserMenu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class UserDesign extends UserMenu {
    private Button design;
    private Button send;
    private EditText file_name;
    private EditText bid;
    private EditText product_description;
    private ProgressBar progress_bar;
    private ImageView image_preview;
    private Uri image_uri;
    private EditText prompt;
    private Context context;
    private String dalle_url;
    private String storage_url;
    private UploadTask uploadTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_design);
        design = findViewById(R.id.choose_file);
        image_preview = findViewById(R.id.image_view);
        file_name = findViewById(R.id.file_name);
        bid = findViewById(R.id.bid);
        product_description = findViewById(R.id.description);
        progress_bar = findViewById(R.id.progress_bar);
        context = this;

        design.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // setup the alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("DALL-E 2");
                builder.setMessage("Let's start making your design");
                prompt = new EditText(context);
                prompt.setHint("Enter your prompt");
                builder.setView(prompt);
                // add the buttons
                builder.setPositiveButton("Generate",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String promptText = prompt.getText().toString();
                        if (promptText.isEmpty()) {
                            Toast.makeText(context, "Please enter a prompt", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        progress_bar.setVisibility(View.VISIBLE);
                        OpenAi openAi = new OpenAi(promptText);
                        openAi.setOnUrlGeneratedListener(new OnUrlGeneratedListener() {
                            @Override
                            public void onUrlGenerated(String url) {
                                Log.d("URL",url);
                                Picasso.get().load(url).into(image_preview);
                                progress_bar.setVisibility(View.GONE);
                                dalle_url = url;
                                image_uri = Uri.parse(url);
                            }
                        });
                    }
                });
                builder.setNegativeButton("Cancel", null);

                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        send = findViewById(R.id.upload);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (image_uri == null || file_name.getText().toString().isEmpty() || bid.getText().toString().isEmpty()
                        || product_description.getText().toString().isEmpty()) {
                    Toast.makeText(UserDesign.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    uploadImage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void setStorageUrl(String new_url) {
        storage_url = new_url;
    }

    public String getStorageUrl() {
        return this.storage_url;
    }
    private void uploadImage() throws IOException {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        uploadImageToStorage(pd);
//        uploadOrderToFirestore(pd);
    }

    private void uploadOrderToFirestore(ProgressDialog pd) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
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
                                    design_info.put("Image URL", getStorageUrl());
                                    design_info.put("User ID", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    design_info.put("User Name", user_name);
                                    design_info.put("Name", name);
                                    design_info.put("Email", email);
                                    design_info.put("Order State", "false");
                                    db.collection("User Design").add(design_info);

                                    db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .collection("Design").add(design_info);
                                    pd.cancel();
                                    Toast.makeText(UserDesign.this, "Design send Successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(UserDesign.this, UserDesign.class));
                                } else {
                                    Toast.makeText(UserDesign.this, "No such document", Toast.LENGTH_SHORT).show();
                                    pd.cancel();
                                }
                            } else {
                                Toast.makeText(UserDesign.this, "Error try again", Toast.LENGTH_SHORT).show();
                                pd.cancel();
                            }
                        }
                    });

                } else {
                    Toast.makeText(UserDesign.this, "Error try again", Toast.LENGTH_SHORT).show();
                    pd.cancel();
                }
            }
        });
    }

    // upload image to firebase storage and get the url
    private void uploadImageToStorage(ProgressDialog pd) throws IOException {
        pd.show();

        // upload image
        StorageReference storage = FirebaseStorage.getInstance().
                getReference().child("User Design").child(System.currentTimeMillis() + "");
        Picasso.get().load(dalle_url).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                uploadTask = storage.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        pd.dismiss();
                        Toast.makeText(UserDesign.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                    }
                }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    setStorageUrl(uri.toString());
                                    uploadOrderToFirestore(pd);
                                    System.out.println(getStorageUrl());
                                }
                            });
                        }
                    }
                });
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                // Handle failed download
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });
    }



//        storage.putFile(image_uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                if (task.isSuccessful()) {
//                    storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//                            String url = uri.toString();
//                            Log.d("DownloadUrl", url);
//                            db.collection("User Design").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                @Override
//                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                    if (task.isSuccessful()) {
//                                        String f_n = file_name.getText().toString();
//                                        String b = bid.getText().toString();
//                                        String p_d = product_description.getText().toString();
//                                        DocumentReference df = db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
//                                        df.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                            @Override
//                                            public void onComplete(@androidx.annotation.NonNull Task<DocumentSnapshot> task) {
//                                                if (task.isSuccessful()) {
//                                                    DocumentSnapshot document = task.getResult();
//                                                    if (document.exists()) {
//                                                        String name = document.getString("Name");
//                                                        String email = document.getString("Email");
//                                                        String user_name = document.getString("User Name");
//                                                        HashMap<String, Object> design_info = new HashMap<>();
//                                                        design_info.put("File Name", f_n);
//                                                        design_info.put("Bid", b);
//                                                        design_info.put("Product Description", p_d);
//                                                        design_info.put("Image URL", url);
//                                                        design_info.put("User ID", FirebaseAuth.getInstance().getCurrentUser().getUid());
//                                                        design_info.put("User Name", user_name);
//                                                        design_info.put("Name", name);
//                                                        design_info.put("Email", email);
//                                                        db.collection("User Design").add(design_info);
//                                                        pd.cancel();
//                                                        Toast.makeText(UserDesign.this, "Design send Successfully", Toast.LENGTH_SHORT).show();
//                                                        startActivity(new Intent(UserDesign.this, UserDesign.class));
//                                                    } else {
//                                                        Log.d(TAG, "No such document");
//                                                    }
//                                                } else {
//                                                    Log.d(TAG, "get failed with ", task.getException());
//                                                }
//                                            }
//                                        });
//
//                                    } else {
//                                        Log.d(TAG, "Error getting documents: ", task.getException());
//                                    }
//                                }
//                            });
//
//                        }
//                    });
//                } else {
//                    pd.dismiss();
//                    Toast.makeText(UserDesign.this, "Upload failed", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });


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
