package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ReservationInfoActivity extends AppCompatActivity {

    private ImageView pay;
    private ImageView photo;
    private TextView title;
    private TextView date;
    private TextView price;
    private TextView content;
    private TextView location;
    private TextView category;
    private TextView user_name;
    private ImageView user_photo;
    private RatingBar user_rate;
    private TextView review_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_info);
        pay = (ImageView)findViewById(R.id.pay);
        //Intent intent = getIntent();
        //String name = intent.getStringExtra("string");
       // Toast.makeText(getApplicationContext(),"name : "+name,Toast.LENGTH_SHORT).show();
        photo = (ImageView)findViewById(R.id.item_photo);
        title = (TextView)findViewById(R.id.item_title);
        date = (TextView)findViewById(R.id.item_reservation_date);
        price = (TextView)findViewById(R.id.item_reservation_price);
        content = (TextView)findViewById(R.id.item_content);
        location = (TextView)findViewById(R.id.location);
        category = (TextView)findViewById(R.id.category);
        user_photo=(ImageView)findViewById(R.id.user_photo);
        user_name = (TextView)findViewById(R.id.user_name);
        user_rate = (RatingBar)findViewById(R.id.rating_star);
        review_number =(TextView)findViewById(R.id.review_number);

        title.setText("zzzzzz");
        date.setText("11.20-11.22");
        price.setText("300원");
        content.setText("content");
        location.setText("lllll");
        category.setText("dfdfdfdf");
        user_name.setText("gggf");

        user_rate.setRating(3);
        review_number.setText("(3)");

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),PayActivity.class);
                startActivity(intent);
            }
        });
    }
}
