package com.banyan.naajilshop.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.banyan.naajilshop.R;
import com.banyan.naajilshop.global.AppConfig;
import com.banyan.naajilshop.utils.SessionManager;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Activity_Profile extends AppCompatActivity {

    private Toolbar mToolbar;
    private Button btn_my_orders,btnLogout,btnUpdate;
    private EditText edtUserName,edtMobileNo,edtEmail;
    private SessionManager sessionManager;

    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "myPref";
    private static final String KEYEMAIL = "email";
    private static final String KEYMOBILE = "mobile";
    private static final String KEYNAME = "name";
    private static final String KEY_USER_ID = "user_id";
    private RequestQueue queue;
    private String userId,name,email,mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        init();



    }
    public void init(){

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        btn_my_orders = (Button) findViewById(R.id.my_orders);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);

        edtUserName = (EditText) findViewById(R.id.edtUSerName);
        edtMobileNo = (EditText) findViewById(R.id.edtMobile);
        edtEmail = (EditText) findViewById(R.id.edtEmail);

         sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();

        name = user.get(SessionManager.KEY_NAME);
        mobile = user.get(SessionManager.KEY_MOBILE_NUMBER);
        email = user.get(SessionManager.KEY_EMAIL_ID);
        userId = user.get(SessionManager.KEY_USER_ID);

       edtUserName.setText(name);
        edtMobileNo.setText(mobile);
        edtEmail.setText(email);



        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Nanjil - Profile");
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btn_my_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Activity_Profile.this, Activity_My_Order.class);
                startActivity(i);
                finish();
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sessionManager.logoutUser();
                TastyToast.makeText(Activity_Profile.this, "Logout Successfully", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();


            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=edtUserName.getText().toString();
                email=edtEmail.getText().toString();
                mobile=edtMobileNo.getText().toString();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                    if (email.matches(emailPattern)) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(KEYNAME, name);
                        editor.putString(KEYEMAIL, email);
                        editor.putString(KEYMOBILE,mobile);
                        editor.putString(KEY_USER_ID,userId);
                        editor.apply();
                        queue = Volley.newRequestQueue(Activity_Profile.this);
                        Action_UPDATE();

                    } else {
                        TastyToast.makeText(Activity_Profile.this, "wrong email or password",
                                TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                    }


            }
        });

    }
    public void Action_UPDATE() {

        System.out.println("### Appconfig.URL_UPDATE_PROFILE " + AppConfig.URL_UPDATE_PROFILE);

        String tag_json_obj = "json_obj_req";
        System.out.println("CAME 1");
        final StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_UPDATE_PROFILE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                PartName.clear();

                System.out.println("###  Appconfig.URL_UPDATE_PROFILE onResposne " + response);

                Log.d("TAG", response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");

                    if (success == AppConfig.TAG_SUCCESS) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Profile.this);
                        builder.setTitle("Update")
                                .setMessage("Update completed successfully!")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {


                                        Intent intent = new Intent(Activity_Profile.this, MainActivity.class);
                                        startActivity(intent);

                                    }
                                });

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();


                    } else {
                        String message = obj.getString("message");
                        TastyToast.makeText(Activity_Profile.this, message, TastyToast.LENGTH_SHORT, TastyToast.INFO);
                    }

                } catch (JSONException e) {
                    //
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println("###  Appconfig.URL_UPDATE_PROFILE onError");
                if (error != null)
                    System.out.println("###  Appconfig.URL_UPDATE_PROFILE onError" + error.getLocalizedMessage());

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("name",name );
                params.put("email_id", email);
                params.put("mobile_no", mobile);
                params.put("user_id", userId);

                System.out.println("### Appconfig.URL_UPDATE_PROFILE username " + name);
                System.out.println("### email " + email);
                System.out.println("### mobile " + mobile);
                System.out.println("### userId " + userId);

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
}