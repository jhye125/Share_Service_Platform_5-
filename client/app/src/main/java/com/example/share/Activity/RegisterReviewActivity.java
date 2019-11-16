package com.example.share.Activity;

import android.media.Image;
import android.media.Rating;
import android.os.Bundle;

import com.example.share.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

public class RegisterReviewActivity extends AppCompatActivity {

    RatingBar review_star;
    EditText review_content;
    ImageView btn_register;
    float send_rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_review);

        review_star = (RatingBar)findViewById(R.id.register_review_star);
        review_content = (EditText)findViewById(R.id.register_review_content);
        btn_register = (ImageView)findViewById(R.id.btnregister_review);

        review_star.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                send_rate = rating;
            }
        });


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = review_content.getText().toString();

                //서버에 보내주셈 유저정보.별점.후기내용
            }
        });
    }

}