package ddwu.mobile.finalproject.ma02_20201018;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class AlarmActivity extends AppCompatActivity {
    final static int SET_CODE = 100;
    final static int UPDATE_CODE = 200;

    final static String TAG = "alarm activity";
    AlarmManager alarmManager = null;
    ArrayList<Alarm> alarmList;
    AManager aManager;
    MyAlarmAdapter myAlarmAdapter;
    ListView listView;
    Button btn_alarm;
    Intent intent;
    private PendingIntent pendingIntent;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        listView = findViewById(R.id.aList);
        btn_alarm = findViewById(R.id.btn_alarm);
        alarmList = new ArrayList<Alarm>();
        aManager = new AManager(this);
        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        intent = new Intent(this, AlarmReceiver.class);

        myAlarmAdapter = new MyAlarmAdapter(this, R.layout.custom_alist_adapter, alarmList);
        listView.setAdapter(myAlarmAdapter);
        btn_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AlarmActivity.this, AlarmSetActivity.class);
                startActivityForResult(intent, SET_CODE);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Alarm alarm = alarmList.get(i);
                int requestCode = alarmList.get(i).getRequestCode();
                intent = new Intent(AlarmActivity.this, AlarmReceiver.class);
                pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                alarmManager.cancel(pendingIntent);

                Intent intent2 = new Intent(AlarmActivity.this, AlarmUpdateActivity.class);
                intent2.putExtra("alarm", alarm);
                startActivityForResult(intent2, UPDATE_CODE);
            }
        });

        //삭제
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int pos = i;
                String a = alarmList.get(i).getAlarmTitle();
                int requestCode = alarmList.get(i).getRequestCode();
                AlertDialog.Builder builder = new AlertDialog.Builder(AlarmActivity.this);
                builder.setTitle("알람 삭제")
                        .setMessage(a+"를 삭제하시겠습니까?")
                        .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                boolean result = aManager.removeAlarm(alarmList.get(pos).get_id());
                                if(result){
                                    pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                                    alarmManager.cancel(pendingIntent);
                                    Toast.makeText(AlarmActivity.this, "삭제를 완료하였습니다", Toast.LENGTH_SHORT).show();
                                    alarmList.clear();
                                    alarmList.addAll(aManager.getAllAlarm());
                                    myAlarmAdapter.notifyDataSetChanged();
                                }
                                else{
                                    Toast.makeText(AlarmActivity.this, "삭제를 실패하였습니다", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("취소", null)
                        .setCancelable(false)
                        .show();
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        alarmList.clear();
        alarmList.addAll(aManager.getAllAlarm());
        myAlarmAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SET_CODE ) {  // AddActivity 호출 후 결과 확인
            switch (resultCode) {
                case RESULT_OK:
                    String alarmTitle = data.getStringExtra("alarmTitle");
                    Toast.makeText(this, "새로운 알람 " + alarmTitle + " 추가 완료", Toast.LENGTH_SHORT).show();
                    break;
                case RESULT_CANCELED:
                    Toast.makeText(this, "새로운 알람 추가 취소", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        else if (requestCode == UPDATE_CODE ) {  // AddActivity 호출 후 결과 확인
            switch (resultCode) {
                case RESULT_OK:
                    String updateATitle = data.getStringExtra("updateATitle");
                    Toast.makeText(this, "알람 " + updateATitle + " 수정 완료", Toast.LENGTH_SHORT).show();
                    break;
                case RESULT_CANCELED:
                    Toast.makeText(this, "알람 수정 취소", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

}
