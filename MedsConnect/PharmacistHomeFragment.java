package com.mycompany.medsconnect;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class PharmacistHomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pharmacist_home, container, false);
    }


    @Override
    public void onStart(){
        super.onStart();
        View view = getView();
        ImageButton imageViewrecpres = (ImageButton) view.findViewById(R.id.recpres);
        imageViewrecpres.setOnClickListener(new RecPresClickedListener());
        ImageButton imageViewrecorders = (ImageButton) view.findViewById(R.id.recorders);
        imageViewrecorders.setOnClickListener(new RecOrdersClickedListener());
        ImageButton imageViewmed = (ImageButton) view.findViewById(R.id.meddetails);
        imageViewmed.setOnClickListener(new MedDetailsClickedListener());
    }

    private class RecPresClickedListener implements View.OnClickListener{
        public void onClick(View view){
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout,new ReceivedPrescriptionFragment(),"visible");
            transaction.addToBackStack(null);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.commit();
        }
    }

    private class RecOrdersClickedListener implements View.OnClickListener{
        public void onClick(View view){
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout,new ReceivedOrdersFragment(),"visible");
            transaction.addToBackStack(null);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.commit();
        }
    }

    private class MedDetailsClickedListener implements View.OnClickListener{
        public void onClick(View view){
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout,new MedDetailsFragment(),"visible");
            transaction.addToBackStack(null);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.commit();
        }
    }

}
