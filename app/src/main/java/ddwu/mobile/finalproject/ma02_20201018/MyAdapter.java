package ddwu.mobile.finalproject.ma02_20201018;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<Record> recordList;
    private LayoutInflater layoutInflater;

    public MyAdapter(Context context, int layout, ArrayList<Record> recordList) {
        this.context=context;
        this.layout=layout;
        this.recordList=recordList;
        layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return recordList.size();
    }

    @Override
    public Object getItem(int i) {
        return recordList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return recordList.get(i).get_id();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final int pos = i;

        if(view == null){
            view = layoutInflater.inflate(layout, viewGroup, false);
        }
        TextView rTitle = view.findViewById(R.id.rTitle);
        TextView rVname = view.findViewById(R.id.rVname);

        rTitle.setText(recordList.get(pos).getRecordTitle());
        rVname.setText(recordList.get(pos).getRecordVName());
        return view;
    }
}
