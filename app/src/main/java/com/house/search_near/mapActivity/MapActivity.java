package com.house.search_near.mapActivity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ImageViewCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.house.search_near.Fragments.FavouriteFragment;
import com.house.search_near.Fragments.HomeFragment;
import com.house.search_near.Fragments.PostFragment;
import com.house.search_near.Fragments.SettingFragment;
import com.house.search_near.R;

public class MapActivity extends AppCompatActivity implements View.OnClickListener{

    ///all wight
    private LinearLayout homeBtn , postBtn , favouriteBtn , settingBtn;
    private ImageView homeImage , postImage , favouriteImage , settingImage;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        init();
    }

    private void init() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmenmt_container , new HomeFragment()).commit();
        homeBtn = (LinearLayout) findViewById(R.id.home_btn);
        postBtn = (LinearLayout) findViewById(R.id.post_btn);
        favouriteBtn = (LinearLayout) findViewById(R.id.favourite_btn);
        settingBtn = (LinearLayout) findViewById(R.id.setting_btn);
        homeImage = (ImageView) findViewById(R.id.home_image);
        postImage = (ImageView) findViewById(R.id.post_image);
        favouriteImage = (ImageView) findViewById(R.id.favourtite_image);
        settingImage = (ImageView) findViewById(R.id.setting_image);

        homeBtn.setOnClickListener(this);
        postBtn.setOnClickListener(this);
        favouriteBtn.setOnClickListener(this);
        settingBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_btn:
                openHomeMethod();
                break;
            case R.id.post_btn:
                openPostMethod();
                break;
            case R.id.favourite_btn:
                openFavouriteMethod();
                break;
            case R.id.setting_btn:
                openSettingMethod();
                break;
        }
    }

    @SuppressLint("ResourceAsColor")
    private void openSettingMethod() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmenmt_container , new SettingFragment()).commit();
        //settingImage.setColorFilter(Color.argb(255, 255, 255, 255));
        ImageViewCompat.setImageTintList(settingImage, getResources().getColorStateList(R.color.teal_200));
        ImageViewCompat.setImageTintList(homeImage, getResources().getColorStateList(R.color.black));
        ImageViewCompat.setImageTintList(postImage, getResources().getColorStateList(R.color.black));
        ImageViewCompat.setImageTintList(favouriteImage, getResources().getColorStateList(R.color.black));
    }

    private void openFavouriteMethod() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmenmt_container , new FavouriteFragment()).commit();
        ImageViewCompat.setImageTintList(favouriteImage, getResources().getColorStateList(R.color.teal_200));
        ImageViewCompat.setImageTintList(homeImage, getResources().getColorStateList(R.color.black));
        ImageViewCompat.setImageTintList(postImage, getResources().getColorStateList(R.color.black));
        ImageViewCompat.setImageTintList(settingImage, getResources().getColorStateList(R.color.black));
    }

    private void openPostMethod() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmenmt_container , new PostFragment()).commit();
        ImageViewCompat.setImageTintList(postImage, getResources().getColorStateList(R.color.teal_200));
        ImageViewCompat.setImageTintList(homeImage, getResources().getColorStateList(R.color.black));
        ImageViewCompat.setImageTintList(settingImage, getResources().getColorStateList(R.color.black));
        ImageViewCompat.setImageTintList(favouriteImage, getResources().getColorStateList(R.color.black));

    }

    private void openHomeMethod() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmenmt_container , new HomeFragment()).commit();
        ImageViewCompat.setImageTintList(homeImage, getResources().getColorStateList(R.color.teal_200));
        ImageViewCompat.setImageTintList(settingImage, getResources().getColorStateList(R.color.black));
        ImageViewCompat.setImageTintList(postImage, getResources().getColorStateList(R.color.black));
        ImageViewCompat.setImageTintList(favouriteImage, getResources().getColorStateList(R.color.black));

    }

    @Override
    public void onBackPressed() {
        showDialog();
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit?");
        builder.setMessage("Do you want to exit?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }
        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}