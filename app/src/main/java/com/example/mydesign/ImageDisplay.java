package com.example.mydesign;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
//import com.anni.uploaddataexcelsheet.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class ImageDisplay extends RecyclerView.Adapter<ImageDisplay.ViewHolder> {
    private ArrayList<String> imageList;
    public Context context;

    @NonNull
    @Override
    public ImageDisplay.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_display,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageDisplay.ViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext()).load(imageList.get(position)).into(holder.imageView);
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
    public ImageDisplay(ArrayList<String> imageList, Context context) {
        this.imageList = imageList;
        this.context = context;
    }
}
