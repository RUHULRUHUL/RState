package com.house.search_near.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.house.search_near.Database.DBHelper;
import com.house.search_near.R;
import com.house.search_near.adapter.SettingAdapter;
import com.house.search_near.model.SettingModel;
import com.house.search_near.sameType.SameData;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingFragment extends Fragment {

    View view;
    ///all wight here
    private ImageView back_btn , more_btn;
    private CircleImageView user_image;
    private TextView user_name , user_mobile , user_email , no_post;
    private RecyclerView setting_post_reycler;
    private ArrayList<SettingModel> list;
    private SettingAdapter adapter;
    private DBHelper helper;

    ///for getData from firebase
    private FirebaseFirestore firestore;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_setting, container, false);

        init();
        allPostMethod();
        return view;
    }

    public void init(){
        back_btn = view.findViewById(R.id.setting_back);
        more_btn = view.findViewById(R.id.setting_more);
        user_image = view.findViewById(R.id.setting_user_image);
        user_name = view.findViewById(R.id.setting_user_name);
        user_mobile = view.findViewById(R.id.setting_user_mobile);
        user_email = view.findViewById(R.id.setting_user_email);

        if(SameData.image_url.isEmpty()){
            Picasso.get().load(SameData.SAVE_IMG_URL).placeholder(R.drawable.person_icon).into(user_image);
        }else{
            Picasso.get().load(SameData.image_url).placeholder(R.drawable.person_icon).into(user_image);
        }

        user_name.setText(SameData.name_text);
        user_email.setText(SameData.email_text);
        user_mobile.setText(SameData.mobile_text);
    }
    private void allPostMethod() {
        firestore = FirebaseFirestore.getInstance();
        no_post = view.findViewById(R.id.no_post_setting);

        list = new ArrayList<>();
        setting_post_reycler = view.findViewById(R.id.user_all_post);
        setting_post_reycler.setLayoutManager(new LinearLayoutManager(getContext()));
        setting_post_reycler.setHasFixedSize(true);

        firestore.collection("User_Posts")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("Post")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<DocumentSnapshot> plist = (ArrayList<DocumentSnapshot>) queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d : plist){
                            SettingModel model = d.toObject(SettingModel.class);
                            list.add(model);

                            if(list.size() == 0){
                                no_post.setVisibility(View.VISIBLE);
                            }
                            //Toast.makeText(getContext(), ""+String.valueOf(pList.size()), Toast.LENGTH_SHORT).show();
                            adapter = new SettingAdapter(getContext() , list);
                            setting_post_reycler.setAdapter(adapter);
                            setting_post_reycler.setHasFixedSize(true);
                        }
                    }
                });
    }


}