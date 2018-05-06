package vytran.cs175.sjsu.alphafitnessandroidapp;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.TextView;
import android.os.Handler;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import android.graphics.Color;
import java.text.DecimalFormat;

import java.util.ArrayList;
import java.util.Locale;

import android.Manifest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.SupportMapFragment;


import android.location.Location;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

import android.os.SystemClock;

public class PortraitRecordWorkout extends Fragment implements OnMapReadyCallback, SensorEventListener {

    private MapView mapView;
    private Button workoutButton;
    private TextView workout_distance;
    private TextView updateTime;

    private WatchTime watchTime;
    public long beginTime, timeInMilliseconds = 0L;

    private Handler handler;
    private UserWorkoutData userData;
    private DBHandler database;

    private GoogleMap mMap;
    private Context context;
    public static boolean isRunning = false;

    SensorManager sensorManager;
    Sensor stepSensor;
    private int stepsCount = 0;
    private int workoutCount = 0;

    private ArrayList<LatLng> locationsList;
    LocationManager locationManager;
    double currentLatitude, currentLongitude;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View portraitView = inflater.inflate(R.layout.fragment_portrait_record_workout, container, false);

        handler = new Handler();
        watchTime = new WatchTime();
        userData = new UserWorkoutData();
        database = new DBHandler(getActivity());
        database.getWritableDatabase();

        workout_distance = (TextView) portraitView.findViewById(R.id.workoutDistance);
        updateTime = (TextView) portraitView.findViewById(R.id.workoutDuration);


        //----------------- Display GoogleMap View ----------------
        mapView = (MapView) portraitView.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);

        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mapView.getMapAsync(this);
        }

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 10, locationListener);

        //----------------- Set up Step Counter and Step Detector ----------------------


        //Register the sensor manager
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        //location
        locationsList = new ArrayList<>();

        //-------set click listener for Start Workout Button---------


        workoutButton = (Button) portraitView.findViewById(R.id.RecordButton);
        workoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isRunning) {
                    workoutCount++;
                    workoutButton.setText("Stop Workout");
                    isRunning = true;
                    workout_distance.setText("0.0");
                    beginTime = SystemClock.uptimeMillis();
                    workoutButton.setBackgroundColor(Color.RED);
                    startTimer(view);
                    //watchTime.setStartTime(SystemClock.uptimeMillis());
                    //handler.postDelayed(updateTimerRunnable, 0);
                    handler.postDelayed(updateLocationRunnable, 20);
                    handler.removeCallbacks(updateDBHandler);
                }

                else {
                    workoutButton.setText("Start Workout");
                    stepsCount =0;
                    isRunning= false;
                    workoutButton.setBackgroundColor(Color.GREEN);
                    stopTimer(view);
                    resetTimer(view);
                    //onStop();
                    //watchTime.addStoredTime(timeInMilliseconds);
                    //handler.removeCallbacks(updateTimerRunnable);
                    handler.postDelayed(updateDBHandler, 0);
                    handler.removeCallbacks(updateLocationRunnable);
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

        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);

        }

        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        LocationManager locationMag = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = locationMag.getBestProvider(criteria,true);
        Location location = locationMag.getLastKnownLocation(locationProvider);

        if(location != null) {
               LatLng here = new LatLng(location.getLatitude(), location.getLongitude());

               mMap.addMarker(new MarkerOptions().position(here).title("Start").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
               mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(here, 19));
        }

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
            if(hours <=0){
                updateTime.setText(String.format("%02d", minutes) + ":"
                        + String.format("%02d", seconds));
            }
            else
            updateTime.setText(String.format("%02d", hours) + ":" +
                    String.format("%02d", minutes) + ":"
                    + String.format("%02d", seconds));

            workout_distance.setText(String.format(Locale.US, "%.03f", km_to_mile(getDistance())));


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

        updateTime.setText(String.format("%02d", minutes) + ":"
                + String.format("%02d", seconds));
    }


    //------------------------- Workout Location Handler ------------------------------
    private Runnable updateLocationRunnable = new Runnable() {
        @Override
        public void run() {
            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    locationsList.add(new LatLng(latitude,longitude));

                        PolylineOptions polyline = new PolylineOptions().width(6).color(Color.RED).geodesic(true);
                        for (int i = 0; i < locationsList.size(); i++) {
                            LatLng point = locationsList.get(i);
                            polyline.add(point);
                        }

                        mMap.addPolyline(polyline);
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            };
            handler.postDelayed(this, 0);
        }
    };

    @NonNull
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            locationsList.add(new LatLng(latitude,longitude));
            if(isRunning) {
                PolylineOptions polyline = new PolylineOptions().width(6).color(Color.RED).geodesic(true);
                for (int i = 0; i < locationsList.size(); i++) {
                    LatLng point = locationsList.get(i);
                    polyline.add(point);
                }

                mMap.addPolyline(polyline);
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };


    //-------------------- GPS Sensor Listener for Workout Session ------------------
    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onPause() {
        super.onPause();
        //sensorManager.unregisterListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this, stepSensor);
        //handler.removeCallbacks(updateDBHandler);
        database.close();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
            Sensor sensor = event.sensor;
            float[] values = event.values;
            int value = -1;

            if(values.length > 0) {
                value = (int) values[0];
            }

            if(sensor.getType() == Sensor.TYPE_STEP_DETECTOR){
                stepsCount++;
            }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public float getDistance(){
        float distance = (float)(stepsCount*78)/(float)100000;
        return distance;
    }


    //------------------- Write Workout Data to DBHandler ------------------
    private Runnable updateDBHandler = new Runnable() {
        @Override
        public void run() {
            workoutCount++;
            float weight = 0;
            UserInfo user;

            if(database.getUser(1) != null) {
                user = database.getUser(1);

                if(user.getUserWeight() >= 0) {
                    weight = user.getUserWeight();
                }
                else
                    weight = 115;
            }

            userData.setWeeklyDistance(getDistance());
            userData.setWeeklyTime(timeInMilliseconds);
            userData.setWeeklyWorkoutCount(workoutCount);
            userData.setWeeklyCalories(((float)(0.5 * weight)/1400)* stepsCount);
            database.addUsersWorkoutData(userData);

            handler.postDelayed(this, 300000);
        }
    };

    public double km_to_mile(double km){
        return 0.621371*km;
    }

}






