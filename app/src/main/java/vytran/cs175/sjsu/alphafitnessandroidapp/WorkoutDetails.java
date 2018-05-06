package vytran.cs175.sjsu.alphafitnessandroidapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.Entry;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.List;
import java.text.DateFormat;

public class WorkoutDetails extends Fragment {
    //Data info
    DBHandler database;
    UserInfo userInfo;
    UserWorkoutData userData;
    Thread thread;

    //profile screen variables
    float total_distance, max_distance, min_distance = 0f;
    float total_time,max_time, min_time = 0f;
    float time, distance = 0f;
    float averageTime, minTime, maxTime = 0f;
    DecimalFormat decimal;

    //profile screen textview
    TextView avg;
    TextView max;
    TextView min;

    //Chart Info
    LineChart chart;
    List<Float> userCalories;
    List<Integer> stepsNum;
    LineData data;
    LineDataSet caloData;
    LineDataSet stepsData;
    List<Entry> caloEntries;
    List<Entry> stepsEntries;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View landscapeView = inflater.inflate(R.layout.fragment_workout_details, container, false);

        avg = (TextView) getActivity().findViewById(R.id.averageTime);
        max = (TextView) getActivity().findViewById(R.id.maxTime);
        min = (TextView) getActivity().findViewById(R.id.minTime);

        long currentUserID = userInfo.getUserId();
        decimal = new DecimalFormat();

        //-------------- Average, min and max velocity calculation -------------------
        List<UserWorkoutData> data = database.getAllUserData();

        for(int i = 0; i < data.size() -1; i++) {
            distance = data.get(i + 1).getWeeklyDistance();
            time = data.get(i + 1).getWeeklyTime();

            total_distance += data.get(i).getWeeklyDistance();
            total_time += data.get(i).getWeeklyTime();

            max_distance = data.get(i).getWeeklyDistance();
            min_distance = data.get(i).getWeeklyDistance();
            max_time = data.get(i).getWeeklyTime();
            min_time = data.get(i).getWeeklyTime();

            if (distance > max_distance) {
                max_distance = distance;
            }

            if (distance < min_distance) {
                min_distance = distance;
            }

            if (time > max_time) {
                max_time = time;
            }

            if (time < min_time) {
                min_time = time;
            }
        }

        averageTime =(float) ((total_distance*0.621371)*3600000) / total_time;
        minTime =(float) ((min_distance*0.621371)*3600000) / min_time;
        maxTime =(float) ((max_distance*0.621371)*3600000) / max_time;

        //set text on screen
        avg.setText(decimal.format(averageTime));
        max.setText(decimal.format(maxTime));
        min.setText(decimal.format(minTime));


        //Chart
        chartCreator();

        return landscapeView;
    }

    //Making chart for plotting real-time data for calories and steps
    public void chartCreator(){

    }
}
