package com.banyan.naajilshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.banyan.naajilshop.R;
import com.banyan.naajilshop.activity.Activity_Order_Online;
import com.banyan.naajilshop.activity.MainActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.HashMap;
import java.util.List;

public class CategoryAdapter extends  RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    ImageView image;
    private List<HashMap<String, String>> categoryList;
    Context context;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView name;

        public MyViewHolder(View itemView) {
            super(itemView);

            // get the reference of item view's
            name = (TextView) itemView.findViewById(R.id.category_txt_name);
            image = (ImageView) itemView.findViewById(R.id.category_img_thumbnail);
        }
    }
    public CategoryAdapter(Context context, List<HashMap<String, String>> categoryList) {
        this.categoryList = categoryList;
        this.context=context;

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.child_layout_category, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        HashMap<String, String> movie = categoryList.get(position);

        String str_categoryId = movie.get(MainActivity.TAG_CATEGORY_ID);
        String str_categoryImage = movie.get(MainActivity.TAG_CATEGORY_IMAGE);
        String str_categoryName = movie.get(MainActivity.TAG_CATEGORY_NAME);
        Log.d(MainActivity.TAG_CATEGORY_ID, "categoryId"+str_categoryId);
        Log.d(MainActivity.TAG_CATEGORY_NAME, "categoryName"+str_categoryName);


        holder.name.setText(str_categoryName);
        if (str_categoryImage.equals("")){

        }else {

            System.out.println("IMG : : " + str_categoryImage);
            Glide.with(context).load(str_categoryImage)
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.nanjil)
                    .into(image);

        }
        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Selected_Category_id", str_categoryId);
                editor.commit();

                Intent intent = new Intent(context, Activity_Order_Online.class);
                context.startActivity(intent); // start Intent
            }
        });
    }




    @Override
    public int getItemCount() {
        return categoryList.size();
    }

}
