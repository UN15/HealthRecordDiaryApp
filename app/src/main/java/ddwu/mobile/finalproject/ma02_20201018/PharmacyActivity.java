package ddwu.mobile.finalproject.ma02_20201018;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class PharmacyActivity extends AppCompatActivity {
    final static String TAG = "PharmacyActivity";
    final int REQ_PERMISSION_CODE = 100;

    EditText pDong;//읍면동명
    EditText pRange;//반경
    String pApiAddress;
    ArrayList<Pharmacy> resultList;
    GoogleMap mGoogleMap;
    SupportMapFragment mapFragment;
    FusedLocationProviderClient flpClient;
    private Marker centerMarker;

    String currentLocation;
   // Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy);

        pDong = findViewById(R.id.pDong);
        pRange = findViewById(R.id.pRange);
        pApiAddress = getResources().getString(R.string.pApiAddress);
        flpClient = LocationServices.getFusedLocationProviderClient(this);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.pMap);
        mapFragment.getMapAsync(mapReadyCallback);

    }

    @Override
    protected void onPause() {
        super.onPause();
        // 위치정보 수신 종료
        flpClient.removeLocationUpdates(mLocCallback);

    }

    OnMapReadyCallback mapReadyCallback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            mGoogleMap = googleMap;
            LatLng latLng = new LatLng(37.606320, 127.041808);
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));

            MarkerOptions options = new MarkerOptions();
            options.position(latLng);
            options.title("현재 위치");
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

            centerMarker = mGoogleMap.addMarker(options);
            centerMarker.showInfoWindow();
        }
    };

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_pSearch:
                if (!isOnline()) {
                    Toast.makeText(PharmacyActivity.this, "네트워크를 사용가능하게 설정해주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    String pDongText = pDong.getText().toString();
                    String pRangeText = pRange.getText().toString();
                    int range = Integer.parseInt(pRangeText);
                    new NetworkAsyncTask().execute(pApiAddress+pDongText+"&radius="+range);    // server_url 에 입력한 날짜를 결합한 후 AsyncTask 실행
                    break;
                }

            case R.id.btn_pCurrent:
                checkPermission();
                break;
        }
    }

    class NetworkAsyncTask extends AsyncTask<String, Void, String> {

        final static String NETWORK_ERR_MSG = "Server Error!";
        public final static String TAG = "pNetworkAsyncTask";
        ProgressDialog progressDlg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDlg = ProgressDialog.show(PharmacyActivity.this, "Wait", "Downloading...");     // 진행상황 다이얼로그 출력
        }

        @Override
        protected String doInBackground(String... strings) {
            String address = strings[0];
            String result = downloadContents(address);
            if (result == null) {
                cancel(true);
                return NETWORK_ERR_MSG;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDlg.dismiss();  // 진행상황 다이얼로그 종료
//          parser 생성 및 OpenAPI 수신 결과를 사용하여 parsing 수행
            PharmacyXmlParser parser = new PharmacyXmlParser();
            resultList = parser.parse(result);

            if (resultList == null) {       // 올바른 결과를 수신하지 못하였을 경우 안내
                Toast.makeText(PharmacyActivity.this, "정보를 올바르게 입력하세요.", Toast.LENGTH_SHORT).show();
            } else if (!resultList.isEmpty()) {
                Intent intent = new Intent(PharmacyActivity.this, PListActivity.class);
                intent.putExtra("list", resultList);
                startActivity(intent);
            }
        }

        @Override
        protected void onCancelled(String msg) {
            super.onCancelled();
            progressDlg.dismiss();
            Toast.makeText(PharmacyActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    protected String downloadContents(String address) {
        HttpURLConnection conn = null;
        InputStream stream = null;
        String result = null;

        try {
            URL url = new URL(address);
            conn = (HttpURLConnection)url.openConnection();
            stream = getNetworkConnection(conn);
            result = readStreamToString(stream);
            Log.d(TAG, "Result: " + result);
            if (stream != null) stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) conn.disconnect();
        }

        return result;
    }

    private InputStream getNetworkConnection(HttpURLConnection conn) throws Exception {
        conn.setReadTimeout(3000);
        conn.setConnectTimeout(3000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);

        if (conn.getResponseCode() != HttpsURLConnection.HTTP_OK) {
            throw new IOException("HTTP error code: " + conn.getResponseCode());
        }

        return conn.getInputStream();
    }

    /* InputStream을 전달받아 문자열로 변환 후 반환 */
    protected String readStreamToString(InputStream stream){
        StringBuilder result = new StringBuilder();

        try {
            InputStreamReader inputStreamReader = new InputStreamReader(stream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String readLine = bufferedReader.readLine();

            while (readLine != null) {
                result.append(readLine + "\n");
                readLine = bufferedReader.readLine();
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    private void checkPermission() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            flpClient.requestLocationUpdates(
                    getLocationRequest(),
                    mLocCallback,
                    Looper.getMainLooper()
            );
        } else {
            // 권한 요청
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, REQ_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQ_PERMISSION_CODE:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "위치권한 획득 완료", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "위치권한 미획득", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private LocationRequest getLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }


    LocationCallback mLocCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            // 위치정보 수신 시 동작 실행
            for(Location loc: locationResult.getLocations()){
                double lat = loc.getLatitude();
                double lng = loc.getLongitude();
                LatLng currentLoc = new LatLng(lat, lng);
                executeGeocoding(loc);
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 17));
                centerMarker.setPosition(currentLoc);
            }
        }
    };

    private void executeGeocoding(Location location) {
        if (Geocoder.isPresent()) {
            //Toast.makeText(this, "Run Geocoder", Toast.LENGTH_SHORT).show();
            if (location != null)  {
                new pGeoTask().execute(location);
            }
        } else {
            //Toast.makeText(this, "No Geocoder", Toast.LENGTH_SHORT).show();
        }
    }


    class pGeoTask extends AsyncTask<Location, Void, List<Address>> {
        //        Geocoder 객체 생성
        Geocoder geocoder = new Geocoder(PharmacyActivity.this, Locale.getDefault());

        @Override
        protected List<Address> doInBackground(Location... locations) {
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(locations[0].getLatitude(), locations[0].getLongitude(), 5);
                // geocoding 수행
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }

        @Override
        protected void onPostExecute(List<Address> addresses) {
            Address address = addresses.get(0);
            currentLocation = address.getThoroughfare();
            pDong.setText(currentLocation);
        }
    }


}
