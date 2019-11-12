package com.example.share;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.ServerAddress;
import com.mongodb.DB;
import com.mongodb.MongoClient;

import com.example.share.Data.Item;
import com.mongodb.client.MongoCursor;

import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONObject;

public class ItemListActivity extends AppCompatActivity {
    //arraylist, adapter and gridview
    private ArrayList<Item> items_from_db = null;
    private ArrayList<Item> items_displaying = null;
    private ItemAdapter adapter = null;
    private GridView gridView = null;
    private LocationManager lm = null;

    //util
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
    private String COLLECTION_NAME = "items";

    //image file bitmap
    private Bitmap bmimg;
    private String new_id;
    private String newName;
    private String newPPD;
    private double newLatitude;
    private double newLongitude;
    private Date newDateFrom;
    private Date newDateTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemlist);

        //for DB connection, replace this with proper solution later..
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        sdf= new SimpleDateFormat("yyyy-MM-dd");
        items_from_db = new ArrayList<Item>();

        //Connect to MongoDB
        MongoClient  mongoClient = new MongoClient(new ServerAddress(MongoDB_IP, MongoDB_PORT)); // failed here?
        DB db = mongoClient.getDB(DB_NAME);
        DBCollection collection = db.getCollection(COLLECTION_NAME);

        //Check Data in Database
        DBCursor cursor = collection.find();
        while (cursor.hasNext()) {

            DBObject dbo = (BasicDBObject)cursor.next();
            try {
                WaitNotify waitNotify = new WaitNotify();

                new_id = dbo.get("_id").toString();
                newName=dbo.get("name").toString();
                newPPD=dbo.get("price_per_date").toString();
                newLatitude=Double.parseDouble(dbo.get("latitude").toString());
                newLongitude=Double.parseDouble(dbo.get("longitude").toString());
                String newImPa=dbo.get("image_path").toString();
                newDateFrom = (Date) dbo.get("available_date_start");
                newDateTo = (Date) dbo.get("available_date_end");



                Log.d("MONGODB",new_id);
                Log.d("MONGODB","");
                Log.d("MONGODB",newName);
                Log.d("MONGODB",newPPD);
                Log.d("MONGODB",""+newLatitude);
                Log.d("MONGODB",""+newLongitude);
                Log.d("MONGODB",newDateFrom.toString());
                Log.d("MONGODB",newDateTo.toString());
                Log.d("MONGODB",newImPa);
                Log.d("MONGODB","-");

                //new ItemListActivity.JSONTask().execute("http://ec2-15-164-51-129.ap-northeast-2.compute.amazonaws.com:3000/get_image",newImPa);



                HttpURLConnection con = null;
                BufferedReader reader = null;
                try {
                    //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.accumulate("file_name", newImPa);

                    //URL url = new URL("http://192.168.25.16:3000/users");
                    URL url = new URL("http://ec2-15-164-51-129.ap-northeast-2.compute.amazonaws.com:3000/get_image");
                    //연결을 함
                    con = (HttpURLConnection) url.openConnection();

                    con.setRequestMethod("POST");//POST방식으로 보냄
                    con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
                    con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송


                    con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
                    con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                    con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미
                    con.connect();

                    //서버로 보내기위해서 스트림 만듬
                    OutputStream outStream = con.getOutputStream();
                    //버퍼를 생성하고 넣음
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();//버퍼를 받아줌

                    //서버로 부터 데이터를 받음
                    InputStream stream = con.getInputStream();
                    bmimg = BitmapFactory.decodeStream(stream);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (con != null) {
                        con.disconnect();
                    }
                    try {
                        if (reader != null) {
                            reader.close();//버퍼를 닫아줌
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

                items_from_db.add(new Item(new_id,bmimg, newName, newPPD,newLatitude, newLongitude,
                        newDateFrom, newDateTo));
                //Pass=false;


        }

        //arraylist, adapter and gridview
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
                intent.putExtra("item_image", item.getItem_image());
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
                        ContextCompat.checkSelfPermission( getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                    Log.d("MYGPS","getting permissoin");
                    ActivityCompat.requestPermissions( ItemListActivity.this, new String[] {  Manifest.permission.ACCESS_FINE_LOCATION  },
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

    public class JSONTask extends AsyncTask<String, Integer, Bitmap> {

        private String mTitle = null;
        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("file_name", urls[1].toString());
                HttpURLConnection con = null;
                BufferedReader reader = null;

                try {
                    //URL url = new URL("http://192.168.25.16:3000/users");
                    URL url = new URL(urls[0]);
                    //연결을 함
                    con = (HttpURLConnection) url.openConnection();

                    con.setRequestMethod("POST");//POST방식으로 보냄
                    con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
                    con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송


                    con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
                    con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                    con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미
                    con.connect();

                    //서버로 보내기위해서 스트림 만듬
                    OutputStream outStream = con.getOutputStream();
                    //버퍼를 생성하고 넣음
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();//버퍼를 받아줌

                    //서버로 부터 데이터를 받음
                    InputStream stream = con.getInputStream();
                    bmimg = BitmapFactory.decodeStream(stream);

                    return bmimg;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (con != null) {
                        con.disconnect();
                    }
                    try {
                        if (reader != null) {
                            reader.close();//버퍼를 닫아줌
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Bitmap img) {
            //main_logo.setImageBitmap(bmimg);

        }
    }

    private class WaitNotify{
        synchronized public void mWait() {
            try {
                wait();
            } catch (Exception e) {
                Log.i("Debug", e.getMessage());
            }
        }
        synchronized public void mNotify() {
            try {
                notify();
            }
            catch(Exception e)
            {
                Log.i("Debug", e.getMessage());
            }
        }
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
        image.setImageBitmap(item.getItem_image());

        TextView name = (TextView)convertView.findViewById(R.id.item_name);
        name.setText(item.getItem_name());

        TextView telephone = (TextView)convertView.findViewById(R.id.item_price_per_day);
        telephone.setText(item.getItem_price_per_day() + " won per a day");

        return convertView;

    }

}
