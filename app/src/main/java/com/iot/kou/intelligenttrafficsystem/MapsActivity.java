package com.iot.kou.intelligenttrafficsystem;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iot.kou.intelligenttrafficsystem.model.RoadSiteUnit;
import com.iot.kou.intelligenttrafficsystem.model.Vehicle;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<RoadSiteUnit> rsu = new ArrayList<>();
    private Vehicle vehicle ;


    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    DatabaseReference vehicleRef = database.child("vehicle").child("0");
    DatabaseReference myRef = database.child("data");
//.child(String.valueOf(rsuId))

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);



        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count ", "" + snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    rsu.add(postSnapshot.getValue(RoadSiteUnit.class));


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("The read failed: ", databaseError.getMessage());
            }
        });


        vehicleRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count ", "" + snapshot.getChildrenCount());
               {
                   vehicle = snapshot.getValue(Vehicle.class);
                    if(mMap!=null)
                        mMap.clear();
                    setMap();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("The read failed: ", databaseError.getMessage());
            }
        });

    }


    private void setMap() {


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setSnippet();



        if (rsu != null && vehicle != null && mMap != null) {

                Double vehicleLtd = vehicle.getLtd();
                Double vehicleLng = vehicle.getLng();
                LatLng vehicleTest = new LatLng(vehicleLng, vehicleLtd);

                Marker vehicleMarker = mMap.addMarker(new MarkerOptions()
                        .position(vehicleTest)
                        .title("Vehicle")
                        .icon(BitmapDescriptorFactory.fromResource(R.raw.vehicle))
                        .snippet(+vehicleLtd + "-" +
                                +vehicleLng));



                for (int i = 0; i < rsu.size() ; i++) {
                RoadSiteUnit roadSiteUnit = rsu.get(i);

                LatLng rsuTest = new LatLng(roadSiteUnit.getLng(), roadSiteUnit.getLtd());

                Marker rsuMarker = mMap.addMarker(new MarkerOptions()
                        .position(rsuTest)
                        .title("RSU")
                        .flat(true)
                        .icon(BitmapDescriptorFactory.fromResource(R.raw.unit))
                        .snippet("Weather:" + roadSiteUnit.getWeather() + "\n" +
                                "Smoothness:" + roadSiteUnit.getSmoothness() + "\n" +
                                "Risk Percentage:" + roadSiteUnit.getRisk() + "\n" +
                                "Road Status:" + roadSiteUnit.getStatus()
                        ));

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(vehicleTest)
                        .zoom(15)
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                    if (Math.abs(roadSiteUnit.getLtd() - vehicleLtd) < 0.000800 && Math.abs(roadSiteUnit.getLng() - vehicleLng) < 0.000600) {
                        sendNotification("RsuId:"+String.valueOf(roadSiteUnit.getRsuId()), ("Weather:" + roadSiteUnit.getWeather() + "," +
                                "Smoothness:" + roadSiteUnit.getSmoothness() + "," +
                                "Risk:" + roadSiteUnit.getRisk() + "," +
                                "Road Status:" + roadSiteUnit.getStatus()
                        ));
                    }
            }







        } else
            return;


    }

    private void sendNotification(String title, String body) {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(121, notificationBuilder.build());
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


