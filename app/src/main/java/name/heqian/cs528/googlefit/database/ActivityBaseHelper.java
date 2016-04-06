package name.heqian.cs528.googlefit.database;

import android.database.sqlite.SQLiteOpenHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import name.heqian.cs528.googlefit.database.ActivityDbSchema.ActivityTable;

/**
 * Created by pluxsuwong on 4/5/16.
 */
public class ActivityBaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "ActivityBaseHelper";
    private static final int VERSION = 2;
    private static final String DATABASE_NAME = "activityBase.db";

    public ActivityBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + ActivityTable.NAME + "(" +
                        " _id integer primary key autoincrement, " +
                        ActivityTable.Cols.STARTTIME + ", " +
                        ActivityTable.Cols.ACTIVITYTYPE +
                        ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}

