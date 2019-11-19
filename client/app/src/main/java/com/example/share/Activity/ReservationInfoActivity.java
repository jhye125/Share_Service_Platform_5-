package com.example.share.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.share.Data.Item;
import com.example.share.R;

public class ReservationInfoActivity extends AppCompatActivity {

    private ImageView pay;
    private Item item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_info);

        Intent intent = getIntent();
        //get all the data passed
        item = (Item)intent.getSerializableExtra("item_object");

        //Intent intent = getIntent();
        //String name = intent.getStringExtra("string");
       // Toast.makeText(getApplicationContext(),"name : "+name,Toast.LENGTH_SHORT).show();
        pay = (ImageView)findViewById(R.id.pay);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),PayActivity.class);
                intent.putExtra("item_object",item);
                startActivity(intent);
            }
        });
    }
}
