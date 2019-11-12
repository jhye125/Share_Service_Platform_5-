package com.example.share;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class AddCardDetailActivity extends AppCompatActivity {

    EditText cardnum1;
    EditText cardnum2;
    EditText cardnum3;
    EditText cardnum4;
    ImageView add_btn;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcarddetail);

        cardnum1 = (EditText)findViewById(R.id.cardnumber1);
        cardnum2 = (EditText)findViewById(R.id.cardnumber2);
        cardnum3 = (EditText)findViewById(R.id.cardnumber3);
        cardnum4 = (EditText)findViewById(R.id.cardnumber4);
        add_btn = (ImageView)findViewById(R.id.add_btn);

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cardnumber = cardnum1.getText().toString()+cardnum2.getText().toString()+cardnum3.getText().toString()+cardnum4.getText().toString();
                Log.d(this.getClass().getName(),cardnumber);
            }
        });

    }
}
