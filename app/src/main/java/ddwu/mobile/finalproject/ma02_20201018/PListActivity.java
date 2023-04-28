package ddwu.mobile.finalproject.ma02_20201018;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PListActivity extends AppCompatActivity {
    ArrayList<Pharmacy> pharmacyList;
    MyPAdapter myPAdapter;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plist);
        pharmacyList = (ArrayList<Pharmacy>) getIntent().getSerializableExtra("list");
        listView = findViewById(R.id.phList);

        myPAdapter = new MyPAdapter(this, R.layout.custom_plist_adapter, pharmacyList);
        listView.setAdapter(myPAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Pharmacy pharmacy = pharmacyList.get(i);
                Intent intent = new Intent(PListActivity.this, PdetailActivity.class);
                intent.putExtra("pharmacy", pharmacy);
                startActivity(intent);
            }
        });

    }
}

