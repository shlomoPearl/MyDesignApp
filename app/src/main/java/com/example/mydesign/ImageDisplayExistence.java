package com.example.mydesign;
import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
//import com.anni.uploaddataexcelsheet.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ImageDisplayExistence extends RecyclerView.Adapter<ImageDisplayExistence.ViewHolder> {
    private ArrayList<String> imageList;
    public Context context;

    @NonNull
    @Override
    public ImageDisplayExistence.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_display_exitence,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageDisplayExistence.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(holder.itemView.getContext()).load(imageList.get(position)).into(holder.imageView);
        Snackbar mySnackbar = Snackbar.make(holder.imageView, "choose an item", 1000);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // display a toast with person name on item click
//                Toast.makeText(context, "press on image num - "+ imageList.get(position), Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Make Order:");
                builder.setMessage("Would you like to continue to place your order?");
                // add the buttons
//                builder.setPositiveButton("Continue", null);
                builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // do something like...
                        System.out.println("order was make:"+imageList.get(position));
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                        builder1.setTitle("Choose a size:");

                        // add a list
                        String[] size = {"small", "medium", "large", "x-large", "xx-large"};
                        final String[] count = {""};
                        builder1.setItems(size, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        count[0] = "small";// small
                                        System.out.println("small");

                                    case 1:
                                        count[0] = "medium";// medium
                                        System.out.println("medium");
                                    case 2: // large
                                        count[0] = "large";
                                        System.out.println("large");
                                    case 3: // x-large
                                        count[0] = "x-large";
                                        System.out.println("x-large");
                                    case 4: // xx-large
                                        count[0] = "xx-large";
                                        System.out.println("xx-large");
                                }

                            }
                        });
                        builder1.show();
                        upload_order(imageList.get(position), "small");
                    }
                });
                builder.setNegativeButton("Cancel", null);
                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
    }
    public static void upload_order(String url,String size){
        System.out.println("---------------");
        FirebaseFirestore store = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        store.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if(document.getId().equals(user.getUid())){
                            String email = document.get("Email").toString();
                            String name = document.get("Name").toString();
                            String user_name = document.get("User Name").toString();
                            DocumentReference df = store.collection("Orders").document(user.getUid());
                            Map<String, Object> user_info = new HashMap<>();
                            user_info.put("User Name",user_name);
                            user_info.put("Name",name);
                            user_info.put("Email",email);
                            user_info.put("URL",url);
                            user_info.put("SIZE","small");
//                            final StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("Orders");
//                            fileRef.putFile(Uri.parse(url));
                            df.set(user_info);
                        }
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });



    }


    @Override
    public int getItemCount() {
        return imageList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.item);
        }
    }

    public ImageDisplayExistence(ArrayList<String> imageList, Context context) {
        this.imageList = imageList;
        this.context = context;
    }



}
