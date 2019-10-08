package com.example.share;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

    }

    public void ReserveTest(View view){ // reservetest 클릭시

        Intent intents = new Intent(this, ReservationInfoActivity.class);
        startActivity(intents);

    }
}