package com.example.share.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

public class ItemDetailActivity extends AppCompatActivity {

    ImageButton pay_button;
    private FromServerImage newImage = new FromServerImage();

    Item item;
    String item_id;
    ImageView question;
    ImageView reservation;

    //mongoDB
    private String MongoDB_IP = "15.164.51.129";
    private int MongoDB_PORT = 27017;
    private String DB_NAME = "local";
    private String ITEM_COLLECTION = "items";
    private String USER_COLLECTION= "users";

    private ImageView editBtn;
    private ImageView deleteBtn;

    private String user_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemdetail);

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        user_email = pref.getString("user_email",null);

        Intent intent = getIntent();
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
        if (Build.VERSION.SDK_INT >= 23) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //Connect to MongoDB
        MongoClient mongoClient = new MongoClient(new ServerAddress(MongoDB_IP, MongoDB_PORT)); // failed here?
        DB db = mongoClient.getDB(DB_NAME);
        DBCollection collection = db.getCollection( ITEM_COLLECTION);

        /** Qurey 1: get Item Info */
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

        /** Qurey 2: get Owner Info */
        String owner_email = dbObj.get("owner_email").toString();
        DBCollection collection2 = db.getCollection(USER_COLLECTION);
        BasicDBObject query2 = new BasicDBObject();
        query2.put("email", owner_email);
        DBObject dbObj2 = collection2.findOne(query2);
        owner_image.setImageResource(R.drawable.owner_sample);
        owner_name.setText(dbObj2.get("name").toString());
        owner_rating.setText("4.5 / 5");

        question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChattingActivity.class);
                intent.putExtra("owner_email",owner_email);
              //  intent.putExtra("owner_name",owner);


                startActivity(intent);
            }
        });

        reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ReservationInfoActivity.class);
                intent.putExtra("item_object",item);
                startActivity(intent);
            }
        });

        /** Delete and Edit button */
        editBtn = (ImageView)findViewById(R.id.edit_item_button) ;
        deleteBtn = (ImageView)findViewById(R.id.delete_item_button) ;
        if(user_email.equals(owner_email)){

            /**Edit Button*/
            //TODO: below asynctask
            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getApplicationContext(), UpdateItemActivity.class);
                    intent.putExtra("item_object",item);

                    startActivity(intent);
                    //new ItemDetailActivity.DeleteTask().execute("http://ec2-15-164-51-129.ap-northeast-2.compute.amazonaws.com:3000/item_update");
                }
            });

            /**Delete Button*/
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ItemDetailActivity.DeleteTask().execute("http://ec2-15-164-51-129.ap-northeast-2.compute.amazonaws.com:3000/item_delete");
                }
            });
        }else{
            editBtn.setVisibility(View.INVISIBLE);
            deleteBtn.setVisibility(View.INVISIBLE);
        }


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

    public class DeleteTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("_id",item_id);
                jsonObject.accumulate("executed_user",user_email);

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{

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

                    reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuffer buffer = new StringBuffer();
                    String line = "";
                    while((line = reader.readLine()) != null){
                        buffer.append(line);
                    }

                    return buffer.toString();//서버로 부터 받은 값을 리턴해줌 아마 OK!!가 들어올것임

                } catch (MalformedURLException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(con != null){
                        con.disconnect();
                    }
                    try {
                        if(reader != null){
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

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.equals("true")) {    //delete 성공시 Home Intents 시작 메소드
                // delete true
                AlertDialog.Builder alert = new AlertDialog.Builder(ItemDetailActivity.this);
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();     //닫기
                        finish();
                    }
                });
                alert.setMessage("삭제 완료");
                alert.show();

            }
            else{   //delete 실패시 에러 메시지 출력
                AlertDialog.Builder alert = new AlertDialog.Builder(ItemDetailActivity.this);
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();     //닫기
                        finish();
                    }
                });
                alert.setMessage(result);
                alert.show();
            }
        }
    }
}
