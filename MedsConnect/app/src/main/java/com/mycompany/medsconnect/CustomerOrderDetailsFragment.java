package com.mycompany.medsconnect;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class CustomerOrderDetailsFragment extends Fragment {
    private TextView nam,add,ema,details;
    private String name,address,email, orderEmail, orderDetails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer_order_details, container, false);
    }

    @Override
    public void onStart(){
        super.onStart();
        View view = getView();
        orderEmail = Medicine.getInstance().getCustomerEmail();
        orderDetails = Medicine.getInstance().getOrderDetails();
        nam = (TextView) view.findViewById(R.id.name);
        add = (TextView) view.findViewById(R.id.address);
        ema = (TextView) view.findViewById(R.id.user_name);
        details = (TextView) view.findViewById(R.id.order_details);
        details.setText(orderDetails);
        new Details().execute(orderEmail);
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
                email = c.getString(Config.TAG_EMAIL);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }

        protected void onPostExecute(Boolean success){
            if(success){
                nam.setText(name);
                add.setText(address);
                ema.setText(email);
            }
        }
    }

}
