package com.udvahana.www.ltracker;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Ltracker extends AppCompatActivity {


    private static final int LOCATION_REQUEST = 1;

    private static final int TURN_ON_DISTANCE = 10;
    //My Home Distance
    private static final double HOME_LONGITUDE = 26.234133d;
    private static final double HOME_LATITUDE = 78.210731d;

    protected LocationManager locationManager;
    protected TextView locationText;

    LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            locationChanged(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
            locationText.setText("Location Provider is Disabled !!");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ltracker);

        Button buttonOn = (Button) findViewById(R.id.button_on);
        Button buttonOff = (Button) findViewById(R.id.button_off);
        locationText = (TextView) findViewById(R.id.location_text);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        buttonOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(Ltracker.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(Ltracker.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
                    return;
                }
                enableLocationupdates();
            }
        });

        buttonOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationText.setText("GPS Disabled !!");
                locationManager.removeUpdates(listener);
            }
        });

    }
    //If user allows the permission to use Location Service
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                             int[] grantResults){
          if(requestCode == LOCATION_REQUEST && grantResults[0]==PackageManager.PERMISSION_GRANTED)
              enableLocationupdates();
    }

    private void enableLocationupdates(){
        final int minTime = 0;
        final int minDistance = 0;
        final Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);  //ACCURACY_COARSE WILL GIVE HIGH BATTERY LIFE
        String provider = locationManager.getBestProvider(criteria, true);
        locationManager.requestLocationUpdates(provider, minTime, minDistance, listener);
    }

    private void locationChanged(Location location){

        //homeLocation
        Location homeLocation = new Location("spoofed");
        //setting homeLocation Manually
        homeLocation.setLongitude(HOME_LONGITUDE);
        homeLocation.setLatitude(HOME_LATITUDE);

        //Finding current location
        float distance = location.distanceTo(homeLocation);
        locationText.setText(Float.toString(distance));
        //if Location withing XX m then turnOn Lights
        //We need to find how far our current location is from our home location
        if(distance <= TURN_ON_DISTANCE)
        toggleLights(true);
        else
            toggleLights(false);
    }

    private void toggleLights(final boolean turnOn){

        AsyncTaskCompat.executeParallel(new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                //Access the Internet
                //turn On the Lights using InterWebs...
                if(turnOn){

                }
                else{

                }
                return turnOn;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                    Toast.makeText(Ltracker.this,aBoolean ?"Lights Turned On !!":"Lights Turned Off !!",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
