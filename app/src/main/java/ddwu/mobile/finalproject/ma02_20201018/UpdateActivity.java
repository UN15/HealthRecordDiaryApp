package ddwu.mobile.finalproject.ma02_20201018;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class UpdateActivity extends AppCompatActivity {
    final static String TAG = "UpdateActivity";
    static final int UPDATE_CODE = 2;

    EditText updateTitle;
    EditText updateContent;
    EditText updateVName;
    EditText updateResult;
    RecordManager recordManager;
    Record record;
    String day;
    Uri uri;
    String image;
    ImageView imageView;
    TextView updateImageText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        record = (Record)getIntent().getSerializableExtra("record");
        updateTitle = findViewById(R.id.updateTitle);
        updateContent = findViewById(R.id.updateContent);
        updateVName = findViewById(R.id.updateVName);
        updateResult = findViewById(R.id.updateResult);
        imageView = findViewById(R.id.imageView);
        updateImageText = findViewById(R.id.updateImageText);

        image = record.getImage();
        recordManager = new RecordManager(this);
        updateTitle.setText(record.getRecordTitle());
        updateContent.setText(record.getRecordContent());
        updateVName.setText(record.getRecordVName());
        updateResult.setText(record.getRecordResult());
        updateImageText.setText(image);
        if(!image.equals("no image")) {
            //Log.e(TAG, "image 값:"+image);
            setImage(Uri.parse(image));
        }
        day = record.getRecordDate();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_updateImage:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, UPDATE_CODE);
                break;
            case R.id.btn_update:
                String title = updateTitle.getText().toString();
                String content = updateContent.getText().toString();
                String vName = updateVName.getText().toString();
                String aResult = updateResult.getText().toString();
                if(TextUtils.isEmpty(title) || TextUtils.isEmpty(content) || TextUtils.isEmpty(vName) || TextUtils.isEmpty(aResult)) {
                    Toast.makeText(this, "필수 항목 입력 후 다시 시도하시오", Toast.LENGTH_SHORT).show();
                }
                else {
                    record.setRecordTitle(title);
                    record.setRecordContent(content);
                    record.setRecordDate(day);
                    record.setRecordVName(vName);
                    record.setRecordResult(aResult);
                    record.setImage(image);
                    Log.d(TAG, title+content+day+vName+aResult+image);
                    boolean result = recordManager.modifyRecord(record);

                    if (result) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("updateTitle", title);
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    }
                    else {
                        setResult(RESULT_CANCELED);
                        finish();
                    }
                }
                break;
            case R.id.btn_updateCancel:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == UPDATE_CODE) {
            if(data !=null) {
                uri = data.getData();
                image = uri.toString();
                updateImageText.setText(image);
            }
        }
    }
    private void setImage(Uri uri) {
        try{
            InputStream in = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(in);
            imageView.setImageBitmap(bitmap);
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }
}