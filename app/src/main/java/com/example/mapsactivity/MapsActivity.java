package com.example.mapsactivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationmanager;
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;


    private static final long MIN_TIME_BW_UPDATES = 1000*5;
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATE = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng birth = new LatLng(32.8850, -117.2255);
        mMap.addMarker(new MarkerOptions().position(birth).title("LA JOLLA,CALIFORNIA"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(birth));

        //assignment 2
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager2)


    }


    public void getLocation()
    try {
        locationmanager = (LocationManager)getSystemService(LOCATION_SERVICE);
        //getGPS Status
        isGPSEnabled = locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(isGPSEnabled) Log.d("MyMaps","getLocation:GPS is enabled");

        //get network status
        //you do this

        if(!isGPSEnabled&&!isNetworkEnabled)
            Log.d("MyMap","getLocation:No Provider is enabled!");
        else {
            if (isNetworkEnabled) {

                locationmanager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATE,locationListenerNEtwork);
            }
            if (isGPSEnabled) {

                locationmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATE,locationListenerGPS);
            }
        }
    }
    catch(Exception e)
    {
        Log.d("MyMaps","Caught exception in getLocation");
        e.printStackTrace();

    }




    Location




    public void changeView(View v)
        Log.d("")
}
