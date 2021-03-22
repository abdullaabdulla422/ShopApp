package com.banyan.naajilshop.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.banyan.naajilshop.R;
import com.banyan.naajilshop.adapter.CartAdapter1;
import com.banyan.naajilshop.global.AppConfig;
import com.banyan.naajilshop.model.Cart_Model;
import com.banyan.naajilshop.utils.SessionManager;
import com.google.android.material.snackbar.Snackbar;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;

import static com.banyan.naajilshop.activity.Activity_Order_Online.public_parent_view;
import static com.banyan.naajilshop.global.AppConfig.URL_REMOVE_PRODUCT_CART;

public class Activity_Cart extends AppCompatActivity {

    public static final String TAG_KEY = "key";
    public static final String TAG_PRODUCT_ID = "product_id";
    public static final String TAG_NAME = "name";
    public static final String TAG_DESCRIPTION = "description";
    public static final String TAG_ORIGINAL_PRICE = "original_price";
    public static final String TAG_DISCOUNT_PRICE = "discount_price";
    public static final String TAG_DISCOUNT_PERCENTAGE = "discount_percentage";
    public static final String TAG_QTY = "qty";
    public static final String TAG_ALLOW_QTY = "allowed_qty";
    public static final String TAG_ALLOW_BACK_ORDER_QTY = "allow_back_order";
    public static final String TAG_CAPACITY = "capacity";
    public static final String TAG_IMAGE = "image";

    private Toolbar mToolbar;
    public static TextView text_view_to_be_paid, text_view_mrp_price, text_view_price_discount;
    private RecyclerView cart_recyler;
    public static List<Cart_Model> cart_list = new ArrayList<>();
    private CartAdapter1 cart_adapter;
    private Button btn_checkout;

    private RequestQueue queue;

    private SpotsDialog dialog;

    private SessionManager session;

    private String str_user_name, str_user_id, str_user_type;

    public static float float_total_amount, float_org_total_amount, float_prod_weight = 0;

    private CartAdapter1 cartAdapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        /*****************************
         *  SESSION
         *****************************/

        session = new SessionManager(Activity_Cart.this);
        session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // name
        str_user_name = user.get(SessionManager.KEY_NAME);
        str_user_id = user.get(SessionManager.KEY_USER_ID);

        /*****************************
         *  FIND VIEW BY ID
         *****************************/

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Cart");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        text_view_mrp_price = (TextView) findViewById(R.id.text_view_mrp_price);
        text_view_price_discount = (TextView) findViewById(R.id.text_view_price_discount);
        text_view_to_be_paid = (TextView) findViewById(R.id.text_view_to_be_paid);

        cart_recyler = (RecyclerView) findViewById(R.id.cart_recycler_view_productlist);
        btn_checkout = (Button) findViewById(R.id.btn_checkout);

        /*****************************
         *  GET DATA
         *****************************/

        dialog = new SpotsDialog(this);
        dialog.show();
        cart_list.clear();
        queue = Volley.newRequestQueue(Activity_Cart.this);
        Function_Get_Cart();

        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (text_view_to_be_paid.getText().toString().equals("0.00")) {
                    TastyToast.makeText(Activity_Cart.this, "empty order", TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                } else {
                    Intent i = new Intent(getApplicationContext(), Activity_CheckOut.class);
                    i.putExtra("prod_weight", float_prod_weight);
                    i.putExtra("mrp_price", text_view_mrp_price.getText().toString());
                    i.putExtra("discount_price", text_view_price_discount.getText().toString());
                    i.putExtra("tobepaid", text_view_to_be_paid.getText().toString());
                    startActivity(i);

                    SharedPreferences sharedPreferences_filter = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor_filter = sharedPreferences_filter.edit();
                    //FILTER TYPE
                    sharedPreferences_filter.edit().remove("noti_count").commit();
                }


            }
        });
    }


    /*****************
     * GET REQ
     ***************/

    public void Function_Get_Cart() {

        System.out.println("### AppConfig.URL_CART_LIST " + AppConfig.URL_CART_LIST);

        String tag_json_obj = "json_obj_req";

        System.out.println("CAME Enquiry" + AppConfig.URL_CART_LIST);

        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_CART_LIST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                System.out.println("### AppConfig.URL_CART_LIST response " + response);

                Log.d("TAG", response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");

                    if (success == 1) {

                        JSONArray arr;

                        arr = obj.getJSONArray("records");

                        for (int i = 0; arr.length() > i; i++) {
                            JSONObject obj1 = arr.getJSONObject(i);

                            String key = obj1.getString("key");
                            String product_id = obj1.getString("product_id");
                            String name = obj1.getString("name");
                            String description = obj1.getString("description");
                            String original_price = obj1.getString("original_price");
                            String discount_percentage = obj1.getString("discount_percentage");
                            String discount_price = obj1.getString("discount_price");
                            String qty = obj1.getString("qty");
                            String total_price = obj1.getString("total_price");
                            String product_total_price = obj1.getString("product_total_price");
                            String allow_quality = obj1.getString("allowed_qty");
                            String capacity = obj1.getString("capacity");
                            String weight = obj1.getString("weight");
                            String image = obj1.getString("image");

                            System.out.println(" GET QUTY : " + qty);
                            System.out.println(" GET TOT PRICE : " + total_price);

                            cart_list.add(new Cart_Model(key, product_id, name, description, original_price, discount_price, discount_percentage
                                    , qty, total_price, product_total_price, allow_quality, "", capacity, weight, image));
                        }
                        cart_adapter = new CartAdapter1(Activity_Cart.this, getApplicationContext(), cart_list);
                        cart_recyler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                        cart_recyler.setAdapter(cart_adapter);

                        cart_adapter.notifyDataSetChanged();

                        Func_Total_calc();

                    } else if (success == 0) {

                        dialog.dismiss();

                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                dialog.dismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // stopping swipe refresh
                dialog.dismiss();

                System.out.println("### AppConfig.URL_CART_LIST onerror");
                if (error != null)
                    System.out.println("### AppConfig.URL_CART_LIST onerror" + error.getLocalizedMessage());

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id", str_user_id);

                System.out.println("### AppConfig.URL_CART_LIST user_id " + str_user_id);

                return params;
            }

        };

        // Adding request to request queue
        queue.add(request);
    }

    public static void Func_Total_calc() {

        System.out.println("FUN CALLED");

        if (cart_list.size() != 0) {

            float_total_amount = 0;
            float_org_total_amount = 0;
            float_prod_weight = 0;

            for (int i = 0; i < cart_list.size(); i++) {
                Cart_Model prod = cart_list.get(i);
                String str_price = prod.getTotal_price();
                String str_mrp = prod.getProduct_total_price();
                String weight = prod.getWeight();

                System.out.println("Price : " + str_price);

                Float price = Float.parseFloat(str_price);
                float_total_amount = float_total_amount + price;
                int a = Math.round(float_total_amount);

                Float org_price = Float.parseFloat(str_mrp);
                float_org_total_amount = float_org_total_amount + org_price;
                int org_a = Math.round(float_org_total_amount);

                float float_discount = float_org_total_amount - float_total_amount;
                int org_discount = Math.round(float_discount);

                float float_weight = Float.parseFloat(weight);
                float_prod_weight = float_prod_weight + float_weight;


                System.out.println("Total Amount : " + a);
                System.out.println("Total org_ Amount : " + org_a);
                System.out.println("Total Discount Amount : " + org_discount);

                text_view_to_be_paid.setText("" + a);
                text_view_mrp_price.setText("" + float_org_total_amount);
                text_view_price_discount.setText("" + org_discount);

            }
        } else {

        }
    }

    /***********************************
     *  Update To Cart
     * ***********************************/
    public void Function_Update_Cart(final Context context, final String str_key, final String str_user_id, final RequestQueue queue,
                                     final CartAdapter1.MyViewHolder holder, final int int_qty, final float float_price,
                                     final String str_type) {

        dialog = new SpotsDialog(Activity_Cart.this);
        dialog.show();
        System.out.println("### Function_Update_Cart ");

        try {

            StringRequest request = new StringRequest(Request.Method.POST,
                    AppConfig.URL_UPDATE_CART_LIST, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject obj = new JSONObject(response);

                        System.out.println(" RESPONSE OBJ " + obj);

                        System.out.println(" JSON : " + obj);
                        int success = obj.getInt("status");

                        if (success == 1) {

                            cart_adapter.Function_Update_Update_Cart(holder, str_type, int_qty, float_price);


                            Snackbar snackbar = Snackbar
                                    .make(public_parent_view, "Cart Updated Successfully", Snackbar.LENGTH_LONG);

                            snackbar.show();
                            dialog.dismiss();

                        } else {
                            Snackbar snackbar = Snackbar
                                    .make(public_parent_view, "Cart Not Updated Successfully", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            dialog.dismiss();
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    dialog.dismiss();
                    System.out.println("### Function_Update_Cart onError");

                    Snackbar.make(public_parent_view, "Error : " + error, Snackbar.LENGTH_SHORT).show();
                }
            }) {


                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    int final_qty = int_qty + 1;
                    String str_qty = "" + final_qty;

                    params.put("user_id", str_user_id);
                    params.put("key", str_key);
                    params.put("qty", str_qty);

                    System.out.println("### Function_Update_Cart PARAMS user_id " + str_user_id);
                    System.out.println("### PARAMS key " + str_key);
                    System.out.println("### PARAMS qty " + str_qty);

                    // return checkParams(params);
                    return params;
                }

                private Map<String, String> checkParams(Map<String, String> map) {
                    Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
                        if (pairs.getValue() == null) {
                            map.put(pairs.getKey(), "");
                        }
                    }
                    return map;
                }
            };

            int socketTimeout = 60000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy);
            // Adding request to request queue
            queue.add(request);

        } catch (Exception e) {

            System.out.println("ERROR : " + e);
        }
    }

    /***********************************
     *  Update To Cart
     * ***********************************/

    public void Function_Remove_Product_Cart(final String str_key, final RequestQueue queue1) {

        dialog = new SpotsDialog(Activity_Cart.this);
        dialog.show();
        try {
            StringRequest request = new StringRequest(Request.Method.POST,
                    URL_REMOVE_PRODUCT_CART, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {

                    System.out.println("RESPONSE : " + response.toString());

                    try {
                        JSONObject obj = new JSONObject(response);

                        int success = obj.getInt("status");

                        if (success == 1) {

                            dialog.dismiss();
                            TastyToast.makeText(Activity_Cart.this, "Product Removed From Cart", TastyToast.LENGTH_SHORT, TastyToast.ERROR);


                            dialog = new SpotsDialog(Activity_Cart.this);
                            dialog.show();
                            cart_list.clear();
                            cart_adapter.notifyDataSetChanged();
                            queue = Volley.newRequestQueue(Activity_Cart.this);
                            Function_Get_Cart();

                        } else {
                            dialog.dismiss();
                        }

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    System.out.println("### Function_Remove_Product_Cart onError");
                    dialog.dismiss();
                    Snackbar.make(public_parent_view, "Error : " + error, Snackbar.LENGTH_SHORT).show();
                }
            }) {


                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("user_id", str_user_id);
                    params.put("key", str_key);

                    System.out.println("### Function_Remove_Product_Cart PARAMS user_id " + str_user_id);
                    System.out.println("### key " + str_key);

                    // return checkParams(params);
                    return params;
                }

                private Map<String, String> checkParams(Map<String, String> map) {
                    Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
                        if (pairs.getValue() == null) {
                            map.put(pairs.getKey(), "");
                        }
                    }
                    return map;
                }
            };

            int socketTimeout = 60000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy);
            // Adding request to request queue
            queue1.add(request);

        } catch (Exception e) {

            System.out.println("ERROR : " + e);
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Activity_Cart.this, MainActivity.class);
        startActivity(i);
    }

}
