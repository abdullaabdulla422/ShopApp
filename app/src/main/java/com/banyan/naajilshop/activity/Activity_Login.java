package com.banyan.naajilshop.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

/***started nanjil shop on 25feb 2021***/

public class Activity_Login extends AppCompatActivity {

    public static final String TAG_USER_ID = "user_id";
    public static final String TAG_USER_TYPE = "usertype";
    public static final String TAG_NAME = "name";
    public static final String TAG_EMAIL_ID = "email_id";
    public static final String TAG_MOBILE_NUMBER = "mobile_no";

    TextInputEditText edtEmail, edtPassword;
    TextView txtSignIn, txtSignUp;
    private RequestQueue queue;
    private Toolbar toolbar;

    private static long back_pressed;
    private View parent_view;
    private SpotsDialog dialog;
    String str_email, str_password;
    private SessionManager session_manager;
    private SharedPreferences.Editor editor;

    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "myPref";
    private static final String KEYEMAIL = "email";
    private static final String KEYPASSWORD = "password";



    //    String prevStarted = "yes";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        parent_view = findViewById(android.R.id.content);

        edtEmail = (TextInputEditText) findViewById(R.id.login_edt_email);
        edtPassword = (TextInputEditText) findViewById(R.id.login_edt_pwd);
        txtSignIn = (TextView) findViewById(R.id.login_btn_submit);
        txtSignUp = (TextView) findViewById(R.id.login_txt_sign_up);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("Nanjil Shop - Login");
        setSupportActionBar(toolbar);


        session_manager = new SessionManager(this);

        txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_email = edtEmail.getText().toString();
                str_password = edtPassword.getText().toString();

                if (str_email.isEmpty()) {
                    TastyToast.makeText(Activity_Login.this, "Please enter username", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                } else if (str_password.isEmpty()) {
                    TastyToast.makeText(Activity_Login.this, "Please enter password", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                } else {

                    dialog = new SpotsDialog(Activity_Login.this);
                    dialog.setCancelable(false);
                    dialog.show();
                    queue = Volley.newRequestQueue(Activity_Login.this);
                    Action_LOGIN();

                }

            }
        });
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Login.this, Activity_Registeration.class);
                startActivity(intent);
            }
        });


    }

    public void Action_LOGIN() {

        System.out.println("### Appconfig.URL_LOGIN " + AppConfig.URL_USER_LOGIN);

        String tag_json_obj = "json_obj_req";
        System.out.println("CAME 1");
        final StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_USER_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                PartName.clear();

                System.out.println("###  Appconfig.URL_LOGIN onResposne " + response);

                Log.d("TAG", response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");
                    String message = obj.getString("message");

                    if (success == AppConfig.TAG_SUCCESS) {

                        JSONObject obj_data = obj.getJSONObject("data");
                        System.out.println("OBJECT : " + obj_data);
                        String user_id = obj_data.getString(TAG_USER_ID);
                        String usertype = obj_data.getString(TAG_USER_TYPE);
                        String name = obj_data.getString(TAG_NAME);
                        String email_id = obj_data.getString(TAG_EMAIL_ID);
                        String mobile_no = obj_data.getString(TAG_MOBILE_NUMBER);
                        session_manager.createLoginSession(user_id, usertype,
                                name, email_id, mobile_no);

                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();

                    } else {
                        message = obj.getString("message");
                        TastyToast.makeText(Activity_Login.this, message, TastyToast.LENGTH_SHORT, TastyToast.INFO);
                        dialog.dismiss();
                    }

                } catch (JSONException e) {
                    //
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println("###  Appconfig.URL_LOGIN onError");
                if (error != null)
                    System.out.println("###  Appconfig.URL_LOGIN onError" + error.getLocalizedMessage());

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", edtEmail.getText().toString());
                params.put("password", edtPassword.getText().toString());
                System.out.println("### Appconfig.URL_LOGIN username " + edtEmail.getText().toString());
                System.out.println("### password " + edtPassword.getText().toString());

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

    @Override
    public void onBackPressed() {

        if (back_pressed + 2000 > System.currentTimeMillis()) {

            this.moveTaskToBack(true);
        } else {

            Snackbar.make(parent_view, "Press once again to exit!", Snackbar.LENGTH_SHORT).show();

        }
        back_pressed = System.currentTimeMillis();
    }
}