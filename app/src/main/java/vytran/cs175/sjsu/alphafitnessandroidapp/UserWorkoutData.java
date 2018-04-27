package vytran.cs175.sjsu.alphafitnessandroidapp;

/**
 * Created by ThaoVy on 4/20/18.
 */

public class UserWorkoutData {
    private long UserId;
    private float user_WeeklyDistance;
    private float user_WeeklyTime;
    private float WorkoutCount_Weekly;
    private float user_WeeklyCalories;

    public UserWorkoutData() {}


    public void setUserDataId(long Id) {
        this.UserId = Id;
    }

    public long getUserDataId() {
        return UserId;
    }

    public void setWeeklyDistance(float distance) {
        this.user_WeeklyDistance = distance;
    }

    public float getWeeklyDistance() {
        return user_WeeklyDistance;
    }

    public void setWeeklyTime(float time) {
        this.user_WeeklyTime = time;
    }

    public float getWeeklyTime() {
        return user_WeeklyTime;
    }


    public void setWeeklyWorkoutCount(float workoutNum) {
        this.WorkoutCount_Weekly = workoutNum;
    }

    public float getWeeklyWorkoutCount() {
        return WorkoutCount_Weekly;
    }


    public void setWeeklyCalories(float calories) {
        this.user_WeeklyCalories = calories;
    }

    public float getWeeklyCalories() {
        return user_WeeklyCalories;
    }


}
