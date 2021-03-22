package com.banyan.naajilshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.balysv.materialripple.MaterialRippleLayout;
import com.banyan.naajilshop.R;
import com.banyan.naajilshop.global.AppConfig;
import com.banyan.naajilshop.utils.SessionManager;
import com.google.android.material.snackbar.Snackbar;
import com.sdsmdg.tastytoast.TastyToast;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import dmax.dialog.SpotsDialog;


public class Activity_CheckOut extends AppCompatActivity {

    private Toolbar mToolbar;

    SpotsDialog dialog;
    private View parent_view;
    public static RequestQueue queue;
    String TAG = "reg";

    private EditText edt_ship_name, edt_ship_phone, edt_ship_address, edt_ship_country, edt_ship_zip, edt_ship_city,
            edt_ship_state, edt_billing_name, edt_billing_phone, edt_billing_address, edt_billing_country, edt_billing_zip,
            edt_billing_city, edt_billing_state;
    private MaterialRippleLayout lyt_next;
    private CheckBox checkout_checkbox;
    private String str_ship_name, str_ship_phone, str_ship_address, str_ship_country, str_ship_zip, str_ship_city, str_ship_state,
            str_billing_name, str_billing_phone, str_billing_address, str_billing_country, str_billing_zip, str_billing_city, str_billing_state;

    private float prod_weight;
    private String str_mrp_amt, str_discount_amt, str_to_be_paid = "";

    private TextView txt_sub_total, txt_tobe_paid, txt_shipping;

    private SessionManager session;

    private String str_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_checkout_page);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Check Out");
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.background_dark));
        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), Activity_Cart.class);
                startActivity(i);
                finish();
            }
        });

        try {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {

                prod_weight = extras.getFloat("prod_weight");
                str_mrp_amt = extras.getString("mrp_price");
                str_discount_amt = extras.getString("discount_price");
                str_to_be_paid = extras.getString("tobepaid");

                System.out.println("WEIGHT : " + prod_weight);
                System.out.println("MRP : " + str_mrp_amt);
                System.out.println("DISCOUNT : " + str_discount_amt);
                System.out.println("TOBEPAID : " + str_to_be_paid);

            }
        } catch (Exception e) {
            System.out.println(e);
        }

        session = new SessionManager(Activity_CheckOut.this);
        session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // name
        str_user_id = user.get(SessionManager.KEY_USER_ID);

        edt_ship_name = (EditText) findViewById(R.id.checkout_edt_ship_name);
        edt_ship_phone = (EditText) findViewById(R.id.checkout_edt_ship_phone);
        edt_ship_address = (EditText) findViewById(R.id.checkout_edt_ship_address);
        edt_ship_country = (EditText) findViewById(R.id.checkout_edt_ship_country);
        edt_ship_zip = (EditText) findViewById(R.id.checkout_edt_ship_zip);
        edt_ship_city = (EditText) findViewById(R.id.checkout_edt_ship_city);
        edt_ship_state = (EditText) findViewById(R.id.checkout_edt_ship_state);

        edt_billing_name = (EditText) findViewById(R.id.checkout_edt_billing_name);
        edt_billing_phone = (EditText) findViewById(R.id.checkout_edt_billing_phone);
        edt_billing_address = (EditText) findViewById(R.id.checkout_edt_billing_address);
        edt_billing_country = (EditText) findViewById(R.id.checkout_edt_billing_country);
        edt_billing_zip = (EditText) findViewById(R.id.checkout_edt_billing_zip);
        edt_billing_city = (EditText) findViewById(R.id.checkout_edt_billing_city);
        edt_billing_state = (EditText) findViewById(R.id.checkout_edt_billing_state);

        checkout_checkbox = (CheckBox) findViewById(R.id.checkout_checkbox);

        lyt_next = (MaterialRippleLayout) findViewById(R.id.lyt_next);

        txt_sub_total = (TextView) findViewById(R.id.text_view_mrp_price);
        txt_shipping = (TextView) findViewById(R.id.checkout_txt_shipping);
        txt_tobe_paid = (TextView) findViewById(R.id.text_view_to_be_paid);

        txt_tobe_paid.setText(""+"50");
        txt_sub_total.setText("" + str_to_be_paid);
        txt_tobe_paid.setText("250");

        checkout_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                str_ship_name = edt_ship_name.getText().toString().trim();
                str_ship_phone = edt_ship_phone.getText().toString().trim();
                str_ship_address = edt_ship_address.getText().toString().trim();
                str_ship_country = edt_ship_country.getText().toString().trim();
                str_ship_zip = edt_ship_zip.getText().toString().trim();
                str_ship_city = edt_ship_city.getText().toString().trim();
                str_ship_state = edt_ship_state.getText().toString().trim();

                boolean checked = ((CheckBox) view).isChecked();
                if (checked) {
                    System.out.println("Checked");
                    edt_billing_name.setText("" + str_ship_name);
                    edt_billing_phone.setText("" + str_ship_phone);
                    edt_billing_address.setText("" + str_ship_address);
                    edt_billing_country.setText("" + str_ship_country);
                    edt_billing_zip.setText("" + str_ship_zip);
                    edt_billing_city.setText("" + str_ship_city);
                    edt_billing_state.setText("" + str_ship_state);
                } else {
                    System.out.println("REleased");
                    edt_billing_name.setText("");
                    edt_billing_phone.setText("");
                    edt_billing_address.setText("");
                    edt_billing_country.setText("");
                    edt_billing_zip.setText("");
                    edt_billing_city.setText("");
                    edt_billing_state.setText("");
                }
            }
        });

        lyt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edt_ship_name.getText().toString().length()==0&&edt_ship_phone.getText().toString().length()==0&&
                        edt_ship_address.getText().toString().length()==0&&
                        edt_ship_country.getText().toString().length()==0&&
                        edt_ship_zip.getText().toString().length()==0&&
                        edt_ship_city.getText().toString().length()==0&&
                        edt_ship_state.getText().toString().length()==0&&
                        edt_billing_name.getText().toString().length()==0&&
                        edt_billing_phone.getText().toString().length()==0&&
                        edt_billing_address.getText().toString().length()==0&&
                        edt_billing_country.getText().toString().length()==0&&
                        edt_billing_zip.getText().toString().length()==0&&
                        edt_billing_city.getText().toString().length()==0&&
                        edt_billing_state.getText().toString().length()==0
                ){
                    TastyToast.makeText(Activity_CheckOut.this, "Fill all the details", TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                }else{
                    str_ship_name = edt_ship_name.getText().toString().trim();
                    str_ship_phone = edt_ship_phone.getText().toString().trim();
                    str_ship_address = edt_ship_address.getText().toString().trim();
                    str_ship_country = edt_ship_country.getText().toString().trim();
                    str_ship_zip = edt_ship_zip.getText().toString().trim();
                    str_ship_city = edt_ship_city.getText().toString().trim();
                    str_ship_state = edt_ship_state.getText().toString().trim();

                    str_billing_name = edt_billing_name.getText().toString().trim();
                    str_billing_phone = edt_billing_phone.getText().toString().trim();
                    str_billing_address = edt_billing_address.getText().toString().trim();
                    str_billing_country = edt_billing_country.getText().toString().trim();
                    str_billing_zip = edt_billing_zip.getText().toString().trim();
                    str_billing_city = edt_billing_city.getText().toString().trim();
                    str_billing_state = edt_billing_state.getText().toString().trim();

                    try {
                        dialog = new SpotsDialog(Activity_CheckOut.this);
                        dialog.show();
                        queue = Volley.newRequestQueue(Activity_CheckOut.this);
                        Func_Place_Order();
                    } catch (Exception e) {

                    }
                }


        }
        });

    }

    /*****************************
     * Place an Order
     *************************/

    public void Func_Place_Order() {

        String tag_json_obj = "json_obj_req";

        System.out.println("CAME 1" + AppConfig.URL_PLACE_ORDER);

        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_PLACE_ORDER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");

                    if (success == 1) {

                        dialog.dismiss();
                        new FancyGifDialog.Builder(Activity_CheckOut.this)
                                .setTitle("Order")
                                .setMessage("Your Order Placed Successfully!")
                                .setPositiveBtnText("Ok")
                                .setPositiveBtnBackground("#4CAF50")
                                .setGifResource(R.drawable.success)   //Pass your Gif here
                                .isCancellable(false)
                                .OnPositiveClicked(new FancyGifDialogListener() {
                                    @Override
                                    public void OnClick() {
                                        Intent i = new Intent(Activity_CheckOut.this, Activity_My_Order.class);
                                        startActivity(i);
                                        finish();
                                    }
                                })
                                .build();

                  //      Snackbar.make(parent_view, "Your Order Placed Successfully", Snackbar.LENGTH_SHORT).show();

                    } else if (success == 0) {

                        dialog.dismiss();

                    }
                    dialog.dismiss();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                dialog.dismiss();
                Snackbar.make(parent_view, "Error : " + error, Snackbar.LENGTH_SHORT).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                String grand_total = txt_tobe_paid.getText().toString();

                params.put("bill_fname", str_billing_name);
                params.put("bill_lname", "");
                params.put("bill_address1", str_billing_address);
                params.put("bill_address2", "");
                params.put("bill_city", str_billing_city);
                params.put("bill_state", str_billing_state);
                params.put("bill_pincode", str_billing_zip);
                params.put("ship_fname", str_ship_name);
                params.put("ship_lname", "");
                params.put("ship_address1", str_ship_address);
                params.put("ship_address2", "");
                params.put("ship_city", str_ship_city);
                params.put("ship_state", str_ship_state);
                params.put("ship_pincode", str_ship_zip);
                params.put("order_subtotal", str_mrp_amt);
                params.put("order_finalsubtotal", str_to_be_paid);
                params.put("order_discount_amt", str_discount_amt);
                params.put("order_roundoff", str_to_be_paid);
                params.put("shipping_charges", "50");
                params.put("order_grand_total", grand_total);
                params.put("user_id", str_user_id);

                return checkParams(params);
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
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        // Adding request to request queue
        queue.add(request);
    }
}
