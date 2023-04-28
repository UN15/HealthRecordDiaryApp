package ddwu.mobile.finalproject.ma02_20201018;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyPAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<Pharmacy> pList;
    private LayoutInflater layoutInflater;

    public MyPAdapter(Context context, int layout, ArrayList<Pharmacy> pList) {
        this.context=context;
        this.layout=layout;
        this.pList=pList;
        layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return pList.size();
    }

    @Override
    public Object getItem(int i) {
        return pList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return pList.get(i).get_id();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final int pos = i;

        if(view == null){
            view = layoutInflater.inflate(layout, viewGroup, false);
        }
        TextView pName = view.findViewById(R.id.pName);
        TextView pAddr = view.findViewById(R.id.pAddr);
        TextView pTel = view.findViewById(R.id.pTel);

        pName.setText(pList.get(pos).getpYadmNm());
        pAddr.setText(pList.get(pos).getpAddr());
        pTel.setText(pList.get(pos).getpTelno());

        return view;
    }
}
