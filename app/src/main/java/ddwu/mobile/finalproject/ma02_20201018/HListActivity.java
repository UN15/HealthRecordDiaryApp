package ddwu.mobile.finalproject.ma02_20201018;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class HListActivity extends AppCompatActivity {
    ArrayList<Hospital> hospitalList;
    MyHAdapter myHAdapter;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hlist);
        hospitalList = (ArrayList<Hospital>) getIntent().getSerializableExtra("list");
        listView = findViewById(R.id.hList);

        myHAdapter = new MyHAdapter(this, R.layout.custom_hlist_adapter,hospitalList);
        listView.setAdapter(myHAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Hospital hospital = hospitalList.get(i);
                Intent intent = new Intent(HListActivity.this, HdetailActivity.class);
                intent.putExtra("hospital", hospital);
                startActivity(intent);
            }
        });

    }
}
