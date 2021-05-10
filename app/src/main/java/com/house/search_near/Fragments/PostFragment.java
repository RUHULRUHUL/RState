package com.house.search_near.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.house.search_near.Database.DBHelper;
import com.house.search_near.R;
import com.house.search_near.adapter.YourLocalityHouseAdapter;
import com.house.search_near.model.PostModel;
import com.house.search_near.model.SettingModel;
import com.house.search_near.sameType.SameData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    ///for database
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference users;
    private StorageReference folder;
    private FirebaseFirestore firestore;
    private FirebaseFirestore user_post;


    private static final int RESULT_OK = -1;
    private View view;

    ////all wight
    private CircleImageView profile_image;
    private TextView postBtn;

    ///for post dialog
    private ProgressBar post_progress;
    private ImageView post_cancel;
    private EditText post_title;
    private TextView post_location;
    private ImageView p_img1 , p_img2 , p_img3;
    private EditText renter_type;
    private EditText post_description;
    private EditText post_rent;
    private Spinner drwaing_spinner;
    private Spinner floor_no_spinner;
    private Spinner bedroom_spinner;
    private Spinner bathroom_spinner;
    private Spinner car_parking_spinner;
    private Spinner belconi_spinner;
    private Spinner generator_spinner;
    private Spinner cctv_spinner;
    private Spinner gas_connection_spinner;
    private Spinner floor_type_spinner;
    private Spinner lift_spinner;
    private Button post_confirm_btn;
    private DBHelper dbHelper;

    private int action = 0;

    private String img_one_url , img_two_url , img_three_url;

    ///all spinner string
    private Dialog dialog;
    private String drawing;
    private String floor_no;
    private String bedroom;
    private String bathroom;
    private String car_parking;
    private String belconi;
    private String generator;
    private String cctv;
    private String gas_connection;
    private String floor_type;
    private String lift;

    private PostModel postModel;


    /// for recyclerview
    private RecyclerView localityRecycler;
    private YourLocalityHouseAdapter adapter;
    private FirebaseFirestore mDb;
    private ArrayList<PostModel> pList;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_post, container, false);

        init();
        localityRecyclerMethod();

        return view;
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        users = database.getReference().child("Users");
        folder = FirebaseStorage.getInstance().getReference().child("Images");
        firestore = FirebaseFirestore.getInstance();

        profile_image = (CircleImageView) view.findViewById(R.id.post_user_image);
        Picasso.get().load(Uri.parse(SameData.image_url)).placeholder(R.drawable.person_icon).into(profile_image);

        postBtn = (TextView) view.findViewById(R.id.create_post_btn);
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPostDialog();
            }
        });

    }

    private void localityRecyclerMethod() {
        localityRecycler = view.findViewById(R.id.recent_house_rent_recyclerView);
        progressBar = view.findViewById(R.id.your_loaclity_progress);
        localityRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        mDb = FirebaseFirestore.getInstance();
        pList = new ArrayList<>();

        mDb.collection("Locations")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<DocumentSnapshot> list = (ArrayList<DocumentSnapshot>) queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d : list){
                            PostModel model = d.toObject(PostModel.class);
                            pList.add(model);

                            progressBar.setVisibility(View.GONE);
                            //Toast.makeText(getContext(), ""+String.valueOf(pList.size()), Toast.LENGTH_SHORT).show();
                            adapter = new YourLocalityHouseAdapter(pList , getContext());
                            localityRecycler.setAdapter(adapter);
                            localityRecycler.setHasFixedSize(true);
                        }
                     }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void openPostDialog() {
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.post_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.show();

        postModel = new PostModel();
        post_progress = dialog.findViewById(R.id.post_progress);
        post_cancel = dialog.findViewById(R.id.post_cancel_btn);
        post_title = dialog.findViewById(R.id.post_title);
        post_location = dialog.findViewById(R.id.post_location);
        p_img1 = dialog.findViewById(R.id.post_image_one);
        p_img2 = dialog.findViewById(R.id.post_image_two);
        p_img3 = dialog.findViewById(R.id.post_image_three);
        renter_type = dialog.findViewById(R.id.post_renter_type);
        post_description = dialog.findViewById(R.id.post_description);
        post_rent = dialog.findViewById(R.id.post_total_rent);
        drwaing_spinner = dialog.findViewById(R.id.post_drawing_spinner);
        floor_no_spinner = dialog.findViewById(R.id.post_floor_no_spinner);
        bedroom_spinner = dialog.findViewById(R.id.post_bedroom_spinner);
        bathroom_spinner = dialog.findViewById(R.id.post_attach_bath_spinner);
        car_parking_spinner = dialog.findViewById(R.id.post_car_parking_spinner);
        belconi_spinner = dialog.findViewById(R.id.post_belconi_spinner);
        generator_spinner = dialog.findViewById(R.id.post_generator_spinner);
        cctv_spinner = dialog.findViewById(R.id.post_cc_tv_spinner);
        gas_connection_spinner = dialog.findViewById(R.id.post_gas_connection_spinner);
        floor_type_spinner = dialog.findViewById(R.id.post_floor_type_spinner);
        lift_spinner = dialog.findViewById(R.id.post_lift_spinner);
        post_confirm_btn = dialog.findViewById(R.id.post_confirm_btn);

        post_cancel.setOnClickListener(v -> dialog.dismiss());
        post_location.setText(SameData.location_text);

        p_img1.setOnClickListener(v -> getFromGallery("one"));
        p_img2.setOnClickListener(v -> getFromGallery("two"));
        p_img3.setOnClickListener(v -> getFromGallery("three"));

        drwaing_spinner.setOnItemSelectedListener(this);
        floor_no_spinner.setOnItemSelectedListener(this);
        bedroom_spinner.setOnItemSelectedListener(this);
        bathroom_spinner.setOnItemSelectedListener(this);
        car_parking_spinner.setOnItemSelectedListener(this);
        belconi_spinner.setOnItemSelectedListener(this);
        generator_spinner.setOnItemSelectedListener(this);
        cctv_spinner.setOnItemSelectedListener(this);
        gas_connection_spinner.setOnItemSelectedListener(this);
        floor_type_spinner.setOnItemSelectedListener(this);
        lift_spinner.setOnItemSelectedListener(this);

        drwaingSpinnerMethod();
        floorNoSpinnerMethod();
        bedroomSpinnerMethod();
        bathroomSpinnerMethod();
        carParkingpinnerMethod();
        belconiSpinnerMethod();
        generatorSpinnerMethod();
        ccTvSpinnerMethod();
        gasConnectionSpinnerMethod();
        floorTypeSpinnerMethod();
        liftSpinnerMethod();

        post_confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processPostMethod();
            }
        });
    }

    private void belconiSpinnerMethod() {
        List<String> category = new ArrayList<String>();
        category.add("1");
        category.add("2");
        category.add("3");
        category.add("4");
        category.add("5");

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, category);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        belconi_spinner.setAdapter(categoryAdapter);
    }

    private void carParkingpinnerMethod() {
        List<String> category = new ArrayList<String>();
        category.add("Yes");
        category.add("No");

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, category);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        car_parking_spinner.setAdapter(categoryAdapter);
    }

    private void bathroomSpinnerMethod() {
        List<String> category = new ArrayList<String>();
        category.add("1");
        category.add("2");
        category.add("3");
        category.add("4");
        category.add("5");

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, category);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bathroom_spinner.setAdapter(categoryAdapter);
    }

    private void bedroomSpinnerMethod() {
        List<String> category = new ArrayList<String>();
        category.add("1");
        category.add("2");
        category.add("3");
        category.add("4");
        category.add("5");

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, category);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bedroom_spinner.setAdapter(categoryAdapter);
    }

    private void processPostMethod() {
        String p_title = post_title.getText().toString();
        String p_location = post_location.getText().toString();
        String p_renter_type = renter_type.getText().toString();
        String P_description = post_description.getText().toString();
        String p_total_rent = post_rent.getText().toString();

        if(TextUtils.isEmpty(p_title)){
            post_title.setError("Title is required!");
            post_title.requestFocus();
        }else if(TextUtils.isEmpty(p_location)){
            post_location.setError("location is required!");
            post_location.requestFocus();
        }else if(TextUtils.isEmpty(p_renter_type)){
            renter_type.requestFocus();
            renter_type.setError("Please enter renter type");
        }else if(TextUtils.isEmpty(P_description)){
            post_description.setError("Please add some short description");
            post_description.requestFocus();
        }else if(TextUtils.isEmpty(p_total_rent)){
            post_rent.requestFocus();
            post_rent.setError("Please enter rant value");
        }else{
            startPosting(p_title , p_location , p_renter_type , P_description , p_total_rent);
            post_progress.setVisibility(View.VISIBLE);
        }
    }

    private void startPosting(String p_title, String p_location, String p_renter_type, String p_description, String p_total_rent) {
        if(TextUtils.isEmpty(img_one_url)){
            img_one_url = SameData.SAVE_IMG_URL;
        }

        if(TextUtils.isEmpty(img_two_url)){
            img_two_url = SameData.SAVE_IMG_URL;
        }

        if(TextUtils.isEmpty(img_three_url)){
            img_three_url = SameData.SAVE_IMG_URL;
        }

        Random r = new Random();
        int randomNumber = r.nextInt(100);

        String key = FirebaseAuth.getInstance().getUid();
        String key2 = FirebaseAuth.getInstance().getUid()+String.valueOf(randomNumber);
        GeoPoint geoPoint = new GeoPoint(SameData.latitude , SameData.longitude);


        postModel.setGeoPoint(geoPoint);
        postModel.setTitle(p_title);
        postModel.setBathroom(bathroom);
        postModel.setBedroom(bedroom);
        postModel.setCar_parking(car_parking);
        postModel.setBelconi(belconi);
        postModel.setCctv(cctv);
        postModel.setDescription(p_description);
        postModel.setDrawing(drawing);
        postModel.setEmail(SameData.email_text);
        postModel.setFloor_no(floor_no);
        postModel.setFloor_type(floor_type);
        postModel.setGas_connection(gas_connection);
        postModel.setGenerator(generator);
        postModel.setImage(SameData.image_url);
        postModel.setImg_one(img_one_url);
        postModel.setImg_two(img_two_url);
        postModel.setImg_three(img_three_url);
        postModel.setKey(key);
        postModel.setLift(lift);
        postModel.setLocation(p_location);
        postModel.setMobile(SameData.mobile_text);
        postModel.setName(SameData.name_text);
        postModel.setRent_per_month(p_total_rent);
        postModel.setRenter_type(p_renter_type);
        postModel.setFavourite(false);

        ///for specific user posts
        SettingModel sm = new SettingModel();
        sm.setTitle(p_title);
        sm.setImages(img_three_url);
        sm.setRent_type(p_renter_type);
        sm.setBedroom(bedroom);
        sm.setBathroom(bathroom);
        sm.setRent_price(p_total_rent);
        sm.setPost_id(key2);

        firestore.collection("Locations")
                .document(key)
                .set(postModel)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isComplete()){
                            firestore.collection("User_Posts")
                                    .document(key)
                                    .collection("Post")
                                    .document(key2)
                                    .set(sm)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isComplete()){
                                                post_progress.setVisibility(View.GONE);
                                                Toast.makeText(getContext(), "Data Uploaded!", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }else{
                                                post_progress.setVisibility(View.GONE);
                                                Toast.makeText(getContext(), "Data Not Uploaded!", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }
                                        }
                                    });
                        }
                        else{
                            post_progress.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "Error : "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        post_progress.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Error : "+e.getMessage(), Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });

    }

    private void liftSpinnerMethod() {
        List<String> category = new ArrayList<String>();
        category.add("Yes");
        category.add("No");

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, category);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lift_spinner.setAdapter(categoryAdapter);
    }

    private void floorTypeSpinnerMethod() {
        List<String> category = new ArrayList<String>();
        category.add("Tiles");
        category.add("Mosaic");

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, category);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        floor_type_spinner.setAdapter(categoryAdapter);
    }

    private void gasConnectionSpinnerMethod() {
        List<String> category = new ArrayList<String>();
        category.add("Line");
        category.add("Cylinder");

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, category);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gas_connection_spinner.setAdapter(categoryAdapter);
    }

    private void ccTvSpinnerMethod() {
        List<String> category = new ArrayList<String>();
        category.add("Yes");
        category.add("No");

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, category);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cctv_spinner.setAdapter(categoryAdapter);
    }

    private void generatorSpinnerMethod() {
        List<String> category = new ArrayList<String>();
        category.add("Yes");
        category.add("No");

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, category);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        generator_spinner.setAdapter(categoryAdapter);
    }

    private void floorNoSpinnerMethod() {
        List<String> category = new ArrayList<String>();
        category.add("1");
        category.add("2");
        category.add("3");
        category.add("4");
        category.add("5");
        category.add("6");
        category.add("7");
        category.add("8");
        category.add("9");
        category.add("10");

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, category);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        floor_no_spinner.setAdapter(categoryAdapter);
    }

    private void getFromGallery(String type) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        if(type.equals("one")){
            startActivityForResult(intent , 1);
        }else if(type.equals("two")){
            startActivityForResult(intent , 2);
        }else if(type.equals("three")){
            startActivityForResult(intent , 3);
        }

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
                                    img_one_url = String.valueOf(uri);
                                }
                            });
                        }
                    });
                    p_img1.setImageURI(imageUrl);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        else if(requestCode == 2){
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
                                    img_two_url = String.valueOf(uri);
                                }
                            });
                        }
                    });
                    p_img2.setImageURI(imageUrl);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        else if(requestCode == 3){
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
                                    img_three_url = String.valueOf(uri);
                                }
                            });
                        }
                    });
                    p_img3.setImageURI(imageUrl);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    private void drwaingSpinnerMethod() {
        List<String> drawing = new ArrayList<String>();
        drawing.add("Yes");
        drawing.add("No");

        ArrayAdapter<String> drawingAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, drawing);
        drawingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drwaing_spinner.setAdapter(drawingAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.post_drawing_spinner:
                drawing = parent.getItemAtPosition(position).toString();
                break;
            case R.id.post_floor_no_spinner:
                floor_no = parent.getItemAtPosition(position).toString();
                break;
            case R.id.post_bedroom_spinner:
                bedroom = parent.getItemAtPosition(position).toString();
                break;
            case R.id.post_attach_bath_spinner:
                bathroom = parent.getItemAtPosition(position).toString();
                break;
            case R.id.post_car_parking_spinner:
                car_parking = parent.getItemAtPosition(position).toString();
                break;
            case R.id.post_belconi_spinner:
                belconi = parent.getItemAtPosition(position).toString();
                break;
            case R.id.post_generator_spinner:
                generator = parent.getItemAtPosition(position).toString();
                break;
            case R.id.post_cc_tv_spinner:
                cctv = parent.getItemAtPosition(position).toString();
                break;
            case R.id.post_gas_connection_spinner:
                gas_connection = parent.getItemAtPosition(position).toString();
                break;
            case R.id.post_floor_type_spinner:
                floor_type = parent.getItemAtPosition(position).toString();
                break;
            case R.id.post_lift_spinner:
                lift = parent.getItemAtPosition(position).toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}