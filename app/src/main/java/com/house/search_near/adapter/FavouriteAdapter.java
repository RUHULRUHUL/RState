package com.house.search_near.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.house.search_near.R;
import com.house.search_near.model.PostModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ViewHolder> {
    Context context;
    private ArrayList<PostModel> list = new ArrayList<>();

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

    public FavouriteAdapter(Context context, ArrayList<PostModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_sample , parent , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        PostModel postModel = list.get(position);
        Picasso.get().load(postModel.getImg_three()).placeholder(R.drawable.house).into(holder.imageView);
        holder.title.setText(postModel.getTitle());
        holder.rent_type.setText(postModel.getRenter_type());
        holder.bedroom.setText("Bedroom : "+postModel.getBedroom());
        holder.bathroom.setText("Bathroom : "+postModel.getBathroom());
        holder.rent_price.setText(postModel.getRent_per_month()+".00 tk");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(postModel);
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
        ImageView imageView;
        TextView title , rent_type ,bedroom , bathroom , rent_price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.favorite_sample_image);
            title = itemView.findViewById(R.id.favorite_sample_title);
            rent_type = itemView.findViewById(R.id.favorite_sample_renter_type);
            bedroom = itemView.findViewById(R.id.favorite_sample_bedroom);
            bathroom = itemView.findViewById(R.id.favorite_sample_bathroom);
            rent_price = itemView.findViewById(R.id.favorite_sample_rent_price);
        }
    }
}
