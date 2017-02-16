package com.mycompany.medsconnect;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.HashMap;

public class    RegisterActivity extends Activity {
    private EditText name,address,username,password,password2;
    private RadioGroup type;
    private RadioButton typeValue;
    private SessionManager sessionManager;
    private String Name, Username;
    private String Type, Address;
    private int flag;
    private TextView category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name = (EditText) findViewById(R.id.name);
        address = (EditText) findViewById(R.id.address);
        username = (EditText) findViewById(R.id.Username);
        password = (EditText) findViewById(R.id.Password);
        password2 = (EditText) findViewById(R.id.Password2);
        type = (RadioGroup) findViewById(R.id.type);
        category = (TextView) findViewById(R.id.category);
        sessionManager = new SessionManager(getApplicationContext());
    }

    public void onRegClicked(View v){
        flag = 0;
        Name = name.getText().toString();
        Address = address.getText().toString();
        typeValue = (RadioButton) findViewById(type.getCheckedRadioButtonId());
        Username = username.getText().toString();
        String Password = password.getText().toString();
        String Password2 = password2.getText().toString();
        if(Name.length()<5) {
            name.setText(null);
            name.setHint("Enter full name");
        } else { flag++;}
        if (Address.length()<15){
            address.setText(null);
            address.setHint("Enter full address");
        } else {flag++;}
        if(typeValue==null){
            category.setText("Select a category");
        } else {
            Type = typeValue.getText().toString();
            flag++;
        }
        if(Username.length()<6) {
            username.setText(null);
            username.setHint("Enter valid email address");
        } else flag++;
        if (Password.length()<6) {
            password.setText(null);
            password.setHint("Password should be at least 6 characters");
        } else flag++;
        if(Password.equals(Password2)) {
            flag++;
        } else {
            password2.setText(null);
            password2.setHint("Passwords do not match");
        }
        if(flag==6){
            new registerVerification().execute(Name,Address,Type,Username,Password);
        }
    }

    private class registerVerification extends AsyncTask<String,Void,Boolean> {
        ProgressDialog loading;
        protected void onPreExecute(){
            super.onPreExecute();
            loading = ProgressDialog.show(RegisterActivity.this,"Registering...","Wait...",false,false);
        }

        protected Boolean doInBackground(String... args){
            HashMap<String,String> params = new HashMap<>();
            params.put(Config.KEY_NAME,args[0]);
            params.put(Config.KEY_ADDRESS,args[1]);
            params.put(Config.KEY_TYPE,args[2]);
            params.put(Config.KEY_EMAIL,args[3]);
            params.put(Config.KEY_PASS,args[4]);

            RequestHandler rh = new RequestHandler();
            String res = rh.sendPostRequest(Config.URL_ADD, params);
            return true;
        }

        protected void onPostExecute(Boolean success){
                loading.dismiss();
                sessionManager.loginSession(Username,Name, Type,Address);
                startActivity(new Intent(RegisterActivity.this,MainActivity.class));
        }
    }
}
