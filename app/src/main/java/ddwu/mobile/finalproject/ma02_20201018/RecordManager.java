package ddwu.mobile.finalproject.ma02_20201018;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class RecordManager {
    RecordHelper recordHelper = null;
    Cursor cursor = null;

    public RecordManager(Context context){
        recordHelper = new RecordHelper(context);
    }

    public ArrayList<Record> getRecordWithDay(String day){
        ArrayList<Record> recordList = new ArrayList<Record>();
        String selection = "recordDate=?";
        String[] selectArgs = new String[]{day};
        SQLiteDatabase db = recordHelper.getReadableDatabase();
        Cursor cursor = db.query(RecordHelper.TABLE_NAME, null,selection, selectArgs,null,null,null,null);

        while(cursor.moveToNext()){
            long id = cursor.getInt(0);
            String recordTitle = cursor.getString(1);
            String recordContent = cursor.getString(2);
            String recordDate = cursor.getString(3);
            String recordVName = cursor.getString(4);
            String recordResult = cursor.getString(5);
            String image = cursor.getString(6);

            recordList.add(new Record(id, recordTitle, recordContent,recordDate, recordVName, recordResult, image));
        }
        cursor.close();
        recordHelper.close();

        return recordList;
    }

    public boolean addNewRecord(Record newRecord){
        SQLiteDatabase db = recordHelper.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(RecordHelper.COL_TITLE, newRecord.getRecordTitle());
        value.put(RecordHelper.COL_CONTENT, newRecord.getRecordContent());
        value.put(RecordHelper.COL_DATE, newRecord.getRecordDate());
        value.put(RecordHelper.COL_VNAME, newRecord.getRecordVName());
        value.put(RecordHelper.COL_RESULT, newRecord.getRecordResult());
        value.put(RecordHelper.COL_IMAGE, newRecord.getImage());

        long count = db.insert(RecordHelper.TABLE_NAME, null, value);
        recordHelper.close();
        if(count>0)
            return true;
        return false;
    }

    public boolean modifyRecord(Record record){

        SQLiteDatabase sqLiteDatabase = recordHelper.getWritableDatabase();

        ContentValues row = new ContentValues();
        row.put(RecordHelper.COL_TITLE, record.getRecordTitle());
        row.put(RecordHelper.COL_CONTENT, record.getRecordContent());
        row.put(RecordHelper.COL_DATE, record.getRecordDate());
        row.put(RecordHelper.COL_VNAME, record.getRecordVName());
        row.put(RecordHelper.COL_RESULT, record.getRecordResult());
        row.put(RecordHelper.COL_IMAGE, record.getImage());

        String whereClause = RecordHelper.COL_ID + "=?";
        String[] whereArgs = new String[] {String.valueOf(record.get_id())};
        int result = sqLiteDatabase.update(RecordHelper.TABLE_NAME, row, whereClause, whereArgs);

        recordHelper.close();
        if(result>0)
            return true;
        return false;
    }

    public boolean removeRecord(long id){
        SQLiteDatabase sqLiteDatabase = recordHelper.getWritableDatabase();
        String whereClause = RecordHelper.COL_ID + "=?";
        String[] whereArgs = new String[] { String.valueOf(id)};
        int result = sqLiteDatabase.delete(RecordHelper.TABLE_NAME, whereClause, whereArgs);
        recordHelper.close();
        if(result>0)
            return true;
        return false;
    }


    public void close(){
        if(recordHelper != null)
            recordHelper.close();
        if(cursor != null)
            cursor.close();
    }
}
