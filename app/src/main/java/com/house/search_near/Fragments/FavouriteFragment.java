package com.house.search_near.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.house.search_near.Database.DBHelper;
import com.house.search_near.R;
import com.house.search_near.adapter.FavouriteAdapter;
import com.house.search_near.model.PostModel;

import java.util.ArrayList;


public class FavouriteFragment extends Fragment {

    View view;
    private RecyclerView fav_recycler;
    private FavouriteAdapter adapter;
    private ArrayList<PostModel> list;
    private DBHelper dbHelper;
    private LinearLayout not_empty , empty;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_favourite, container, false);

        init();

        return view;
    }

    private void init() {
        not_empty = view.findViewById(R.id.favourite_not_empty_layout);
        empty = view.findViewById(R.id.favourite_empty_layout);
        fav_recycler = view.findViewById(R.id.favourite_recycler);
        dbHelper = new DBHelper(getContext());
        list = new ArrayList<>();
        list = dbHelper.list();

        if(list.size() == 0){
            ///visible null activity
            empty.setVisibility(View.VISIBLE);
            not_empty.setVisibility(View.GONE);
        }else{
            fav_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
            fav_recycler.setHasFixedSize(true);

            adapter = new FavouriteAdapter(getContext() , list);
            fav_recycler.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
}