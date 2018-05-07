package vytran.cs175.sjsu.alphafitnessandroidapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

/**
 * Created by ThaoVy on 4/19/18.
 * Following CHapter 9 SQLite example in the book
 */

public class DBHandler extends SQLiteOpenHelper {

    //Task 1: DEFINE THE DATABASE AND TABLE
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "usersDatabase";

    //TASK 2: DEFINE THE COLUMN NAMES FOR THE TABLE
    static final String USERS_INFO_TABLE = "users";
    static final String USER_ID = "userID";
    static final String USER_NAME = "name";
    static final String USER_GENDER = "gender";
    static final String USER_WEIGHT= "weight";

    static final String USER_WORKOUT_TABLE = "userData";
    static final String WEEKLY_DISTANCE= "workoutDistance";
    static final String WEEKLY_TIME = "workoutTime";
    static final String WEEKLY_NUMWORKOUTS= "numWorkouts";
    static final String WEEKLY_CALORIES= "workoutCalories";


    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String UserInfo_table = " CREATE TABLE " + USERS_INFO_TABLE
                + " (" + USER_ID + " INTEGER PRIMARY KEY, "
                + USER_NAME + " TEXT, "
                + USER_GENDER + " TEXT, "
                + USER_WEIGHT + " NUMERIC" + ")";
        database.execSQL(UserInfo_table);

        String UserWorkout_table = " CREATE TABLE " + USER_WORKOUT_TABLE
                + " (" + USER_ID + " INTEGER PRIMARY KEY, "
                + WEEKLY_DISTANCE + " NUMERIC, "
                + WEEKLY_TIME + " NUMERIC, "
                + WEEKLY_NUMWORKOUTS + " NUMERIC, "
                + WEEKLY_CALORIES + " NUMERIC" + ")";
        database.execSQL(UserWorkout_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL(" DROP TABLE IF EXISTS "+ USERS_INFO_TABLE);
        database.execSQL(" DROP TABLE IF EXISTS " + USER_WORKOUT_TABLE);
        onCreate(database);
    }


    //********************************************************************************
    //*************USER INFO: DATABASE OPERATIONS: ADD, EDIT, DELETE *****************

    //ADD A TASK TO THE DATABASE
    public void addUsers(UserInfo task){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(USER_NAME, task.getUserName());
        values.put(USER_GENDER, task.getUserGender());
        values.put(USER_WEIGHT, task.getUserWeight());

        int putID = (int) db.insert(USERS_INFO_TABLE, null, values);
        task.setUserId(putID);

        db.close();
    }

    //EDIT A TASK IN THE DATABASE
    public void editUsers (UserInfo task){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(USER_NAME, task.getUserName());
        values.put(USER_GENDER, task.getUserGender());
        values.put(USER_WEIGHT, task.getUserWeight());

        db.update(USERS_INFO_TABLE, values, USER_ID + " = ?",
                new String[]{String.valueOf(task.getUserId())});
        db.close();
    }

    //RETURN A SPECIFIC TASK IN THE DATABASE
    public UserInfo getUser (int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(USERS_INFO_TABLE,
                new String[]{USER_ID, USER_NAME, USER_GENDER, USER_WEIGHT},
                USER_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null, null);

        if(cursor!= null){
            cursor.moveToFirst();
        }

        UserInfo task = new UserInfo(
                Integer.getInteger(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                Float.parseFloat(cursor.getString(3)));

        db.close();
        return task;
    }

    //DELETE A SPECIFIC TASK FROM THE DATABASE
    public void deleteUser (UserInfo task){
        SQLiteDatabase db = this.getReadableDatabase();

        //delete the table row
        db.delete(USERS_INFO_TABLE, USER_ID + " = ?",
                new String[]{String.valueOf(task.getUserId())});
        db.close();
    }

    //ADD A TASK TO THE DATABASE
    public ArrayList<UserInfo> getAllUsersInfo(){
        ArrayList<UserInfo> AllUsersInfo = new ArrayList<UserInfo>();
        String queryList = "SELECT * FROM " + USERS_INFO_TABLE;

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(queryList, null);

        //COLLECT EACH ROW IN THE TABLE
        if(cursor.moveToFirst()){
            do{
                UserInfo task = new UserInfo();
                task.setUserId(cursor.getInt(0));
                task.setUserName(cursor.getString(1));
                task.setUserGender(cursor.getString(2));
                task.setUserWeight(cursor.getFloat(3));

                AllUsersInfo.add(task);

            }while (cursor.moveToNext());
        }
        return AllUsersInfo;
    }

    //********************************************************************************
    //********** USERS WORKOUT DATA: DATABASE OPERATIONS: ADD, EDIT, DELETE *************

    //ADD A TASK TO THE DATABASE
    public void addUsersWorkoutData (UserWorkoutData task)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(WEEKLY_DISTANCE, task.getWeeklyDistance());
        values.put(WEEKLY_TIME, task.getWeeklyTime());
        values.put(WEEKLY_NUMWORKOUTS, task.getWeeklyWorkoutCount());
        values.put(WEEKLY_CALORIES, task.getWeeklyCalories());

        int putID = (int) db.insert(USER_WORKOUT_TABLE, null, values);
        task.setUserDataId(putID);

        db.close();
    }

    //EDIT A TASK IN THE DATABASE
    public void editUsersWorkoutData (UserWorkoutData task){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(WEEKLY_DISTANCE, task.getWeeklyDistance());
        values.put(WEEKLY_TIME, task.getWeeklyTime());
        values.put(WEEKLY_NUMWORKOUTS, task.getWeeklyWorkoutCount());
        values.put(WEEKLY_CALORIES, task.getWeeklyCalories());

        db.update(USER_WORKOUT_TABLE, values, USER_ID + " = ?",
                new String[]{String.valueOf(task.getUserDataId())});
        db.close();
    }

    //ADD A TASK TO THE DATABASE
    public ArrayList<UserWorkoutData> getAllWeeklyUserData(){
        ArrayList<UserWorkoutData> AllWeeklyUserData = new ArrayList<UserWorkoutData>();
        String queryList = "SELECT * FROM " + USER_WORKOUT_TABLE;

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(queryList, null);

        int count =  0;
        //COLLECT EACH ROW IN THE TABLE
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext() && count < 7) {
                UserWorkoutData task = new UserWorkoutData();
                task.setUserDataId((cursor.getInt(cursor.getColumnIndex(DBHandler.USER_ID))));
                task.setWeeklyDistance(cursor.getFloat(cursor.getColumnIndex(DBHandler.WEEKLY_DISTANCE)));
                task.setWeeklyTime(cursor.getFloat(cursor.getColumnIndex(DBHandler.WEEKLY_TIME)));;
                task.setWeeklyWorkoutCount(cursor.getFloat(cursor.getColumnIndex(DBHandler.WEEKLY_NUMWORKOUTS)));
                task.setWeeklyCalories(cursor.getFloat(cursor.getColumnIndex(DBHandler.WEEKLY_CALORIES)));

                AllWeeklyUserData.add(task);
                count++;
            }
        }

        cursor.close();
        return AllWeeklyUserData;
    }

    //ADD A TASK TO THE DATABASE
    public ArrayList<UserWorkoutData> getAllUserData(){
        ArrayList<UserWorkoutData> AllUserData = new ArrayList<UserWorkoutData>();
        String queryList = "SELECT * FROM " + USER_WORKOUT_TABLE;

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(queryList, null);

        //COLLECT EACH ROW IN THE TABLE
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                UserWorkoutData task = new UserWorkoutData();
                task.setUserDataId((cursor.getInt(cursor.getColumnIndex(DBHandler.USER_ID))));
                task.setWeeklyDistance(cursor.getFloat(cursor.getColumnIndex(DBHandler.WEEKLY_DISTANCE)));
                task.setWeeklyTime(cursor.getFloat(cursor.getColumnIndex(DBHandler.WEEKLY_TIME)));
                task.setWeeklyWorkoutCount(cursor.getFloat(cursor.getColumnIndex(DBHandler.WEEKLY_NUMWORKOUTS)));
                task.setWeeklyCalories(cursor.getFloat(cursor.getColumnIndex(DBHandler.WEEKLY_CALORIES)));

                AllUserData.add(task);
            }
        }

        cursor.close();
        return AllUserData;
    }

    //Get distance for current user workout
    public double getCurrentDistance(int id){
       double distance = 0;
       SQLiteDatabase database = this.getWritableDatabase();
       Cursor cursor = database.rawQuery("SELECT * FROM " + USER_WORKOUT_TABLE + " WHERE "+ USER_ID +
       "=" + Integer.toString(id), null);
       cursor.moveToFirst();

       if(cursor.getCount() >0 ){
           distance = cursor.getDouble(cursor.getColumnIndex(WEEKLY_DISTANCE));
       }

       cursor.close();
       return distance;
    }


    //Get distance for current user workout
    public double getCurrentTime(int id){
        double time = 0;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + USER_WORKOUT_TABLE + " WHERE "+ USER_ID +
                "=" + Integer.toString(id), null);
        cursor.moveToFirst();

        if(cursor.getCount() >0 ){
            time = cursor.getLong(cursor.getColumnIndex(WEEKLY_TIME));
        }

        cursor.close();
        return time;
    }

    //DELETE A SPECIFIC TASK FROM THE DATABASE
    public void deleteUserData (UserWorkoutData task){
        SQLiteDatabase db = this.getReadableDatabase();

        //delete the table row
        db.delete(USER_WORKOUT_TABLE, USER_ID + " = ?",
                new String[]{String.valueOf(task.getUserDataId())});
        db.close();
    }
}
