package com.retor.busseskazan.frontend;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.retor.busloader.lib.Bus;
import com.retor.busloader.lib.Loader;
import com.retor.busloader.lib.TaskListener;

public class MapsActivity extends FragmentActivity implements TaskListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Loader loader;
    private final String data_url = "http://data.kzn.ru:8082/api/v0/dynamic_datasets/bus.json";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        loader = Loader.getInstance(this, data_url);
        if (savedInstanceState==null){


        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void fillMap(){
        mMap.clear();
        for (Bus b:loader.getBuses()){
            mMap.addMarker(new MarkerOptions().position(new LatLng(b.getLatitude(), b.getLongitude())).title(String.valueOf(b.getGaragNumb())));
        }
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    @Override
    public void onLoadingStarted(String msg) {
        Log.d("Task", msg);
    }

    @Override
    public void onLoadingFinish(String msg) {
        Log.d("Task", msg);
        fillMap();
    }

    @Override
    public void onLoadingError(String msg) {
        Log.d("Task", msg);
    }
}
