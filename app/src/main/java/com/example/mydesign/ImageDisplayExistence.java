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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ImageDisplayExistence extends RecyclerView.Adapter<ImageDisplayExistence.ViewHolder> {
    private ArrayList<String> imageList;
    private ArrayList<String> id_list;
    private ArrayList<Integer> price_list;
    public Context context;
    private String[] quantity_table = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    private String[] size_table = {"S", "M", "L", "XL", "XXL"};
    private String size;
    private FirebaseFirestore store = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private HashMap<String, Object> user_order = new HashMap<String, Object>(){{
        put("Supplier ID", null);
        put("Supplier Phone", null);
        put("Supplier Name", null);
        put("Supplier Email", null);
        put("Supplier Address", null);
        put("Quantity", null);
        put("Size", null);
        put("Total Price", null);
        put("Order State", null);
        put("URL", null);
    }};
    private HashMap<String, Object> supplier_order = new HashMap<String, Object>(){{
        put("User ID", user.getUid());
        put("User Name", null);
        put("Name", null);
        put("User Email", user.getEmail());
        put("Quantity", null);
        put("Size", null);
        put("Total Price", null);
        put("Order State", null);
        put("URL", null);
    }};

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
                                        int total_price = price_list.get(position)
                                                        * Integer.parseInt(quantity);
                                        confirmOrder(imageList.get(position), id_list.get(position)
                                                , size, quantity, total_price);
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
    public void confirmOrder(String url, String supplier_id ,String size, String quantity, int total_price){
        /**
         get user info and put into supplier_order hashmap this upload to supplier order
         insert those field - "UserName", "Name", "User ID", "User Email", "Price"
         "Quantity", "Size", "Total Price", "Order State", "URL"
         **/
        store.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if(document.getId().equals(user.getUid())){

                            String name = document.get("Name").toString();
                            String user_name = document.get("User Name").toString();
                            supplier_order.put("Name", name);
                            supplier_order.put("User Name", user_name);
                            supplier_order.put("SIZE", size);
                            supplier_order.put("Quantity", quantity);
                            supplier_order.put("URL", url);
                            supplier_order.put("Total Price", total_price);
                            supplier_order.put("Order State", "false");
                            store.collection("Admins").document(supplier_id)
                                    .collection("Order").add(supplier_order);

                            /**
                             * get supplier info and put into user_order hashmap. this upload to user order
                             * insert those field - "Supplier ID", "Supplier Phone", "Supplier Name","Supplier Email",
                             *                      "Supplier Address","Total Price", "Quantity", "Size",
                             *                      "Total Price", "Order State", "URL"
                             **/
                            store.collection("Admins").document(supplier_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document =  task.getResult();
                                        String supplier_name = document.get("Company Name").toString();
                                        String supplier_phone = document.get("Phone Number").toString();
                                        String supplier_email = document.get("Email").toString();
                                        String supplier_address = document.get("Address").toString();
                                        user_order.put("Supplier ID", supplier_id);
                                        user_order.put("SIZE", size);
                                        user_order.put("Quantity", quantity);
                                        user_order.put("URL", url);
                                        user_order.put("Total Price", total_price);
                                        user_order.put("Order State", "false");
                                        user_order.put("Supplier Phone", supplier_phone);
                                        user_order.put("Supplier Name", supplier_name);
                                        user_order.put("Supplier Email", supplier_email);
                                        user_order.put("Supplier Address", supplier_address);
                                        store.collection("Users").document(user.getUid())
                                                .collection("Orders").add(user_order)
                                                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(context, " Your order has been placed", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(context, "Order Failed", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

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

    public ImageDisplayExistence(ArrayList<String> imageList,ArrayList<String> id_list, ArrayList<Integer> price_list,Context context) {
        this.imageList = imageList;
        this.price_list = price_list;
        this.id_list = id_list;
        this.context = context;
    }



}
