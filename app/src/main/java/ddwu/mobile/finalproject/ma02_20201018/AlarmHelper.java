package ddwu.mobile.finalproject.ma02_20201018;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.Serializable;

public class AlarmHelper extends SQLiteOpenHelper {
    final static String TAG = "AlarmHelper";
    final static String DB_NAME = "alarm.db";

    public final static String TABLE_NAME = "alarm_table";
    public final static String COL_ID = "_id";
    public final static String COL_ATITLE = "alarmTitle";
    public final static String COL_HOUR = "hour";
    public final static String COL_MINUTE = "minute";
    public final static String COL_AM_PM = "am_pm";
    public final static String COL_MONTH = "month";
    public final static String COL_DAY = "day";
    public final static String COL_REQUESTCODE = "requestCode";



    public AlarmHelper(Context context){
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + " (" + COL_ID + " integer primary key autoincrement, " +
                COL_ATITLE + " TEXT, " + COL_HOUR + " TEXT, " +
                COL_MINUTE + " TEXT, " +  COL_AM_PM + " TEXT, " + COL_MONTH + " TEXT, " +
                COL_DAY + " TEXT, " + COL_REQUESTCODE + " TEXT)";
        Log.d(TAG, sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}
