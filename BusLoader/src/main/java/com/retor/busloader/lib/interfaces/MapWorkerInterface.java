package com.retor.busloader.lib.interfaces;


import com.google.android.gms.maps.SupportMapFragment;
import com.retor.busloader.lib.Bus;

import java.util.ArrayList;

/**
 * Created by retor on 30.12.2014.
 */
public interface MapWorkerInterface<T> {

    public void setupMap(SupportMapFragment fragment) throws  NullPointerException;
    public boolean isNull(T map);
    public void drawMarkers(ArrayList<Bus> array);
    public void clearMap();
    public void setupLocation();
    public T getMap();
    public void whatNearMe();
}
