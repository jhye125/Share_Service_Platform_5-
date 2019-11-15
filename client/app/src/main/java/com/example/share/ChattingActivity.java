package com.example.share;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ChattingActivity extends AppCompatActivity {

    ImageView btn_return;
    ImageView btn_review;
    private final int GET_REVIEW = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        btn_return = (ImageView)findViewById(R.id.return_btn);
        btn_review = (ImageView)findViewById(R.id.review_btn);

        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        btn_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RegisterReviewActivity.class);
                startActivity(intent);
            }
        });
    }
}
