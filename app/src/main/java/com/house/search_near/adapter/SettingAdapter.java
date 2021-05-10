package com.house.search_near.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.house.search_near.R;
import com.house.search_near.model.SettingModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.ViewHolder> {
    private Context context;
    private ArrayList<SettingModel> list = new ArrayList<>();

    ///for databse
    private FirebaseFirestore firestore;

    public SettingAdapter(Context context, ArrayList<SettingModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.setting_post_sample , parent , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SettingModel model = list.get(position);
        Picasso.get().load(model.getImages()).placeholder(R.drawable.house).into(holder.image);
        holder.title.setText(model.getTitle());
        holder.rent_type.setText(model.getRent_type());
        holder.bedroom.setText("Bedroom : "+model.getBedroom());
        holder.bathroom.setText("Bathroom : "+model.getBathroom());
        holder.price.setText("Price : "+model.getRent_price());

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePost(model);
            }
        });

        holder.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Working on It", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deletePost(SettingModel model) {
        firestore = FirebaseFirestore.getInstance();
        firestore.collection("Locations")
                .document(FirebaseAuth.getInstance().getUid())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isComplete()){
                            firestore.collection("User_Posts")
                                    .document(FirebaseAuth.getInstance().getUid())
                                    .collection("Post")
                                    .document(model.getPost_id())
                                    .delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isComplete()){
                                                Toast.makeText(context, "Delete Success", Toast.LENGTH_SHORT).show();
                                            }else{
                                                Toast.makeText(context, "Error : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }else{
                            Toast.makeText(context, "Error : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView title , rent_type , bedroom , bathroom , price;
        private Button updateBtn , deleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.setting_sample_image);
            title = itemView.findViewById(R.id.setting_sample_title);
            rent_type = itemView.findViewById(R.id.setting_sample_renter_type);
            bedroom = itemView.findViewById(R.id.post_sample_bedroom);
            bathroom = itemView.findViewById(R.id.post_sample_bathroom);
            price = itemView.findViewById(R.id.post_sample_price);
            updateBtn = itemView.findViewById(R.id.post_update);
            deleteBtn = itemView.findViewById(R.id.post_delete);
        }
    }
}
