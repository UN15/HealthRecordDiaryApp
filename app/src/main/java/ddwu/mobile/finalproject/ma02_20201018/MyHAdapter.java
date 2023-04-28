package ddwu.mobile.finalproject.ma02_20201018;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

public class MyHAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<Hospital> hList;
    private LayoutInflater layoutInflater;

    public MyHAdapter(Context context, int layout, ArrayList<Hospital> hList) {
        this.context=context;
        this.layout=layout;
        this.hList=hList;
        layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return hList.size();
    }

    @Override
    public Object getItem(int i) {
        return hList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return hList.get(i).get_id();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final int pos = i;

        if(view == null){
            view = layoutInflater.inflate(layout, viewGroup, false);
        }
        TextView hName = view.findViewById(R.id.hName);
        TextView hAddr = view.findViewById(R.id.hAddr);
        TextView hTel = view.findViewById(R.id.hTel);

        hName.setText(hList.get(pos).getYadmNm());
        hAddr.setText(hList.get(pos).getAddr());
        hTel.setText(hList.get(pos).getTelno());

        return view;
    }
}

