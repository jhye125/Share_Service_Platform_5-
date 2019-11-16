package com.example.share.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.MenuItem;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.share.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;


    private ImageButton etcButton;
    private ImageButton placeButton;
    private ImageButton toolButton;
    private ImageButton sound_equipmentButton;
    private ImageButton medical_equimentButton;
    private ImageButton baby_goodsButton;

    private String UserEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent homeintent = getIntent();

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        UserEmail = pref.getString("user_email",null);

        etcButton = (ImageButton)findViewById(R.id.etc);
        placeButton = (ImageButton)findViewById(R.id.place);
        toolButton = (ImageButton)findViewById(R.id.tools);
        sound_equipmentButton = (ImageButton)findViewById(R.id.music);
        medical_equimentButton = (ImageButton)findViewById(R.id.medical);
        baby_goodsButton = (ImageButton)findViewById(R.id.child);


        etcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ItemListActivity.class);
                intent.putExtra("category", "etc");
                startActivity(intent);
            }

        });
        placeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ItemListActivity.class);
                intent.putExtra("category", "place");
                startActivity(intent);
            }

        });
        toolButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ItemListActivity.class);
                intent.putExtra("category", "tool");
                startActivity(intent);
            }

        });
        sound_equipmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ItemListActivity.class);
                intent.putExtra("category", "sound_equipment");
                startActivity(intent);
            }

        });
        medical_equimentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ItemListActivity.class);
                intent.putExtra("category", "medical_equipment");
                startActivity(intent);
            }

        });
        baby_goodsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ItemListActivity.class);
                intent.putExtra("category", "baby_goods");
                startActivity(intent);
            }

        });

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);  //bottom navigation bar 에서 메뉴 클릭 리스너

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch(item.getItemId()) {
                            case R.id.navigation_menu1:
                                Intent ChatList_Intents = new Intent(getApplicationContext(), ChatListActivity.class);  //메시지 activity 실행 수정필요
                                startActivity(ChatList_Intents);
                                break;
                            case R.id.navigation_menu2:
                                Intent BucketList_Intents = new Intent(getApplicationContext(), BucketListActivity.class);  //찜목록 activity 실행 수정필요
                                startActivity(BucketList_Intents);
                                break;
                            case R.id.navigation_menu3:

                                break;
                            case R.id.navigation_menu4:

                                break;
                            case R.id.navigation_menu5:
                                Intent Share_Intents = new Intent(getApplicationContext(), MyPageActivity.class);
                                startActivity(Share_Intents);
                                break;
                        }
                        return true;
                    }
                });
    }

}