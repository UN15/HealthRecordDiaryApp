package ddwu.mobile.finalproject.ma02_20201018;

import android.content.Intent;

import java.io.Serializable;

public class Alarm implements Serializable {
    long _id;
    private String alarmTitle;
    private int hour;
    private int minute;
    private String am_pm;
    private String month;
    private String day;
    private int requestCode;
    public Alarm(String alarmTitle, int hour, int minute, String am_pm, String month, String day, int requestCode) {
        this.alarmTitle = alarmTitle;
        this.hour = hour;
        this.minute = minute;
        this.am_pm = am_pm;
        this.month = month;
        this.day = day;
        this.requestCode = requestCode;
    }

    public Alarm(long _id, String alarmTitle, int hour, int minute, String am_pm, String month, String day, int requestCode) {
        this._id = _id;
        this.alarmTitle = alarmTitle;
        this.hour = hour;
        this.minute = minute;
        this.am_pm = am_pm;
        this.month = month;
        this.day = day;
        this.requestCode = requestCode;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getAlarmTitle() {
        return alarmTitle;
    }

    public void setAlarmTitle(String alarmTitle) {
        this.alarmTitle = alarmTitle;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getAm_pm() {
        return am_pm;
    }

    public void setAm_pm(String am_pm) {
        this.am_pm = am_pm;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

}
