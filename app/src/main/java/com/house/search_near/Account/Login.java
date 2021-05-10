package com.house.search_near.Account;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.house.search_near.R;
import com.house.search_near.mapActivity.MapActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {

    ///for database
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference users;

    private EditText emailEt , passwordEt;
    private Button loginBtn;
    private TextView have_not_account_btn;
    private ProgressBar progressBar;

    ////for location
    private static final int PERMISSION_REQUEST_CODE = 1234;
    private GoogleMap mMap;

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final float DEFAULT_ZOOM = 15f;

    private boolean permissionGranted = false;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationManager locationManager;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        getLocationService();
        init();
    }

    private void getLocationService() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            // Do something for lollipop and above versions
            statusCheck();
        } else{
            // do something for phones running an SDK before lollipop
            getLocationPermission();
        }
    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }else{
            init();
            //Toast.makeText(this, "Allow", Toast.LENGTH_SHORT).show();
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        init();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void init() {
        emailEt = (EditText) findViewById(R.id.login_email_et);
        passwordEt = (EditText) findViewById(R.id.login_password_et);
        loginBtn = (Button) findViewById(R.id.login_btn);
        have_not_account_btn = (TextView) findViewById(R.id.have_not_account);
        progressBar = (ProgressBar) findViewById(R.id.login_progress);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        users = database.getReference().child("Users");

        have_not_account_btn.setOnClickListener(v -> {
           Intent intent = new Intent(Login.this , Register.class);
           intent.putExtra("cityName","");
           startActivity(intent);
        });

        loginBtn.setOnClickListener(v -> loginProcess());
    }

    private void loginProcess() {
        String email = emailEt.getText().toString();
        String pass = passwordEt.getText().toString();

        if(TextUtils.isEmpty(email)){
            emailEt.setError("Please enter your email");
            emailEt.requestFocus();
        }else if(TextUtils.isEmpty(pass)){
            passwordEt.requestFocus();
            passwordEt.setError("Password is needed");
        }else{
            startLogin(email , pass);
        }
    }

    private void startLogin(String email , String pass) {
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email , pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressBar.setVisibility(View.GONE);
                            startActivity(new Intent(Login.this , MapActivity.class));
                            finish();
                        }else{
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(Login.this, "SError "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(Login.this, "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
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