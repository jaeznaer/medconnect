package com.mycompany.medsconnect;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class ReceivedOrdersFragment extends Fragment {
    private ListView listView;

    private String JSON_STRING;


    ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_received_orders, container, false);
    }

    @Override
    public void onStart(){
        super.onStart();
        View view = getView();
        listView = (ListView) view.findViewById(R.id.listView);
        getJSON();
    }
    @Override
     public void onPause(){
        super.onPause();
        list.clear();
    }

    private void showOrder(){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String orderid = jo.getString(Config.TAG_ORDERID);
                String cost = jo.getString(Config.TAG_COST);
                String name = jo.getString(Config.TAG_NAME);
                String address = jo.getString(Config.TAG_ADDRESS);
                String email = jo.getString(Config.TAG_EMAIL);

                HashMap<String,String> orders = new HashMap<>();
                orders.put(Config.TAG_ORDERID,orderid);
                orders.put(Config.TAG_COST, cost);
                orders.put(Config.TAG_NAME, name);
                orders.put(Config.TAG_ADDRESS, address);
                orders.put(Config.TAG_EMAIL, email);
                list.add(orders);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        BaseAdapter adapter = new custom_list_two(this.getActivity(),list);

        listView.setAdapter(adapter);
    }

    class custom_list_two extends BaseAdapter
    {
        private LayoutInflater layoutInflater;
        ViewHolder holder;
        private ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        int listCounter;
        Context context;

        public custom_list_two(Context context, ArrayList<HashMap<String,String>> list) {
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
                convertView = layoutInflater.inflate(R.layout.listtwo_custom, null);
                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.cost = (TextView) convertView.findViewById(R.id.cost);
                holder.orderid = (TextView) convertView.findViewById(R.id.orderid);
                holder.email =(TextView) convertView.findViewById(R.id.email);
                holder.address =(TextView) convertView.findViewById(R.id.address);
                holder.order = (Button) convertView.findViewById(R.id.orderedprod);

                convertView.setTag(holder);
            }
            else
            {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.orderid.setText(list.get(position).get(Config.TAG_ORDERID));
            holder.email.setText(list.get(position).get(Config.TAG_EMAIL));
            holder.address.setText(list.get(position).get(Config.TAG_ADDRESS));

            holder.name.setText(list.get(position).get(Config.TAG_NAME));

            holder.cost.setText("Rs " + list.get(position).get(Config.TAG_COST));

            holder.order.setOnClickListener(new OrderListener(list.get(position).get(Config.TAG_ORDERID)));

            return convertView;
        }
        class ViewHolder
        {
            TextView name;
            TextView cost;
            TextView orderid;
            TextView email;
            TextView address;
            Button order;

        }

    }

    class OrderListener implements View.OnClickListener{
        String id;
        OrderListener(String id){
            this.id = id;
        }

        @Override
        public void onClick(View v) {
            Medicine.getInstance().setOrderid(id);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout,new OrderedProductsFragment(),"visible");
            transaction.addToBackStack(null);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.commit();
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
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Config.URL_GETORDER);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

}
