package com.retor.busloader.lib;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by retor on 29.12.2014.
 */
public interface CreatorInterface<T> {

    public T next(JSONObject object);

    public ArrayList<T> createArray();
}
