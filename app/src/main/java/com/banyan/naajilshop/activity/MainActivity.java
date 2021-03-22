package com.banyan.naajilshop.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.banyan.naajilshop.R;
import com.banyan.naajilshop.adapter.CategoryAdapter;
import com.banyan.naajilshop.adapter.SliderAdapter;
import com.banyan.naajilshop.global.AppConfig;
import com.banyan.naajilshop.model.SliderItem;
import com.banyan.naajilshop.utils.SessionManager;
import com.google.android.material.snackbar.Snackbar;
import com.sdsmdg.tastytoast.TastyToast;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {

    List<SliderItem> sliderItemList;

    private Toolbar toolbar;
    SliderView sliderView;
    private SliderAdapter adapter;
    private SpotsDialog dialog;

    private int flag = 0;
    private RequestQueue queue;
    private RecyclerView recyclerView;
    private TextView txtField, txt_user_name;
    private ImageView imageCart, imgProfile;
    private EditText edt_city;
    private Spinner alert_spinner_city;

    // CART
    RelativeLayout notification_Count, notification_batch, message_Count, message_batch;
    public static TextView tv_notification, tv_message;
    int i = 0;
    public static int cart_count = 0;
    String value = "nothing";
    private String str_noti_count;

    public static final String TAG_DISTRICT_NAME = "name";
    public static final String TAG_DISTRICT_ID = "id";

    private List<HashMap<String, String>> list_category = new ArrayList<>();
    public static final String TAG_CATEGORY_ID = "category_id";
    public static final String TAG_CATEGORY_NAME = "name";
    public static final String TAG_CATEGORY_IMAGE = "image";

    private String str_selected_city, str_selected_city_id;
    private String str_is_city_selected = "";
    private List<String> array_list_city_id, array_list_city_name;
    private ArrayAdapter<String> adapter_city;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String str_user_id, str_user_name = "";

    private CategoryAdapter categoryAdapter;

    SessionManager session;

    private static long back_pressed;
    private View parent_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(MainActivity.this);
        editor = sharedPreferences.edit();
        str_is_city_selected = sharedPreferences.getString("Selected_City", "");
        str_selected_city_id = sharedPreferences.getString("Selected_City_id", "");

        setContentView(R.layout.activity_main);

        session = new SessionManager(getApplicationContext());
        session.checkLogin();

        if (!session.isLoggedIn()){
            return;
        }

        HashMap<String, String> user = session.getUserDetails();
        str_user_id = user.get(SessionManager.KEY_USER_ID);
        str_user_name = user.get(SessionManager.KEY_NAME);

        init();
        str_selected_city=sharedPreferences.getString("SelectedCity","");
        edt_city.setText(str_selected_city);


        try {
            dialog = new SpotsDialog(MainActivity.this);
            dialog.show();
            queue = Volley.newRequestQueue(MainActivity.this);
            Action_Get_City();

            queue = Volley.newRequestQueue(MainActivity.this);
            Function_Get_Cart();
        }catch (Exception e){
            System.out.println(e);
        }

        if (str_is_city_selected.equals("1")){
            if (!str_selected_city_id.equals("")){
                queue = Volley.newRequestQueue(MainActivity.this);
                Action_Get_Category(str_selected_city_id);
            }else {
                TastyToast.makeText(MainActivity.this, "Please Select Your City", TastyToast.LENGTH_SHORT, TastyToast.INFO);
            }
        }else {
            Func_Alert_Dialog();
        }

    }

    public void init() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Nanjil Shop");
        setSupportActionBar(toolbar);

        txt_user_name = (TextView) findViewById(R.id.user_name);
        edt_city = (EditText) findViewById(R.id.main_edt_city);
        sliderView = (SliderView) findViewById(R.id.imageSlider);
        txtField = (TextView) findViewById(R.id.txtField);
        parent_view = findViewById(android.R.id.content);

        recyclerView = (RecyclerView) findViewById(R.id.main_recycle_grid);

        txt_user_name.setText(""+ "Hey, " + str_user_name);

        array_list_city_id = new ArrayList<>();
        array_list_city_name = new ArrayList<>();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView

        edt_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Func_Alert_Dialog();
            }
        });

        SetSlider();
        renewSlideItems();
    }

    /**********************************
     * Main Menu
     *********************************/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Notification Counter
        MenuItem item1 = menu.findItem(R.id.action_notification);
        MenuItemCompat.setActionView(item1, R.layout.toolbar_cart_update_count_layout);
        notification_Count = (RelativeLayout) MenuItemCompat.getActionView(item1);
        notification_batch = (RelativeLayout) MenuItemCompat.getActionView(item1);
        tv_notification = (TextView) notification_batch.findViewById(R.id.badge_notification);
        tv_notification.setVisibility(View.GONE);
        try {
//            if (!str_noti_count.equals("noti_count")){
//                tv_notification.setText(""+ str_noti_count);
//                Log.d("", "onCreateOptionsMenu: "+""+str_noti_count);
//                tv_notification.setVisibility(View.VISIBLE);
//
//            }else {
//                tv_notification.setVisibility(View.GONE);
////                tv_notification.setText("0");
//            }
        }catch (Exception e) {

        }


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

    public void SetSlider() {

        adapter = new SliderAdapter(this);
        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.DROP); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(getResources().getColor(R.color.colorPrimaryDark));
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();

        sliderView.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {
                Log.i("GGG", "onIndicatorClicked: " + sliderView.getCurrentPagePosition());
            }
        });
    }

    /*********************
     * Slider
     * **********************/
    public void renewSlideItems() {
        List<SliderItem> sliderItemList = new ArrayList<>();
        //dummy data
        for (int i = 0; i < 4; i++) {
            SliderItem sliderItem = new SliderItem();
            if (i % 2 == 0) {
                sliderItem.setImageUrl("http://epictech.in/Nanjilshop/uploads/banner1.jpg");
                sliderItem.setDescription("Get 50% Off ");
            } else {
                sliderItem.setImageUrl("http://epictech.in/Nanjilshop/uploads/banner2.jpg");
                sliderItem.setDescription("Get 30% Off ");
            }
            sliderItemList.add(sliderItem);
        }
        adapter.renewItems(sliderItemList);
    }

    /************************
     *  Get City
     * **********************/

    public void Action_Get_City() {

        System.out.println("### Appconfig.URL_STATE " + AppConfig.URL_CITY);

        String tag_json_obj = "json_obj_req";
        System.out.println("CAME 1");
        final StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_CITY, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                array_list_city_id.clear();
                array_list_city_name.clear();
                System.out.println("###  Appconfig.URL_STATE onResposne " + response);

                Log.d("TAG", response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");

                    if (success == AppConfig.TAG_SUCCESS) {

                        JSONArray array_results = obj.getJSONArray("records");

                        for (int count = 0; count < array_results.length(); count++) {
                            JSONObject obj_data = array_results.getJSONObject(count);
                            String Str_id = obj_data.getString(TAG_DISTRICT_ID);
                            String str_name = obj_data.getString(TAG_DISTRICT_NAME);

                            array_list_city_id.add(Str_id);
                            array_list_city_name.add(str_name);
                        }
                        dialog.dismiss();

                    } else {
                        dialog.dismiss();
                        TastyToast.makeText(MainActivity.this, "Oops! Something Went Wrong, Try Again", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }

                } catch (JSONException e) {
                    //
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                dialog.dismiss();
                System.out.println("###  Appconfig.URL_STATE onError");
                if (error != null)
                    System.out.println("###  Appconfig.URL_STATE onError" + error.getLocalizedMessage());

            }
        }) {


        };

        // Adding request to request queue
        request.setRetryPolicy(new DefaultRetryPolicy(
                AppConfig.TAG_VOLLERY_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(request);
    }

    /************************
     *  Get Category
     * **********************/

    public void Action_Get_Category(String param_city_id) {

        System.out.println("### Appconfig.URL_CATEGORY " + AppConfig.URL_CATEGORY);

        System.out.println("CAME 1");
        final StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_CATEGORY, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                list_category.clear();

                System.out.println("###  Appconfig.URL_CATEGORY onResposne " + response);
                Log.d(response, "onResponse: " + response);
                Log.d("TAG", response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    String success = obj.getString("status");

                    if (success.equals("1")) {

                        JSONArray array_results = obj.getJSONArray("records");

                        for (int count = 0; count < array_results.length(); count++) {

                            JSONObject obj_data = array_results.getJSONObject(count);

                            String str_category_id = obj_data.getString(TAG_CATEGORY_ID);
                            String str_category_name = obj_data.getString(TAG_CATEGORY_NAME);
                            String str_category_image = obj_data.getString(TAG_CATEGORY_IMAGE);

                            HashMap<String, String> item = new HashMap<>();

                            item.put(TAG_CATEGORY_ID, str_category_id);
                            item.put(TAG_CATEGORY_NAME, str_category_name);
                            item.put(TAG_CATEGORY_IMAGE, str_category_image);

                            list_category.add(item);
                        }

                        categoryAdapter = new CategoryAdapter(MainActivity.this, list_category);
                        recyclerView.setAdapter(categoryAdapter);

                    } else {
                        TastyToast.makeText(MainActivity.this, "Oops! No Data Found", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }

                } catch (JSONException e) {
                    //
                    e.printStackTrace();
                }

                if (list_category.size() != 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    txtField.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    txtField.setVisibility(View.VISIBLE);
                    txtField.setText("No data Available");
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println("###  Appconfig.URL_POPULAR_KITCHEN onError");
                if (error != null)
                    System.out.println("###  Appconfig.URL_POPULAR_KITCHEN onError" + error.getLocalizedMessage());

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("district_id", param_city_id);
                System.out.println("CITY ID : " + param_city_id);
                return params;
            }

        };

        // Adding request to request queue
        request.setRetryPolicy(new DefaultRetryPolicy(
                AppConfig.TAG_VOLLERY_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(request);
    }

    /*****************
     * GET Cart Count
     ***************/

    public void Function_Get_Cart() {

        System.out.println("### AppConfig.URL_CART_LIST "+ AppConfig.URL_CART_LIST);

        String tag_json_obj = "json_obj_req";

        System.out.println("CAME Enquiry" + AppConfig.URL_CART_LIST);

        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_CART_LIST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                System.out.println("### AppConfig.URL_CART_LIST response "+response);

                Log.d("TAG", response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");

                    if (success == 1) {

                        JSONArray arr;

                        arr = obj.getJSONArray("records");


                        if(arr.length()!=0){
                            String str_cart_count = ""+arr.length();
                            tv_notification.setText("" + str_cart_count);
                            tv_notification.setVisibility(View.VISIBLE);

                        }else {
                            tv_notification.setVisibility(View.GONE);
                        }


                        SharedPreferences sharedPreferences = PreferenceManager
                                .getDefaultSharedPreferences(MainActivity.this);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putString("noti_count", tv_notification.getText().toString());

                        editor.commit();


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
                    System.out.println("### AppConfig.URL_CART_LIST onerror"+error.getLocalizedMessage());

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id", str_user_id);

                System.out.println("### AppConfig.URL_CART_LIST user_id "+str_user_id);

                return params;
            }

        };

        // Adding request to request queue
        queue.add(request);
    }

    /**********************************
     * Function Alert
     ***********************************/
    private void Func_Alert_Dialog() {

        LayoutInflater li = LayoutInflater.from(MainActivity.this);
        View promptsView = li.inflate(R.layout.alert_spinner_city, null);

        AlertDialog alert_dialog = new AlertDialog.Builder(MainActivity.this)
                .setView(promptsView)
                .setTitle(R.string.app_name)
                .setPositiveButton("Select",null)
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        alert_spinner_city = (SearchableSpinner) promptsView.findViewById(R.id.home_alert_spinner_city);

        try {
            alert_spinner_city
                    .setAdapter(new ArrayAdapter<String>(MainActivity.this,
                            android.R.layout.simple_spinner_dropdown_item,
                            array_list_city_name));

            alert_spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                   TextView t1 = (TextView) view;
                    str_selected_city_id = array_list_city_id.get(position);
                    str_selected_city = array_list_city_name.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            // spinner_currency.setSelection(2);
        } catch (Exception e) {

        }

        alert_dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface alert_dialog) {

                Button button = ((AlertDialog) alert_dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                Button button_negative = ((AlertDialog) alert_dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        edt_city.setText("" + str_selected_city);
                        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        editor = sharedPreferences.edit();
                        editor.putString("SelectedCity", str_selected_city);
                        editor.commit();
                        if (!str_selected_city_id.equals("")){
                            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            editor = sharedPreferences.edit();
                            editor.putString("Selected_City", "1");
                            editor.putString("Selected_City_id", str_selected_city_id);
                            editor.commit();

                            try {
                                queue = Volley.newRequestQueue(MainActivity.this);
                                Action_Get_Category(str_selected_city_id);
                            }catch (Exception e){

                            }
                        }else {
                            TastyToast.makeText(MainActivity.this, "Internal Error!/", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        }

                        alert_dialog.dismiss();

                    }
                });

                button_negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       alert_dialog.dismiss();
                    }
                });
            }
        });

        alert_dialog.show();
    }

    @Override
    public void onBackPressed() {

        if (back_pressed + 2000 > System.currentTimeMillis()) {

          finish();
        } else {

            Snackbar.make(parent_view, "Press once again to exit!", Snackbar.LENGTH_SHORT).show();

        }
        back_pressed = System.currentTimeMillis();
    }

}