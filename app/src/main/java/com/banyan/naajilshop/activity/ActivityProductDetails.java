package com.banyan.naajilshop.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.banyan.naajilshop.R;
import com.banyan.naajilshop.adapter.SubImageAdapter;
import com.banyan.naajilshop.model.Product_SubImages_Model;
import com.banyan.naajilshop.utils.SessionManager;
import com.banyan.naajilshop.utils.Sessiondata;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.banyan.naajilshop.global.AppConfig.URL_ADD_TO_CART;

public class ActivityProductDetails extends AppCompatActivity {
    private TextView title, spec, price, mrp, offer, txtDescription;
    private ImageView img;
    private Button IcAdd;
    private Toolbar toolbar;
    private String str_product_id;
    public static RequestQueue queue;
    private View parent_view;
    public static View public_parent_view;
    private static String str_user_id = "";
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    SubImageAdapter adapter;
    ArrayList<Product_SubImages_Model> subImage=new ArrayList<>();
    ArrayList<String> imagePath=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        init();
        function();


    }

    private void init() {
        img = (ImageView) findViewById(R.id.thumbnail);
        IcAdd = (Button) findViewById(R.id.ic_add);
        txtDescription = findViewById(R.id.txtDescription);
        title = (TextView) findViewById(R.id.name);
        spec = (TextView) findViewById(R.id.spec);
        price = (TextView) findViewById(R.id.price);
        mrp = (TextView) findViewById(R.id.mrp);
        offer = (TextView) findViewById(R.id.offer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        parent_view = findViewById(android.R.id.content);
        public_parent_view = findViewById(android.R.id.content);

    }

    private void function() {

        toolbar.setTitle("Product Details");
        setSupportActionBar(toolbar);
        title.setText(Sessiondata.getInstance().getName());
        String strMrp= ActivityProductDetails.this.getString(R.string.Rs) + Sessiondata.getInstance().getOriginalPrice();
        String strPrice=ActivityProductDetails.this.getString(R.string.Rs) + Sessiondata.getInstance().getDiscontPrice();
        System.out.println("### stringprice"+strMrp);
        System.out.println("### stringmrp"+strPrice);
        spec.setText(Sessiondata.getInstance().getCategoryId() + " Grams");
            price.setText(strPrice);
        if(strMrp.equals(strPrice)){
            mrp.setVisibility(View.GONE);
        }else{
            mrp.setText("MRP " +strMrp);
            mrp.setPaintFlags(mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }



        str_product_id = Sessiondata.getInstance().getProductId();
        txtDescription.setText(Sessiondata.getInstance().getDescription());

        String str_img_path = Sessiondata.getInstance().getImage();
        System.out.println(Sessiondata.getInstance().getSubimage()+"subimages");
        String strOffer=Sessiondata.getInstance().getDiscountPercentage();
        System.out.println("### stringoffer"+strOffer);

        if(strOffer!=null&& !strOffer.equals("0")){
            offer.setText("Off " + Sessiondata.getInstance().getDiscountPercentage() + "%");

        }else{
            offer.setVisibility(View.GONE);

        }

//        if(Sessiondata.getInstance().getDiscountPercentage().equals("")){
//            offer.setVisibility(View.GONE);
//        }

//        String checkOrigin = Sessiondata.getInstance().getDiscountPercentage();
//        if(checkOrigin != null && checkOrigin.equals("")){
//            offer.setText("Off " +Sessiondata.getInstance().getDiscountPercentage() + "%");
//
//
//        }else{
//            offer.setVisibility(View.GONE);
//        }


        if (str_img_path.equals("")) {

        } else {

            System.out.println("IMG : : " + str_img_path);
            Glide.with(ActivityProductDetails.this).load(str_img_path)
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(img);
        }
        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        str_user_id = user.get(SessionManager.KEY_USER_ID);
        IcAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queue = Volley.newRequestQueue(ActivityProductDetails.this);
                Function_Add_Cart(str_product_id);
            }
        });

        for(int i=0;i<Sessiondata.getInstance().getSubimage().size();i++){
            String model=Sessiondata.getInstance().getSubimage().get(i);
            Sessiondata.getInstance().setImagePath(model);
            imagePath.add(Sessiondata.getInstance().getImagePath());

        }

        // Calling the RecyclerView
        recyclerView.setHasFixedSize(true);
        // The number of Columns
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SubImageAdapter(ActivityProductDetails.this, imagePath);
        recyclerView.setAdapter(adapter);

    }



    public static void Function_Add_Cart(final String str_prod_id) {

        try {

            StringRequest request = new StringRequest(Request.Method.POST,
                    URL_ADD_TO_CART, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d("PUBLIC_TAG", response.toString());

                    System.out.println(" RESPONSE " + "PUBLIC_TAG");

                    try {
                        JSONObject obj = new JSONObject(response);

                        System.out.println(" RESPONSE OBJ " + obj);

                        System.out.println(" JSON : " + obj);
                        int success = obj.getInt("status");

                        if (success == 1) {


                            Snackbar snackbar = Snackbar
                                    .make(public_parent_view, "Added to Cart Successfully", Snackbar.LENGTH_LONG)
                                    .setAction("Go to Cart", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            // activity.startActivity(new Intent(activity, Activity_Cart.class));
                                        }
                                    });

                            snackbar.show();

                        } else {


                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    Snackbar.make(public_parent_view, "Error : " + error, Snackbar.LENGTH_SHORT).show();
                }
            }) {


                @Override
                protected Map<String, String> getParams() {
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_share, menu);

        // Notification Counter
        MenuItem item1 = menu.findItem(R.id.action_share);

       item1.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
           @Override
           public boolean onMenuItemClick(MenuItem item) {
               Intent shareIntent =   new Intent(android.content.Intent.ACTION_SEND);
               shareIntent.setType("text/plain");
               shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Insert Subject here");
               String app_url = " https://play.google.com/store/apps/details?id=my.example.javatpoint";
               shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,app_url);
               startActivity(Intent.createChooser(shareIntent, "Share via"));
               return false;

           }
       });



        return true;
    }


}