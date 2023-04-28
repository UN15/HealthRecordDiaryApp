package ddwu.mobile.finalproject.ma02_20201018;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
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

public class AlarmUpdateActivity extends AppCompatActivity {
    final static String TAG = "AlarmUpdateActivity";
    EditText updateATitle;
    TimePicker update_timePicker;
    Alarm alarm;
    AlarmManager alarmManager = null;
    AManager aManager;
    private PendingIntent pendingIntent;
    String month, day, am_pm;
    int hour, minute;
    Date currentTime;
    Intent intent;
    int requestCode;
    int prehour, preminute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_update);
        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        createNotificationChannel();
        aManager = new AManager(this);
        alarm = (Alarm) getIntent().getSerializableExtra("alarm");
        prehour = alarm.getHour();
        preminute = alarm.getMinute();
        updateATitle = findViewById(R.id.updateATitle);

        update_timePicker = findViewById(R.id.update_time_picker);
        update_timePicker.setHour(prehour);
        update_timePicker.setMinute(preminute);
        updateATitle.setText(alarm.getAlarmTitle());
        requestCode = alarm.getRequestCode();

        currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat m = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat d = new SimpleDateFormat("dd", Locale.getDefault());
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
        hour = update_timePicker.getHour();
        minute = update_timePicker.getMinute();
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
        intent.putExtra("title", updateATitle.getText().toString());

        Log.d(TAG, "requestCode about: "+ requestCode);

        this.pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        this.alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAUpdate:
                String title = updateATitle.getText().toString();

                if(title.equals("")) {
                    Toast.makeText(this, "필수 항목 입력 후 다시 시도하시오", Toast.LENGTH_SHORT).show();
                }
                else{
                    start();
                    Log.d(TAG, "alarm update about: "+ title + hour+minute+am_pm+month+day);
                    alarm.setAlarmTitle(title);
                    alarm.setHour(timeSet(hour));
                    alarm.setMinute(minute);
                    alarm.setAm_pm(am_pm);
                    alarm.setMonth(month);
                    alarm.setDay(day);
                    alarm.setRequestCode(requestCode);
                    boolean result = aManager.modifyAlarm(alarm);

                    if (result) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("updateATitle", title);
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    } else {
                        Toast.makeText(this, "수정에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.btnAUpdate_cancel:
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