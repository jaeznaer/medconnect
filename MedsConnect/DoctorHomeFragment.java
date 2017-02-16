package com.mycompany.medsconnect;


import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorHomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_doctor_home, container, false);
    }

    @Override
    public void onStart(){
        super.onStart();
        View view = getView();
        ImageButton imageUpload = (ImageButton) view.findViewById(R.id.upload);
        imageUpload.setOnClickListener(new UploadClickedListener());
        ImageButton imageViewmed = (ImageButton) view.findViewById(R.id.meddetails);
        imageViewmed.setOnClickListener(new MedDetailsClickedListener());
    }

    private class UploadClickedListener implements View.OnClickListener{
        public void onClick(View view){
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout,new EPrescriptionFragment(),"visible");
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
