package com.example.apple.sos;

/**
 * Created by apple on 9/14/17.
 */

public class Locatn {

    String lat;
    String lont;

    public Locatn()
    {

    }

    public Locatn(String lat, String lont) {
        this.lat = lat;
        this.lont = lont;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLont() {
        return lont;
    }

    public void setLont(String lont) {
        this.lont = lont;
    }


}
