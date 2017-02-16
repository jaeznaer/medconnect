package com.mycompany.medsconnect;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class AccountFragment extends Fragment {
    private SessionManager sessionManager;
    private String[] details;
    private EditText nam,add,typ,ema,pass;
    private String name,address,type,email,password;
    private Button update;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onStart(){
        super.onStart();
        View view = getView();
        sessionManager = new SessionManager(getActivity());
        details = sessionManager.getUserDetails();
        nam = (EditText) view.findViewById(R.id.name);
        add = (EditText) view.findViewById(R.id.address);
        typ = (EditText) view.findViewById(R.id.type);
        ema = (EditText) view.findViewById(R.id.user_name);
        pass = (EditText) view.findViewById(R.id.password);
        update = (Button) view.findViewById(R.id.update);
        update.setOnClickListener(new UpdateListener());

        new Details().execute(details[0]);
    }

    private class Details extends AsyncTask<String,Void,Boolean> {
        protected void onPreExecute(){}

        protected Boolean doInBackground(String... args){
            RequestHandler rh = new RequestHandler();
            String s = rh.sendGetRequestParam(Config.URL_GET_USER,args[0]);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
                JSONObject c = result.getJSONObject(0);
                name = c.getString(Config.TAG_NAME);
                address = c.getString(Config.TAG_ADDRESS);
                type = c.getString(Config.TAG_TYPE);
                email = c.getString(Config.TAG_EMAIL);
                password = c.getString(Config.TAG_PASS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }

        protected void onPostExecute(Boolean success){
            if(success){
                nam.setText(name);
                add.setText(address);
                typ.setText(type);
                ema.setText(email);
                pass.setText(password);
            }
        }
    }

    private class UpdateListener implements View.OnClickListener {
        public void onClick(View v) {
            name = nam.getText().toString();
            address = add.getText().toString();
            type = typ.getText().toString();
            email = ema.getText().toString();
            password = pass.getText().toString();

            class UpdateUser extends AsyncTask<Void, Void, String> {
                ProgressDialog loading;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loading = ProgressDialog.show(getActivity(), "Updating...", "Wait...", false, false);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    loading.dismiss();
                    Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                }

                @Override
                protected String doInBackground(Void... params) {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put(Config.KEY_NAME, name);
                    hashMap.put(Config.KEY_ADDRESS, address);
                    hashMap.put(Config.KEY_TYPE, type);
                    hashMap.put(Config.KEY_EMAIL, email);
                    hashMap.put(Config.KEY_PASS, password);

                    RequestHandler rh = new RequestHandler();

                    String s = rh.sendPostRequest(Config.URL_UPDATE_USER, hashMap);

                    return s;
                }
            }

            UpdateUser ue = new UpdateUser();
            ue.execute();
        }
    }

}
