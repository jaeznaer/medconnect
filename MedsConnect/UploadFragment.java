package com.mycompany.medsconnect;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;


public class  UploadFragment extends Fragment {
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    ImageView ivImage;
    Boolean flag = true, imageFlag = false;
    Button upload,add;
    Bitmap bitmap;
    SessionManager sessionManager;
    String[] details;
    EditText detailsText;
    String orderDetails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upload, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        ivImage = (ImageView) view.findViewById(R.id.image);
        add = (Button) view.findViewById(R.id.add);
        add.setOnClickListener(new AddListener());
        detailsText = (EditText) view.findViewById(R.id.orderDetails);
        upload = (Button) view.findViewById(R.id.upload);
        upload.setOnClickListener(new UploadListener());
        sessionManager = new SessionManager(getActivity());
        details = sessionManager.getUserDetails();
        if (Medicine.getInstance().getUploadFlag()==true){
            add.setVisibility(view.INVISIBLE);
            Medicine.getInstance().setUploadFlag(false);
            getImage(Medicine.getInstance().getImageUrl());
            imageFlag = true;
        }
        else {
            add.setVisibility(view.VISIBLE);
            if (flag) {
                selectImage();
            }
        }
    }

    private void getImage(String urlToImage){
        class GetImage extends AsyncTask<String,Void,Drawable> {
            ProgressDialog loading;
            @Override
            protected Drawable doInBackground(String... params) {
                URL url = null;
                Drawable d = null;

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
                bitmap = Bitmap.createBitmap(d.getIntrinsicWidth(),d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                d.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                d.draw(canvas);
                ivImage.setImageBitmap(bitmap);
            }
        }
        GetImage gi = new GetImage();
        gi.execute(urlToImage);
    }

    public class AddListener implements View.OnClickListener{
        public void onClick(View v){
            selectImage();
        }
    }

    private void selectImage() {
        flag = false;
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Prescription");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ivImage.setImageBitmap(thumbnail);
        bitmap = thumbnail;
        imageFlag = true;
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Uri selectedImageUri = data.getData();
        String[] projection = { MediaColumns.DATA };
        CursorLoader loader = new CursorLoader(getActivity(),selectedImageUri, projection, null, null,null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
        cursor.moveToFirst();

        String selectedImagePath = cursor.getString(column_index);

        Bitmap bm;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        final int REQUIRED_SIZE = 200;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                && options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(selectedImagePath, options);

        ivImage.setImageBitmap(bm);
        bitmap=bm;
        imageFlag = true;
    }

    public class UploadListener implements View.OnClickListener {
        public void onClick(View view) {
            if (imageFlag) {
                new UploadImage().execute(bitmap);
            } else {
                Toast.makeText(getActivity(), "Add prescription first", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class UploadImage extends AsyncTask<Bitmap, Void, String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Uploading Image", "Please wait...", true, true);
                orderDetails = detailsText.getText().toString();
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
                data.put(Config.KEY_EMAIL, details[0]);
                data.put(Config.KEY_IMAGE, uploadImage);
                data.put(Config.KEY_ORDER_DETAILS, orderDetails);

                String result = rh.sendPostRequest(Config.URL_UPLOAD, data);

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