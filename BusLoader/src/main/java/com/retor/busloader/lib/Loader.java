package com.retor.busloader.lib;

import android.app.Activity;
import android.content.Context;
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
public class Loader implements TaskInterface{

    private String url;
    private ArrayList<Bus> buses;
    private Context context;
    private Activity activity;
    private MakingTask task;
    private ScheduledExecutorService executor;

    protected static volatile Loader instance = null;

    public static Loader getInstance(Activity activity, String url_from){
        Loader localInstance = instance;
        if (localInstance == null) {
            synchronized (Loader.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new Loader(activity, url_from);
                }
            }
        }
        return instance;
    }

    protected Loader(final Activity activity, String url) {
        this.activity = activity;
        this.url = url;
        this.context = this.activity.getApplicationContext();
        this.executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        task = new MakingTask((TaskListener)activity, Loader.this);
                        task.execute();
                    }
                });
            }
        }, 0, 60, TimeUnit.SECONDS);
    }

    @Override
    protected void finalize() throws Throwable {
        buses = null;
        activity = null;
        if (!task.isCancelled())
            task = null;
        executor.shutdownNow();
        executor.shutdown();
        instance = null;
        super.finalize();
    }

    protected String getResponseString(){
        String out = null;
        StringBuilder builder = new StringBuilder();
        try {
            URLConnection connection = new URL(url).openConnection();
            connection.setConnectTimeout(120);
            String tmp;
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()), 8192);
            while ((tmp = reader.readLine())!=null){
                builder.append(tmp);
            }
        } catch (IOException e) {
            e.printStackTrace();
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

    protected void setBuses(ArrayList<Bus> buses) {
        this.buses = buses;
    }

    @Override
    public void putListenerinTask(Activity activity) {
        this.activity = activity;
        task.listener = (TaskListener)activity;
    }

    @Override
    public void delListenerFromTask() {
        task.listener = null;
        task.cancel(true);
    }
}
