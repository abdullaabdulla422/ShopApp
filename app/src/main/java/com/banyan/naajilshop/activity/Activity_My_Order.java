package com.banyan.naajilshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.banyan.naajilshop.R;
import com.banyan.naajilshop.adapter.MyOrderAdapter;
import com.banyan.naajilshop.global.AppConfig;
import com.banyan.naajilshop.model.My_Orders_Model;
import com.banyan.naajilshop.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Activity_My_Order extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private Toolbar mToolbar;

    SessionManager session;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private TextView txt_msg;
    SpotsDialog dialog;
    private View parent_view;

    public static RequestQueue queue;
    String TAG = "reg";

    private List<My_Orders_Model> my_order_list = new ArrayList<>();

    private MyOrderAdapter adapter;

    private static String str_user_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("My Orders");
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.background_dark));
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


        SessionManager sessionManager = new SessionManager(Activity_My_Order.this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        str_user_id = user.get(SessionManager.KEY_USER_ID);

        recyclerView = (RecyclerView) findViewById(R.id.ref_recycler_view);
        txt_msg = (TextView) findViewById(R.id.ref_txt_empty_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(false);

                                        try {
                                            my_order_list.clear();
                                            queue = Volley.newRequestQueue(Activity_My_Order.this);
                                            Function_Get_My_Order();

                                        } catch (Exception e) {
                                            // TODO: handle exceptions
                                        }
                                    }
                                }
        );

    }

    @Override
    public void onRefresh() {

        try {

            my_order_list.clear();
            adapter.notifyDataSetChanged();
            queue = Volley.newRequestQueue(Activity_My_Order.this);
            Function_Get_My_Order();

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void Function_Get_My_Order() {

        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_MY_ORDERS, new Response.Listener<String>() {

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

                            String customername = obj1.getString("customername");
                            String order_no = obj1.getString("order_no");
                            String order_id = obj1.getString("order_id");
                            String order_subtotal = obj1.getString("order_subtotal");
                            String order_discount = obj1.getString("order_discount");
                            String order_coupon_code = obj1.getString("order_coupon_code");
                            String order_tax_amt = obj1.getString("order_tax_amt");
                            String order_shipping_charges = obj1.getString("order_shipping_charges");
                            String order_status = obj1.getString("order_status");
                            String order_grand_total = obj1.getString("order_grand_total");
                            String order_createdat = obj1.getString("order_createdat");

                            my_order_list.add(new My_Orders_Model(customername, order_no, order_id, order_subtotal, order_discount, order_coupon_code,
                                    order_tax_amt, order_shipping_charges, order_status, order_grand_total, order_createdat));
                        }
                        txt_msg.setVisibility(View.GONE);
                        adapter = new MyOrderAdapter(getApplicationContext(), my_order_list);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                        recyclerView.setAdapter(adapter);

                        adapter.notifyDataSetChanged();

                        swipeRefreshLayout.setRefreshing(false);

                    } else if (success == 0) {

                        txt_msg.setVisibility(View.VISIBLE);
                        swipeRefreshLayout.setRefreshing(false);
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                swipeRefreshLayout.setRefreshing(false);
                txt_msg.setVisibility(View.VISIBLE);
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

