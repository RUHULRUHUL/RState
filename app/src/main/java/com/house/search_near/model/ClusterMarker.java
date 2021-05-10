package com.house.search_near.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class ClusterMarker implements ClusterItem {

    private final LatLng position;
    private final String title;
    private final String snippet;
    private final int imageIcon;
    private final PostModel postModel;

    public ClusterMarker(double lat, double lng, String title, String snippet , int imageIcon , PostModel postModel) {
        position = new LatLng(lat, lng);
        this.title = title;
        this.snippet = snippet;
        this.imageIcon = imageIcon;
        this.postModel = postModel;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSnippet() {
        return snippet;
    }

    public int getImageIcon() {
        return imageIcon;
    }

    public PostModel getPostModel() {
        return postModel;
    }
}