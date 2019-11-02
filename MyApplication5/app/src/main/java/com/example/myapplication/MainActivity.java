package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // Intent intent = new Intent(getApplicationContext(),PayActivity.class);
        Intent intent = new Intent(getApplicationContext(),RegisterItemActivity.class);
        //Intent intent = new Intent(getApplicationContext(),MapsMarkerActivity.class);
        startActivity(intent);

    }
}
