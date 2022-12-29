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
    private final SelectItem selectItem;
    private ArrayList<String> imageList;
    public Context context;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_display,parent,false);
        return new ViewHolder(view, selectItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext()).load(imageList.get(position)).into(holder.imageView);
    }


    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(@NonNull View itemView , SelectItem selectItem) {
            super(itemView);
            imageView=itemView.findViewById(R.id.item);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selectItem != null){
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION){
                            selectItem.onItemClick(pos);
                        }
                    }

                }
            });
        }

    }
    public ImageDisplay(ArrayList<String> imageList, Context context, SelectItem selectItem) {
        this.imageList = imageList;
        this.context = context;
        this.selectItem = selectItem;
    }
}