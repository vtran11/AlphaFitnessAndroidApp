package vytran.cs175.sjsu.alphafitnessandroidapp;

import android.*;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

import android.Manifest;
import android.net.Uri;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import android.location.Location;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;


public class PortraitRecordWorkout extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapView mapView;
    private Context context;
    private boolean isRunning = false;
    Button workoutButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View portraitView = inflater.inflate(R.layout.fragment_portrait_record_workout, container, false);

        mapView = (MapView) portraitView.findViewById(R.id.map);
        mapView .onCreate(savedInstanceState);
        mapView .onResume();
        mapView .getMapAsync(this);

        workoutButton = (Button) portraitView.findViewById(R.id.RecordButton);


                //-------set click listener for User Profile Button---------
        ImageButton userProfileButton = (ImageButton) portraitView.findViewById(R.id.userProfileID);
        userProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), UserProfileButton.class);
                startActivity(intent);
            }
        });

        return portraitView;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


       // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }


    public void buttonStartWorkoutCLicked(View view){
        if(!isRunning)
        {
            workoutButton.setText("Stop Workout");
            isRunning = true;
        }

        else
        {
            workoutButton.setText("Start Workout");
            isRunning= false;
        }
    }
}
