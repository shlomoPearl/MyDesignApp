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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
//import com.anni.uploaddataexcelsheet.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ImageDisplayExistence extends RecyclerView.Adapter<ImageDisplayExistence.ViewHolder> {
    private ArrayList<String> imageList;
    private ArrayList<String> id_list;
    public Context context;
    private String[] quantity_table = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    private String[] size_table = {"S", "M", "L", "XL", "XXL"};
    private String size;

    @NonNull
    @Override
    public ImageDisplayExistence.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_display_existence,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageDisplayExistence.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(holder.itemView.getContext()).load(imageList.get(position)).into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder order_dialog = new AlertDialog.Builder(context);
                order_dialog.setTitle("Make Order:");
                order_dialog.setMessage("Would you like to continue to place your order?");
                order_dialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder size_build = new AlertDialog.Builder(context);
                        size_build.setTitle("Choose a size:");
                        size_build.setItems(size_table, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                size = size_table[which];
                                AlertDialog.Builder quantity_build = new AlertDialog.Builder(context);
                                quantity_build.setTitle("Choose a quantity:");
                                quantity_build.setItems(quantity_table, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String quantity = quantity_table[which];
                                        confirmOrder(imageList.get(position), id_list.get(position)
                                                , size, quantity);
                                    }
                            });
                            AlertDialog quantity_dialog = quantity_build.create();
                            quantity_dialog.show();
                            }
                        });
                        AlertDialog size_dialog = size_build.create();
                        size_dialog.show();
                    }
                });
                order_dialog.setNegativeButton("Cancel", null);
                // create and show the alert dialog
                AlertDialog dialog = order_dialog.create();
                dialog.show();
            }
        });
    }
    public void confirmOrder(String url, String supplier_id ,String size, String quantity){
        FirebaseFirestore store = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        store.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if(document.getId().equals(user.getUid())){
                            // get user info
                            String email = document.get("Email").toString();
                            String name = document.get("Name").toString();
                            String user_name = document.get("User Name").toString();
                            // create collection Order
                            CollectionReference df = store.collection("Admins").
                                    document(supplier_id).collection("Order");
                            Map<String, Object> user_info = new HashMap<>();
                            user_info.put("User Name",user_name);
                            user_info.put("Name",name);
                            user_info.put("Email",email);
                            user_info.put("URL",url);
                            user_info.put("SIZE",size);
                            user_info.put("Quantity",quantity);
                            user_info.put("Order State","false");
                            // add all the order detail to Order collection with unique id
                            df.add(user_info).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(context, "Order has been placed successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, "Order has been placed unsuccessfully", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
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

    public ImageDisplayExistence(ArrayList<String> imageList,ArrayList<String> id_list, Context context) {
        this.imageList = imageList;
        this.id_list = id_list;
        this.context = context;
    }



}
