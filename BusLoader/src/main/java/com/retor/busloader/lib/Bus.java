package com.retor.busloader.lib;

import org.json.JSONObject;

/**
 * Created by retor on 29.12.2014.
 */
public class Bus {

    private int GaragNumb;
    private long Azimuth;
    private int Graph;
    private double Latitude;
    private double Longitude;
    private double prevLatitude;
    private double prevLongitude;
    private String Marsh;
    private int Smena;
    private int Speed;
    private String TimeNav;
    private String updated_at;
    private float alpha = 1;
    private boolean visible = true;

    public Bus(JSONObject object) {
        if (object != null) {
            GaragNumb = object.optJSONObject("data").optInt("GaragNumb");
            Azimuth = object.optJSONObject("data").optLong("Azimuth");
            Graph = object.optJSONObject("data").optInt("Graph");
            Latitude = object.optJSONObject("data").optDouble("Latitude");
            Longitude = object.optJSONObject("data").optDouble("Longitude");
            Marsh = object.optJSONObject("data").optString("Marsh");
            Smena = object.optJSONObject("data").optInt("Smena");
            Speed = object.optJSONObject("data").optInt("Speed");
            TimeNav = object.optJSONObject("data").optString("TimeNav");
            updated_at = object.optString("updated_at");
        }
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public long getAzimuth() {
        return Azimuth;
    }

    public int getGaragNumb() {
        return GaragNumb;
    }

    public int getGraph() {
        return Graph;
    }

    public double getLatitude() {
        return Latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public String getMarsh() {
        return Marsh;
    }

    public double getPrevLatitude() {
        return prevLatitude;
    }

    public void setPrevLatitude(double prevLatitude) {
        this.prevLatitude = prevLatitude;
    }

    public double getPrevLongitude() {
        return prevLongitude;
    }

    public void setPrevLongitude(double prevLongitude) {
        this.prevLongitude = prevLongitude;
    }

    public int getSmena() {
        return Smena;
    }

    public int getSpeed() {
        return Speed;
    }

    public String getTimeNav() {
        return TimeNav;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
