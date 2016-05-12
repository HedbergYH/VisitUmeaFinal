package com.example.umyhnystma.matsvisitumea;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by umyhhedbjo on 2016-05-02.
 */
public class Site {

    String name, description;
    Long latitude, longitude;
    Bitmap image;

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    Marker marker;

    public Site(){

        //Empty constructor

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getLatitude() {
        return latitude;
    }

    public void setLatitude(Long latitude) {
        this.latitude = latitude;
    }

    public Long getLongitude() {
        return longitude;
    }

    public void setLongitude(Long longitude) {
        this.longitude = longitude;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

}
