package com.mycompany.medsconnect;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class CartFragment extends Fragment {

    ArrayList<Product> cart_list = new ArrayList<Product>();
    SQLiteDatabase sqLite;
    int count=0;
    int totalCartItemCount =0;
    int totalCartValue = 0;
    View myFragmentView;
    final String[] qtyValues = {"1","2","3","4","5","6","7","8","9","10"};
    ListView lv1;
    BaseAdapter adapter;
    TextView itemText,itemCount,shippingText,shippingAmount,totalAmount,cartEmpty;
    Button checkout;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myFragmentView = inflater.inflate(R.layout.fragment_cart, container, false);

        getCartData();
        totalCartItemCount = cart_list.size();
        totalCartValue =0;
        for (int temp1=0; temp1 < cart_list.size(); temp1++)
        {
            totalCartValue = totalCartValue + Integer.parseInt(cart_list.get(temp1).getProductValue());
        }
        CustomerTopActivity activity = (CustomerTopActivity) getActivity();


        itemText = (TextView) myFragmentView.findViewById(R.id.item_text);
        itemCount = (TextView) myFragmentView.findViewById(R.id.item_count);
        shippingText = (TextView) myFragmentView.findViewById(R.id.shipping_text);
        shippingAmount = (TextView) myFragmentView.findViewById(R.id.shipping_amount);
        totalAmount = (TextView) myFragmentView.findViewById(R.id.total_amount);
        checkout = (Button) myFragmentView.findViewById(R.id.checkout);
        checkout.setOnClickListener(new OrderListener());
        lv1=(ListView) myFragmentView.findViewById(R.id.listView1);
        cartEmpty = (TextView) myFragmentView.findViewById(R.id.cart_empty);

        if (totalCartItemCount == 0)
        {
            itemText.setVisibility(myFragmentView.INVISIBLE);
            itemCount.setVisibility(myFragmentView.INVISIBLE);
            shippingText.setVisibility(myFragmentView.INVISIBLE);
            shippingAmount.setVisibility(myFragmentView.INVISIBLE);
            totalAmount.setVisibility(myFragmentView.INVISIBLE);
            checkout.setVisibility(myFragmentView.INVISIBLE);
            lv1.setVisibility(myFragmentView.INVISIBLE);
            cartEmpty.setVisibility(myFragmentView.VISIBLE);
        }

        else
        {
            itemText.setVisibility(myFragmentView.VISIBLE);
            itemCount.setVisibility(myFragmentView.VISIBLE);
            shippingText.setVisibility(myFragmentView.VISIBLE);
            shippingAmount.setVisibility(myFragmentView.VISIBLE);
            totalAmount.setVisibility(myFragmentView.VISIBLE);
            checkout.setVisibility(myFragmentView.VISIBLE);
            lv1.setVisibility(myFragmentView.VISIBLE);
            cartEmpty.setVisibility(myFragmentView.INVISIBLE);

        }


        itemCount.setText("(" + totalCartItemCount + ")");
        if (totalCartValue>500)
        {
            shippingAmount.setText("Free");
            totalAmount.setText("Rs "+ totalCartValue);
        }
        else
        {
            shippingAmount.setText("Rs 50");
            totalAmount.setText("Rs "+ (totalCartValue+50));
        }


        adapter = new custom_list_one(this.getActivity(),cart_list);
        lv1.setAdapter(adapter);

        return myFragmentView;
    }
    
    class custom_list_one extends BaseAdapter
    {
        private LayoutInflater layoutInflater;
        ViewHolder holder;
        private ArrayList<Product> cartList=new ArrayList<Product>();
        int cartCounter;
        Typeface type;
        Context context;

        public custom_list_one(Context context, ArrayList<Product> cart_list) {
            layoutInflater = LayoutInflater.from(context);
            this.cartList=cart_list;
            this.cartCounter= cartList.size();
            this.context = context;
        }

        @Override
        public int getCount() {

            return cartCounter;
        }

        @Override
        public Object getItem(int arg0) {

            return cartList.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {

            return arg0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            Product tempProduct = cart_list.get(position);


            if (convertView == null)
            {
                convertView = layoutInflater.inflate(R.layout.listone_custom, null);
                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.product_name);
                holder.product_mrp = (TextView) convertView.findViewById(R.id.product_mrp);
                holder.product_mrpvalue = (TextView) convertView.findViewById(R.id.product_mrpvalue);
                holder.qty = (Spinner) convertView.findViewById(R.id.spinner1);
                holder.cancel = (ImageButton) convertView.findViewById(R.id.delete);
                holder.product_value = (TextView) convertView.findViewById(R.id.product_value);
                holder.qty_text =(TextView) convertView.findViewById(R.id.qty_text);

                convertView.setTag(holder);
            }
            else
            {
                holder = (ViewHolder) convertView.getTag();
            }


            holder.name.setText(tempProduct.getProductName());

            holder.product_mrpvalue.setText("Rs " + tempProduct.getProductMRP());
            
            ArrayAdapter<String> aa=new ArrayAdapter<String>(context,R.layout.qty_spinner_item,qtyValues);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            holder.qty.setAdapter(aa);

            holder.qty.setSelection(Integer.parseInt(tempProduct.getProductQty()) - 1);


            holder.product_value.setText("Rs " + Integer.parseInt(tempProduct.getProductValue()) + "");




            holder.cancel.setOnClickListener(new MyPersonalClickListener("button_delete",tempProduct));
            
            holder.qty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

                @Override
                public void onItemSelected(AdapterView<?> parent, View view,int selectionIndex, long id)
                {
                    //if user has changed the quantity, then save it in the DB. refresh cart_list  

                    if ((parent.getSelectedItemPosition()+1) != Integer.parseInt(cart_list.get(position).getProductQty()))
                    {

                        SQLiteOpenHelper helper = new MedsConnectDBHelper(getActivity());
                        sqLite = helper.getReadableDatabase();
                        sqLite.execSQL("UPDATE CART SET PRODUCT_QTY ='"+ (parent.getSelectedItemPosition()+1)+"' WHERE NAME ="+ DatabaseUtils.sqlEscapeString(cart_list.get(position).getProductName()));
                        sqLite.execSQL("UPDATE CART SET TOTALCOST='" + (parent.getSelectedItemPosition()+1) * Integer.parseInt(cart_list.get(position).getProductMRP())  +"' WHERE NAME ="+ DatabaseUtils.sqlEscapeString(cart_list.get(position).getProductName()));
                        sqLite.close();
                        getCartData();

                        notifyDataSetChanged();

                        //refresh data outside the listview - Cart Total, Total Items, Shipping Cost etc  
                        View parentView = (View) view.getParent().getParent().getParent().getParent();

                        TextView txtTotalAmount = (TextView) parentView.findViewById(R.id.total_amount);
                        TextView txtTotalItems = (TextView) parentView.findViewById(R.id.item_count);
                        TextView txtShippingAmt = (TextView) parentView.findViewById(R.id.shipping_amount);

                        totalCartItemCount = cart_list.size();
                        totalCartValue =0;

                        for (int temp1=0; temp1 < cart_list.size(); temp1++)
                        {
                            totalCartValue = totalCartValue + Integer.parseInt(cart_list.get(temp1).getProductValue());
                        }

                        txtTotalItems.setText("("+ totalCartItemCount + ")");

                        if (totalCartValue> 500)
                        {
                            txtShippingAmt.setText("Free");
                            txtTotalAmount.setText("Rs "+ totalCartValue);
                        }
                        else
                        {
                            txtShippingAmt.setText("Rs 50");
                            txtTotalAmount.setText("Rs "+ (totalCartValue+50));
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0)
                {

                }
            });

            return convertView;
        }
        class ViewHolder
        {
            TextView name;
            TextView product_mrp;
            TextView product_mrpvalue;
            TextView qty_text;
            TextView product_value;
            ImageButton cancel;
            Spinner qty;

        }

    }
    
    public class MyPersonalClickListener implements View.OnClickListener
    {

        String button_name;
        Product prod_name;
        int tempQty;
        int tempValue;

        public MyPersonalClickListener(String button_name, Product prod_name)
        {
            this.prod_name = prod_name;
            this.button_name = button_name;
        }

        @Override
        public void onClick(View v)
        {

            if (button_name == "button_delete")
            {

                SQLiteOpenHelper helper = new MedsConnectDBHelper(getActivity());
                sqLite = helper.getWritableDatabase();
                sqLite.execSQL("DELETE FROM CART WHERE NAME ="+DatabaseUtils.sqlEscapeString(prod_name.getProductName()));
                sqLite.close();
                Toast.makeText(getActivity(), "Item " + prod_name.getProductName() + " deleted from Cart", Toast.LENGTH_SHORT).show();
                getCartData();

                View lView = (View) v.getParent().getParent();

                ((ListView) lView).setAdapter(new custom_list_one(getActivity(),cart_list));


                TextView txtTotalAmount = (TextView) myFragmentView.findViewById(R.id.total_amount);
                TextView txtTotalItems = (TextView) myFragmentView.findViewById(R.id.item_count);
                TextView txtShippingAmt = (TextView) myFragmentView.findViewById(R.id.shipping_amount);
                TextView itemText = (TextView) myFragmentView.findViewById(R.id.item_text);
                TextView shippingText = (TextView) myFragmentView.findViewById(R.id.shipping_text);
                Button checkout = (Button) myFragmentView.findViewById(R.id.checkout);
                ListView lv1=(ListView) myFragmentView.findViewById(R.id.listView1);
                TextView cartEmpty = (TextView) myFragmentView.findViewById(R.id.cart_empty);

                totalCartItemCount = cart_list.size();
                totalCartValue =0;
                for (int temp1=0; temp1 < cart_list.size(); temp1++)
                {
                    totalCartValue = totalCartValue + Integer.parseInt(cart_list.get(temp1).getProductValue());
                }

                txtTotalItems.setText("("+ totalCartItemCount + ")");

                if (totalCartValue> 500)
                {
                    txtShippingAmt.setText("Free");
                    txtTotalAmount.setText("Rs "+ totalCartValue);
                }
                else
                {
                    txtShippingAmt.setText("Rs 50");
                    txtTotalAmount.setText("Rs "+ (totalCartValue+50));
                }


                if (totalCartItemCount == 0)
                {
                    itemText.setVisibility(myFragmentView.INVISIBLE);
                    txtTotalItems.setVisibility(myFragmentView.INVISIBLE);
                    shippingText.setVisibility(myFragmentView.INVISIBLE);
                    txtShippingAmt.setVisibility(myFragmentView.INVISIBLE);
                    txtTotalAmount.setVisibility(myFragmentView.INVISIBLE);
                    checkout.setVisibility(myFragmentView.INVISIBLE);
                    lv1.setVisibility(myFragmentView.INVISIBLE);
                    cartEmpty.setVisibility(myFragmentView.VISIBLE);
                }

                else
                {
                    itemText.setVisibility(myFragmentView.VISIBLE);
                    txtTotalItems.setVisibility(myFragmentView.VISIBLE);
                    shippingText.setVisibility(myFragmentView.VISIBLE);
                    txtShippingAmt.setVisibility(myFragmentView.VISIBLE);
                    txtTotalAmount.setVisibility(myFragmentView.VISIBLE);
                    checkout.setVisibility(myFragmentView.VISIBLE);
                    lv1.setVisibility(myFragmentView.VISIBLE);
                    cartEmpty.setVisibility(myFragmentView.INVISIBLE);

                }

            }

        }

    }

    public void getCartData() {

        Product tempCartItem;

        cart_list.clear();
        SQLiteOpenHelper helper = new MedsConnectDBHelper(getActivity());
        sqLite = helper.getReadableDatabase();
        Cursor c=sqLite.rawQuery("SELECT  * FROM CART",null);
        count=0;
        if(c.moveToFirst())
        {
            do{

                tempCartItem = new Product();
                tempCartItem.setProductCode(c.getString(c.getColumnIndex("_id")));
                tempCartItem.setProductName(c.getString(c.getColumnIndex("NAME")));
                tempCartItem.setProductMRP(c.getString(c.getColumnIndex("COST")));
                tempCartItem.setProductQty(c.getString(c.getColumnIndex("PRODUCT_QTY")));
                tempCartItem.setProductValue(c.getString(c.getColumnIndex("TOTALCOST")));
                cart_list.add(tempCartItem);
                count++;
            }while(c.moveToNext());

        }
        c.close();
        sqLite.close();

    }

    private class OrderListener implements View.OnClickListener{
        public void onClick(View view){
            SessionManager sessionManager = new SessionManager(getActivity());
            String[] details = sessionManager.getUserDetails();
            SQLiteOpenHelper helper = new MedsConnectDBHelper(getActivity());
            sqLite = helper.getWritableDatabase();
            sqLite.execSQL("INSERT INTO COUNTER (NOVALUE) VALUES("+null+");");
            Cursor cc = sqLite.rawQuery("SELECT _id FROM COUNTER;",null);
            cc.moveToLast();
            String id = cc.getString(0);
            sqLite.close();

            new Order().execute(details[0] + id, Integer.toString(totalCartValue), details[1], details[3], details[0]);
        }
    }

    public class Order extends AsyncTask<String,Void,Boolean> {
        ProgressDialog loading;
        protected void onPreExecute(){
            super.onPreExecute();
            loading = ProgressDialog.show(getActivity(),"Placing order...","Wait...",false,false);
        }

        protected Boolean doInBackground(String... args){
            HashMap<String,String> params = new HashMap<>();
            params.put(Config.KEY_ORDERID,args[0]);
            params.put(Config.KEY_COST,args[1]);
            params.put(Config.KEY_NAME,args[2]);
            params.put(Config.KEY_ADDRESS,args[3]);
            params.put(Config.KEY_EMAIL,args[4]);

            RequestHandler rh = new RequestHandler();
            String res = rh.sendPostRequest(Config.URL_ADDMAINORDER, params);
            for (int temp1=0; temp1 < cart_list.size(); temp1++)
            {

                HashMap<String,String> para = new HashMap<>();
                para.put(Config.KEY_ORDERID,args[0]);
                para.put(Config.KEY_PRODUCT,cart_list.get(temp1).getProductName());
                para.put(Config.KEY_QUANTITY,cart_list.get(temp1).getProductQty());
                rh.sendPostRequest(Config.URL_ADDORDEREDPRODUCTS,para);
            }
            return true;
        }

        protected void onPostExecute(Boolean success){
            SQLiteOpenHelper helper = new MedsConnectDBHelper(getActivity());
            sqLite = helper.getWritableDatabase();
            sqLite.execSQL("DELETE FROM CART");
            sqLite.close();
            itemText.setVisibility(myFragmentView.INVISIBLE);
            itemCount.setVisibility(myFragmentView.INVISIBLE);
            shippingText.setVisibility(myFragmentView.INVISIBLE);
            shippingAmount.setVisibility(myFragmentView.INVISIBLE);
            totalAmount.setVisibility(myFragmentView.INVISIBLE);
            checkout.setVisibility(myFragmentView.INVISIBLE);
            lv1.setVisibility(myFragmentView.INVISIBLE);
            cartEmpty.setVisibility(myFragmentView.VISIBLE);
            loading.dismiss();
            Toast.makeText(getActivity(),"Order placed",Toast.LENGTH_SHORT).show();
        }
    }
}
