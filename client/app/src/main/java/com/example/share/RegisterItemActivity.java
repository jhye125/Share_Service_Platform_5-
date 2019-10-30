package com.example.share;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterItemActivity extends AppCompatActivity {

    private static final String TAG_TEXT = "text";
    private static final String TAG_IMAGE = "image";
    private final int GET_GALLERY_IMAGE = 200;

    ImageView photo;
    ImageView category;
    EditText title;
    ImageView location;
    EditText price;
    EditText content;
    ImageView register;
    List<Map<String, Object>> categorylist;
    String send_category;


    int[] image = {R.drawable.select, R.drawable.select, R.drawable.select, R.drawable.select, R.drawable.select};
    String[] text = {"장소", "공구", "음향기기", "의료", "유아용품", "기타"};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_item);

        photo = (ImageView) findViewById(R.id.register_photo);
        category = (ImageView) findViewById(R.id.register_category);
        title = (EditText) findViewById(R.id.register_title);
        location = (ImageView) findViewById(R.id.register_location);
        content = (EditText) findViewById(R.id.register_content);
        price = (EditText) findViewById(R.id.register_price);
        register = (ImageView) findViewById(R.id.btnregister);

        categorylist = new ArrayList<>();

        for (int i = 0; i < image.length; i++) {
            Map<String, Object> categoryMap = new HashMap<>();
            categoryMap.put(TAG_IMAGE, image[i]);
            categoryMap.put(TAG_TEXT, text[i]);

            categorylist.add(categoryMap);

        }

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photosetting();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register_content_send();
            }
        });

    }

    public void photosetting()
    {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, GET_GALLERY_IMAGE);

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri selectedImageUri = data.getData();
            photo.setImageURI(selectedImageUri);


        }

    }

    public void register_content_send()
    {
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Log.d(this.getClass().getName(),send_category);

        try {
            //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("name",title.getText().toString());
            jsonObject.accumulate("price_per_date",price.getText().toString());
            jsonObject.accumulate("latitude","35.89270621122815");
            jsonObject.accumulate("longitude","128.62514225293165");
            jsonObject.accumulate("available_date_start","2019-10-29");
            jsonObject.accumulate("available_date_end","2019-11-04");
            jsonObject.accumulate("category",send_category);
            jsonObject.accumulate("contents",content.getText().toString());
            jsonObject.accumulate("owner_email","admin");
            jsonObject.accumulate("image_path","admin"+title.getText().toString()+"2019-10-29"+".png");

            HttpURLConnection con = null;
            BufferedReader reader = null;

            try{
                URL url = new URL("http://ec2-15-164-51-129.ap-northeast-2.compute.amazonaws.com:3000/item_insert");
                //연결을 함
                con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("POST");//POST방식으로 보냄
                con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
                con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송


                con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
                con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미
                con.connect();

                //서버로 보내기위해서 스트림 만듬
                OutputStream outStream = con.getOutputStream();
                //버퍼를 생성하고 넣음
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                writer.write(jsonObject.toString());
                writer.flush();
                writer.close();//버퍼를 받아줌

                //서버로 부터 데이터를 받음
                InputStream stream = con.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line = "";
                while((line = reader.readLine()) != null){
                    buffer.append(line);
                }
                Log.d("true or false : ",buffer.toString());


            } catch (MalformedURLException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(con != null){
                    con.disconnect();
                }
                try {
                    if(reader != null){
                        reader.close();//버퍼를 닫아줌
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void selectCategory(View v){
        CreateListDialog();
    }

    public void CreateListDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.alert_dialog, null);
        builder.setView(view);

        final ListView listview = (ListView)view.findViewById(R.id.listview_alterdialog_list);
        final AlertDialog dialog = builder.create();


        SimpleAdapter simpleAdapter = new SimpleAdapter(this, categorylist,
                R.layout.alert_dialog_row,
                new String[]{TAG_IMAGE, TAG_TEXT},
                new int[]{R.id.alertDialogItemImageView, R.id.alertDialogItemTextView});


        listview.setAdapter(simpleAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.d(this.getClass().getName(),text[position]);
                send_category = text[position];
                dialog.dismiss();
            }
        });

        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }

}
