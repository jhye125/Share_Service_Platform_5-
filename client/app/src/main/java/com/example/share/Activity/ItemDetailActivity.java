package com.example.share.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.share.Chatting.ChattingActivity;
import com.example.share.Data.Item;
import com.example.share.MongoDB.FromServerImage;
import com.example.share.R;
import com.google.android.gms.maps.model.LatLng;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import androidx.appcompat.app.AppCompatActivity;

import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ItemDetailActivity extends AppCompatActivity {

    ImageButton pay_button;
    private FromServerImage newImage = new FromServerImage();

    Item item;
    String item_id;
    ImageView question;
    ImageView reservation;
    private String user_name;
    private String chatroom_name;

    //mongoDB
    private String MongoDB_IP = "15.164.51.129";
    private int MongoDB_PORT = 27017;
    private String DB_NAME = "local";
    private String ITEM_COLLECTION = "items";
    private String USER_COLLECTION= "users";
    private SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemdetail);

        Intent intent = getIntent();

        pref = getSharedPreferences("pref", AppCompatActivity.MODE_PRIVATE);
        user_name = pref.getString("user_name",null);
        //get all the data passed
        item = (Item)intent.getSerializableExtra("item_object");


        item_id = item.getItem_id();

        //get all the layout
        ImageView item_image = (ImageView)findViewById(R.id.item_detail_item_image);
        TextView item_name = (TextView)findViewById(R.id.item_detail_item_name);
        TextView item_price_per_day = (TextView)findViewById(R.id.item_detail_item_price);
        question = (ImageView)findViewById(R.id.btn_question);
        reservation = (ImageView)findViewById(R.id.btn_reservation);
        //item detail, TODO
        TextView item_detail = (TextView)findViewById(R.id.item_detail_item_detail);
        TextView item_location = (TextView)findViewById(R.id.item_detail_item_location);
        TextView item_category = (TextView)findViewById(R.id.item_detail_item_category);
        //Owner info, TODO
        ImageView owner_image = (ImageView)findViewById(R.id.item_detail_owner_image);
        TextView owner_name = (TextView)findViewById(R.id.item_detail_owner_name);
        TextView owner_rating = (TextView)findViewById(R.id.item_detail_owner_rating);

        //for DB connection, replace this with proper solution later..
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //Connect to MongoDB
        MongoClient mongoClient = new MongoClient(new ServerAddress(MongoDB_IP, MongoDB_PORT)); // failed here?
        DB db = mongoClient.getDB(DB_NAME);
        DBCollection collection = db.getCollection( ITEM_COLLECTION);

        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(item_id));
        DBObject dbObj = collection.findOne(query);

        Log.d("MONGODB_",dbObj.toString());

        //set contents
        item_name.setText(dbObj.get("name").toString());
        item_price_per_day.setText(dbObj.get("price_per_date").toString());
        item_detail.setText(dbObj.get("contents").toString());

        LatLng add = new LatLng( Double.parseDouble(dbObj.get("latitude").toString()), Double.parseDouble(dbObj.get("longitude").toString()) );
        String set_address = getCurrentAddress(add);
        item_location.setText(set_address);

        item_category.setText("카테고리   " + dbObj.get("category").toString());

        //set all the layout contents
        item_image.setImageBitmap(newImage.getImage(item.getFilepath()));

        //owner query
        String owner_email = dbObj.get("owner_email").toString();
        DBCollection collection2 = db.getCollection(USER_COLLECTION);
        BasicDBObject query2 = new BasicDBObject();
        query2.put("email", owner_email);
        DBObject dbObj2 = collection2.findOne(query2);


        Log.d("jihye",dbObj2.get("name").toString()+"");
        owner_image.setImageResource(R.drawable.owner_sample);
        owner_name.setText(dbObj2.get("name").toString());
        owner_rating.setText("4.5 / 5");
        String owner = dbObj2.get("name").toString();

        Log.d("dong",owner+","+user_name);
       /* if(owner.compareTo(user_name) ==-1){
            chatroom_name = owner+"-"+user_name;
        }else{
            chatroom_name = user_name+"-"+owner;
        }*/

        question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChattingActivity.class);
                intent.putExtra("owner_email",owner_email);
                intent.putExtra("owner_name",owner);


                startActivity(intent);
            }
        });

        reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ReservationInfoActivity.class);
                startActivity(intent);
            }
        });

        return;
    }

    public String getCurrentAddress(LatLng latlng) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latlng.latitude,
                    latlng.longitude,
                    1);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }


        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        } else {
            Address address = addresses.get(0);
            return address.getAddressLine(0).toString();
        }

    }
}