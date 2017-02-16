package com.mycompany.medsconnect;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class OrderedProductsFragment extends Fragment {
    private ListView listView;

    private String JSON_STRING;


    ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ordered_products, container, false);
    }

    @Override
    public void onStart(){
        super.onStart();
        View view = getView();
        listView = (ListView) view.findViewById(R.id.listView);
        getJSON();
    }

    private void showOrder(){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String product = jo.getString(Config.TAG_PRODUCT);
                String quantity = jo.getString(Config.TAG_QUANTITY);

                HashMap<String,String> orders = new HashMap<>();
                orders.put(Config.TAG_PRODUCT, product);
                orders.put(Config.TAG_QUANTITY, quantity);
                list.add(orders);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        BaseAdapter adapter = new custom_list_three(this.getActivity(),list);

        listView.setAdapter(adapter);
    }

    class custom_list_three extends BaseAdapter
    {
        private LayoutInflater layoutInflater;
        ViewHolder holder;
        private ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        int listCounter;
        Context context;

        public custom_list_three(Context context, ArrayList<HashMap<String,String>> list) {
            layoutInflater = LayoutInflater.from(context);
            this.list=list;
            this.listCounter= list.size();
            this.context = context;
        }

        @Override
        public int getCount() {

            return listCounter;
        }

        @Override
        public Object getItem(int arg0) {

            return list.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {

            return arg0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {

            if (convertView == null)
            {
                convertView = layoutInflater.inflate(R.layout.listthree_custom, null);
                holder = new ViewHolder();
                holder.product = (TextView) convertView.findViewById(R.id.product);
                holder.quantity = (TextView) convertView.findViewById(R.id.quantity);

                convertView.setTag(holder);
            }
            else
            {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.product.setText(list.get(position).get(Config.TAG_PRODUCT));
            holder.quantity.setText(list.get(position).get(Config.TAG_QUANTITY));

            return convertView;
        }
        class ViewHolder
        {
            TextView product;
            TextView quantity;

        }

    }

    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(),"Fetching Data","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showOrder();
            }

            @Override
            protected String doInBackground(Void... params) {
                String id = Medicine.getInstance().getOrderid();
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.URL_GETORDEREDPRODUCTS, id);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

}
