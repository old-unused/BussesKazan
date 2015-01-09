package com.retor.busseskazan.frontend;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;
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
        loader = Loader.getInstance(this, data_url, ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMap = loader.getMap();
    }

    @Override
    public void onLoadingStarted(String msg) {
        Log.d("Task", msg);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadingFinish(String msg) {
        Log.d("Task", msg);
/*        if(mMap.isMyLocationEnabled() && loader.isLocationEnabled())
            loader.setupLocation();*/
        loader.clearMap();
        loader.drawMarkers(loader.getBuses());
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadingError(String msg) {
        Log.d("Task", msg);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        loader.dispatch();
        super.onBackPressed();
    }
}
