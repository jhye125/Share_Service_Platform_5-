package com.example.share;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.support.annotation.NonNull;
import android.widget.ImageButton;

import com.example.share.Data.Item;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    private String Email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent homeintent = getIntent();

        Email = homeintent.getExtras().getString("Email");  //LoginActivity로 부터 email 읽어오기


        ImageButton etcButton = (ImageButton)findViewById(R.id.etc);

        etcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signupintent = new Intent(HomeActivity.this, ItemListActivity.class);
                startActivity(signupintent);
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

                                break;
                        }
                        return true;
                    }
                });
    }

    public void ReserveTest(View view){ // reservetest 클릭시

        Intent intents = new Intent(this, ReservationInfoActivity.class);
        startActivity(intents);

    }
}