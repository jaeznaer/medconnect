package com.mycompany.medsconnect;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MedDetailsAlternativesFragment extends Fragment {
    ArrayList<String> medDetails;
    ArrayList<String> medAlt;
    TextView a,s,d,f,g,h,j,k,l,z,x;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_med_details_alternatives, container, false);
    }

    @Override
    public void onStart(){
        super.onStart();
        View view = getView();
        medDetails=Medicine.getInstance().getMedDetails();
        medAlt=Medicine.getInstance().getAltMed();
        a = (TextView) view.findViewById(R.id.medTitle);
        s = (TextView) view.findViewById(R.id.packq);
        d = (TextView) view.findViewById(R.id.packt);
        f = (TextView) view.findViewById(R.id.manu);
        g = (TextView) view.findViewById(R.id.price);
        h = (TextView) view.findViewById(R.id.alttitle);
        j = (TextView) view.findViewById(R.id.altpackq);
        k = (TextView) view.findViewById(R.id.altpackt);
        l = (TextView) view.findViewById(R.id.altprice);
        z = (TextView) view.findViewById(R.id.conname);
        x = (TextView) view.findViewById(R.id.constrength);
        a.setText(medDetails.get(0));
        s.setText(medDetails.get(1));
        d.setText(medDetails.get(2));
        f.setText(medDetails.get(3));
        g.setText("Rs. "+medDetails.get(4));
        z.setText(medDetails.get(5));
        x.setText(medDetails.get(6));
        h.setText(medAlt.get(0));
        j.setText(medAlt.get(1));
        k.setText(medAlt.get(2));
        l.setText("Rs. "+medAlt.get(3));
    }

}
