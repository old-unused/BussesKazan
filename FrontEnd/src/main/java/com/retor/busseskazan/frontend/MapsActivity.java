package com.retor.busseskazan.frontend;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.retor.busloader.lib.Loader;
import com.retor.busloader.lib.interfaces.TaskListener;

public class MapsActivity extends FragmentActivity implements TaskListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    Loader loader;
    private final String data_url = "http://data.kzn.ru:8082/api/v0/dynamic_datasets/bus.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        loader = Loader.getInstance(this, data_url);
        mMap = loader.setupMap(mMap);
    }


    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
        }
    }

    @Override
    public void onLoadingStarted(String msg) {
        Log.d("Task", msg);
    }

    @Override
    public void onLoadingFinish(String msg) {
        Log.d("Task", msg);
        if(mMap.isMyLocationEnabled() && loader.isLocationEnabled())
            loader.setupLocation(mMap);
        loader.clearMap(mMap);
        loader.drawMarkers(mMap, loader.getBuses());
    }

    @Override
    public void onLoadingError(String msg) {
        Log.d("Task", msg);
    }

    @Override
    public void onBackPressed() {
        loader.dispatch();
        super.onBackPressed();
    }
}
