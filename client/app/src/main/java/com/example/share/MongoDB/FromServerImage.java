package com.example.share.MongoDB;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class FromServerImage {
    String FilePath;
    Bitmap image;

    public FromServerImage(){

    }
    public Bitmap getImage(String newFilePath){
        this.FilePath = newFilePath;
        FromImage();
        return image;
    }
    public void FromImage(){

        try {
            //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
            HttpURLConnection con = null;
            BufferedReader reader = null;
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("image_path", FilePath);

            //URL url = new URL("http://192.168.25.16:3000/users");
            URL url = new URL("http://ec2-15-164-51-129.ap-northeast-2.compute.amazonaws.com:3000/get_image");
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
            image = BitmapFactory.decodeStream(stream);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
