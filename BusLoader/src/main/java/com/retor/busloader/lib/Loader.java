package com.retor.busloader.lib;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.retor.busloader.lib.interfaces.MapWorkerInterface;
import com.retor.busloader.lib.interfaces.TaskInterface;
import com.retor.busloader.lib.interfaces.TaskListener;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by retor on 29.12.2014.
 */
public class Loader implements TaskInterface, MapWorkerInterface<GoogleMap>, TaskListener {

    private final String data_url = "http://data.kzn.ru:8082/api/v0/dynamic_datasets/bus.json";
    private String url;
    private ArrayList<Bus> buses;
    private Context context;
    private Activity activity;
    private MakingTask task;
    private ScheduledExecutorService executor;
    private SupportMapFragment fragment;
    private GoogleMap map;
    private TaskListener taskListener;

    protected static volatile Loader instance = null;

    public static Loader getInstance(Activity activity, String url_from, SupportMapFragment fragment){
        Loader localInstance = instance;
        if (localInstance == null) {
            synchronized (Loader.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new Loader(activity, url_from, fragment);
                }
            }
        }
        return instance;
    }

    protected Loader(final Activity activity, String url, SupportMapFragment fragment) {
        this.activity = activity;
        this.url = url;
        this.context = this.activity.getApplicationContext();
        this.executor = Executors.newScheduledThreadPool(1);
        if (isConnected()) {
            executor.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            task = new MakingTask(Loader.this, Loader.this);
                            task.execute();

                        }
                    });
                }
            }, 0, 60, TimeUnit.SECONDS);
        }else{
            onLoadingError("No internet connection");
        }
        this.fragment = fragment;
        this.map = fragment.getMap();
        setupMap(fragment);
    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    protected String getResponseString(){
        String out = null;
        StringBuilder builder = new StringBuilder();
     //for tests start
        if (url==null)
            url=data_url;
     //end test
        try {
            URLConnection connection = new URL(url).openConnection();
            connection.setConnectTimeout(500);
            String tmp;
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()), 8192);
            while ((tmp = reader.readLine())!=null){
                builder.append(tmp);
            }
        } catch (IOException e) {
            final IOException error = e;
            onLoadingError(error.getMessage());
        }
        out = builder.toString();
        if (out!=null)
            return out;
        else
            throw new NullPointerException("Null response");
    }

    protected ArrayList<Bus> createArrayFromResponse(String responseJson){
        ArrayList<Bus> out = new ArrayList<Bus>();
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(responseJson);
            out.addAll(new BusCreator(jsonArray).createArray());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (out!=null)
            return out;
        else
            throw new NullPointerException("Null array");
    }

    public ArrayList<Bus> getBuses() {
        return buses;
    }

    public ArrayList<Bus> getMarshrut(String number) throws NullPointerException{
        ArrayList<Bus> out = new ArrayList<>();
        if (buses!=null){
            for (Bus b:buses){
                if (b.getMarsh().equals(number))
                    out.add(b);
            }
            return out;
        }
        throw new NullPointerException("Null marshrut");
    }

    protected void setBuses(ArrayList<Bus> buses) {
        this.buses = buses;
    }

    @Override
    public void putListenerinTask(Activity activity) {
        this.activity = activity;
        task.listener = this;
    }

    @Override
    public void delListenerFromTask() {
        task.listener = null;
        task.cancel(true);
    }

    public void dispatch(){
        delListenerFromTask();
        executor.shutdown();
        map = null;
        buses = null;
        activity = null;
        executor.shutdown();
        task = null;
        instance = null;
    }

    public boolean isLocationEnabled(){
        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        return !(!lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER) && !lm.isProviderEnabled(LocationManager.GPS_PROVIDER) && !lm.isProviderEnabled(LocationManager.GPS_PROVIDER));
    }

    @Override
    public GoogleMap getMap() {
        return this.map;
    }

    @Override
    public void whatNearMe() {
        if (!isNull(map) && map.isMyLocationEnabled() && isLocationEnabled()){
            Location location;
            LatLng me = null;
            if ((location=map.getMyLocation())!=null) {
                me = new LatLng(location.getLatitude(), location.getLongitude());
                float[] res = new float[100];
                for (Bus b : buses) {
                    LatLng buska = new LatLng(b.getLatitude(), b.getLongitude());
                    Location.distanceBetween(me.latitude, me.longitude, buska.latitude, buska.longitude, res);
                }
                for (float f : res) {
                    Log.d("Distance", String.valueOf(f));
                }
            }
        }
    }

    @Override
    public void setupMap(SupportMapFragment fragment) throws NullPointerException{
        if (isNull(map)){
            map = fragment.getMap();
        }
        map.setTrafficEnabled(true);
        map.setBuildingsEnabled(true);
        map.setMyLocationEnabled(true);
        if (isLocationEnabled())
            map.setMyLocationEnabled(true);
        else
            onLoadingError("Location service is OFF");
    }

    @Override
    public boolean isNull(GoogleMap map) {
        if (map==null)
            return true;
        else
            return false;
    }

    @Override
    public void drawMarkers(ArrayList<Bus> array) {
        if (!isNull(map) && !array.isEmpty())
            for (Bus b:array){
                MarkerOptions marker = new MarkerOptions();
                marker.position(new LatLng(b.getLatitude(), b.getLongitude()));
                marker.title(String.valueOf("Marshrut: " + b.getMarsh() + " Speed: " + b.getSpeed()));
                map.addMarker(marker);
            }
        whatNearMe();
    }

    @Override
    public void clearMap() {
        if (!isNull(map))
            map.clear();
    }

    @Override
    public void setupLocation() {
        if (!isNull(map) && isLocationEnabled())
            if (map.getMyLocation()!=null) {
                CameraUpdate position = CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(map.getMyLocation().getLatitude(), map.getMyLocation().getLongitude()), 9, 30, 0));
                map.moveCamera(position);
            }

    }

    private boolean hasInterface(Activity activity){
        if (activity instanceof TaskListener)
            return true;
        else
            return false;
    }

    @Override
    public void onLoadingStarted(String msg) {
        if (hasInterface(activity)){
            taskListener = (TaskListener)activity;
            taskListener.onLoadingStarted(msg);
        }
    }

    @Override
    public void onLoadingFinish(String msg) {
        if (hasInterface(activity)){
            taskListener = (TaskListener)activity;
            taskListener.onLoadingFinish(msg);
        }
    }

    @Override
    public void onLoadingError(String msg) {
        if (hasInterface(activity)){
            taskListener = (TaskListener)activity;
            taskListener.onLoadingError(msg);
        }
    }
}
