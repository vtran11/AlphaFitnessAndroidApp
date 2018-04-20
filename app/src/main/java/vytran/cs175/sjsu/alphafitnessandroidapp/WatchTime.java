package vytran.cs175.sjsu.alphafitnessandroidapp;

/**
 * Created by ThaoVy on 4/19/18.
 */

public class WatchTime {
    private long mStartTime;
    private long mTimeUpdate;
    private long mStoredTime;

    public WatchTime()
    {
        mStartTime = 0L;
        mTimeUpdate = 0L;
        mStoredTime = 0L;
    }

    public void resetWatchTime(){
        mStartTime = 0L;
        mTimeUpdate = 0L;
        mStoredTime = 0L;
    }

    public void setStartTime(long startTime){
        mStartTime = startTime;
    }

    public long getStartTime(){
        return mStartTime;
    }

    public void setTimeUpdate(long timeUpdate){
        mTimeUpdate = timeUpdate;
    }

    public long getTimeUpdate(){
        return mTimeUpdate;
    }

    public void addStoredTime(long timeInSeconds){
        mStoredTime += timeInSeconds;
    }

    public long getStoredTime(){
        return mStoredTime;
    }
}
