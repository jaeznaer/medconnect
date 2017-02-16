package com.mycompany.medsconnect;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MedSuggestionsFragment extends Fragment {
    String[] medNames;
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_med_suggestions, container, false);
    }

    @Override
    public void onStart(){
        super.onStart();
        View view = getView();
        medNames = Medicine.getInstance().getMyData();
        listView = (ListView) view.findViewById(R.id.medselect);
        listView.setAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,
                medNames));
        listView.setOnItemClickListener(new MedItemClickListener());
    }

    private class MedItemClickListener implements
            ListView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View
                view, int position, long id) {
            new GetMedDetails().execute(medNames[position]);
        }
    }

    private class GetMedDetails extends
            AsyncTask<String,Void,Boolean> {
        ArrayList<String> medDetails = new ArrayList<String>();
        ArrayList<String> altMed = new ArrayList<>();
        ProgressDialog loading;

        protected void onPreExecute(){
            loading = ProgressDialog.show
                    (getActivity(), "Fetching details", "Please wait...", true, true);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            Medicine medicine = MD.getMedicineDetails(params
                    [0],MD.key);
            medDetails.add(medicine.getBrand());
            medDetails.add(medicine.getPackageQty());
            medDetails.add(medicine.getPackageType());
            medDetails.add(medicine.getManufacturer());
            medDetails.add(medicine.getPackagePrice());
            medDetails.add(medicine.getConstituentName());
            medDetails.add(medicine.getConstituentStrength());

            ArrayList<Medicine> medicineAlternatives=
                    MD.getMedicineAlternatives(params[0],MD.key);
            altMed.add(medicineAlternatives.get(0).getBrand());
            altMed.add(medicineAlternatives.get(0).getPackageQty());
            altMed.add(medicineAlternatives.get(0).getPackageType());
            altMed.add(medicineAlternatives.get(0).getPackagePrice());
            return true;
        }

        protected void onPostExecute(Boolean b){
            loading.dismiss();
            Medicine.getInstance().setMedDetails(medDetails);
            Medicine.getInstance().setAltMed(altMed);

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, new MedDetailsAlternativesFragment(), "visible");
            transaction.addToBackStack(null);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.commit();
        }
    }

}
