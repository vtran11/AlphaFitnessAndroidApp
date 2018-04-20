package vytran.cs175.sjsu.alphafitnessandroidapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ThaoVy on 4/19/18.
 */

public class DBHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION = 1;

    static final String USERS_IFO_TABLE = "users";
    static final String COLUMN_ID = "userID";
    static final String COLUMN_NAME = "name";
    static final String COLUMN_GENDER = "gender";
    static final String COLUMN_WEIGHT= "weight";

    static final String USER_WORKOUT_TABLE = "userData";
    static final String DISTANCE_WEEKLY= "workoutDistance";
    static final String TIME_WEEKLY= "workoutTime";
    static final String NUMWORKOUTS_WEEKLY= "numWorkouts";
    static final String CALORIES_WEEKLY= "workoutCalories";

    private static final String USERINFO_TABLE =
            " CREATE TABLE " + USERS_IFO_TABLE + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_GENDER + " TEXT, " +
                    COLUMN_WEIGHT + " NUMERIC" + ")";

    private static final String USERWORKOUT_TABLE =
            " CREATE TABLE " + USER_WORKOUT_TABLE + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DISTANCE_WEEKLY + " NUMERIC, " +
                    TIME_WEEKLY + " NUMERIC, " +
                    NUMWORKOUTS_WEEKLY + " NUMERIC, " +
                    CALORIES_WEEKLY + " NUMERIC" + ")";

    DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USERINFO_TABLE);
        db.execSQL(USERWORKOUT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS "+ USERS_IFO_TABLE);
        db.execSQL(" DROP TABLE IF EXISTS " + USER_WORKOUT_TABLE);
        db.execSQL(USERINFO_TABLE);
        db.execSQL(USERWORKOUT_TABLE);
    }
}
