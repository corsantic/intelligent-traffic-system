package com.iot.kou.intelligenttrafficsystem;

import java.util.List;

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

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback
{

    private GoogleMap mMap;
    private RoadSiteUnit rsu;
    private Vehicle vehicle;
    private int vehicleId = 0;

    private int rsuId = 0;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    DatabaseReference vehicleRef = database.child("vehicle").child(String.valueOf(vehicleId));
    DatabaseReference myRef = database.child("data").child(String.valueOf(rsuId));


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        myRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                rsu =  dataSnapshot.getValue(RoadSiteUnit.class);//verileri getiriyor
                if (rsu != null)
                {
                    setMap();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
            }

        });

        vehicleRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                vehicle = dataSnapshot.getValue(Vehicle.class);
                if (vehicle != null)
                {
                    setMap();
                    if (mMap != null)
                        mMap.clear();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });


    }

    private void setMap()
    {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        setSnippet();


        if (rsu != null && vehicle != null && mMap != null) {
            Double vehicleLtd = vehicle.getLtd();
            Double vehicleLng = vehicle.getLng();
            Double rsuLtd = rsu.getLtd();
            Double rsuLng = rsu.getLng();

            LatLng rsuTest = new LatLng(rsuLng, rsuLtd);
            LatLng vehicleTest = new LatLng(vehicleLng, vehicleLtd);

            if(vehicleLng==42.183452 && vehicleLtd==25.244719)
                sendNotification("Road Site Unit",("Weather:" + rsu.getWeather()+","  +
                        "Smoothness:" + rsu.getSmoothness() +","  +
                        "Risk:" + rsu.getRisk() +","+
                        "Road Status:" + rsu.getStatus()
                ));

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
                            "Smoothness:" + rsu.getSmoothness() + "\n" +
                            "Risk Percentage:" + rsu.getRisk() + "\n" +
                            "Road Status:" + rsu.getStatus()
                    ));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(vehicleTest));

            vehicleMarker.showInfoWindow();
            rsuMarker.showInfoWindow();


        } else
            return;


    }

    private void sendNotification(String title, String body)
    {
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

    private void setSnippet()
    {
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter()
        {

            @Override
            public View getInfoWindow(Marker arg0)
            {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker)
            {

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


