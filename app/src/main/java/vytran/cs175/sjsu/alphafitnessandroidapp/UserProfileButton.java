package vytran.cs175.sjsu.alphafitnessandroidapp;

import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class UserProfileButton extends AppCompatActivity {

    private Handler handler;
    private DBHandler database;
    UserInfo newUser;
    UserWorkoutData userData;
    private DecimalFormat decimal;

    long userID = 0;
    private TextView userName;
    private Spinner userGender;
    private TextView userWeight;

    private TextView weeklyDistance;
    private TextView weeklyTime;
    private TextView weeklyWorkoutCount;
    private TextView weeklyCalories;

    private TextView totalDistance;
    private TextView totalTime;
    private TextView totalWorkoutCount;
    private TextView totalCalories;

    private TextView uneditedName;
    private TextView uneditedGender;
    private TextView uneditedWeight;

    private float allTimeData;
    private float weeklyTimeData;
    private boolean isEditing;

    Integer no = View.INVISIBLE;
    Integer yes = View.VISIBLE;
    public int sec, second;

    private float weekly_distance_text = 0;
    private float weekly_time_text = 0;
    private int weekly_workoutCount_text = 0;
    private float weekly_calories_text = 0;

    private float total_distance_text = 0;
    private float total_time_text = 0;
    private int total_workoutCount_text = 0;
    private float total_calories_text = 0;

    Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_button);

        userName = (TextView) findViewById(R.id.editedName);
        userGender = (Spinner) findViewById(R.id.genderSpinner);
        userWeight = (TextView) findViewById(R.id.editedWeight);

        weeklyDistance= (TextView) findViewById(R.id.weeklyDistanceText);
        weeklyTime = (TextView) findViewById(R.id.weeklyTimeText);
        weeklyWorkoutCount = (TextView) findViewById(R.id.weeklyWorkoutsText);
        weeklyCalories = (TextView) findViewById(R.id.weeklyCaloriesBurnedText);

        totalDistance= (TextView) findViewById(R.id.allTimeDistanceText);
        totalTime = (TextView) findViewById(R.id.allTimeTimeText);
        totalWorkoutCount = (TextView) findViewById(R.id.allTimeWorkoutsText);
        totalCalories = (TextView) findViewById(R.id.allTimeCaloriesBurnedText);

        uneditedName= (TextView) findViewById(R.id.uneditedName);
        uneditedGender= (TextView) findViewById(R.id.uneditedGender);
        uneditedWeight= (TextView) findViewById(R.id.uneditedWeight);

        newUser = new UserInfo();
        userData = new UserWorkoutData();
        database = new DBHandler(this);
        handler = new Handler();


        weeklyTimeData = userData.getWeeklyTime();

        decimal = new DecimalFormat("#.###");

        //------------------------ Set Weekly Data ------------------------------
        List<UserWorkoutData> weekly_data;
        weekly_data = database.getAllWeeklyUserData();

        for(UserWorkoutData data: weekly_data){
            weekly_distance_text += data.getWeeklyDistance();
            weekly_time_text += data.getWeeklyTime();
            weekly_workoutCount_text = (int) data.getWeeklyWorkoutCount();
            weekly_calories_text += data.getWeeklyCalories();
        }

        sec = (int) weekly_time_text*60;

        weeklyDistance.setText( String.format(java.util.Locale.US,"%.3f",weekly_distance_text) + " miles");
        weeklyTime.setText(sec/3600 + " Hrs " + (sec%3600)/60 + " Mins " + sec + " Secs ");
        weeklyCalories.setText(String.valueOf(weekly_workoutCount_text) + " times");
        weeklyCalories.setText(String.format(java.util.Locale.US,"%.2f",weekly_calories_text) + " calories");


        //------------------------ Set All Time Data ------------------------------
        List<UserWorkoutData> total_data;
        total_data = database.getAllUserData();

        for(UserWorkoutData data: total_data){
            total_distance_text += data.getWeeklyDistance();
            total_time_text += data.getWeeklyTime();
            total_workoutCount_text = (int) data.getWeeklyWorkoutCount();
            total_calories_text += data.getWeeklyCalories();
        }

        second = (int) total_time_text*60;

        totalDistance.setText( String.format(java.util.Locale.US,"%.3f",total_distance_text) + " miles");
        totalTime.setText(second/3600 + " Hrs " + (second%3600)/60 + " Mins " + second + " Secs ");
        totalWorkoutCount.setText(String.valueOf(total_workoutCount_text) + " times");
        totalCalories.setText(String.format(java.util.Locale.US,"%.2f",total_calories_text) + " calories");




        //--------------  SET UP SPINNER FOR GENDER  -----------------
        ArrayList<String> genders = new ArrayList<>();
        genders.add("Male");
        genders.add("Female");
        genders.add("Others");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, genders);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        userGender.setAdapter(adapter);
        userGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedGender = userGender.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        adapter.notifyDataSetChanged();

        //--------------- Set up thread running in the background ----------------
        BackgroundRunningThread();

    }


    //-------------- Menu Items for Edit/Save current Profile Data --------------
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.resetData:
                DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        database.deleteUser(newUser);
                        database.deleteUserData(userData);
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Reset all records");
                builder.setMessage("Are you sure?").setPositiveButton("Yes", clickListener).setNegativeButton("No", clickListener).show();
                return true;

            case R.id.editData:
                if(isEditing)
                {
                    uneditedName.setVisibility(no);
                    userName.setVisibility(yes);
                    userName.setText(newUser.getUserName());

                    uneditedGender.setVisibility(no);
                    userGender.setVisibility(yes);
                    if(userGender.getItemAtPosition(0).toString() != "Female")
                        userGender.setSelection(0);
                    else
                        userGender.setSelection(1);


                    uneditedWeight.setVisibility(no);
                    userWeight.setVisibility(yes);
                    userWeight.setText(newUser.getUserName());
                }
                return true;
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_user_profile, menu);

        return true;
    }

    public void BackgroundRunningThread(){
        thread = new Thread(){
            @Override
            public void run(){
                try{
                    while (!isInterrupted()){
                        Thread.sleep(2000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(PortraitRecordWorkout.isRunning)
                                {
                                    weeklyDistance.setText( String.format(java.util.Locale.US,"%.3f",weekly_distance_text) + " miles");
                                    weeklyTime.setText(sec/3600 + " Hrs " + (sec%3600)/60 + " Mins " + sec + " Secs ");
                                    weeklyCalories.setText(String.valueOf(weekly_workoutCount_text) + " times");
                                    weeklyCalories.setText(String.format(java.util.Locale.US,"%.2f",weekly_calories_text) + " calories");

                                    totalDistance.setText( String.format(java.util.Locale.US,"%.3f",total_distance_text) + " miles");
                                    totalTime.setText(second/3600 + " Hrs " + (second%3600)/60 + " Mins " + second + " Secs ");
                                    totalWorkoutCount.setText(String.valueOf(total_workoutCount_text) + " times");
                                    totalCalories.setText(String.format(java.util.Locale.US,"%.2f",total_calories_text) + " calories");

                                }
                            }
                        });
                    }
                } catch (InterruptedException e){}
            }
        };
        thread.start();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        thread.interrupt();
    }

}
