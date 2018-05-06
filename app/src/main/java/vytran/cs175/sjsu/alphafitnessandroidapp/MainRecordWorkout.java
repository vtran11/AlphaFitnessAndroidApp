package vytran.cs175.sjsu.alphafitnessandroidapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainRecordWorkout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_record_workout);

        Configuration config = getResources().getConfiguration();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //handle portrait mode
        if(config.orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            Fragment portraitFragment = new PortraitRecordWorkout();
            fragmentTransaction.replace(android.R.id.content, portraitFragment);
        }

        //handle landscape mode
        else  {
            Fragment landscapeFragment = new WorkoutDetails();
            fragmentTransaction.replace(android.R.id.content, landscapeFragment);
        }

        fragmentTransaction.commit();

    }
}