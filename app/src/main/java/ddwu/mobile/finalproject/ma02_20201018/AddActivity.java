package ddwu.mobile.finalproject.ma02_20201018;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddActivity extends AppCompatActivity {
    static final int REQUEST_CODE = 1;
    EditText etTitle;
    EditText etContent;
    EditText etVName;
    EditText etResult;
    TextView imageText;
    RecordManager recordManager;
    Uri uri;
    String day;
    String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        etTitle = findViewById(R.id.addTitle);
        etContent = findViewById(R.id.addContent);
        etVName = findViewById(R.id.addVName);
        etResult = findViewById(R.id.addResult);
        imageText = findViewById(R.id.imageText);
        day = getIntent().getStringExtra("day");
        recordManager = new RecordManager(this);
        image = "no image";

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_addImage:
                if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    startActivityForResult(intent, REQUEST_CODE);
                }else{
                    requestPermissions(new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
                }


                break;
            case R.id.btn_add:
                String title = etTitle.getText().toString();
                String content = etContent.getText().toString();
                String vName = etVName.getText().toString();
                String aResult = etResult.getText().toString();

                if(title.equals("") || content.equals("") || vName.equals("") || aResult.equals("")) {
                    Toast.makeText(this, "필수 항목 입력 후 다시 시도하시오", Toast.LENGTH_SHORT).show();
                }
                else{
                    boolean result = recordManager.addNewRecord(new Record(title, content, day, vName, aResult, image));

                    if (result) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("recordTitle", title);
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    } else {
                        Toast.makeText(this, "추가에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.btn_addCancel:   // 취소에 따른 처리
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == REQUEST_CODE) {
            if(data != null) {
                uri = data.getData();
                image = uri.toString();
                imageText.setText(image);
            }
        }
    }
}