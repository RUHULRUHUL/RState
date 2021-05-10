package com.house.search_near.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.house.search_near.Database.DBHelper;
import com.house.search_near.R;
import com.house.search_near.model.ClusterMarker;
import com.house.search_near.model.PostModel;
import com.house.search_near.sameType.SameData;
import com.house.search_near.utlis.MyClusterRenderer;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.collections.MarkerManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment implements OnMapReadyCallback ,
        ClusterManager.OnClusterClickListener<ClusterMarker>,
        ClusterManager.OnClusterItemClickListener<ClusterMarker>,
        View.OnClickListener {

    //for sharedPrefrence
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    ///firebase database
    private FirebaseFirestore mDb;
    private ClusterManager<ClusterMarker> clusterManager;
    public static ArrayList<PostModel> doc;

    ///for details dialog
    private Dialog dialog;

    ///for cluster manager
    private MyClusterRenderer mClusterRenderer;
    private ArrayList<ClusterMarker> mClusterMarker = new ArrayList<>();

    ////for location
    private static final int PERMISSION_REQUEST_CODE = 1234;
    private GoogleMap mMap;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final float DEFAULT_ZOOM = 15f;
    private boolean permissionGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private View view;
    private String address;

    ////for location details
    private TextView location_title;
    private ImageView location_user_image , location_image1 , location_image2 , location_image3;
    private TextView location_bedroom;
    private TextView location_bathroom;
    private TextView location_belconi;
    private TextView location_car_parking;
    private TextView location_rent_per_month;
    private TextView location_user_name;
    private TextView location_user_email;
    private TextView location_user_mobile;
    private TextView location_user_location;
    private TextView location_preferred_renter;
    private TextView location_drawing_room;
    private TextView location_floor_no;
    private TextView location_generator;
    private TextView location_cc_tv;
    private TextView location_gas_connection;
    private TextView location_floor_type;
    private TextView location_lift;
    private TextView location_description;
    private Button location_add_to_favourite;
    private FirebaseFirestore firestore;

    ///for get sharedPreference
    String name , email , image , job , location , mobile , pass , uid;


    ///for search
    private EditText searchInput;
    private ImageView searchBtn;

    ///for navigation view
    private ImageView nav_btn;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private TextView side_name_tv;
    private TextView side_email_tv;
    private TextView side_phone_tv;
    private CircleImageView side_image_view;
    private LinearLayout side_dashboard_btn;
    private LinearLayout side_favourite_btn;
    private LinearLayout side_setting_btn;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_home, container, false);

        mDb = FirebaseFirestore.getInstance();
        doc = new ArrayList<>();
        preferences = getActivity().getSharedPreferences(SameData.PREFERENCE_NAME , Context.MODE_PRIVATE);
        editor = preferences.edit();

        getLocationPermission();
        getAndSetDataIntoSharedPreference();
        forSearchOperation();

        return view;
    }

    private void navigationMethod() {
        nav_btn = (ImageView) view.findViewById(R.id.nav_btn);
        drawerLayout = (DrawerLayout) view.findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) view.findViewById(R.id.navigationView);

        side_image_view = view.findViewById(R.id.side_menu_person_image);
        side_name_tv = view.findViewById(R.id.side_menu_person_name);
        side_email_tv = view.findViewById(R.id.side_menu_person_email);
        side_phone_tv = view.findViewById(R.id.side_menu_person_mobile);
        side_dashboard_btn = view.findViewById(R.id.side_menu_dashboard_btn);
        side_favourite_btn = view.findViewById(R.id.side_menu_favourite_btn);
        side_setting_btn = view.findViewById(R.id.side_menu_setting_btn);

        Picasso.get().load(SameData.image_url).placeholder(R.drawable.person_icon).into(side_image_view);
        side_name_tv.setText(SameData.name_text);
        side_email_tv.setText(SameData.email_text);
        side_phone_tv.setText(SameData.mobile_text);

        nav_btn.setOnClickListener(this);
        side_dashboard_btn.setOnClickListener(this);
        side_favourite_btn.setOnClickListener(this);
        side_setting_btn.setOnClickListener(this);
    }

    private void forSearchOperation() {
        searchInput = view.findViewById(R.id.location_search);
        searchBtn = view.findViewById(R.id.search_btn);
        searchBtn.setOnClickListener(v -> searchGeoLocateHere());
    }

    private void searchGeoLocateHere() {
        String searchTex = searchInput.getText().toString();
        Geocoder geocoder = new Geocoder(getContext());
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchTex ,1);
        }catch (Exception e){
            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        Address address = list.get(0);
        LatLng latLng = new LatLng(address.getLatitude() , address.getLongitude());
        mMap.addMarker(new MarkerOptions().position(latLng).title("Location"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
    }

    private void getAndSetDataIntoSharedPreference() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Users").child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        name = (String) snapshot.child("name").getValue();
                        email = (String) snapshot.child("email").getValue();
                        image = (String) snapshot.child("image").getValue();
                        job = (String) snapshot.child("job").getValue();
                        location = (String) snapshot.child("location").getValue();
                        mobile = (String) snapshot.child("mobile").getValue();
                        pass = (String) snapshot.child("pass").getValue();

                        SharedPreferences preferences = getContext().getSharedPreferences(SameData.PREFERENCE_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();

                        editor.putString(SameData.NAME , name);
                        editor.putString(SameData.EMAIL , email);
                        editor.putString(SameData.IMAGE_URL , image);
                        editor.putString(SameData.JOB , job);
                        editor.putString(SameData.LOCATION , location);
                        editor.putString(SameData.MOBILE , mobile);
                        editor.putString(SameData.PASSWORD , pass);

                        SameData.name_text = name;
                        SameData.email_text = email;
                        SameData.image_url = image;
                        SameData.job_text = job;
                        SameData.location_text = location;
                        SameData.mobile_text = mobile;
                        SameData.password_text = pass;

                        editor.apply();

                        navigationMethod();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void setUpClusterer() {
//        // Position the map.
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(26.01799, 88.485556), 15));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        clusterManager = new ClusterManager<ClusterMarker>(getContext(), mMap , new MarkerManager(mMap){
            @Override
            public boolean onMarkerClick(Marker marker) {
                //here will get the clicked marker
                return super.onMarkerClick(marker);
            }
        });
        mClusterRenderer = new MyClusterRenderer(
                    getContext(),
                    mMap,
                    clusterManager
                );
        clusterManager.setRenderer(mClusterRenderer);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraIdleListener(clusterManager);
        clusterManager.setOnClusterItemClickListener(
                new ClusterManager.OnClusterItemClickListener<ClusterMarker>() {
                    @Override public boolean onClusterItemClick(ClusterMarker clusterItem) {
                        openDialog(clusterItem);
                        return false;
                    }
                });
        // Add cluster items (markers) to the cluster manager.
        addItems();
    }

    private void addItems() {

        //Toast.makeText(getContext(), ""+FirebaseAuth.getInstance().getUid(), Toast.LENGTH_SHORT).show();

        mDb.collection("Locations")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<DocumentSnapshot> list = (ArrayList<DocumentSnapshot>) queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d : list){
                            PostModel model = d.toObject(PostModel.class);
                            doc.add(model);

                            finallySetCluster();
                        }
                    }
                });
    }

    private void finallySetCluster() {
        // Set some lat/lng coordinates to start with.
        double lat = 26.025123f;
        double lng = 88.481096f;

        for (int i = 0; i < doc.size(); i++) {
            double offset_lat = doc.get(i).getGeoPoint().getLatitude();
            double offset_long = doc.get(i).getGeoPoint().getLongitude();
            lat =offset_lat;
            lng =offset_long;
            ClusterMarker offsetItem = new ClusterMarker(lat, lng, "" + doc.get(i).getLocation(), " " , R.drawable.ic_baseline_house_balance_24 ,
                    new PostModel(new GeoPoint(lat , lng),doc.get(i).getTitle() , doc.get(i).getImg_one() , doc.get(i).getImg_two() , doc.get(i).getImg_three(),
                            doc.get(i).getRenter_type() , doc.get(i).getDescription() , doc.get(i).getRent_per_month()  , doc.get(i).getDrawing(),
                            doc.get(i).getFloor_no() , doc.get(i).getBedroom() , doc.get(i).getBathroom() , doc.get(i).getGenerator(), doc.get(i).getCctv(),
                            doc.get(i).getGas_connection(), doc.get(i).getFloor_type(), doc.get(i).getLift() , doc.get(i).getGeo_point() , doc.get(i).getKey(),
                            doc.get(i).getEmail(), doc.get(i).getImage() , doc.get(i).getLocation(),doc.get(i).getMobile(),doc.get(i).getName(),
                            doc.get(i).getCar_parking(),doc.get(i).getBelconi() , doc.get(i).getFavourite()));
            clusterManager.addItem(offsetItem);
            mClusterMarker.add(offsetItem);
        }
    }

    private void init() {
        //setUserPosition();
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(permissionGranted){
            try {
                boolean success = googleMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                getContext(), R.raw.map_style));

                if (!success) {
                    Log.e("TAG", "Style parsing failed.");
                }
            } catch (Resources.NotFoundException e) {
                Log.e("TAG", "Can't find style. Error: ", e);
            }
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            setUpClusterer();
            SameData.uid_text = FirebaseAuth.getInstance().getUid();
            mMap.setMyLocationEnabled(true);
        }
    }

    private void getDeviceLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        try {
            if (permissionGranted) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Location location1 = (Location) task.getResult();

                        LatLng myCordinate = new LatLng(location1.getLatitude() , location1.getLongitude());
                        moveCameraTo(new LatLng(location1.getLatitude() , location1.getLongitude()),DEFAULT_ZOOM);
                        SameData.latitude = location1.getLatitude();
                        SameData.longitude = location1.getLongitude();
                    }
                });
            }
        }catch (Exception e){
            Toast.makeText(getContext(), "Exception = "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void moveCameraTo(LatLng latLng , float zoom){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng , zoom));
    }

    public void  getLocationPermission(){
        String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION , Manifest.permission.ACCESS_COARSE_LOCATION};
        if(ContextCompat.checkSelfPermission(getContext() , FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(getContext() , COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                permissionGranted = true;
                init();
            }else{
                ActivityCompat.requestPermissions(getActivity() , permission , PERMISSION_REQUEST_CODE);
            }
        }
        else{
            ActivityCompat.requestPermissions(getActivity() , permission , PERMISSION_REQUEST_CODE);
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


    @Override
    public boolean onClusterClick(Cluster<ClusterMarker> cluster) {
        animateZoomIn(cluster.getPosition());
        return true;
    }

    @Override
    public boolean onClusterItemClick(ClusterMarker item) {
        animateZoomIn(item.getPosition());
        return true;
    }

    private void openDialog(ClusterMarker clusterItem) {
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.location_details);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.show();

        ImageView closeBtn = dialog.findViewById(R.id.location_details_cancel_btn);
        closeBtn.setOnClickListener(v -> dialog.dismiss());

        ////find of all dialog wight
        location_title = dialog.findViewById(R.id.location_details_title);
        location_image1 = dialog.findViewById(R.id.location_details_image_one);
        location_image2 = dialog.findViewById(R.id.location_details_image_two);
        location_image3 = dialog.findViewById(R.id.location_details_image_three);
        location_bedroom = dialog.findViewById(R.id.location_details_bed_no);
        location_bathroom = dialog.findViewById(R.id.location_details_bathroom_no);
        location_belconi = dialog.findViewById(R.id.location_details_belconi_no);
        location_car_parking = dialog.findViewById(R.id.location_details_car_parking);
        location_rent_per_month = dialog.findViewById(R.id.location_details_rent_per_month);
        location_user_name = dialog.findViewById(R.id.location_details_person_name);
        location_user_email = dialog.findViewById(R.id.location_details_person_email);
        location_user_mobile = dialog.findViewById(R.id.location_details_person_mobile);
        location_user_location = dialog.findViewById(R.id.location_details_person_location);
        location_user_image = dialog.findViewById(R.id.location_details_user_image);
        location_preferred_renter = dialog.findViewById(R.id.location_details_preferred_renter);
        location_drawing_room = dialog.findViewById(R.id.location_details_drawing_room);
        location_floor_no = dialog.findViewById(R.id.location_details_floor_no);
        location_generator = dialog.findViewById(R.id.location_details_generator);
        location_cc_tv = dialog.findViewById(R.id.location_details_cc_tv_camera);
        location_gas_connection = dialog.findViewById(R.id.location_details_gas_connection);
        location_floor_type = dialog.findViewById(R.id.location_details_floor_type);
        location_lift = dialog.findViewById(R.id.location_details_lift);
        location_description = dialog.findViewById(R.id.location_details_description);
        location_add_to_favourite = dialog.findViewById(R.id.location_details_add_to_favourite);

        //Toast.makeText(getContext(), ""+String.valueOf(clusterItem.getPostModel().getKey()), Toast.LENGTH_SHORT).show();
        //location_title.setText(clusterItem.getPostModel().getT());
        Picasso.get().load(clusterItem.getPostModel().getImg_one()).placeholder(R.drawable.ic_baseline_add_photo_alternate_24).into(location_image1);
        Picasso.get().load(clusterItem.getPostModel().getImg_two()).placeholder(R.drawable.ic_baseline_add_photo_alternate_24).into(location_image2);
        Picasso.get().load(clusterItem.getPostModel().getImg_three()).placeholder(R.drawable.ic_baseline_add_photo_alternate_24).into(location_image3);
        Picasso.get().load(clusterItem.getPostModel().getImage()).placeholder(R.drawable.person_icon).into(location_user_image);

        location_title.setText(clusterItem.getPostModel().getTitle());
        location_bedroom.setText(clusterItem.getPostModel().getBedroom());
        location_bathroom.setText(clusterItem.getPostModel().getBathroom());
        location_belconi.setText(clusterItem.getPostModel().getBelconi());
        location_car_parking.setText(clusterItem.getPostModel().getCar_parking());
        location_rent_per_month.setText(clusterItem.getPostModel().getRent_per_month()+".00 tk");
        location_user_name.setText(clusterItem.getPostModel().getName());
        location_user_email.setText(clusterItem.getPostModel().getEmail());
        location_user_mobile.setText(clusterItem.getPostModel().getMobile());
        location_user_location.setText(clusterItem.getPostModel().getLocation());
        location_preferred_renter.setText(clusterItem.getPostModel().getRenter_type());
        location_drawing_room.setText(clusterItem.getPostModel().getDrawing());
        location_floor_no.setText(clusterItem.getPostModel().getFloor_no());
        location_generator.setText(clusterItem.getPostModel().getGenerator());
        location_cc_tv.setText(clusterItem.getPostModel().getCctv());
        location_gas_connection.setText(clusterItem.getPostModel().getGas_connection());
        location_floor_type.setText(clusterItem.getPostModel().getFloor_type());
        location_lift.setText(clusterItem.getPostModel().getLift());
        location_description.setText(clusterItem.getPostModel().getDescription());

        SharedPreferences preferences = getContext().getSharedPreferences(SameData.FAVOURITE_NAME, Context.MODE_PRIVATE);
        String fav = preferences.getString(clusterItem.getPostModel().getKey(), "");
        if(clusterItem.getPostModel().getKey().equals(fav)) {
            location_add_to_favourite.setVisibility(View.GONE);
        }

        location_add_to_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences(SameData.FAVOURITE_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString(clusterItem.getPostModel().getKey() , clusterItem.getPostModel().getKey());
                editor.apply();

                DBHelper helper = new DBHelper(getContext());
                boolean result = helper.insertData(
                        clusterItem.getPostModel().getId(),
                        clusterItem.getPostModel().getTitle(),
                        clusterItem.getPostModel().getImg_one(),
                        clusterItem.getPostModel().getImg_two(),
                        clusterItem.getPostModel().getImg_three(),
                        clusterItem.getPostModel().getBedroom(),
                        clusterItem.getPostModel().getBathroom(),
                        clusterItem.getPostModel().getBelconi(),
                        clusterItem.getPostModel().getCar_parking(),
                        clusterItem.getPostModel().getRent_per_month(),
                        clusterItem.getPostModel().getName(),
                        clusterItem.getPostModel().getEmail(),
                        clusterItem.getPostModel().getMobile(),
                        clusterItem.getPostModel().getLocation(),
                        clusterItem.getPostModel().getImage(),
                        clusterItem.getPostModel().getRenter_type(),
                        clusterItem.getPostModel().getDrawing(),
                        clusterItem.getPostModel().getFloor_no(),
                        clusterItem.getPostModel().getGenerator(),
                        clusterItem.getPostModel().getCctv(),
                        clusterItem.getPostModel().getGas_connection(),
                        clusterItem.getPostModel().getFloor_type(),
                        clusterItem.getPostModel().getLift(),
                        clusterItem.getPostModel().getDescription());

                if(result == true){
                    Toast.makeText(getContext(), "Data add your favourite", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(), "Sorry , we can not add this product in your favourite list", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void animateZoomIn(LatLng position) {
        Toast.makeText(getContext(), "Working", Toast.LENGTH_SHORT).show();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position , 15f));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.nav_btn:
                drawerLayout.openDrawer(navigationView);
                break;
            case R.id.side_menu_dashboard_btn:
            case R.id.side_menu_setting_btn:
                drawerLayout.closeDrawer(navigationView);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmenmt_container , new SettingFragment()).commit();
                break;
            case R.id.side_menu_favourite_btn:
                drawerLayout.closeDrawer(navigationView);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmenmt_container , new FavouriteFragment()).commit();
                break;

        }
    }
}