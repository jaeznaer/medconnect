package com.mycompany.medsconnect;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;


public class ProductDetailFragment extends Fragment {
    ImageLoader imageLoader = new ImageLoader(getActivity());
    int loader = R.drawable.noimageicon;
    String name,image,cost,quantity,productName;
    EditText text;
    Button orderButton;
    private SessionManager sessionManager;

    SQLiteDatabase sqLite;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_detail, container, false);
    }

    @Override
    public void onStart(){
        super.onStart();
        View view = getView();
        sessionManager = new SessionManager(getActivity());
        productName = Medicine.getInstance().getProductName();
        try{
            SQLiteOpenHelper helper = new MedsConnectDBHelper(getActivity());
            SQLiteDatabase db = helper.getReadableDatabase();
            Cursor cursor = db.query("PRODUCT", new String[]{"NAME","COST","IMAGE"}, "NAME=?", new String[]{productName}, null, null, null);
            cursor.moveToFirst();
            name = cursor.getString(0);
            cost = cursor.getString(1);
            image = cursor.getString(2);
        } catch (SQLiteException e){
            e.printStackTrace();
        }
        TextView textView = (TextView)view.findViewById(R.id.product_text);
        textView.setText(name);
        TextView costText = (TextView) view.findViewById(R.id.product_cost);
        costText.setText("Rs "+cost);
        ImageView imageView = (ImageView) view.findViewById(R.id.product_image);
        imageLoader.DisplayImage(image, loader, imageView);
        imageView.setContentDescription(name);
        text = (EditText) view.findViewById(R.id.quantity);
        orderButton = (Button) view.findViewById(R.id.orderbutton);
        orderButton.setOnClickListener(new OrderListener());
    }

    private class OrderListener implements View.OnClickListener {
        public void onClick(View view) {
            quantity = text.getText().toString();
            if (quantity.length() < 1) {
                Toast.makeText(getActivity(), "Please enter a quantity first", Toast.LENGTH_SHORT).show();
            } else {
                int totalCost = Integer.parseInt(cost)*Integer.parseInt(quantity);
                SQLiteOpenHelper helper = new MedsConnectDBHelper(getActivity());
                sqLite = helper.getWritableDatabase();

                Cursor cc = sqLite.rawQuery("SELECT PRODUCT_QTY FROM CART WHERE NAME ="+ DatabaseUtils.sqlEscapeString(productName)+";", null);

                if (cc.getCount()== 0)
                {
                    //product not already there in cart..add to cart
                    sqLite.execSQL("INSERT INTO CART (NAME,COST,PRODUCT_QTY,TOTALCOST,IMAGE"+
                            ") VALUES("+DatabaseUtils.sqlEscapeString(productName)+","+cost+","+quantity+","+totalCost+",'"+image+"');");

                    Toast.makeText(getActivity(),"Item "+productName+" added to Cart", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity(),"Item "+productName+" is already in Cart", Toast.LENGTH_SHORT).show();
                }
                //String[] details = sessionManager.getUserDetails();
                //new Order().execute(productName, quantity, details[1], details[3], details[0]);
            }
        }
    }

    public class Order extends AsyncTask<String,Void,Boolean> {
        ProgressDialog loading;
        protected void onPreExecute(){
            super.onPreExecute();
            loading = ProgressDialog.show(getContext(),"Placing order...","Wait...",false,false);
        }

        protected Boolean doInBackground(String... args){
            HashMap<String,String> params = new HashMap<>();
            params.put(Config.KEY_PRODUCT,args[0]);
            params.put(Config.KEY_QUANTITY,args[1]);
            params.put(Config.KEY_NAME,args[2]);
            params.put(Config.KEY_ADDRESS,args[3]);
            params.put(Config.KEY_EMAIL,args[4]);

            RequestHandler rh = new RequestHandler();
            String res = rh.sendPostRequest(Config.URL_ADDORDER, params);
            return true;
        }

        protected void onPostExecute(Boolean success){
            loading.dismiss();
            Toast.makeText(getContext(),"Order placed",Toast.LENGTH_SHORT).show();
        }
    }

}
