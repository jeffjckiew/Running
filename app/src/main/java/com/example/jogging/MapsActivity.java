package com.example.jogging;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.example.jogging.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap gMap;

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
        gMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.addCircle(new CircleOptions().center(sydney).radius(300).strokeColor(Color.RED).fillColor(Color.RED));
//        mMap.getMyLocation();
//        Circle circle =  mMap.addCircle(new CircleOptions()
//                .center(new LatLng(-34, 158))
//                .radius(1000)
//                .strokeColor(Color.RED)
//                .fillColor(Color.rgb(255,255,255)));


//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else{
            if(!gMap.isMyLocationEnabled())
                gMap.setMyLocationEnabled(true);

            LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            Location myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (myLocation == null) {
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_COARSE);
                String provider = lm.getBestProvider(criteria, true);
                myLocation = lm.getLastKnownLocation(provider);
//                System.out.println("test0");
            }

            if(myLocation!=null){
                LatLng userLocation = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15), 2000, null);
//                System.out.println("test1");

//                gMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
                gMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(myLocation.getLatitude(),myLocation.getLongitude()) , 20.0f) );

            }
        }



    }
}