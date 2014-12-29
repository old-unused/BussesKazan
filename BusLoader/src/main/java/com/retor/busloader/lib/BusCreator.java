package com.retor.busloader.lib;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by retor on 29.12.2014.
 */
public class BusCreator implements CreatorInterface<Bus>, Iterable<Bus> {

    private JSONArray jsonArray;
    private int arraySize;

    public BusCreator (JSONArray array) {
        jsonArray = array;
        arraySize = array.length();
    }

    @Override
    public Bus next(JSONObject object) {
        return new Bus(object);
    }

    @Override
    public ArrayList<Bus> createArray() {
        ArrayList out = new ArrayList();
        for (Bus b:new BusCreator(jsonArray)){
            out.add(b);
        }
        return out;
    }

    @Override
    public Iterator<Bus> iterator() {
        return new Iterator<Bus>() {
            @Override
            public boolean hasNext() {
                return ((arraySize-1)>0);
            }

            @Override
            public Bus next() {
                arraySize--;
                Bus b = null;
                try {
                    b = new Bus((JSONObject)jsonArray.get(arraySize));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return b;
            }

            @Override
            public void remove() {

            }
        };
    }
}
