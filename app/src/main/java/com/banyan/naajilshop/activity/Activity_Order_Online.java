package com.banyan.naajilshop.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.banyan.naajilshop.adapter.ProductAdapter;
import com.banyan.naajilshop.global.AppConfig;
import com.banyan.naajilshop.model.Product_Model;
import com.banyan.naajilshop.utils.SessionManager;
import com.banyan.naajilshop.utils.Sessiondata;
import com.google.android.material.snackbar.Snackbar;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;

import static com.banyan.naajilshop.global.AppConfig.URL_ADD_TO_CART;
import static com.banyan.naajilshop.global.AppConfig.URL_PRODUCT;

public class Activity_Order_Online extends AppCompatActivity {

    SpotsDialog dialog;

    public static RequestQueue queue;
    String TAG = "Products & Category";
    public static String PUBLIC_TAG = "Products & Category";
    private View parent_view;
    public static View public_parent_view;
    private Toolbar mToolbar;
    private RecyclerView category_recyler, product_recycler;

    private List<Product_Model> prod_list = new ArrayList<>();
    private ProductAdapter product_adapter;
    ArrayList<String> imagepath;


    // CART
    RelativeLayout notification_Count, notification_batch, message_Count, message_batch;
    public static TextView tv_notification, tv_message;
    int i = 0;
    public static int cart_count = 0;
    String value = "nothing";
    private String str_noti_count;

    private String str_category_id, str_district_id = "";
    private static String str_user_id = "";

    private AVLoadingIndicatorView loadingPB;
    private NestedScrollView nestedSV;
    int page = 0, limit = 10;

    SessionManager session;
    String subImage;
    private static int noti_count;
    static int notificationCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(Activity_Order_Online.this);
        str_noti_count = sharedPreferences.getString("noti_count", "noti_count");
        str_category_id = sharedPreferences.getString("Selected_Category_id", "Selected_Category_id");
        str_district_id = sharedPreferences.getString("Selected_City_id", "Selected_City_id");

        setContentView(R.layout.activity_online_store);

        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        str_user_id = user.get(SessionManager.KEY_USER_ID);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        product_recycler = (RecyclerView) findViewById(R.id.order_online_recycler_view_productlist);
        loadingPB = findViewById(R.id.idPBLoading);
        nestedSV = findViewById(R.id.idNestedSV);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Nanjil - Products");
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        parent_view = findViewById(android.R.id.content);
        public_parent_view = findViewById(android.R.id.content);

        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back));

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();

            }
        });

        prod_list.clear();
        queue = Volley.newRequestQueue(Activity_Order_Online.this);
        Function_Get_Product();

        queue = Volley.newRequestQueue(Activity_Order_Online.this);
        Function_Get_Cart();

        nestedSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    page = limit;
                    limit = limit + 10;
                    loadingPB.setVisibility(View.VISIBLE);
                    queue = Volley.newRequestQueue(Activity_Order_Online.this);
                    Function_Get_Product();
                }
            }
        });

    }


    /**********************************
     * Main Menu
     *********************************/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(Activity_Order_Online.this);
        str_noti_count = sharedPreferences.getString("noti_count", "noti_count");
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Notification Counter
        MenuItem item1 = menu.findItem(R.id.action_notification);
        MenuItemCompat.setActionView(item1, R.layout.toolbar_cart_update_count_layout);
        notification_Count = (RelativeLayout) MenuItemCompat.getActionView(item1);
        notification_batch = (RelativeLayout) MenuItemCompat.getActionView(item1);
        tv_notification = (TextView) notification_batch.findViewById(R.id.badge_notification);
        tv_notification.setVisibility(View.GONE);


        notification_batch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), Activity_Cart.class);
                startActivity(i);
            }
        });
        tv_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), Activity_Cart.class);
                startActivity(i);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_user) {

            Intent i = new Intent(getApplicationContext(), Activity_Profile.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }


    public static void Fun_Update_Cart(final Context context) {


        String str_noti_count = tv_notification.getText().toString().trim();

        if (!str_noti_count.isEmpty() && !str_noti_count.equals(null) && !str_noti_count.equals("")) {

            noti_count = Integer.parseInt(str_noti_count);

            noti_count = noti_count + 1;
            tv_notification.setText("" + noti_count);


            SharedPreferences sharedPreferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("noti_count", tv_notification.getText().toString());

            editor.commit();
        }
    }

    /*****************************
     * GET PRODUCT
     ***************/

    public void Function_Get_Product() {

        try {

            System.out.println("### URL_PRODUCT URL " + URL_PRODUCT);

            StringRequest request = new StringRequest(Request.Method.POST,
                    URL_PRODUCT, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response.toString());

                    if (page > limit) {
                        Toast.makeText(Activity_Order_Online.this, "That's all the data..", Toast.LENGTH_SHORT).show();
                        loadingPB.setVisibility(View.GONE);
                        return;
                    }

                    System.out.println("### URL_PRODUCT  RESPONSE " + response);

                    try {
                        JSONObject obj = new JSONObject(response);

                        System.out.println(" RESPONSE OBJ " + obj);

                        System.out.println(" JSON : " + obj);
                        int success = obj.getInt("status");

                        if (success == 1) {

                            JSONArray arr;

                            arr = obj.getJSONArray("records");

                            System.out.println(" ARR : " + arr);

                            for (int i = 0; arr.length() > i; i++) {

                                JSONObject obj1 = arr.getJSONObject(i);

                                String product_id = obj1.getString("product_id");
                                String name = obj1.getString("name");
                                String description = obj1.getString("description");
                                String original_price = obj1.getString("original_price");
                                String discount_price = obj1.getString("discount_price");
                                String discount_percentage = obj1.getString("discount_percentage");
                                String stock = obj1.getString("stock");
                                String capacity = obj1.getString("capacity");
                                String weight = obj1.getString("weight");
                                String image = obj1.getString("image");
                                /*String stockstatus = obj1.getString("stockstatus");
                                String category_id = obj1.getString("category_id");*/
                                JSONArray array = obj1.getJSONArray("sub_images");
                                for (int j = 0; array.length() > j; j++) {

                                    JSONObject object = array.getJSONObject(j);

                                    String imagepath1 = object.getString("image_path");
                                    imagepath = new ArrayList<String>();
                                    imagepath.add(imagepath1);

                                    System.out.println(imagepath + "imagepath");
                                }

                                prod_list.add(new Product_Model(product_id, name, description, original_price,
                                        discount_price, discount_percentage,
                                        stock, "", image, capacity, weight, "", "", imagepath));

                                loadingPB.setVisibility(View.GONE);


                            }

                        } else {
                            loadingPB.setVisibility(View.GONE);
                            Snackbar.make(parent_view, "That's IT", Snackbar.LENGTH_SHORT).show();

                        }

                        product_adapter = new ProductAdapter(Activity_Order_Online.this, getApplicationContext(), prod_list);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
                        product_recycler.setLayoutManager(gridLayoutManager);
                        product_recycler.setAdapter(product_adapter);

                        product_adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    loadingPB.setVisibility(View.GONE);
                    Snackbar.make(parent_view, "Error : " + error, Snackbar.LENGTH_SHORT).show();

                    System.out.println("### URL_PRODUCT onError");
                    if (error != null)
                        System.out.println("### URL_PRODUCT onError" + error.getLocalizedMessage());

                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("category_id", str_category_id);
                    params.put("district_id", str_district_id);
                    params.put("start", String.valueOf(page));
                    params.put("limit", String.valueOf(limit));

                    System.out.println("### URL_PRODUCT PARAMS category_id " + str_category_id);
                    System.out.println("### URL_PRODUCT PARAMS district_id " + str_district_id);
                    System.out.println("### PARAMS start " + String.valueOf(page));
                    System.out.println("### PARAMS limit " + String.valueOf(limit));

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
     *  ADD To Cart
     * ***********************************/
    public static void Function_Add_Cart(final Context context, final Activity activity, final String str_prod_id) {

        try {

            StringRequest request = new StringRequest(Request.Method.POST,
                    URL_ADD_TO_CART, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(PUBLIC_TAG, response.toString());

                    System.out.println(" RESPONSE " + PUBLIC_TAG);

                    try {
                        JSONObject obj = new JSONObject(response);

                        System.out.println(" RESPONSE OBJ " + obj);

                        System.out.println(" JSON : " + obj);
                        int success = obj.getInt("status");
                        if (success == 1) {
                            tv_notification.setVisibility(View.VISIBLE);

                            Fun_Update_Cart(context);


                        Snackbar snackbar = Snackbar
                                .make(public_parent_view, "Added to Cart Successfully", Snackbar.LENGTH_LONG)
                                .setAction("Go to Cart", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        // activity.startActivity(new Intent(activity, Activity_Cart.class));
                                    }
                                });

                        snackbar.show();


                    } else{


                    }
                } catch(
                JSONException e)

                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        },new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Snackbar.make(public_parent_view, "Error : " + error, Snackbar.LENGTH_SHORT).show();
            }
        }){


            @Override
            protected Map<String, String> getParams () {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id", str_user_id);
                params.put("product_id", str_prod_id);
                params.put("qty", "1");

                System.out.println("PARAMS user_id " + str_user_id);
                System.out.println("PARAMS product_id " + str_prod_id);
                System.out.println("PARAMS qty " + "1");

                // return checkParams(params);
                return params;
            }

            private Map<String, String> checkParams (Map < String, String > map){
                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
                    if (pairs.getValue() == null) {
                        map.put(pairs.getKey(), "");
                    }
                }
                return map;
            }
        } ;

        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        // Adding request to request queue
        queue.add(request);

    } catch(
    Exception e)

    {

        System.out.println("ERROR : " + e);
    }

}

    /*****************
     * GET Cart Count
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

//                        String str_cart_count = ""+arr.length();
                        if (arr.length() != 0) {
                            String str_cart_count = "" + arr.length();
                            tv_notification.setText("" + str_cart_count);
                            tv_notification.setVisibility(View.VISIBLE);
                            SharedPreferences sharedPreferences = PreferenceManager
                                    .getDefaultSharedPreferences(Activity_Order_Online.this);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("noti_count", str_cart_count);
                            editor.commit();
                        } else {
                            tv_notification.setVisibility(View.GONE);
                        }

//                        tv_notification.setText("" + str_cart_count);


                    } else if (success == 0) {


                    }


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // stopping swipe refresh
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


}