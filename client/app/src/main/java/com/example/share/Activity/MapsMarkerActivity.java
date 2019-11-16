package com.example.share.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.share.Data.Item;
import com.example.share.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * An activity that displays a Google map with a marker (pin) to indicate a particular location.
 */
public class MapsMarkerActivity extends AppCompatActivity
        implements OnMapReadyCallback {
    private  ArrayList<Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        Intent intent = getIntent();
        items = (ArrayList<Item>)intent.getSerializableExtra("items");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user receives a prompt to install
     * Play services inside the SupportMapFragment. The API invokes this method after the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        LatLng daegu = new LatLng(35.888836,  128.6102997);

        for(int i =0; i < items.size();i++){
            Item item = items.get(i);
            double latitude = item.getLatitude();
            double longitude = item.getLongitude();
            //Log.d("MYGOOGLEMAP",""+latitude+" "+longitude);
            googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latitude, longitude))
                            .anchor(0.5f, 0.5f)
                            .title(item.getItem_name())
                            .snippet(item.getItem_price_per_day()+" won per a day")
                    //.flat(true)
                    //.alpha(0.7f)//투명도
                    //.icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(item.getItem_image(),100,100)))
            );

        }
//        gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(startingPoint,16));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(daegu,14));
    }

    public Bitmap resizeMapIcons(int id, int width, int height){
        Bitmap imageBitmap =  BitmapFactory.decodeResource(getResources(), id);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }
}
