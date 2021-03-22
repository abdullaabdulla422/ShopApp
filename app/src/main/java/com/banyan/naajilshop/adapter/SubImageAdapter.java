package com.banyan.naajilshop.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.banyan.naajilshop.R;
import com.banyan.naajilshop.model.Product_SubImages_Model;
import com.banyan.naajilshop.utils.Sessiondata;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class SubImageAdapter extends RecyclerView.Adapter <SubImageAdapter.ViewHolder>{
    private ArrayList<String > subImage;
    private Context context;
    public SubImageAdapter(Context context, ArrayList<String > subImage) {
        super();
        this.context = context;
        this.subImage = subImage;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.sub_images_list_item, viewGroup, false);
        return new ViewHolder(v);
    }
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int pos) {
        String str_img_path= subImage.get(pos);
       System.out.println(str_img_path+"adapterImagePath");

        if (str_img_path.equals("")){

        }else {


            System.out.println("IMG : : " + str_img_path);
            Glide.with(context).load(str_img_path)
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(viewHolder.imgThumbnail);
        }
    }
    @Override
    public int getItemCount() {
        return subImage.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgThumbnail;

        ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = itemView.findViewById(R.id.imgThumbnail);

        }

    }
}