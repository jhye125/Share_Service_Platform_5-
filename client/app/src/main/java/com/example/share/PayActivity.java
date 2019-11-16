package com.example.share;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.share.Chatting.ChattingActivity;
import com.example.share.Data.Item;

public class PayActivity extends AppCompatActivity {

    private ImageView complete;
    ViewPager cardlist;
    Item item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        Intent intent = getIntent();

        //get all the data passed
        item = (Item)intent.getSerializableExtra("item_object");

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
                Intent intent = new Intent(getApplicationContext(), ChattingActivity.class);
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
        public pagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        public Fragment getItem(int position)
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
