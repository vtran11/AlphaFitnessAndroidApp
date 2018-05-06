package vytran.cs175.sjsu.alphafitnessandroidapp;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

public class UserProfileButton extends AppCompatActivity {

    private Handler handler;
    private DBHandler database;
    UserInfo newUser;
    UserWorkoutData userData;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_button);

        userName = (TextView) findViewById(R.id.userName_view);
        userGender = (Spinner) findViewById(R.id.userGender_view);
        userWeight = (TextView) findViewById(R.id.userWeight_view);

        weeklyDistance= (TextView) findViewById(R.id.weeklyDistance_view);
        weeklyTime = (TextView) findViewById(R.id.weeklyTime_view);
        weeklyWorkoutCount = (TextView) findViewById(R.id.weeklyWorkoutCount_view);
        weeklyCalories = (TextView) findViewById(R.id.weeklyCalories_view);

        totalDistance= (TextView) findViewById(R.id.totalDistance_view);
        totalTime = (TextView) findViewById(R.id.totalTime_view);
        totalWorkoutCount = (TextView) findViewById(R.id.totalWorkoutCount_view);
        totalCalories = (TextView) findViewById(R.id.totalCalories_view);

        newUser = new UserInfo();
        database = new DBHandler(this);
        handler = new Handler();
        //database.open();
        //database.getWritableDatabase();


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


        //setUserInfo();

        //------------------- SET UP USER INFO ---------------------
     /*   newUser.setUserName(userName.getText().toString());
        newUser.setUserGender(userGender.getSelectedItem().toString());
        newUser.setUserWeight(Float.parseFloat(userWeight.getText().toString()));
        database.addUsers(newUser);
*/

        //-------------------- Get Weekly Workout Info ----------------------

        //handler.postDelayed(getWeekly_WorkoutData, 30);
        //handler.postDelayed(getAllTime_WorkoutData, 30);

    }

    private void setUserInfo(){
       UserInfo currentUser = database.getUser(1);

        if(currentUser != null){
            userName.setText(currentUser.getUserName());
            if(currentUser.getUserGender().contains("Male"))
                userGender.setSelection(0);
            else if(currentUser.getUserGender().contains("Female"))
                userGender.setSelection(1);
            else
                userGender.setSelection(2);

            userWeight.setText(String.valueOf(currentUser.getUserWeight()));
        }
    }
    /*
    public void saveUserInfo(View view){
        String gender = String.valueOf(userGender.getSelectedItem());
        String name = userName.getText().toString();
        Float weight = Float.parseFloat(userWeight.getText().toString());

        if(!userName.getText().toString().isEmpty()){
            newUser.setUserName(name);
        }

        newUser.setUserGender(gender);

        if(!userWeight.getText().toString().isEmpty()){
            newUser.setUserWeight(weight);
        }

        database.addUsers(newUser);

    }*/

/*
    private Runnable getWeekly_WorkoutData = new Runnable() {
        @Override
        public void run() {
            List<UserWorkoutData> weeklyWorkoutData;
            weeklyWorkoutData = database.getAllWeeklyUserData();

            float totalDistance = 0;
            float totalTime = 0;
            int totalWorkouts = 0;
            float totalCalories = 0;

            for (UserWorkoutData data: weeklyWorkoutData) {
                //distance
                totalDistance += data.getWeeklyDistance();
                //time
                totalTime += data.getWeeklyTime();
                //workouts
                totalWorkouts = (int) data.getWeeklyWorkoutCount();
                //calories
                totalCalories += data.getWeeklyCalories();
            }

            String timeInString = timeToString( (int) totalTime);

            String distance = String.format(java.util.Locale.US,"%.2f",totalDistance) + " miles";
            String workouts = String.valueOf(totalWorkouts) + " times";
            String calories = String.format(java.util.Locale.US,"%.2f",totalCalories) + " calories";

            weeklyDistance.setText(distance);
            weeklyTime.setText(timeInString);
            weeklyWorkoutCount.setText(workouts);
            weeklyCalories.setText(calories);
        }
    };


    private String timeToString (int mins) {
        int seconds = mins * 60;

        int minutes = (seconds % 3600) / 60;
        int hours = seconds / 3600;
        int days = seconds / 86400;
        seconds = (seconds % 3600) % 60;

        return days + " day " + hours + " hr " + minutes + " min " + seconds + " sec";
    }

    private Runnable getAllTime_WorkoutData = new Runnable() {
        @Override
        public void run() {

        }
    };
*/
}
