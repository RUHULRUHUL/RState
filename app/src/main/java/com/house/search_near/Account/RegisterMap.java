package com.house.search_near.Account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.house.search_near.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class RegisterMap extends AppCompatActivity implements OnMapReadyCallback {

    ////for location
    private static final int PERMISSION_REQUEST_CODE = 1234;
    private GoogleMap mMap;

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final float DEFAULT_ZOOM = 15f;

    private boolean permissionGranted = false;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationManager locationManager;


    ////all wight
    private ImageView closeBtn;
    private TextView saveBtn;
    private String address = "";

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(permissionGranted){
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
        }
    }

    private void getDeviceLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (permissionGranted) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Location location = (Location) task.getResult();

                            try {
                                LatLng myCordinate = new LatLng(location.getLatitude() , location.getLongitude());
                                moveCameraTo(new LatLng(location.getLatitude() , location.getLongitude()),DEFAULT_ZOOM);
                                getCityName(myCordinate);

                            }catch (Exception e)
                            {

                            }


                        }
                    }
                });
            }
        }catch (Exception e){
            Toast.makeText(this, "Exception = "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void getCityName(LatLng myCordinate) {
        Geocoder geocoder = new Geocoder(RegisterMap.this , Locale.getDefault());
        try {
            List<Address> list = geocoder.getFromLocation(myCordinate.latitude , myCordinate.longitude , 1);
            address = list.get(0).getAddressLine(0);
            Toast.makeText(this, ""+address, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void moveCameraTo(LatLng latLng , float zoom){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng , zoom));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_map);

        getLocationPermission();

        closeBtn = findViewById(R.id.map_close_btn);
        saveBtn = findViewById(R.id.save_location_btn);

        closeBtn.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterMap.this , Register.class);
            intent.putExtra("cityName","");
            startActivity(intent);
            finish();
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(address.isEmpty()){
                    address = "";
                }
                Intent intent = new Intent(RegisterMap.this , Register.class);
                intent.putExtra("cityName",address);
                startActivity(intent);
                finish();
            }
        });
    }

    private void init() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    public void  getLocationPermission(){
        String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION , Manifest.permission.ACCESS_COARSE_LOCATION};
        if(ContextCompat.checkSelfPermission(this.getApplicationContext() , FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext() , COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                permissionGranted = true;
                init();
            }else{
                ActivityCompat.requestPermissions(this , permission , PERMISSION_REQUEST_CODE);
            }
        }
        else{
            ActivityCompat.requestPermissions(this , permission , PERMISSION_REQUEST_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_REQUEST_CODE:
                if(grantResults.length > 0){
                    for(int i = 0 ; i < grantResults.length ; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            permissionGranted = false;
                        }
                    }
                    permissionGranted = true;
                    init();
                }
        }
    }

}