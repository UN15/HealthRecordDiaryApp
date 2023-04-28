package ddwu.mobile.finalproject.ma02_20201018;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public CalendarView calendarView;
    ImageButton imageButton;
    Date currentTime;
    TextView today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        today = findViewById(R.id.today);
        calendarView = findViewById(R.id.calendarView);
        imageButton = findViewById(R.id.imageBtn);
        currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat y = new SimpleDateFormat("yyyy", Locale.getDefault());
        SimpleDateFormat m = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat d = new SimpleDateFormat("dd", Locale.getDefault());

        String year = y.format(currentTime);
        String month = m.format(currentTime);
        String date = d.format(currentTime);

        today.setText(year+"년 "+month+"월 "+date+"일 ");

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                Intent intent = new Intent(MainActivity.this, RecordActivity.class);
                intent.putExtra("year", year);
                intent.putExtra("month", month);
                intent.putExtra("date", dayOfMonth);
                startActivity(intent);
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AlarmActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_alarm:
                Intent intent_alarm = new Intent(this, AlarmActivity.class);
                startActivity(intent_alarm);
                break;
            case R.id.menu_hospital:
                Intent intent_hospital = new Intent(this, HospitalActivity.class);
                startActivity(intent_hospital);
                break;
            case R.id.menu_pharmacy:
                Intent intent_pharmacy = new Intent(this, PharmacyActivity.class);
                startActivity(intent_pharmacy);
                break;
        }
        return true;
    }

}