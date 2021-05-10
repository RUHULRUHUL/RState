package com.house.search_near.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.house.search_near.Database.DBHelper;
import com.house.search_near.R;
import com.house.search_near.model.PostModel;
import com.house.search_near.sameType.SameData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class YourLocalityHouseAdapter extends RecyclerView.Adapter<YourLocalityHouseAdapter.ViewHolder>{

    private ArrayList<PostModel> list = new ArrayList<>();
    private Context context;
    private Dialog dialog;

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
    DBHelper helper;

    public YourLocalityHouseAdapter(ArrayList<PostModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rent_house_sample , parent , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SharedPreferences preferences = context.getSharedPreferences(SameData.FAVOURITE_NAME, Context.MODE_PRIVATE);
        helper = new DBHelper(context);


        PostModel model = list.get(position);
        Picasso.get().load(model.getImg_three()).placeholder(R.drawable.house).into(holder.image);
        holder.title.setText(model.getTitle());
        holder.renter_type.setText(model.getRenter_type());
        holder.rent_price.setText(model.getRent_per_month() + ".00 tk");

        String fav = preferences.getString(model.getKey(), "");
        if(model.getKey().equals(fav)) {
            holder.favourite.setImageResource(R.drawable.ic_baseline_favorite_24);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(model);
            }
        });

        SharedPreferences.Editor editor = preferences.edit();

        holder.favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(model.getKey().equals(fav)) {
                    editor.putString(model.getKey() , "");
                    editor.apply();

                    holder.favourite.setImageResource(R.drawable.ic_baseline_not_favorite_24);
                    int result = helper.Delete(String.valueOf(model.getId()));
                    Toast.makeText(context, ""+String.valueOf(model.getId()), Toast.LENGTH_SHORT).show();
//                    if(result == 0){
//                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
//                    }else{
//                        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
//                    }
                }else{

                    editor.putString(model.getKey() , model.getKey());
                    editor.apply();

                    holder.favourite.setImageResource(R.drawable.ic_baseline_favorite_24);
                    boolean result = helper.insertData(
                            model.getId(),
                            model.getTitle(),
                            model.getImg_one(),
                            model.getImg_two(),
                            model.getImg_three(),
                            model.getBedroom(),
                            model.getBathroom(),
                            model.getBelconi(),
                            model.getCar_parking(),
                            model.getRent_per_month(),
                            model.getName(),
                            model.getEmail(),
                            model.getMobile(),
                            model.getLocation(),
                            model.getImage(),
                            model.getRenter_type(),
                            model.getDrawing(),
                            model.getFloor_no(),
                            model.getGenerator(),
                            model.getCctv(),
                            model.getGas_connection(),
                            model.getFloor_type(),
                            model.getLift(),
                            model.getDescription());

                    if(result == true){
                        Toast.makeText(context, "Data add your favourite", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(context, "Sorry , we can not add this product in your favourite list", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void openDialog(PostModel model) {
        dialog = new Dialog(context);
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
        Picasso.get().load(model.getImg_one()).placeholder(R.drawable.ic_baseline_add_photo_alternate_24).into(location_image1);
        Picasso.get().load(model.getImg_two()).placeholder(R.drawable.ic_baseline_add_photo_alternate_24).into(location_image2);
        Picasso.get().load(model.getImg_three()).placeholder(R.drawable.ic_baseline_add_photo_alternate_24).into(location_image3);
        Picasso.get().load(model.getImage()).placeholder(R.drawable.person_icon).into(location_user_image);

        location_title.setText(model.getTitle());
        location_bedroom.setText(model.getBedroom());
        location_bathroom.setText(model.getBathroom());
        location_belconi.setText(model.getBelconi());
        location_car_parking.setText(model.getCar_parking());
        location_rent_per_month.setText(model.getRent_per_month()+".00 tk");
        location_user_name.setText(model.getName());
        location_user_email.setText(model.getEmail());
        location_user_mobile.setText(model.getMobile());
        location_user_location.setText(model.getLocation());
        location_preferred_renter.setText(model.getRenter_type());
        location_drawing_room.setText(model.getDrawing());
        location_floor_no.setText(model.getFloor_no());
        location_generator.setText(model.getGenerator());
        location_cc_tv.setText(model.getCctv());
        location_gas_connection.setText(model.getGas_connection());
        location_floor_type.setText(model.getFloor_type());
        location_lift.setText(model.getLift());
        location_description.setText(model.getDescription());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image , favourite;
        TextView title , renter_type , rent_price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.adapter_sample_image);
            favourite = itemView.findViewById(R.id.adapter_sample_favourite);
            title = itemView.findViewById(R.id.adapter_sample_title);
            renter_type = itemView.findViewById(R.id.adapter_sample_renter_type);
            rent_price = itemView.findViewById(R.id.adapter_sample_rent_price);
        }
    }
}
