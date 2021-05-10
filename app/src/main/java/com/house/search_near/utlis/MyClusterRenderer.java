package com.house.search_near.utlis;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.house.search_near.R;
import com.house.search_near.model.ClusterMarker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

public class MyClusterRenderer extends DefaultClusterRenderer<ClusterMarker> {
    private final IconGenerator iconGenerator;
    private final ImageView imageView;
    private final int markerWeight;
    private final int markerHeight;

    public MyClusterRenderer(Context context, GoogleMap map, ClusterManager<ClusterMarker> clusterManager) {
        super(context, map, clusterManager);

        iconGenerator = new IconGenerator(context.getApplicationContext());
        imageView = new ImageView(context.getApplicationContext());
        markerWeight = (int) context.getResources().getDimension(R.dimen.cluster_icon);
        markerHeight = (int) context.getResources().getDimension(R.dimen.cluster_icon);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(markerWeight , markerHeight));
        int padding = (int) context.getResources().getDimension(R.dimen.cluster_icon_padding);
        imageView.setPadding(padding , padding , padding , padding);
        iconGenerator.setContentView(imageView);

    }

    @Override
    protected void onBeforeClusterItemRendered(@NonNull ClusterMarker item, @NonNull MarkerOptions markerOptions) {
        imageView.setImageResource(item.getImageIcon());
        Bitmap icon = iconGenerator.makeIcon();
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(item.getTitle());
    }



    @Override
    protected boolean shouldRenderAsCluster(@NonNull Cluster<ClusterMarker> cluster) {
        return false;
    }
}
