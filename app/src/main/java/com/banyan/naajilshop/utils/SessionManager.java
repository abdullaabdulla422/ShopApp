package com.banyan.naajilshop.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.banyan.naajilshop.activity.Activity_Login;

import java.util.HashMap;

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref, default_sharedPreferences;

    // Editor for Shared preferences
    SharedPreferences.Editor editor, default_editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "AndroidHivePref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)

    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_UER_TYPE = "usertype";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL_ID = "email_id";
    public static final String KEY_MOBILE_NUMBER = "mobile_no";
    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();

        default_sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        default_editor = default_sharedPreferences.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(String user_id, String usertype,
                                   String name,String email_id, String mobile_no){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_USER_ID, user_id);
        editor.putString(KEY_UER_TYPE, usertype);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL_ID, email_id);
        editor.putString(KEY_MOBILE_NUMBER, mobile_no);

        // commit changes
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, Activity_Login.class); // Change it as Login Activity
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }



    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_USER_ID, pref.getString(KEY_USER_ID, ""));
        user.put(KEY_UER_TYPE, pref.getString(KEY_UER_TYPE, ""));
        user.put(KEY_NAME, pref.getString(KEY_NAME, ""));
        user.put(KEY_EMAIL_ID, pref.getString(KEY_EMAIL_ID, ""));
        user.put(KEY_MOBILE_NUMBER, pref.getString(KEY_MOBILE_NUMBER, ""));


        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        default_editor.clear();
        default_editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, Activity_Login.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}
