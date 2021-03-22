package com.banyan.naajilshop.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.banyan.naajilshop.R;
import com.banyan.naajilshop.activity.ActivityProductDetails;
import com.banyan.naajilshop.activity.Activity_Order_Online;
import com.banyan.naajilshop.activity.MainActivity;
import com.banyan.naajilshop.model.Product_Model;
import com.banyan.naajilshop.utils.Sessiondata;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private List<Product_Model> prod_list;
    private Context mContext;
    private Activity activity;
    private int i = 0 ;
    public static RequestQueue queue;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title, spec, price, mrp, offer;
        public ImageView img;
        public Button IcAdd;

        public MyViewHolder(View view) {
            super(view);

            img = (ImageView) view.findViewById(R.id.thumbnail);
            IcAdd = (Button) view.findViewById(R.id.ic_add);

            title = (TextView) view.findViewById(R.id.name);
            spec = (TextView) view.findViewById(R.id.spec);
            price = (TextView) view.findViewById(R.id.price);
            mrp = (TextView) view.findViewById(R.id.mrp);
            offer = (TextView) view.findViewById(R.id.offer);
        }
    }


    public ProductAdapter(Activity activity, Context context, List<Product_Model> prod_list) {
        this.prod_list = prod_list;
        this.mContext = context;
        this.activity = activity;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
       final Product_Model movie = prod_list.get(position);

            holder.title.setText(movie.getName());
            holder.spec.setText(movie.getCategory_id() + " Grams");
            holder.price.setText(mContext.getString(R.string.Rs) + movie.getDiscount_price());

        if(movie.getDiscount_price().equals(movie.getOriginal_price())){
            holder.mrp.setVisibility(View.GONE);
        }else{
            holder.mrp.setText("MRP " +mContext.getString(R.string.Rs)+ movie.getOriginal_price());

        }

        if(movie.getDiscount_percentage().equals("0")){
            holder.offer.setVisibility(View.GONE);
        }else{
            holder.offer.setText("Off " +movie.getDiscount_percentage() + "%");

        }

            String str_img_path = movie.getImage();

            holder.mrp.setPaintFlags(holder.mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            if (str_img_path.equals("")){

            }else {

                System.out.println("IMG : : " + str_img_path);
                Glide.with(mContext).load(str_img_path)
                        .thumbnail(0.5f)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.img);
            }





        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String product_id, name, description, original_price, discount_price, discount_percent,  stock, stockstatus, image,
                        capacity, category_id;
                ArrayList<String>imagePath;

                product_id = movie.getProduct_id();
                name = movie.getName();
                description = movie.getDescription();
                original_price = movie.getOriginal_price();
                discount_price = movie.getDiscount_price();
                discount_percent = movie.getDiscount_percentage();
                stock = movie.getStock();
                stockstatus = movie.getStockstatus();
                image = movie.getImage();
                capacity = movie.getCapacity();
                category_id = movie.getCategory_id();
                imagePath=movie.getImagePath();

                SharedPreferences sharedPreferences = PreferenceManager
                        .getDefaultSharedPreferences(mContext);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("product_id", product_id);
                editor.putString("name", name);
                editor.putString("description", description);
                editor.putString("original_price", original_price);
                editor.putString("discount_price", discount_price);
                editor.putString("discount_percent", discount_percent);
                editor.putString("stock", stock);
              //  editor.putString("stockstatus", stockstatus);
                editor.putString("image", image);
                editor.putString("capacity", capacity);

                Sessiondata.getInstance().setName(name);
                Sessiondata.getInstance().setDescription(description);
                Sessiondata.getInstance().setOriginalPrice(original_price);
                Sessiondata.getInstance().setDiscontPrice(discount_price);
                Sessiondata.getInstance().setStock(stock);
                Sessiondata.getInstance().setImage(image);
                Sessiondata.getInstance().setCategoryId(category_id);
                Sessiondata.getInstance().setProductId(product_id);
                Sessiondata.getInstance().setSubimage(imagePath);
                Sessiondata.getInstance().setDiscountPercentage(discount_percent);

                System.out.println(imagePath+"pa imagePath");


//                editor.putString("category_id", category_id);

                editor.commit();

                activity.startActivity(new Intent(activity, ActivityProductDetails.class));
            }
        });

        holder.IcAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_product_id = movie.getProduct_id();
                System.out.println("str_product_id : " + str_product_id);
                queue = Volley.newRequestQueue(mContext);
                Activity_Order_Online.Function_Add_Cart(mContext, activity, str_product_id);

            }
        });

    }

    @Override
    public int getItemCount() {
        return prod_list.size();
    }
}
