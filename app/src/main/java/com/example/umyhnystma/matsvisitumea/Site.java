package com.example.umyhnystma.matsvisitumea;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by umyhhedbjo on 2016-05-02.
 */
public class Site implements java.io.Serializable {

    String name;
    String description;

    String category,picture_URL;

    double latitude, longitude;
    Bitmap image;

    Marker marker;

    public Site(){

        //Empty constructor

    }

    public Site(String name, String description, double latitude, double longitude, String category, String picture_URL){

        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.category = category;
        this.picture_URL = picture_URL;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
/*
    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
*/
    public String getPictureURL() {
        return picture_URL;
    }

    public void setPictureURL(String picture_URL) {
        this.picture_URL = picture_URL;
    }

    // Metod för att jämföra namnat i två Site-objekt
    public static Comparator<Site> COMPARE_BY_BUILDING_NAME = new Comparator<Site>() {//Ny anonym inre klass
        public int compare(Site one, Site other) {
            return one.name.compareTo(other.name);
        }
    };



}
