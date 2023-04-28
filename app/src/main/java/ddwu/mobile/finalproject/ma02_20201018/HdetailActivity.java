package ddwu.mobile.finalproject.ma02_20201018;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
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

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class HdetailActivity extends AppCompatActivity {
    final int REQ_PERMISSION_CODE = 100;

    Hospital hospital;
    FusedLocationProviderClient hd_flpClient;
    GoogleMap mGoogleMap;
    SupportMapFragment hmapFragment;
    private Marker hd_centerMarker;
    private Marker hd_resultMarker;

    String hAddr;
    String hName;
    String hTel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hdetail);
        checkPermission();
        hospital = (Hospital) getIntent().getSerializableExtra("hospital");
        hAddr = hospital.getAddr();
        hName = hospital.getYadmNm();
        hTel = hospital.getTelno();
        hd_flpClient = LocationServices.getFusedLocationProviderClient(this);
        hmapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.hdetailMap);
        hmapFragment.getMapAsync(mapReadyCallback);
        hd_flpClient.requestLocationUpdates(
                getLocationRequest(),
                mLocCallback,
                Looper.getMainLooper()
        );
    }

    @Override
    protected void onPause() {
        super.onPause();
        hd_flpClient.removeLocationUpdates(mLocCallback);
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

            hd_centerMarker = mGoogleMap.addMarker(options);
            hd_centerMarker.showInfoWindow();

            executeGeocoding(hAddr);
        }
    };

    private LocationRequest getLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
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
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 14));
                hd_centerMarker.setPosition(currentLoc);
            }
        }
    };

    private void executeGeocoding(String location) {
        if (Geocoder.isPresent()) {
            //Toast.makeText(this, "Run Geocoder", Toast.LENGTH_SHORT).show();
            if (location != null)  {
                new HdReverseGeoTask().execute(location);
            }
        } else {
            Toast.makeText(this, "No Geocoder", Toast.LENGTH_SHORT).show();
        }
    }

    class HdReverseGeoTask extends AsyncTask<String, Void, List<Address>> {
        Geocoder geocoder = new Geocoder(HdetailActivity.this, Locale.getDefault());
        @Override
        protected List<Address> doInBackground(String... p) {
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocationName(p[0],1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }
        @Override
        protected void onPostExecute(List<Address> addresses) {
            Address address = addresses.get(0);
            double lat = address.getLatitude();
            double lng = address.getLongitude();
            LatLng result = new LatLng(lat, lng);

            MarkerOptions hdresultMarkerOptions = new MarkerOptions()
                    .position(result)
                    .title(hName)
                    .snippet(hTel)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            hd_resultMarker = mGoogleMap.addMarker(hdresultMarkerOptions);
        }
    }

    private void checkPermission() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "위치권한이 있습니다", Toast.LENGTH_SHORT).show();

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
}
