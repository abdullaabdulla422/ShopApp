package com.banyan.naajilshop.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Activity_Registeration extends AppCompatActivity {

    TextInputEditText edtUserNme, edtMobNo, edtEmail, edtPwd, edtRePwd;
    TextView btnRegister,txtSignIn;
    ImageView imageBack;
    private Toolbar toolbar;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "myPref";
    private static final String KEYEMAIL = "email";
    private static final String KEYPASSWORD = "password";
    String mobileNo, email_in, password_in, username;

    private RequestQueue queue;

    private static long back_pressed;
    private View parent_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        parent_view = findViewById(android.R.id.content);

        edtUserNme = (TextInputEditText) findViewById(R.id.signup_edt_name);
        edtMobNo = (TextInputEditText) findViewById(R.id.signup_edt_mobile);
        edtEmail = (TextInputEditText) findViewById(R.id.signup_edt_email);
        edtPwd = (TextInputEditText) findViewById(R.id.signup_edt_password);
        edtRePwd = (TextInputEditText) findViewById(R.id.signup_edt_retype_password);
        btnRegister = (TextView) findViewById(R.id.signup_btn_signup);
        txtSignIn = (TextView) findViewById(R.id.txtSignIn);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("Nanjil Shop - Register");
        setSupportActionBar(toolbar);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
        txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Activity_Registeration.this,Activity_Login.class);
                startActivity(i);
            }
        });


    }

    private void validate() {
        mobileNo = edtMobNo.getText().toString();
        email_in = edtEmail.getText().toString();
        password_in = edtPwd.getText().toString();
        String re_password = edtRePwd.getText().toString();
        username = edtUserNme.getText().toString();

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if(password_in.length()<5){
            TastyToast.makeText(Activity_Registeration.this, "set minimum 5 character for password",
                    TastyToast.LENGTH_SHORT, TastyToast.WARNING);
        }else{
            if (password_in.equals(re_password) && email_in.matches(emailPattern)) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(KEYEMAIL, email_in);
                editor.putString(KEYPASSWORD, password_in);
                editor.apply();
                queue = Volley.newRequestQueue(Activity_Registeration.this);
                Action_REGISTRATION();

            } else {
                TastyToast.makeText(Activity_Registeration.this, "wrong email or password",
                        TastyToast.LENGTH_SHORT, TastyToast.WARNING);
            }
        }



    }

    public void Action_REGISTRATION() {

        System.out.println("### Appconfig.URL_LOGIN " + AppConfig.URL_USER_REGISTRATION);

        String tag_json_obj = "json_obj_req";
        System.out.println("CAME 1");
        final StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_USER_REGISTRATION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                PartName.clear();

                System.out.println("###  Appconfig.URL_USER_REGISTRATION onResposne " + response);

                Log.d("TAG", response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");

                    if (success == AppConfig.TAG_SUCCESS) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Registeration.this);
                        builder.setTitle("Registration")
                                .setMessage("Registration completed successfully! You can Login Now.")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        Intent intent = new Intent(Activity_Registeration.this, Activity_Login.class);
                                        startActivity(intent);

                                    }
                                });

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();


                    } else {
                        String message = obj.getString("message");
                        TastyToast.makeText(Activity_Registeration.this, message, TastyToast.LENGTH_SHORT, TastyToast.INFO);
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

                params.put("name", username);
                params.put("email_id", email_in);
                params.put("mobile_no", mobileNo);
                params.put("password", password_in);

                System.out.println("### Appconfig.URL_REGISTRATION username " + email_in);
                System.out.println("### password " + password_in);

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

           onDestroy();
        } else {

            Snackbar.make(parent_view, "Press once again to exit!", Snackbar.LENGTH_SHORT).show();

        }
        back_pressed = System.currentTimeMillis();
    }

}