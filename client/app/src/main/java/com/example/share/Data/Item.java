package com.example.share.Data;


import java.io.Serializable;
import java.util.Date;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Item implements Comparable<Item>,Serializable {

    private String item_id;
    private String item_name;
    private Bitmap item_image;
    private String item_price_per_day;
    private double latitude;
    private double longitude;
    private float[] distanceToUser;
    private Date availableFrom;
    private Date availableTo;

    public Item(String item_id, Bitmap item_image, String item_name, String item_price_per_day, double latitude, double longitude, Date availableFrom, Date availableTo){
        this.item_id = item_id;
        this.item_name = item_name;
        this.item_image = item_image;
        this.item_price_per_day = item_price_per_day;
        this.latitude = latitude;
        this.longitude = longitude;
        this.availableFrom = availableFrom;
        this.availableTo = availableTo;
    }

    public String getItem_id() {
        return item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public Bitmap getItem_image() {
        return item_image;
    }

    public String getItem_price_per_day() {
        return item_price_per_day;
    }

    public double getLatitude() { return latitude;  }

    public double getLongitude() { return longitude; }

    public void setDistanceToUser(float[] distanceToUser) {
        this.distanceToUser = distanceToUser;
    }

    public float[] getDistanceToUser() {  return distanceToUser;  }

    public Date getAvailableFrom() {  return availableFrom;  }

    public Date getAvailableTo() {  return availableTo;  }

    @Override
    public int compareTo(Item i) {
        return  Double.compare(this.distanceToUser[0],i.getDistanceToUser()[0]);
    }
}