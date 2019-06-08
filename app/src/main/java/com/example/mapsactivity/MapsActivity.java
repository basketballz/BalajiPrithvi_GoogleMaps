package com.example.mapsactivity;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private EditText locationSearch;
    private LocationManager locationManager;
    private Location myLocation;
    private boolean isNetworkEnabled = false;
    private boolean isGPSEnabled = false;
    private boolean gotMyLocationOneTime;
    private double lat, longit;
    private int trackMarkerDropCounter=0;
    private boolean notTrackingMyLocation;
    private double previousLatitude;
    private double previousLongitude;
    private boolean canGetLocation=false;

    private static final long MIN_TIME_BW_UPDATES = 1000 * 5;

    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 0.0f;

    private static final int MY_LOC_ZOOM_FACTOR = 17;



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

        locationSearch = (EditText) findViewById(R.id.editText_addr);
        gotMyLocationOneTime = false;
        getLocation();


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d("Mymapapp", "failed fine location permission check");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);

        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d("Mymapapp", "failed coarse location permission check");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 2);

        }
        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) || (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)) {
            mMap.setMyLocationEnabled(true);
        }
    }


        public void changeView (View v  )
        {
            Log.d("MyMApApp", "changing view");
            if (mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL)
            {   mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);}
            else
            {mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);}
        }

        public void onSearch (View v)
        {
            String location = locationSearch.getText().toString();
            List<Address> addressList = null;
            List<Address> addressListZip = null;

            LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String provider = service.getBestProvider(criteria, false);

            Log.d("mymappapp", "Onsearch: location=" + location);
            Log.d("mymappapp", "Onsearch: provider=" + provider);

            LatLng userLocation = null;
            try {
                if (locationManager != null) {
                    Log.d("mymapapp", "onsearch: Locationmanager is not null");
                    if ((myLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)) != null) {
                        userLocation = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                        Toast.makeText(this, "UserLoc" + myLocation.getLatitude() + myLocation.getLongitude(), Toast.LENGTH_SHORT);

                    } else if ((myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)) != null) {
                        userLocation = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                        Toast.makeText(this, "UserLoc" + myLocation.getLatitude() + myLocation.getLongitude(), Toast.LENGTH_SHORT);
                    }

                }
            }
            catch(SecurityException|IllegalArgumentException e)
            {
                Toast.makeText(this,"Exception getLastKnownLocation", Toast.LENGTH_SHORT);
            }



            if(!location.matches("")){
                Geocoder geocoder= new Geocoder(this, Locale.US);
                try{
                    addressList=geocoder.getFromLocationName(location,10000,userLocation.latitude-(5.0/60.0),userLocation.longitude-(5.0/60.0),userLocation.latitude+(5.0/60.0),userLocation.longitude+(5.0/60.0));
                }
                catch(IOException e)
                { e.printStackTrace();}
                if (!addressList.isEmpty()){
                    for (int i =0;i<addressList.size();i++)
                    {
                        Address address = addressList.get(i);
                        LatLng latLng = new LatLng((address.getLatitude(),address.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(latLng).title(i+": "+address.getSubThoroughfare()+ " "+ address.getThoroughfare()));
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                    }
                }
            }
        }

    public void getLocation()
    {
        try{
            locationManager=(LocationManager)getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled=locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if(!isGPSEnabled&&!isNetworkEnabled)
            {
                Log.d("mymaps", "getlocation");

            }
            else
            {
                if (isNetworkEnabled) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListenerNetwork);
                }
                    if(isGPSEnabled)
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES,locationListenerGPS);

                }
            }catch(Exception e)
        {
            e.printStackTrace();
        }
    }



            LocationListener locationListenerNetwork = new LocationListener() {

                public void onLocationChanged(Location location) {
                    dropAmarker(LocationManager.NETWORK_PROVIDER);
                    if(gotMyLocationOneTime==false)
                    {
                        locationManager.removeUpdates(this);
                        locationManager.removeUpdates(locationListenerGPS);
                        gotMyLocationOneTime=true;
                        previousLatitude=lat;
                        previousLongitude=longit;
                    }
                    else{
                        if(ActivityCompat.checkSelfPermission(MapsActivity.this,))
                }


                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                public void onProviderEnabled(String provider) {

                }


                public void onProviderDisabled(String provider) {

                }
            }

            }




    }



