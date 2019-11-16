package com.example.share.Chatting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.share.R;
import com.example.share.RegisterReviewActivity;

public class ChattingActivity extends AppCompatActivity {

    ImageView btn_return;
    ImageView btn_review;
    Button btn_send;
    private final int GET_REVIEW = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        btn_return = (ImageView)findViewById(R.id.return_btn);
        btn_review = (ImageView)findViewById(R.id.review_btn);
        btn_send = (Button)findViewById(R.id.send);


        /*if() // 빌린사람아니라 문의하는 사람이면~
        {
            btn_return.setVisibility(View.INVISIBLE);
            btn_review.setVisibility(View.INVISIBLE);
        }*/

        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        btn_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterReviewActivity.class);
                startActivity(intent);
            }
        });
    }
}
