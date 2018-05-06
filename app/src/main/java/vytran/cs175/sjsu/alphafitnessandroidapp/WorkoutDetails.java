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
import java.util.ArrayList;
import java.util.List;
import java.text.DateFormat;
import android.graphics.Color;


public class WorkoutDetails extends Fragment {
    //Data info
    DBHandler database;
    UserInfo userInfo;
    UserWorkoutData userData;
    Thread thread;

    int currentUserID;

    //profile screen variables
    float total_distance, max_distance, min_distance = 0f;
    float total_time,max_time, min_time = 0f;
    float time, distance = 0f;
    double averageTime, minTime, maxTime = 0f;
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
    LineDataSet caloDataSet;
    LineDataSet stepsDataSet;
    List<Entry> caloEntries;
    List<Entry> stepsEntries;
    int firstTime;
    int secondTime;
    Double miles_per_hour;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View landscapeView = inflater.inflate(R.layout.fragment_workout_details, container, false);

        avg = (TextView) getActivity().findViewById(R.id.averageTime);
        max = (TextView) getActivity().findViewById(R.id.maxTime);
        min = (TextView) getActivity().findViewById(R.id.minTime);

        currentUserID = userInfo.getUserId();
        decimal = new DecimalFormat();
        database= new DBHandler(getActivity());

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
        caloEntries = new ArrayList<>();
        stepsEntries = new ArrayList<>();

        chartCreator();

        return landscapeView;
    }

    //Making chart for plotting real-time data for calories and steps
    public void chartCreator(){
        chart = getView().findViewById(R.id.chart);

        List<UserWorkoutData> allData;
        allData = database.getAllUserData();

        for(UserWorkoutData data: allData) {
            userCalories.add(data.getWeeklyCalories());
            stepsNum.add((int) data.getWeeklyDistance()*100000/78);
        }

        caloEntries.add(new Entry(0, (float)0.0));
        stepsEntries.add(new Entry(0, (float)0.0));

        stepsGraph();
        caloriesGraph();

        data = new LineData(stepsDataSet, caloDataSet);
        chart.setData(data);
        chart.invalidate();

        thread = new Thread(){
            @Override
            public void run(){
                try {
                    while (!isInterrupted()) {
                       Thread.sleep(1500);

                       if(PortraitRecordWorkout.isRunning){
                            miles_per_hour = ((database.getCurrentDistance(currentUserID)*0.621371)*3600000)
                                   /database.getCurrentTime(currentUserID);

                            if(miles_per_hour < minTime)
                                minTime = miles_per_hour;
                           if(miles_per_hour > maxTime)
                               maxTime = miles_per_hour;

                           avg.setText(decimal.format(miles_per_hour));
                           max.setText(decimal.format(maxTime));
                           min.setText(decimal.format(minTime));


                           List<UserWorkoutData> all_data;
                           all_data = database.getAllUserData();

                           for(UserWorkoutData data: all_data) {
                               userCalories.add(data.getWeeklyCalories());
                               stepsNum.add((int) data.getWeeklyDistance()*100000/78);
                           }

                           for(int i = stepsDataSet.getEntryCount()-1; i < stepsNum.size(); i++){
                               firstTime+= 15;
                               stepsDataSet.addEntry(new Entry(firstTime,
                                       (float)(stepsNum.get(i)-stepsNum.get(i-1))));
                           }

                           for(int i = caloDataSet.getEntryCount()-1; i < userCalories.size(); i++){
                               secondTime+= 15;
                               caloDataSet.addEntry(new Entry(secondTime,
                                       (float)10*(userCalories.get(i)-userCalories.get(i-1))));
                           }

                           caloDataSet.notifyDataSetChanged();
                           stepsDataSet.notifyDataSetChanged();
                           chart.notifyDataSetChanged();
                           data.notifyDataChanged();
                           chart.invalidate();
                       }

                    }
                } catch (InterruptedException e){ }
            }
        };

        thread.start();

    }


    public void stepsGraph(){
        for(int i = 0; i < stepsNum.size();i++){
            if( i != 0)
                stepsEntries.add(new Entry(firstTime, ((float) stepsNum.get(i) - stepsNum.get(i-1))));
            else
                stepsEntries.add(new Entry(firstTime, (float) stepsNum.get(i)));
            firstTime += 15;
        }

        stepsDataSet = new LineDataSet(stepsEntries, "Steps");
        stepsDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        stepsDataSet.setDrawCircles(false);
        stepsDataSet.setDrawFilled(true);
        stepsDataSet.setColor(Color.YELLOW);
        stepsDataSet.setFillColor(Color.YELLOW);
        stepsDataSet.setDrawValues(true);
    }


    public void caloriesGraph(){
        for(int i=0; i <userCalories.size(); i++){
            if(i != 0)
                caloEntries.add(new Entry(secondTime,
                        (float)(10*(userCalories.get(i) - userCalories.get(i-1)))));
            else
                caloEntries.add(new Entry(secondTime, (float)10* userCalories.get(i)));
            secondTime+=15;
        }

        caloDataSet = new LineDataSet(caloEntries, "Calories Burned");
        caloDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        caloDataSet.setDrawCircles(false);
        caloDataSet.setDrawFilled(true);
        caloDataSet.setColor(Color.GREEN);
        caloDataSet.setFillColor(Color.GREEN);
        caloDataSet.setDrawValues(true);
    }

}
