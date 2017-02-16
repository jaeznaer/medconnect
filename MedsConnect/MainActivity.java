package com.mycompany.medsconnect;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.mycompany.medsconnect.gcm.RegistrationIntentService;

public class MainActivity extends Activity {
    SessionManager sessionManager;
    String[] details;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(Config.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    Log.v("meow","hi");
                }
            }
        };

        // Registering BroadcastReceiver
        registerReceiver();

        if (checkPlayServices()) {
            Log.v("meow","yo");
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }


        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        details = sessionManager.getUserDetails();
        if(details[2]==null){
            startActivity(new Intent(this,LoginActivity.class));
        } else if(details[2].equals("Customer")){
            startActivity(new Intent(this,CustomerTopActivity.class));
        } else if(details[2].equals("Doctor")){
            startActivity(new Intent(this,DoctorTopActivity.class));
        } else if(details[2].equals("Pharmacist")){
            startActivity(new Intent(this,PharmacistTopActivity.class));
        }

    }

    @Override
    public void onResume(){
        super.onResume();

        registerReceiver();

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        details = sessionManager.getUserDetails();
        if(details[2]==null){
            startActivity(new Intent(this,LoginActivity.class));
        } else if(details[2].equals("Customer")){
            startActivity(new Intent(this,CustomerTopActivity.class));
        } else if(details[2].equals("Doctor")){
            startActivity(new Intent(this,DoctorTopActivity.class));
        } else if(details[2].equals("Pharmacist")){
            startActivity(new Intent(this,PharmacistTopActivity.class));
        }
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }

    private void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(Config.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                Log.v("meow","bow");
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }


}
