package ddwu.mobile.finalproject.ma02_20201018;
import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AlarmSetActivity extends AppCompatActivity {
    final int FLAG = 0;
    final static String TAG = "setalarm";
    EditText alarmTitle;
    TimePicker timePicker;
    AlarmManager alarmManager = null;
    AManager aManager;
    private PendingIntent pendingIntent;
    String month, day, am_pm;
    int hour, minute;
    Date currentTime;
    Intent intent;
    int requestCode;
    public static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_set);

        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        createNotificationChannel();
        aManager = new AManager(this);
        alarmTitle = findViewById(R.id.alarmTitle);
        timePicker = findViewById(R.id.time_picker);
        currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat m = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat d = new SimpleDateFormat("dd", Locale.getDefault());
        context = this;
        month = m.format(currentTime);
        day = d.format(currentTime);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);       // strings.xml 에 채널명 기록
            String description = getString(R.string.channel_description);       // strings.xml에 채널 설명 기록
            int importance = NotificationManager.IMPORTANCE_DEFAULT;    // 알림의 우선순위 지정
            NotificationChannel channel = new NotificationChannel(getString(R.string.CHANNEL_ID), name, importance);    // CHANNEL_ID 지정
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);  // 채널 생성
            notificationManager.createNotificationChannel(channel);
        }
    }

    //알람 시작
    private void start(){
        Calendar calendar = Calendar.getInstance();
        hour = timePicker.getHour();
        minute = timePicker.getMinute();
        am_pm = AM_PM(hour);

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        //현재시간보다 이전이면
        if(calendar.before(Calendar.getInstance())){
            //다음날로 설정
            calendar.add(Calendar.DATE, 1);
            day = String.valueOf(Integer.parseInt(day)+1);
        }
        //Receiver 설정
        intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("title", alarmTitle.getText().toString());

        requestCode = Integer.parseInt(month+day+hour+minute);
        Log.d(TAG, "setRequestCode about: "+ requestCode);

        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_alarm_add:
                String title = alarmTitle.getText().toString();
                if(title.equals("")) {
                    Toast.makeText(this, "필수 항목 입력 후 다시 시도하시오", Toast.LENGTH_SHORT).show();
                }
                else{
                    start();
                    boolean result = aManager.addNewAlarm(new Alarm(title, timeSet(hour), minute, am_pm, month, day, requestCode));

                    if (result) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("alarmTitle", title);
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    } else {
                        Toast.makeText(this, "추가에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.btn_alarm_cancel:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
    }

    private String AM_PM(int hour){
        if(hour>=12){
            am_pm = "오후";
        }
        else{
            am_pm="오전";
        }
        return am_pm;
    }

    private int timeSet(int hour){
        if(hour>12){
            hour = hour-12;
        }
        return hour;
    }
}
