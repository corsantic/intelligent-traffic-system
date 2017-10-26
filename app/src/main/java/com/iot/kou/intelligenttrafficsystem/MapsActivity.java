package com.iot.kou.intelligenttrafficsystem;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iot.kou.intelligenttrafficsystem.model.RoadSiteUnit;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    private RoadSiteUnit rsu;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    DatabaseReference myRef = database.child("data");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


// ...
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                rsu = dataSnapshot.getValue(RoadSiteUnit.class);//verileri getiriyor
                if(rsu!=null) {
                    setMap();
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {}

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

        // Add a marker in Sydney and move the camera
        Double rsuLtd=rsu.getLtd();
        Double rsuLng=rsu.getLng();

        LatLng rsuTest = new LatLng(rsuLtd,rsuLng);
      //  LatLng test = new LatLng(10, -20); //todo:test data
        if(mMap!=null) {
            mMap.addMarker(new MarkerOptions().position(rsuTest).title("Road Site Unit"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(rsuTest));
        }
    }
}
