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
/*
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
*/


        //------------------- SET UP USER INFO ---------------------
        //newUser = null;
        //newUser.setUserName(userName.getText().toString());
        //newUser.setUserGender(userGender.getSelectedItem().toString());
        //newUser.setUserWeight(Float.parseFloat(userWeight.getText().toString()));
        //database.addUsers(newUser);
        //userID = newUser.getUserId();


        //-------------------- Get Weekly Workout Info ----------------------
        //List<UserWorkoutData> weeklyWorkoutData = database.getAllWeeklyUserData();

        //UserWorkoutData data = database.getAllWeeklyUserData(userName.getText().toString());


    }
}
