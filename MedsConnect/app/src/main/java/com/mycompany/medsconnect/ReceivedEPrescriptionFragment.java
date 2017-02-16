package com.mycompany.medsconnect;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ReceivedEPrescriptionFragment extends Fragment implements View.OnClickListener{
    private String imagesJSON;

    private static final String JSON_ARRAY ="result";
    private static final String IMAGE_URL = "url";
    private static final String IMAGE_USER_ID = "id";
    private static String email, details;

    private JSONArray arrayImages= null;

    private int TRACK;

    private Button buttonFetchImages;
    private Button buttonMoveNext;
    private Button buttonMovePrevious, buttonDetails;
    private ImageView imageView;
    private View view;
    Drawable d;
    SessionManager sessionManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_received_eprescription, container, false);
    }

    @Override
    public void onStart(){
        super.onStart();
        view = getView();
        sessionManager = new SessionManager(getActivity());
        imageView = (ImageView) view.findViewById(R.id.imageView);
        buttonFetchImages = (Button) view.findViewById(R.id.buttonFetchImages);
        buttonMoveNext = (Button) view.findViewById(R.id.buttonNext);
        buttonMovePrevious = (Button) view.findViewById(R.id.buttonPrev);
        buttonDetails = (Button) view.findViewById(R.id.userdetails);
        buttonFetchImages.setOnClickListener(this);
        buttonMoveNext.setOnClickListener(this);
        buttonMovePrevious.setOnClickListener(this);
        buttonDetails.setOnClickListener(this);
    }

    private void extractJSON(){
        try {
            JSONObject jsonObject = new JSONObject(imagesJSON);
            arrayImages = jsonObject.getJSONArray(JSON_ARRAY);
            TRACK = arrayImages.length()-1;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showImage(){
        try {
            JSONObject jsonObject = arrayImages.getJSONObject(TRACK);
            String urlString = jsonObject.getString(Config.TAG_ID);
            new DisplayUserDetails().execute(urlString);
            getImage(jsonObject.getString(IMAGE_URL));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void moveNext(){
        if(TRACK < arrayImages.length()){
            TRACK++;
            showImage();
        }
    }

    private void movePrevious(){
        if(TRACK>0){
            TRACK--;
            showImage();
        }
    }



    private void getAllImages() {
        class GetAllImages extends AsyncTask<String,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Fetching Data...","Please Wait...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                imagesJSON = s;
                extractJSON();
                showImage();
            }

            @Override
            protected String doInBackground(String... params) {
                RequestHandler rh = new RequestHandler();
                String[] details = sessionManager.getUserDetails();
                return rh.sendGetRequestParam(params[0],details[0]);
            }
        }
        GetAllImages gai = new GetAllImages();
        gai.execute(Config.URL_RECEIVE);
    }

    private void getImage(String urlToImage){
        Medicine.getInstance().setImageUrl(urlToImage);
        class GetImage extends AsyncTask<String,Void,Drawable> {
            ProgressDialog loading;
            @Override
            protected Drawable doInBackground(String... params) {
                URL url = null;
                Bitmap image = null;

                String urlToImage = params[0];
                try {
                    url = new URL(urlToImage);
                    d = Drawable.createFromStream(url.openConnection().getInputStream(),"something.png");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return d;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Downloading Image...", "Please wait...", true, true);
            }

            @Override
            protected void onPostExecute(Drawable d) {
                super.onPostExecute(d);
                loading.dismiss();
                imageView.setImageDrawable(d);
            }
        }
        GetImage gi = new GetImage();
        gi.execute(urlToImage);
    }

    @Override
    public void onClick(View v) {
        if(v == buttonFetchImages) {
            getAllImages();
        }
        if(v == buttonMoveNext){
            if(email!=null) moveNext();
            else Toast.makeText(getActivity(), "Fetch prescriptions first", Toast.LENGTH_SHORT).show();
        }
        if(v== buttonMovePrevious){
            if(email!=null) movePrevious();
            else Toast.makeText(getActivity(),"Fetch prescriptions first",Toast.LENGTH_SHORT).show();
        }
        if(v==buttonDetails) {
            if(email!=null) getUserDetails();
            else Toast.makeText(getActivity(),"Fetch prescriptions first",Toast.LENGTH_SHORT).show();
        }
    }

    public class DisplayUserDetails extends AsyncTask<String,Void,String>{
        protected void onPreExecute(){}

        protected void onPostExecute(String b){
        }

        protected String doInBackground(String... args){
            RequestHandler requestHandler = new RequestHandler();
            String response = requestHandler.sendGetRequestParam(Config.URL_IMAGE_EMAIL,args[0]);
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
                JSONObject c = result.getJSONObject(0);
                email = c.getString(Config.TAG_EMAIL);
                details = c.getString(Config.TAG_ORDER_DETAILS);
                return email;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void getUserDetails(){
        Medicine.getInstance().setUploadFlag(true);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, new UploadFragment(), "visible");
        transaction.addToBackStack(null);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        email=null;
    }
}
