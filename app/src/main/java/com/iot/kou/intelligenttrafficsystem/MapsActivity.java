package com.iot.kou.intelligenttrafficsystem;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iot.kou.intelligenttrafficsystem.model.RoadSiteUnit;
import com.iot.kou.intelligenttrafficsystem.model.Vehicle;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    private RoadSiteUnit rsu;
    private Vehicle vehicle;
    private int vehicleId=0;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        DatabaseReference vehicleRef = database.child("vehicle").child(String.valueOf(vehicleId));
        DatabaseReference myRef = database.child("data");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                rsu = dataSnapshot.getValue(RoadSiteUnit.class);//verileri getiriyor
                if (rsu != null) {
                    setMap();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });

        vehicleRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                vehicle = dataSnapshot.getValue(Vehicle.class);
                if (vehicle != null) {
                    setMap();
                    if (mMap != null)
                        mMap.clear();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //  myRef.setValue(new RoadSiteUnit(1,120.0,90.0,"20","60"));//todo: data base eklemesi bununla yapılabilir
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    }

    private void setMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Double vehicleLtd = vehicle.getLtd();
        Double vehicleLng = vehicle.getLng();
        Double rsuLtd = rsu.getLtd();
        Double rsuLng = rsu.getLng();


        LatLng rsuTest = new LatLng(rsuLng, rsuLtd);
        LatLng vehicleTest = new LatLng(vehicleLng, vehicleLtd);
        setSnippet();
        //  LatLng test = new LatLng(10, -20); //todo:test data
        if (mMap != null && rsu != null && vehicle != null) {
            Marker vehicleMarker = mMap.addMarker(new MarkerOptions()
                    .position(vehicleTest)
                    .title("Vehicle")
                    .icon(BitmapDescriptorFactory.fromResource(R.raw.vehicle))
                    .snippet(+vehicleLtd + "-" +
                            +vehicleLng));

            Marker rsuMarker = mMap.addMarker(new MarkerOptions()
                    .position(rsuTest)
                    .title("RSU")
                    .flat(true)
                    .icon(BitmapDescriptorFactory.fromResource(R.raw.unit))
                    .snippet("Weather:" + rsu.getWeather() + "\n" +
                            "Smoothness:" + rsu.getSmoothness()+"\n"+
                            "Risk Percentage:"+rsu.getRisk() + "\n" +
                            "Road Status:"+rsu.getStatus()
                    ));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(vehicleTest));

            vehicleMarker.showInfoWindow();
            rsuMarker.showInfoWindow();
        } else
            return;


    }

    private void setSnippet() {
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                Context appContext = getApplicationContext();
                LinearLayout info = new LinearLayout(appContext);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(appContext);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(appContext);
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });
    }

}


