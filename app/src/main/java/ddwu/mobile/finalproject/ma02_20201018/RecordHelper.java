package ddwu.mobile.finalproject.ma02_20201018;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RecordHelper extends SQLiteOpenHelper {
    final static String TAG = "RecordHelper";
    final static String DB_NAME = "record.db";

    public final static String TABLE_NAME = "record_table";
    public final static String COL_ID = "_id";
    public final static String COL_TITLE = "recordTitle";
    public final static String COL_CONTENT = "recordContent";
    public final static String COL_DATE = "recordDate";
    public final static String COL_VNAME = "recordVName";
    public final static String COL_RESULT = "recordResult";
    public final static String COL_IMAGE = "image";


    public RecordHelper(Context context){
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + " (" + COL_ID + " integer primary key autoincrement, " +
                COL_TITLE + " TEXT, " + COL_CONTENT + " TEXT, " + COL_DATE + " TEXT, " +COL_VNAME + " TEXT, " +
                COL_RESULT + " TEXT, " +COL_IMAGE + " TEXT)";
        Log.d(TAG, sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}