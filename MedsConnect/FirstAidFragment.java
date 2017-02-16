package com.mycompany.medsconnect;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class FirstAidFragment extends Fragment {

    Cursor newCursor;
    SQLiteDatabase db;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView)inflater.inflate(
                R.layout.fragment_products_material, container, false);

        try {
            SQLiteOpenHelper helper = new MedsConnectDBHelper(getActivity());
            db = helper.getReadableDatabase();
            newCursor = db.query("PRODUCT", new String[]{"_id", "NAME", "IMAGE"}, "TYPE2=?", new String[] {"First Aid"}, null, null, null);
            MeowAdapter adapter = new MeowAdapter(getActivity(),newCursor);
            recyclerView.setAdapter(adapter);
            adapter.setListener(new MeowAdapter.Listener() {
                public void onClick(String name) {
                    Medicine.getInstance().setProductName(name);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_layout,new ProductDetailFragment(),"visible");
                    transaction.addToBackStack(null);
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    transaction.commit();
                }
            });
            GridLayoutManager manager = new GridLayoutManager(getActivity(),2);
            recyclerView.setLayoutManager(manager);
        } catch (SQLiteException e){
            Toast.makeText(getActivity(),"errrpr",Toast.LENGTH_SHORT).show();
        }
        return recyclerView;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        newCursor.close();
        db.close();
    }

}
