package com.example.share.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.share.R;

public class ReservationInfoActivity extends AppCompatActivity {

    private ImageView pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_info);

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
