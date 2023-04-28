package ddwu.mobile.finalproject.ma02_20201018;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class AManager {
    AlarmHelper alarmHelper = null;
    Cursor cursor = null;

    public AManager(Context context){
        alarmHelper = new AlarmHelper(context);
    }

    public ArrayList<Alarm> getAllAlarm(){
        ArrayList<Alarm> alarmList = new ArrayList<Alarm>();
        SQLiteDatabase db = alarmHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + AlarmHelper.TABLE_NAME, null);

        while(cursor.moveToNext()){
            long id = cursor.getInt(0);
            String alarmTitle = cursor.getString(1);
            int hour = cursor.getInt(2);
            int minute = cursor.getInt(3);
            String am_pm = cursor.getString(4);
            String month = cursor.getString(5);
            String day = cursor.getString(6);
            int requestCode = cursor.getInt(7);
            alarmList.add(new Alarm(id, alarmTitle, hour, minute, am_pm, month, day, requestCode));
        }
        cursor.close();
        alarmHelper.close();

        return alarmList;
    }

    public boolean addNewAlarm(Alarm newAlarm){
        SQLiteDatabase db = alarmHelper.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(AlarmHelper.COL_ATITLE, newAlarm.getAlarmTitle());
        value.put(AlarmHelper.COL_HOUR, newAlarm.getHour());
        value.put(AlarmHelper.COL_MINUTE, newAlarm.getMinute());
        value.put(AlarmHelper.COL_AM_PM, newAlarm.getAm_pm());
        value.put(AlarmHelper.COL_MONTH, newAlarm.getMonth());
        value.put(AlarmHelper.COL_DAY, newAlarm.getDay());
        value.put(AlarmHelper.COL_REQUESTCODE, newAlarm.getRequestCode());

        long count = db.insert(AlarmHelper.TABLE_NAME, null, value);
        alarmHelper.close();
        if(count>0)
            return true;
        return false;
    }

    public boolean modifyAlarm(Alarm alarm){

        SQLiteDatabase sqLiteDatabase = alarmHelper.getWritableDatabase();

        ContentValues row = new ContentValues();
        row.put(AlarmHelper.COL_ATITLE, alarm.getAlarmTitle());
        row.put(AlarmHelper.COL_HOUR, alarm.getHour());
        row.put(AlarmHelper.COL_MINUTE, alarm.getMinute());
        row.put(AlarmHelper.COL_AM_PM, alarm.getAm_pm());
        row.put(AlarmHelper.COL_MONTH, alarm.getMonth());
        row.put(AlarmHelper.COL_DAY, alarm.getDay());
        row.put(AlarmHelper.COL_REQUESTCODE, alarm.getRequestCode());

        String whereClause = AlarmHelper.COL_ID + "=?";
        String[] whereArgs = new String[] {String.valueOf(alarm.get_id())};
        int result = sqLiteDatabase.update(AlarmHelper.TABLE_NAME, row, whereClause, whereArgs);

        alarmHelper.close();
        if(result>0)
            return true;
        return false;
    }

    public boolean removeAlarm(long id){
        SQLiteDatabase sqLiteDatabase = alarmHelper.getWritableDatabase();
        String whereClause = AlarmHelper.COL_ID + "=?";
        String[] whereArgs = new String[] { String.valueOf(id)};
        int result = sqLiteDatabase.delete(AlarmHelper.TABLE_NAME, whereClause, whereArgs);
        alarmHelper.close();
        if(result>0)
            return true;
        return false;
    }


    public void close(){
        if(alarmHelper != null)
            alarmHelper.close();
        if(cursor != null)
            cursor.close();
    }

}

