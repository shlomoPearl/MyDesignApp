package com.example.mydesign;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class SupplierOrderDisplay extends RecyclerView.Adapter<SupplierOrderDisplay.ViewHolder> {
    private ArrayList<String> imageList;
    private FirebaseFirestore store;
    private ArrayList<String[]> info;
    public Context context;

    public SupplierOrderDisplay(ArrayList<String> imageList, ArrayList<String[]> info, Context context) {
        this.imageList = imageList;
        this.info = info;
        this.context = context;
        this.store = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public SupplierOrderDisplay.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.supplier_order_display,parent,false);
        return new SupplierOrderDisplay.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SupplierOrderDisplay.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.email.setText(info.get(position)[0]);
        holder.name.setText(info.get(position)[1]);
        holder.user_name.setText(info.get(position)[2]);
        holder.size.setText(info.get(position)[3]);
        Glide.with(holder.itemView.getContext()).load(imageList.get(position)).into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // show dialog  message
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Ordering decision");
                builder.setMessage("Would you like to confirm or decline the order?");
                // add the buttons
                builder.setNegativeButton("Decline", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        deleteFromStorage(imageList.get(position));
                        deleteFromFireStore(imageList.get(position));
                        imageList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, imageList.size());
                    }
                });
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do something like...
                    }
                });
                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void deleteFromFireStore(String url) {
        store.collection("Orders").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // for each document
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if(document.get("URL").toString().equals(url)){
                            store.collection("Orders").document(document.getId()).delete();
                        }
                    }
                }
            }
        });
    }

    private void deleteFromStorage(String url) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(url);
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
                Log.e("firebasestorage", "onSuccess: deleted file");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
                Log.e("firebasestorage", "onFailure: did not delete file");
            }
        });
    }


    @Override
    public int getItemCount() {
        return imageList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView email;
        TextView name;
        TextView user_name;
        TextView size;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.item);
            email=itemView.findViewById(R.id.info_email);
            name=itemView.findViewById(R.id.info_name);
            user_name=itemView.findViewById(R.id.info_username);
            size=itemView.findViewById(R.id.info_size);
        }
    }

}
