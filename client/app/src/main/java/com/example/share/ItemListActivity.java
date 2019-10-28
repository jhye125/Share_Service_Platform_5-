package com.example.share;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.ServerAddress;
import com.mongodb.DB;
import com.mongodb.MongoClient;

import com.example.share.Data.Item;

public class ItemListActivity extends AppCompatActivity {
    private ArrayList<Item> items_from_db = null;
    private ArrayList<Item> items_displaying = null;
    private ItemAdapter adapter = null;
    private LocationManager lm = null;
    private GridView gridView = null;
    private SimpleDateFormat sdf= null;

    //within the dialog
    private EditText dateFrom =null;
    private EditText dateTo =null;
    private Dialog dialog;

    //activityForResult
    private int MAP_ACT = 1;

    //mongoDB
    private String MongoDB_IP = "15.164.51.129";
    private int MongoDB_PORT = 27017;
    private String DB_NAME = "local";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemlist);

        //for DB connection, replace this with proper solution later..
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //Connect to MongoDB
        MongoClient  mongoClient = new MongoClient(new ServerAddress(MongoDB_IP, MongoDB_PORT));

        //View Database List
        List<String> databases = mongoClient.getDatabaseNames();
        int num =1 ;
        for (String dbName : databases) {
            Log.d("MONGODB", num  + ". " + dbName);
            num++;
        }

        //Connect Database and Show Collection List in Database
        DB db = mongoClient.getDB(DB_NAME);
        Set<String> collections = db.getCollectionNames();

        Log.d("MONGODB","Database : " + DB_NAME);
        for (String colName : collections) {
            Log.d("MONGODB"," + Collection: " + colName);
        }
        DBCollection collection = db.getCollection("users");

        //Check Data in Database
        DBCursor cursorDocBuilder = collection.find();
        while (cursorDocBuilder.hasNext()) {
            Log.d("MONGODB",""+cursorDocBuilder.next());
        }


        sdf= new SimpleDateFormat("yyyy-MM-dd");

        items_from_db = new ArrayList<Item>();

        try {
            items_from_db.add(new Item("0", R.drawable.item_sample_ipad, "ipad", "5000", 35.811111, 128.550042, sdf.parse("2019-05-01"), sdf.parse("2019-12-31")));
            items_from_db.add(new Item("1", R.drawable.item_sample_hammer, "hammer", "500", 35.856708, 128.590808, sdf.parse("2019-10-01"), sdf.parse("2019-11-31")));
            items_from_db.add(new Item("2", R.drawable.item_sample_mouse, "mouse", "2000", 35.86969, 128.59367, sdf.parse("2019-10-01"), sdf.parse("2019-11-01")));
            items_from_db.add(new Item("3", R.drawable.item_sample_longboard, "long board", "4000", 35.8947593, 128.5881245, sdf.parse("2019-10-01"), sdf.parse("2019-10-31")));
            items_from_db.add(new Item("4", R.drawable.item_sample_handmixer, "hand mixer", "3000", 35.9051093, 128.5883257, sdf.parse("2019-10-01"), sdf.parse("2019-10-31")));
        }catch(ParseException e){
            //handle error for the date format
            Log.d("ItemListActivity","parse Error",e);
        }
        items_displaying = new ArrayList<Item>(items_from_db);

        adapter = new ItemAdapter(this, items_from_db);

        gridView = (GridView) findViewById(R.id.item_grid_view);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = items_from_db.get(position);

                Intent intent = new Intent(getApplicationContext(), ItemDetailActivity.class);
                intent.putExtra("item_id", item.getItem_id());
                intent.putExtra("item_image", item.getItem_image() + "");
                intent.putExtra("item_name", item.getItem_name());
                intent.putExtra("item_price_per_day", item.getItem_price_per_day());

                startActivity(intent);
            }
        });



//        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                switch (actionId) {
//                    case EditorInfo.IME_ACTION_SEARCH:
//                        // 검색 동작
//                        break;
//                    default:
//                        // 기본 엔터키 동작
//                        return false;
//                }
//                return true;
//            }
//        });

        Button mapsButton = (Button)findViewById(R.id.map_button);
        mapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), MapsMarkerActivity.class);
                intent.putExtra("items",items_displaying);
                startActivityForResult(intent,MAP_ACT);

            }

        });



        /** Date button */
        Button dateButton = (Button)findViewById(R.id.date_button);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MYDIALOG","start date filtering");
                dialog = new Dialog(ItemListActivity.this);
                dialog.setContentView(R.layout.itemlist_date_dialog);
                dialog.show();

                /** start date */
                dateFrom = (EditText)dialog.findViewById(R.id.date_from);
                dateFrom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // DatePickerDialog
                        DatePickerDialog datePickerDialog = new DatePickerDialog(ItemListActivity.this, new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker view,
                                                  int year, int month,int day) {
                                dateFrom.setText(year+"-"+(month+1)+"-"+day);
                            }
                        }, 2019, 10, 1);//11월 1일임. month 값이 0부터 시작함.
                        datePickerDialog.show();

                    }
                });

                /** end date */
                dateTo = (EditText)dialog.findViewById(R.id.date_to);
                dateTo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // DatePickerDialog
                        DatePickerDialog datePickerDialog = new DatePickerDialog(ItemListActivity.this, new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker view,
                                                  int year, int month,int day) {
                                dateTo.setText(year+"-"+(month+1)+"-"+day);

                            }
                        }, 2019, 10, 30); //11월30일임. month 값이 0부터 시작함.
                        datePickerDialog.show();

                    }
                });

                /** ok button */
                Button okButton = (Button)dialog.findViewById(R.id.date_ok_button);
                okButton.setOnClickListener( new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Date selectedDateFrom = null;
                        Date selectedDateTo = null;
                        try {
                            selectedDateFrom = sdf.parse(""+dateFrom.getText());
                            selectedDateTo = sdf.parse(""+dateTo.getText());
                        } catch (ParseException e){
                            Log.d("MYPARSE","parse Error_filter,date",e);
                        }
                        Log.d("MYPARSE","dstart: "+selectedDateFrom.toString());
                        Log.d("MYPARSE","dend: "+selectedDateTo.toString());
                        items_displaying = new ArrayList<Item>();
                        for (int i = 0; i < items_from_db.size(); i++){
                            Item item = items_from_db.get(i);
                            Date iAvailableFrom = item.getAvailableFrom();
                            Date iAvailableTo = item.getAvailableTo();

                            if( (iAvailableFrom.compareTo(selectedDateFrom) <= 0) &&(selectedDateTo.compareTo(iAvailableTo) <= 0)  )
                                items_displaying.add(item);

                        }
                        adapter = new ItemAdapter(getApplicationContext(), items_displaying);
                        gridView.setAdapter(adapter);
                        dialog.dismiss();
                    }

                } );

                /** cancel button */
                Button cancelButton = (Button)dialog.findViewById(R.id.date_cancel_button);
                cancelButton.setOnClickListener( new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        dialog.dismiss();
                    }

                } );

            }

        });

        /** location button */
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Button locationButton = (Button)findViewById(R.id.location_button);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("MYGPS","clicked");

                if ( Build.VERSION.SDK_INT >= 23 &&
                        ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                    Log.d("MYGPS","getting permissoin");
                    ActivityCompat.requestPermissions( ItemListActivity.this, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                            0 );
                }
                else{
                    Log.d("MYGPS","gogo");
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            1000,
                            1,
                            gpsLocationListener);
                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            1000,
                            1,
                            gpsLocationListener);
                }
            }
        });
    }

    final LocationListener gpsLocationListener = new LocationListener() {

        public void onLocationChanged(Location location) {

            String provider = location.getProvider();
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            double altitude = location.getAltitude();
            float accuracy = location.getAccuracy();

            String info = "위치정보: " + provider + " 위도: " + latitude  + " 경도: " + longitude + " 고도 : " + altitude + " 정확도(the lower the better): "+accuracy;
            Log.d("MYGPSCHK_lisnter",info);

            if(accuracy <35.0f) {
                Log.d("MYGPSCHK","sort~~");

                for(Item i : items_from_db){
                    Log.d("MYGPSCHK","dist");
                    float [] distance = new float [10];

                    Location.distanceBetween(latitude,longitude,i.getLatitude(),i.getLongitude(),distance);
                    i.setDistanceToUser(distance);
                    Log.d("MYGPSCHK",""+distance[0]);
                }
                Collections.sort(items_from_db);
                adapter = new ItemAdapter(getApplicationContext(), items_from_db);
                Log.d("MYGPSCHK", "refresh"  );
                gridView.setAdapter(adapter);

                Log.d("MYGPSCHK", "removeUpdates");
                lm.removeUpdates(this);
            }
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };

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
        telephone.setText(item.getItem_price_per_day() + " won per a day");

        return convertView;

    }

}

