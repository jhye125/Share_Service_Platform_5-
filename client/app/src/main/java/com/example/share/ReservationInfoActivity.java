package com.example.share;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class ReservationInfoActivity extends AppCompatActivity {

    private ImageView pay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_info);

        //Intent intent = getIntent();
        //String name = intent.getStringExtra("string");
       // Toast.makeText(getApplicationContext(),"name : "+name,Toast.LENGTH_SHORT).show();
        pay = (ImageView)findViewById(R.id.pay);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),PayActivity.class);
                startActivity(intent);
            }
        });
    }
}
