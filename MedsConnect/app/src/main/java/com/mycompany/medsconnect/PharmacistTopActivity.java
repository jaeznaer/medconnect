package com.mycompany.medsconnect;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PharmacistTopActivity extends Activity {
    private SessionManager sessionManager;
    private String[] titles, listTitles;
    private ListView drawerList;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);
        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.checkLogin();
        listTitles = getResources().getStringArray(R.array.pharmacist_list_titles);
        drawerList = (ListView) findViewById(R.id.drawer);
        drawerList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_activated_1,
                listTitles));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(savedInstanceState!=null){
            currentPosition = savedInstanceState.getInt("position");
            setActionBarTitle(currentPosition);
        } else {
            selectItem(0);
        }
        toggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.drawerOpen,R.string.drawerClose);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                FragmentManager manager = getFragmentManager();
                Fragment fragment = manager.findFragmentByTag("visible");
                if(fragment instanceof PharmacistHomeFragment){
                    currentPosition = 0;
                }
                if(fragment instanceof ReceivedPrescriptionFragment){
                    currentPosition = 1;
                }
                if(fragment instanceof ReceivedOrdersFragment){
                    currentPosition =2;
                }
                if(fragment instanceof MedDetailsFragment){
                    currentPosition =3;
                }
                if(fragment instanceof AccountFragment){
                    currentPosition =4;
                }
                if(fragment instanceof MedSuggestionsFragment){
                    currentPosition = 5;
                }
                if(fragment instanceof MedDetailsAlternativesFragment){
                    currentPosition = 6;
                }
                if(fragment instanceof CustomerOrderDetailsFragment){
                    currentPosition = 7;
                }
                if(fragment instanceof OrderedProductsFragment){
                    currentPosition = 8;
                }
                setActionBarTitle(currentPosition);
                drawerList.setItemChecked(currentPosition,true);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt("position", currentPosition);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id){
            selectItem(position);
        }
    }

    private void selectItem(int position){
        currentPosition = position;
        Fragment fragment;
        switch (position){
            case 1:
                fragment = new ReceivedPrescriptionFragment();
                break;
            case 2:
                fragment = new ReceivedOrdersFragment();
                break;
            case 3:
                fragment = new MedDetailsFragment();
                break;
            case 4:
                fragment = new AccountFragment();
                break;
            case 5:
                fragment = new MedSuggestionsFragment();
                break;
            case 6:
                fragment = new MedDetailsAlternativesFragment();
                break;
            case 7:
                fragment = new CustomerOrderDetailsFragment();
                break;
            case 8:
                fragment = new OrderedProductsFragment();
                break;
            default:
                fragment = new PharmacistHomeFragment();
        }
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout,fragment,"visible");
        transaction.addToBackStack(null);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
        setActionBarTitle(position);
        drawerLayout.closeDrawer(drawerList);
    }

    private void setActionBarTitle(int position){
        String title;
        if(position==0){
            title = getResources().getString(R.string.app_name);
        } else {
            titles = getResources().getStringArray(R.array.pharmacist_titles);
            title = titles[position];
        }
        getActionBar().setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }
        switch (item.getItemId()) {
            case R.id.logout:
                drawerLayout.closeDrawer(drawerList);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Logout...");
                alertDialog
                        .setMessage("Are you sure?")
                        .setCancelable(false)
                        .setPositiveButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        sessionManager.logout();
                                    }
                                });

                AlertDialog alertDial = alertDialog.create();
                alertDial.show();
                return true;
            case R.id.account:
                drawerLayout.closeDrawer(drawerList);
                selectItem(4);
                return true;
            case R.id.exit:
                drawerLayout.closeDrawer(drawerList);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Exit MedConnect...");
                alertDialogBuilder
                        .setMessage("Are you sure?")
                        .setCancelable(false)
                        .setPositiveButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        moveTaskToBack(true);
                                        android.os.Process.killProcess(android.os.Process.myPid());
                                        System.exit(1);
                                    }
                                });

                AlertDialog alert = alertDialogBuilder.create();
                alert.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager manager = getFragmentManager();
        Fragment fragment = manager.findFragmentByTag("visible");
        if(fragment instanceof PharmacistHomeFragment){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Exit MedConnect...");
            alertDialogBuilder
                    .setMessage("Are you sure?")
                    .setCancelable(false)
                    .setPositiveButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.cancel();
                        }
                    })
                    .setNegativeButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    moveTaskToBack(true);
                                    android.os.Process.killProcess(android.os.Process.myPid());
                                    System.exit(1);
                                }
                            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else super.onBackPressed();
    }

}
