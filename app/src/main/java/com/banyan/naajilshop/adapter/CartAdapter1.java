package com.banyan.naajilshop.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.banyan.naajilshop.activity.Activity_Cart;
import com.banyan.naajilshop.model.Cart_Model;
import com.banyan.naajilshop.utils.SessionManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.HashMap;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class CartAdapter1 extends RecyclerView.Adapter<CartAdapter1.MyViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private List<Cart_Model> cart_list;
    private Context mContext;
    private Activity_Cart activity;
    private int i = 0 ;
    public static RequestQueue queue;
    private final SessionManager session;
    public static String str_user_id, str_key;
    public static SpotsDialog dialog;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title, spec, price, qty;
        public ImageView img, IcAdd, IcRemove;
        public Button btn_delete;

        public MyViewHolder(View view) {
            super(view);

            img = (ImageView) view.findViewById(R.id.cart_itemImage);
            IcAdd = (ImageView) view.findViewById(R.id.cart_ic_add);
            IcRemove = (ImageView) view.findViewById(R.id.cart_ic_remove);
            btn_delete = (Button) view.findViewById(R.id.cart_btn_remove);
            title = (TextView) view.findViewById(R.id.cart_name);
            spec = (TextView) view.findViewById(R.id.cart_spec);
            qty = (TextView) view.findViewById(R.id.cart_text_view_quantity);
            price = (TextView) view.findViewById(R.id.cart_total);
        }
    }


    public CartAdapter1(Activity_Cart activity_cart, Context context, List<Cart_Model> cart_list) {
        this.cart_list = cart_list;
        this.mContext = context;
        this.activity = activity_cart;

        session = new SessionManager(activity);
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        str_user_id = user.get(SessionManager.KEY_USER_ID);

        queue = Volley.newRequestQueue(activity);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder,final int position) {
        Cart_Model movie = cart_list.get(position);

        System.out.println("Discount Price : " + movie.getDDiscount_price());
        System.out.println("Total Price : " + movie.getTotal_price());

        holder.title.setText(movie.getName());
        holder.spec.setText(movie.getWeight() + " Grams");
        holder.qty.setText(movie.getQty());
        holder.price.setText(movie.getTotal_price());
        String str_img_path = movie.getimage();

        if (str_img_path.equals("")){

        }else {

            System.out.println("IMG : : " + str_img_path);
            Glide.with(mContext).load(str_img_path)
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.img);
        }

        int int_qty = Integer.parseInt(holder.qty.getText().toString());

        if (int_qty <= 1) {
            holder.IcRemove.setVisibility(View.GONE);
        } else {
            holder.IcRemove.setVisibility(View.VISIBLE);
        }

        holder.IcAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int int_qty = Integer.parseInt(holder.qty.getText().toString());

                if (int_qty <= 1) {
                    holder.IcRemove.setVisibility(View.GONE);
                } else {
                    holder.IcRemove.setVisibility(View.VISIBLE);
                }

                Cart_Model hero = cart_list.get(position);
                str_key = hero.getkey();
                String value = hero.getDDiscount_price();
                String product_id = hero.getProduct_id();

                float float_price = Float.valueOf(value);

                activity.Function_Update_Cart(
                        mContext, str_key, str_user_id,
                        queue, holder,int_qty, float_price, "ADD");


            }
        });

        holder.IcRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int int_qty = Integer.parseInt(holder.qty.getText().toString());

                if (int_qty <= 1) {
                    holder.IcRemove.setVisibility(View.GONE);
                } else {
                    holder.IcRemove.setVisibility(View.VISIBLE);
                }

                Cart_Model hero = cart_list.get(position);
                str_key = hero.getkey();
                String value = hero.getDDiscount_price();
                String product_id = hero.getProduct_id();

                float float_price = Float.valueOf(value);

                activity.Function_Update_Cart(
                        mContext, str_key, str_user_id,
                        queue, holder,int_qty, float_price, "REDUCE");


            }
        });

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cart_Model hero = cart_list.get(position);
                str_key = hero.getkey();

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Are you sure you want to delete this?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        activity.Function_Remove_Product_Cart(str_key, queue);
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                //creating and displaying the alert dialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return cart_list.size();
    }

    public void Function_Update_Update_Cart(MyViewHolder holder, String Str_Response, int int_qty1, float float_price) {

        System.out.println("RESPONSE :: " + Str_Response);
        System.out.println("QTY :: " + int_qty1);
        System.out.println("PRICE :: " + float_price);

        if (Str_Response.equals("ADD")) {

            int int_qty = int_qty1 + 1;
            holder.qty.setText("" + int_qty);

            String str_up_qty = holder.qty.getText().toString().trim();
            float float_qty = Float.valueOf(str_up_qty);
            float float_total = float_qty * float_price;
            holder.price.setText("" + float_total);

            if (int_qty <= 1) {
                holder.IcRemove.setVisibility(View.GONE);
            } else {
                holder.IcRemove.setVisibility(View.VISIBLE);
            }


        }else if (Str_Response.equals("REDUCE")){

            int int_qty = int_qty1 - 1;
            holder.qty.setText("" + int_qty);

            String str_up_qty = holder.qty.getText().toString().trim();
            float float_qty = Float.valueOf(str_up_qty);
            float float_total = float_qty * float_price;
            holder.price.setText("" + float_total);

            if (int_qty <= 1) {
                holder.IcRemove.setVisibility(View.GONE);
            } else {
                holder.IcRemove.setVisibility(View.VISIBLE);
            }
        }

        Activity_Cart.Func_Total_calc();
    }

}
