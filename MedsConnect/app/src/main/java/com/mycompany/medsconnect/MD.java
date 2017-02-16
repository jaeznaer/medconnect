package com.mycompany.medsconnect;

/**
 * Created by joel on 28-02-2016.
 */

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MD {

    static String url;
    static JSONArray medicineSuggestions = null;
    static JSONArray medicineDetails = null;
    static JSONArray medicineAlternatives = null;
    static String key = "535d576e67014d002b06f1bd7adcf7";

    public static ArrayList<String> getMedicineSuggestions(String find,String key)
    {
        ArrayList<String> listOfSuggestions= new ArrayList<String> ();
        try {

            find= URLEncoder.encode(find, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        url = "http://www.truemd.in/api/medicine_suggestions/?id="+find+"&key="+key+"&limit=100";

        RequestHandler sh = new RequestHandler();

        // Making a request to url and getting response
        String jsonStr = sh.sendGetRequest(url);

        Log.d("Response: ", "> " + jsonStr);

        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                JSONObject object = jsonObj.getJSONObject("response");

                // Getting JSON Array node
                medicineSuggestions = object.getJSONArray("suggestions");

                // looping through All Contacts
                for (int i = 0; i < medicineSuggestions.length(); i++) {
                    JSONObject c = medicineSuggestions.getJSONObject(i);


                    String medicineName = c.getString("suggestion");
                    listOfSuggestions.add(medicineName);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any medicine suggestions from the url");
        }


        return listOfSuggestions;
    }
    public static Medicine getMedicineDetails(String find, String key)
    {
        Medicine medicineDetailsObject= Medicine.getInstance();
        try {

            find= URLEncoder.encode(find, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        url = "http://www.truemd.in/api/medicine_details/?id="+find+"&key="+key+"&limit=100";

        RequestHandler sh = new RequestHandler();

        // Making a request to url and getting response
        String jsonStr = sh.sendGetRequest(url);

        Log.d("Response: ", "> " + jsonStr);

        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                JSONObject object = jsonObj.getJSONObject("response");

                // Getting JSON Array node
                JSONObject c = object.getJSONObject("medicine");

                    medicineDetailsObject.setManufacturer(c.getString("manufacturer"));
                    medicineDetailsObject.setBrand(c.getString("brand"));
                    medicineDetailsObject.setCategory(c.getString("category"));
                    medicineDetailsObject.setDClass(c.getString("d_class"));
                    medicineDetailsObject.setUnitQty(c.getString("unit_qty"));
                    medicineDetailsObject.setUnitType(c.getString("unit_type"));
                    medicineDetailsObject.setPackageQty(c.getString("package_qty"));
                    medicineDetailsObject.setPackageType(c.getString("package_type"));
                    medicineDetailsObject.setPackagePrice(c.getString("package_price"));
                    medicineDetailsObject.setUnitPrice(c.getString("unit_price"));
                    medicineDetailsObject.setGenericId(c.getString("generic_id"));

                JSONArray con = object.getJSONArray("constituents");
                for (int i = 0; i < con.length(); i++) {
                    JSONObject d = con.getJSONObject(i);
                    medicineDetailsObject.setConstituentName(d.getString("name"));
                    medicineDetailsObject.setConstituentStrength(d.getString("strength"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any medicine details from the url");
        }


        return medicineDetailsObject;
    }
    public static ArrayList<Medicine> getMedicineAlternatives(String find, String key)
    {
        ArrayList<Medicine> medicineAlternativesList=new ArrayList<Medicine>();
        try {

            find= URLEncoder.encode(find, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        url = "http://oaayush-aayush.rhcloud.com/old_api/search.json?id="+find+"&key="+key+"&limit=100";

        RequestHandler sh = new RequestHandler();

        // Making a request to url and getting response
        String jsonStr = sh.sendGetRequest(url);

        Log.d("Response: ", "> " + jsonStr);

        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);

                // Getting JSON Array node
                medicineAlternatives = jsonObj.getJSONArray("drugs");

                // looping through All Contacts
                for (int i = 0; i < medicineAlternatives.length(); i++) {
                    JSONObject c = medicineAlternatives.getJSONObject(0);

                    //String medicineName = c.getString("suggestion");

                    Medicine medicineDetailsObject= Medicine.getInstance();
                    medicineDetailsObject.setManufacturer(c.getString("manufacturer"));
                    medicineDetailsObject.setBrand(c.getString("brand"));
                    medicineDetailsObject.setCategory(c.getString("category"));
                    medicineDetailsObject.setDClass(c.getString("d_class"));
                    medicineDetailsObject.setUnitQty(c.getString("unit_qty"));
                    medicineDetailsObject.setUnitType(c.getString("unit_type"));
                    medicineDetailsObject.setPackageQty(c.getString("package_qty"));
                    medicineDetailsObject.setPackageType(c.getString("package_type"));
                    medicineDetailsObject.setPackagePrice(c.getString("package_price"));
                    medicineDetailsObject.setUnitPrice(c.getString("unit_price"));
                    medicineDetailsObject.setGenericId(c.getString("generic_id"));

                    medicineAlternativesList.add(medicineDetailsObject);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any medicine alternatives from the url");
        }


        return medicineAlternativesList;
    }

}

