package com.retor.busseskazan.frontend;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.retor.busloader.lib.Loader;
import com.retor.busloader.lib.interfaces.TaskListener;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements TaskListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    public Loader loader;
    private final String data_url = "http://data.kzn.ru:8082/api/v0/dynamic_datasets/bus.json";
    int lastNumber;

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
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        initSpinner((Spinner)findViewById(R.id.spinnerMarshrut));
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

    private void initSpinner(final Spinner spinner) {
        List<String> list = new ArrayList<>();
        list.add(0,"all");
        list.addAll(loader.getBusNumbers());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinner.setAdapter(adapter);
        spinner.setEnabled(true);
        spinner.setPrompt("Bus number");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            spinner.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tmp = parent.getItemAtPosition(position).toString();
                if (tmp.equals("all")){
                    loader.drawMarkers(loader.getBuses());
                }else{
                    loader.drawMarkers(loader.getMarshrut(tmp));
                    lastNumber = position;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast tt = Toast.makeText(parent.getContext(), "Nothing selected", Toast.LENGTH_SHORT);
                tt.setGravity(78, 56, 195);
                tt.show();
            }
        });
    }
}
