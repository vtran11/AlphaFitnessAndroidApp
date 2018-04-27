package vytran.cs175.sjsu.alphafitnessandroidapp;

import android.*;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import android.util.Log;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import android.graphics.Color;

import java.util.ArrayList;

import android.Manifest;
import android.net.Uri;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;


import android.location.Location;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

import android.os.SystemClock;

public class PortraitRecordWorkout extends Fragment implements OnMapReadyCallback {

    private MapView mapView;
    private Button workoutButton;
    private TextView workout_distance;
    private TextView updateTime;

    private WatchTime watchTime;
    private long timeInMilliseconds = 0L;

    private Handler handler;
    private UserWorkoutData userData;
    private DBHandler database;

    private GoogleMap mMap;
    private Context context;
    private boolean isRunning = false;

    private ArrayList<LatLng> locationsList;
    LocationManager myLocationManager;

    private static int REQUEST_FINE_LOCATION = 0;
    private static int REQUEST_COARSE_LOCATION = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View portraitView = inflater.inflate(R.layout.fragment_portrait_record_workout, container, false);

        handler = new Handler();
        watchTime = new WatchTime();

        workout_distance = (TextView) portraitView.findViewById(R.id.workoutDistance);
        updateTime = (TextView) portraitView.findViewById(R.id.workoutDuration);


        //----------------- Display GoogleMap View ----------------
        context = portraitView.getContext();
        mapView = (MapView) portraitView.findViewById(R.id.map);
        mapView .onCreate(savedInstanceState);
        mapView .onResume();

        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
        {
            mapView .getMapAsync(this);
        }

        // myLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // myLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 200, 10, locationGPS);


        //-------set click listener for Start WOrkout Button---------
        workoutButton = (Button) portraitView.findViewById(R.id.RecordButton);
        workoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isRunning) {
                    workoutButton.setText("Stop Workout");
                    isRunning = true;
                    workoutButton.setBackgroundColor(Color.RED);
                    //startTimer(view);
                    watchTime.setStartTime(SystemClock.uptimeMillis());
                    handler.postDelayed(updateTimerRunnable, 20);
                    //handler.removeCallbacks();
                    //handler.postDelayed(locationChangedRunnable, 20);
                }

                else {
                    workoutButton.setText("Start Workout");
                    isRunning= false;
                    workoutButton.setBackgroundColor(Color.GREEN);
                    stopTimer(view);
                    resetTimer(view);
                    watchTime.addStoredTime(timeInMilliseconds);
                    handler.removeCallbacks(updateTimerRunnable);
                    //handler.postDelayed(, 0);
                    //handler.removeCallbacks(locationChangedRunnable);
                }
            }
        });

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
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;

        LocationManager locationManager;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        String locationProvider = locationManager.getBestProvider(criteria,true);

        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_COARSE_LOCATION);
        }
        else
            mMap.setMyLocationEnabled(true);


        Location location = locationManager.getLastKnownLocation(locationProvider);
        LatLng here = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(here).title("Start"));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(here));
    }




    //---------------------- Workout time handler ----------------------------
    public void startTimer(View view){
        watchTime.setStartTime(SystemClock.uptimeMillis());
        handler.postDelayed(updateTimerRunnable, 0);
    }

    private Runnable updateTimerRunnable = new Runnable() {
        @Override
        public void run() {
            //compute the time difference
            timeInMilliseconds = SystemClock.uptimeMillis() - watchTime.getStartTime();
            watchTime.setTimeUpdate(watchTime.getStoredTime() + timeInMilliseconds);

            int time = (int) (watchTime.getTimeUpdate() / 1000);

            //compute minutes, seconds, and milliseconds
            int all_minutes = time/60;

            int hours = all_minutes/60;
            int minutes = all_minutes - hours*60;
            int seconds = time % 60;
            int milliseconds = (int) (watchTime.getTimeUpdate() % 1000);

            //display the time in the tex view
            updateTime.setText(String.format("%02d", hours) + ":" +
                    String.format("%02d", minutes) + ":"
                    + String.format("%02d", seconds));

            //specify no time lapse between posting
            handler.postDelayed(this, 0);
        }
    };

    public void stopTimer(View view){
        watchTime.addStoredTime(timeInMilliseconds);
        handler.removeCallbacks(updateTimerRunnable);
    }

    public void resetTimer(View view){
        watchTime.resetWatchTime();
        timeInMilliseconds = 0L;
        int minutes = 0;
        int seconds = 0;
        int milliseconds = 0;
        int hours = 0;

        updateTime.setText(String.format("%02d", hours) + ":" +
                String.format("%02d", minutes) + ":"
                + String.format("%02d", seconds));
    }


    /*
    //-------------------- GPS Sensor Listener for Workout Session ------------------
    private Runnable locationChangedRunnable = new Runnable() {
        @Override
        public void run() {
            LocationListener locationGPS = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    locationsList.add(new LatLng(latitude, longitude));

                    PolylineOptions polyline = new PolylineOptions().width(4).color(Color.GREEN).geodesic(true);
                    for(int i =0; i<locationsList.size();i++)
                    {
                        LatLng point = locationsList.get(i);
                        polyline.add(point);
                    }
                    mMap.addPolyline(polyline);
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {}

                @Override
                public void onProviderEnabled(String s) {}

                @Override
                public void onProviderDisabled(String s) {}
            };
            handler.postDelayed(this, 0);
        }
    };

    @Override
    public void onSensorChanged(SensorEvent event) {}

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    LocationListener locationGPS = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            locationsList.add(new LatLng(latitude, longitude));

            if(isRunning) {
                PolylineOptions polyline = new PolylineOptions().width(4).color(Color.GREEN).geodesic(true);
                for (int i = 0; i < locationsList.size(); i++) {
                    LatLng point = locationsList.get(i);
                    polyline.add(point);
                }
                mMap.addPolyline(polyline);
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {}

        @Override
        public void onProviderEnabled(String s) {}

        @Override
        public void onProviderDisabled(String s) {}
    };
    */
}






