package com.house.search_near.Account;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import com.house.search_near.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class Register extends AppCompatActivity {

    ///for database
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference users;
    private StorageReference folder;


    //for sharedPreference
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;


    private EditText emailEt , passEt , jobEt , mobileEt , nameEt;
    private Button submitBtn;
    private CircleImageView profile_image;
    public TextView locationEt;
    private TextView have_account ;
    private ProgressBar progressBar;
    private String cityName , imgUrl , finalImgUrl;

    private int action = 0;

    private static String TAG = "OnResult";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        init();
    }

    private void getIntentMethod() {
        String cName = getIntent().getStringExtra("cityName");
        if(cName.equals("")){
            cityName = "Location";
        }else{
            cityName = cName;
        }
        locationEt.setText(cityName);
    }

    private void init() {
        emailEt = (EditText) findViewById(R.id.register_email_et);
        passEt = (EditText) findViewById(R.id.register_password_et);
        locationEt = (TextView) findViewById(R.id.register_location_et);
        jobEt = (EditText) findViewById(R.id.register_job_et);
        mobileEt = (EditText) findViewById(R.id.register_mobile_et);
        nameEt = (EditText) findViewById(R.id.register_name_et);
        submitBtn = (Button) findViewById(R.id.register_btn);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        have_account = (TextView) findViewById(R.id.have_account);
        profile_image = (CircleImageView) findViewById(R.id.register_profile_image);

        profile_image.setOnClickListener(v -> getImageFromGallry());

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        users = database.getReference().child("Users");
        folder = FirebaseStorage.getInstance().getReference().child("Images");

        have_account.setOnClickListener(v -> {
            startActivity(new Intent(Register.this , Login.class));
            finish();
        });

        locationEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this , RegisterMap.class);
                startActivity(intent);
                finish();
            }
        });
        submitBtn.setOnClickListener(v -> registerProcess());

        getIntentMethod();
    }

    private void getImageFromGallry() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent , 1);
    }

    private void registerProcess() {
        String name = nameEt.getText().toString();
        String email = emailEt.getText().toString();
        String pass = passEt.getText().toString();
        String location = locationEt.getText().toString();
        String mobile = mobileEt.getText().toString();
        String job = jobEt.getText().toString();

        if(TextUtils.isEmpty(name)){
            nameEt.requestFocus();
            nameEt.setError("Please input your name");
        }
        else if(TextUtils.isEmpty(email)){
            emailEt.requestFocus();
            emailEt.setError("Please input email");
        }
        else if(TextUtils.isEmpty(pass)){
            passEt.setError("Password is needed!");
            passEt.requestFocus();
        }
        else if(TextUtils.isEmpty(location)){
            locationEt.requestFocus();
            locationEt.setError("Location is needed!");
        }
        else if(TextUtils.isEmpty(mobile)){
            mobileEt.setError("Mobile number is needed!");
            mobileEt.requestFocus();
        }else{
            startRegister(email , pass , location , mobile , name , job);
        }
    }

    private void startRegister(String email, String pass, String location, String mobile,String name , String job) {
        progressBar.setVisibility(View.VISIBLE);

        if(imgUrl.equals(null)){
           imgUrl = "https://firebasestorage.googleapis.com/v0/b/amarmess-a03dc.appspot.com/o/Images%2Fimages.png?alt=media&token=dff08ac4-5786-4518-a562-34232af88df2";
        }


        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                            User user = new User();
                            user.setEmail(email);
                            user.setName(name);
                            user.setPass(pass);
                            user.setLocation(location);
                            user.setJob(job);
                            user.setImage(imgUrl);
                            user.setMobile(mobile);

                            users.child(FirebaseAuth.getInstance().getUid())
                                    .setValue(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                progressBar.setVisibility(View.GONE);
                                                Intent intent = new Intent(Register.this , Login.class);
                                                startActivity(intent);
                                                finish();
                                            }else{
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(Register.this, "Error = "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                            // Toast.makeText(Register.this, "Registration Complete", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(Register.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(Register.this, "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                try {
                    final Uri imageUrl = data.getData();

                    StorageReference fileName = folder.child("image"+imageUrl.getLastPathSegment());

                    fileName.putFile(imageUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    imgUrl = String.valueOf(uri);
                                }
                            });
                        }
                    });
                    profile_image.setImageURI(imageUrl);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}