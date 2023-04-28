package ddwu.mobile.finalproject.ma02_20201018;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAlarmAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<Alarm> aList;

    private LayoutInflater layoutInflater;

    public MyAlarmAdapter(Context context, int layout, ArrayList<Alarm> aList) {
        this.context=context;
        this.layout=layout;
        this.aList=aList;
        layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return aList.size();
    }

    @Override
    public Object getItem(int i) {
        return aList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return aList.get(i).get_id();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final int pos = i;

        if(view == null){
            view = layoutInflater.inflate(layout, viewGroup, false);
        }
        TextView am_pm = view.findViewById(R.id.am_pm);
        TextView textHour = view.findViewById(R.id.textHour);
        TextView textMinute = view.findViewById(R.id.textMinute);
        TextView timeMonth = view.findViewById(R.id.timeMonth);
        TextView timeDay = view.findViewById(R.id.timeDay);
        TextView atitle = view.findViewById(R.id.aTitle);

        am_pm.setText(aList.get(pos).getAm_pm());
        textHour.setText(String.valueOf(aList.get(pos).getHour())+"시");
        textMinute.setText(String.valueOf(aList.get(pos).getMinute())+"분");
        timeMonth.setText(aList.get(pos).getMonth()+"월");
        timeDay.setText(aList.get(pos).getDay()+"일");
        atitle.setText(aList.get(pos).getAlarmTitle());

        return view;
    }
}


