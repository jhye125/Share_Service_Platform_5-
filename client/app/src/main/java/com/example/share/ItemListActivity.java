package com.example.share;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ItemListActivity extends AppCompatActivity {
    private ArrayList<Item> items = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemlist);

        items = new ArrayList<Item>();
        items.add(new Item("0", R.drawable.item_sample_ipad, "ipad", "5000"));
        items.add(new Item("1", R.drawable.item_sample_hammer, "hammer", "500"));
        items.add(new Item("2", R.drawable.item_sample_mouse, "mouse", "2000"));
        items.add(new Item("3", R.drawable.item_sample_longboard, "long board", "4000"));
        items.add(new Item("4", R.drawable.item_sample_handmixer, "hand mixer", "3000"));


        ItemAdapter adapter = new ItemAdapter(this, items);

        GridView gridView = (GridView) findViewById(R.id.item_grid_view);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = items.get(position);

                Intent intent = new Intent(getApplicationContext(), ItemDetailActivity.class);
                intent.putExtra("item_id", item.getItem_id());
                intent.putExtra("item_image", item.getItem_image() + "");
                intent.putExtra("item_name", item.getItem_name());
                intent.putExtra("item_price_per_day", item.getItem_price_per_day());

                startActivity(intent);
            }
        });

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        int gpsCheck = ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION);
        Location location = null;
        try {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                double lat = location.getLatitude();
                double lng = location.getLongitude();
                Log.d("MY_GPSCHK", "latitude: " + lat + ", longitude: " + lng);
            } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                double lat = location.getLatitude();
                double lng = location.getLongitude();
                Log.d("MY_GPSCHK", "latitude: " + lat + ", longitude: " + lng);
            } else {
                Log.d("MY_GPSCHK", "else");
            }
            Log.d("MY_GPSCHK", "here??????");
        }catch (Exception e){
            Log.d("MY_GPSCHK", "hi");
        }
        Log.d("MY_GPSCHK", "here?");

    }
}

class Item{

    private String item_id;
    private String item_name;
    private int item_image;
    private String item_price_per_day;

    public Item(String item_id, int item_image, String item_name,String item_price_per_day){
        this.item_id = item_id;
        this.item_name = item_name;
        this.item_image = item_image;
        this.item_price_per_day = item_price_per_day;
    }

    public String getItem_id() {
        return item_id;
    }

    public String getItem_name() {
        return item_name;
}

    public int getItem_image() {
        return item_image;
    }

    public String getItem_price_per_day() {
        return item_price_per_day;
    }
}

class ItemAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private ArrayList<Item> items = null;
    private int count = 0;

    public ItemAdapter(Context context, ArrayList<Item> items) {
        this.items = items;
        this.count = items.size();
        this.inflater = (LayoutInflater)context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.itemlist_item, parent, false);
        }

        Item item = items.get(position);

        ImageView image = (ImageView)convertView.findViewById(R.id.item_image);
        image.setImageResource(item.getItem_image());

        TextView name = (TextView)convertView.findViewById(R.id.item_name);
        name.setText(item.getItem_name());

        TextView telephone = (TextView)convertView.findViewById(R.id.item_price_per_day);
        telephone.setText(item.getItem_price_per_day() + "won per day");

        return convertView;

    }
}

