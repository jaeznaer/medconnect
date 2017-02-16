package com.mycompany.medsconnect;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.HashMap;


public class EPrescriptionFragment extends Fragment {
    RelativeLayout prescriptionView;
    TextView date;
    EditText patient, prescription, email;
    Button send;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_eprescription, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        prescriptionView = (RelativeLayout) view.findViewById(R.id.prescriptionview);
        send = (Button) view.findViewById(R.id.send);
        send.setOnClickListener(new SendListener());
        patient = (EditText) view.findViewById(R.id.patient);
        prescription = (EditText) view.findViewById(R.id.prescription);
        email = (EditText) view.findViewById(R.id.patientemail);
        date = (TextView) view.findViewById(R.id.date);
        Date now = new Date();
        date.setText(now.toString());
    }

    public class SendListener implements View.OnClickListener {
        public void onClick(View view) {
            if (email.getText().length()>1 && patient.getText().length()>1 && prescription.getText().length()>1) {
                Bitmap bitmap = screenShot(prescriptionView);
                new UploadImage().execute(bitmap);
            } else {
                Toast.makeText(getActivity(), "Fill in the details first", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public Bitmap screenShot(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    class UploadImage extends AsyncTask<Bitmap, Void, String> {

        ProgressDialog loading;
        RequestHandler rh = new RequestHandler();
        String patientEmail;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(getActivity(), "Sending", "Please wait...", true, true);
            patientEmail = email.getText().toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loading.dismiss();
            Toast.makeText(getActivity(), "Successfull", Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(Bitmap... params) {
            Bitmap bitmap = params[0];
            String uploadImage = getStringImage(bitmap);

            HashMap<String, String> data = new HashMap<>();
            data.put(Config.KEY_EMAIL, patientEmail);
            data.put(Config.KEY_IMAGE, uploadImage);

            String result = rh.sendPostRequest(Config.URL_SEND, data);

            return result;
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
}
