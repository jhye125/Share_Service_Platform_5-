package com.example.share;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class PayActivity extends AppCompatActivity {

    private ImageView complete;
    ViewPager cardlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        complete = (ImageView)findViewById(R.id.complete);
        cardlist = (ViewPager)findViewById(R.id.cardlist);
        /*cardlist.setOnItemClickListener(new ClickableViewPager.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(getApplicationContext(),"kkkk",Toast.LENGTH_SHORT).show();
            }
        });*/
        cardlist.setAdapter(new pagerAdapter(getSupportFragmentManager()));

        cardlist.setCurrentItem(0);

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ChattingActivity.class);
                startActivity(intent);
            }
        });

    }

    View.OnClickListener movePageListener = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            int tag = (int)v.getTag();
            cardlist.setCurrentItem(tag);
        }
    };

    private class pagerAdapter extends FragmentStatePagerAdapter
    {
        public pagerAdapter(android.support.v4.app.FragmentManager fm)
        {
            super(fm);
        }

        public android.support.v4.app.Fragment getItem(int position)
        {
            switch(position)
            {
                case 0:
                    return new FirstCard();
                case 1:
                    return new AddCard();
                default:
                    return null;
            }
        }

        public int getCount()
        {
            return 2;
        }



    }

}
