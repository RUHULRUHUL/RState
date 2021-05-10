package com.house.search_near;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import com.house.search_near.Account.Login;
import com.house.search_near.mapActivity.MapActivity;
import com.house.search_near.sameType.SameData;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences(SameData.PREFERENCE_NAME , MODE_PRIVATE);
        String email = preferences.getString(SameData.EMAIL , "b");
        if(email.equals("b")){
            new Handler().postDelayed(() -> {
                startActivity(new Intent(MainActivity.this , Login.class));
                finish();
            },2000);
        }else{
            new Handler().postDelayed(() -> {
                startActivity(new Intent(MainActivity.this , MapActivity.class));
                finish();
            },1000);
        }
    }
}