package com.retor.busloader.lib.interfaces;


import com.google.android.gms.maps.GoogleMap;
import com.retor.busloader.lib.Bus;

import java.util.ArrayList;

/**
 * Created by retor on 30.12.2014.
 */
public interface MapWorkerInterface {

    public GoogleMap setupMap(GoogleMap map) throws  NullPointerException;
    public boolean isNull(GoogleMap map);
    public void drawMarkers(GoogleMap map, ArrayList<Bus> array);
    public void clearMap(GoogleMap map);
    public void setupLocation(GoogleMap map);

}
