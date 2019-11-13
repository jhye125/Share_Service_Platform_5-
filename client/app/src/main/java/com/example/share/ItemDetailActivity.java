package com.example.share;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.example.share.Data.Item;

import androidx.appcompat.app.AppCompatActivity;

public class ItemDetailActivity extends AppCompatActivity {

    ImageButton pay_button;
    private FromServerImage newImage = new FromServerImage();

    Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemdetail);

        Intent intent = getIntent();

        //get all the data passed
        item = (Item)intent.getSerializableExtra("item_object");

        //get all the layout
        ImageView item_image = (ImageView)findViewById(R.id.item_detail_item_image);
        TextView item_name = (TextView)findViewById(R.id.item_detail_item_name);
        TextView item_price_per_day = (TextView)findViewById(R.id.item_detail_item_price);
        //item detail, TODO
        TextView item_detail = (TextView)findViewById(R.id.item_detail_item_detail);
        TextView item_location = (TextView)findViewById(R.id.item_detail_item_location);
        TextView item_category = (TextView)findViewById(R.id.item_detail_item_category);
        //Owner info, TODO
        ImageView owner_image = (ImageView)findViewById(R.id.item_detail_owner_image);
        TextView owner_name = (TextView)findViewById(R.id.item_detail_owner_name);
        TextView owner_rating = (TextView)findViewById(R.id.item_detail_owner_rating);

        //set all the layout contents
        item_image.setImageBitmap(newImage.getImage(item.getFilepath()));
        item_name.setText(item.getItem_name());
        item_price_per_day.setText(item.getItem_price_per_day());
        //set this according to the database values, TODO
        item_detail.setText("굿노트 어플 사용 가능\n" +
                "프로크리에이트 어플 사용 가능\n" +
                "애플 펜슬도 함께 빌려 드립니다\n");
        item_location.setText("거래 지역: "+"대구광역시 북구 침산동");
        item_category.setText("카테고리"+"기타");
        owner_image.setImageResource(R.drawable.owner_sample);
        owner_name.setText("지혜");
        owner_rating.setText("4.5 / 5");

        pay_button = findViewById(R.id.pay);

        pay_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ItemDetailActivity.this, PayActivity.class);
                intent.putExtra("item_object",item);
                startActivity(intent);
            }

        });

        return;
    }
}
