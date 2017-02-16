package com.mycompany.medsconnect;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Created by joel on 23-02-2016.
 */
public class SessionManager {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;
    int private_mode = 0;
    private static final String PREF_NAME = "meds";
    private static final String LOGGED_IN = "loggedIn";
    public static final String USERNAME = "username";
    public static final String NAME = "name";
    public static final String TYPE = "type";
    public static final String ADDRESS = "address";

    public SessionManager(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME,private_mode);
        editor = sharedPreferences.edit();
    }

    public void loginSession(String username,String name,String type,String address){
        editor.putBoolean(LOGGED_IN,true);
        editor.putString(USERNAME,username);
        editor.putString(NAME, name);
        editor.putString(TYPE, type);
        editor.putString(ADDRESS, address);
        editor.commit();
    }

    public String[] getUserDetails(){
        String[] details = {sharedPreferences.getString(USERNAME,null),
                sharedPreferences.getString(NAME, null),sharedPreferences.getString(TYPE,null),sharedPreferences.getString(ADDRESS,null)};
        return details;
    }

    public boolean isLoggedIn(){
        return sharedPreferences.getBoolean(LOGGED_IN,false);
    }

    public void checkLogin(){
        if(!this.isLoggedIn()){
            Intent i = new Intent(context,LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }

    public void logout(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(context,LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
