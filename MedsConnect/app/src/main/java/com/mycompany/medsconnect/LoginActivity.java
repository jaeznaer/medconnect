package com.mycompany.medsconnect;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends Activity {
    private EditText username;
    private EditText password;
    private SessionManager sessionManager;
    private String userName, passWord;
    private String NAME, TYPE, EMAIL, ADDRESS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (EditText) findViewById(R.id.user_name);
        password = (EditText) findViewById(R.id.password);
        sessionManager = new SessionManager(getApplicationContext());
    }

    public void onLoginClicked(View view){
        int flag =0;
        userName = username.getText().toString();
        passWord = password.getText().toString();
        if(userName.length()<4){
            username.setText(null);
            username.setHint("Enter your email address");
        } else flag++;
        if(passWord.length()<4){
            password.setText(null);
            password.setHint("Enter your password");
        } else flag++;
        if(flag==2){
            new LoginVerification().execute(userName,passWord);
        }
    }

    public void onRegisterClicked(View v){
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }

    private class LoginVerification extends AsyncTask<String,Void,Boolean> {
        String pass;
        ProgressDialog loading;
        protected void onPreExecute(){
            loading = ProgressDialog.show(LoginActivity.this,"Verifying...","Wait...",false,false);
        }

        @Override
        protected Boolean doInBackground(String... args){
            RequestHandler rh = new RequestHandler();
            String s = rh.sendGetRequestParam(Config.URL_GET_USER, args[0]);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
                JSONObject c = result.getJSONObject(0);
                NAME = c.getString(Config.TAG_NAME);
                TYPE = c.getString(Config.TAG_TYPE);
                EMAIL = c.getString(Config.TAG_EMAIL);
                pass = c.getString(Config.TAG_PASS);
                ADDRESS = c.getString(Config.TAG_ADDRESS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(EMAIL!=null&&pass!=null){
                if (EMAIL.equals(args[0])&&pass.equals(args[1])){
                    return true;
                } else return false;
            } else return false;
        }

        protected void onPostExecute(Boolean success){
            loading.dismiss();
            if(success){
                sessionManager.loginSession(EMAIL,NAME,TYPE,ADDRESS);
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
            } else Toast.makeText(LoginActivity.this,"Incorrect email address/password",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Exit MedConnect...");
        alertDialogBuilder
                .setMessage("Are you sure?")
                .setCancelable(false)
                .setPositiveButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                })
                .setNegativeButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                moveTaskToBack(true);
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
