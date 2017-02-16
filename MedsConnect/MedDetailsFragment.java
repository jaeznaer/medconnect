package com.mycompany.medsconnect;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MedDetailsFragment extends Fragment {
    EditText editText;
    Button button;
    String med;
    String[] medSugg;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_med_details, container, false);
    }

    @Override
    public void onStart(){
        super.onStart();
        View view = getView();
        editText = (EditText) view.findViewById(R.id.searchmed);
        button = (Button) view.findViewById(R.id.search);
        button.setOnClickListener(new ButtonClicked());
    }

    @Override
    public void onPause(){
        super.onPause();
        editText.setText(null);
        med = null;
    }

    class ButtonClicked implements View.OnClickListener {
        public void onClick(View view) {
            med = editText.getText().toString();
            if (med.length()<1) {
                Toast.makeText(getActivity(), "Please enter a search term", Toast.LENGTH_SHORT).show();
            } else {
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                // check if no view has focus:
                View v = ((Activity) getActivity()).getCurrentFocus();
                if (v == null)
                    return;

                inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                editText.setText(null);
                new MedSuggestions().execute(med);
            }
        }
    }

    private class MedSuggestions extends AsyncTask<String,Void,Boolean> {
        ProgressDialog loading;
        protected void onPreExecute(){
            loading = ProgressDialog.show(getActivity(), "Fetching results", "Please wait...", true, true);
        }

        protected void onPostExecute(Boolean b){
            loading.dismiss();
            if(b) {
                Medicine.getInstance().setMyData(medSugg);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout,new MedSuggestionsFragment(),"visible");
                transaction.addToBackStack(null);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.commit();
            } else Toast.makeText(getActivity(),"No results found",Toast.LENGTH_SHORT).show();
        }

        protected Boolean doInBackground(String... params){
            ArrayList medicineSuggestions= MD.getMedicineSuggestions(params[0], MD.key);
            medSugg = new String[medicineSuggestions.size()];
            medicineSuggestions.toArray(medSugg);
            if(medSugg.length==0) return false;
            else {
                return true;
            }
        }
    }
}
