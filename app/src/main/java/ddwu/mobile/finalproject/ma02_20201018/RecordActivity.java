package ddwu.mobile.finalproject.ma02_20201018;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RecordActivity extends AppCompatActivity {
    final static int ADD_CODE = 100;
    final static int UPDATE_CODE = 200;

    TextView textView;
    ListView listView;
    ArrayList<Record> recordList = null;
    RecordManager recordManager;
    MyAdapter myAdapter;
    int year;
    int month;
    int date;
    String day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        recordList = new ArrayList<Record>();
        textView = findViewById(R.id.textView2);
        listView = findViewById(R.id.listView);

        myAdapter = new MyAdapter(this, R.layout.custom_adapter, recordList);
        listView.setAdapter(myAdapter);
        recordManager = new RecordManager(this);

        year = getIntent().getIntExtra("year", 1);
        month = getIntent().getIntExtra("month", 1)+1;
        date = getIntent().getIntExtra("date", 1);
        day=year+"년 "+month+"월 "+date+"일";
        textView.setText(day);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Record record = recordList.get(i);
                Intent intent = new Intent(RecordActivity.this, UpdateActivity.class);
                intent.putExtra("record", record);
                startActivityForResult(intent, UPDATE_CODE);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int pos = i;
                String n = recordList.get(i).getRecordTitle();
                AlertDialog.Builder builder = new AlertDialog.Builder(RecordActivity.this);
                builder.setTitle("건강기록 삭제")
                        .setMessage(n+"를 삭제하시겠습니까?")
                        .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                boolean result = recordManager.removeRecord(recordList.get(pos).get_id());
                                if(result){
                                    Toast.makeText(RecordActivity.this, "삭제를 완료하였습니다", Toast.LENGTH_SHORT).show();
                                    recordList.clear();
                                    recordList.addAll(recordManager.getRecordWithDay(day));
                                    myAdapter.notifyDataSetChanged();
                                }
                                else{
                                    Toast.makeText(RecordActivity.this, "삭제를 실패하였습니다", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("취소", null)
                        .setCancelable(false)
                        .show();
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        recordList.clear();
        recordList.addAll(recordManager.getRecordWithDay(day));
        myAdapter.notifyDataSetChanged();
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.addBtn:
                Intent intent = new Intent(RecordActivity.this, AddActivity.class);
                intent.putExtra("day", day);
                startActivityForResult(intent, ADD_CODE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_CODE) {  // AddActivity 호출 후 결과 확인
            switch (resultCode) {
                case RESULT_OK:
                    String recordTitle = data.getStringExtra("recordTitle");
                    Toast.makeText(this, "새로운 건강 기록 " + recordTitle + " 추가 완료", Toast.LENGTH_SHORT).show();
                    break;
                case RESULT_CANCELED:
                    Toast.makeText(this, "새로운 건강 기록 추가 취소", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        else if (requestCode == UPDATE_CODE) {    // UpdateActivity 호출 후 결과 확인
            switch (resultCode) {
                case RESULT_OK:
                    String updateTitle = data.getStringExtra("updateTitle");
                    Toast.makeText(this, "건강 기록 " + updateTitle+ " 수정 완료", Toast.LENGTH_SHORT).show();
                    break;
                case RESULT_CANCELED:
                    Toast.makeText(this, "건강 기록 수정 취소", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
