package com.example.share.Chatting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import com.example.share.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChattingActivity extends AppCompatActivity {


    private String CHAT_NAME;
    private String USER_NAME;

    private ListView chat_view;
    private EditText chat_edit;
    private Button chat_send;

    private String owner_name;
    private TextView chatroom_title;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private ArrayList<ChatlistItem> mItems;

    private SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        mItems = new ArrayList<ChatlistItem>();
        // 위젯 ID 참조
        chat_view = (ListView) findViewById(R.id.chattinglistView);
        chat_edit = (EditText) findViewById(R.id.chat_EditText);
        chat_send = (Button) findViewById(R.id.send);

        // 로그인 화면에서 받아온 채팅방 이름, 유저 이름 저장
        Intent intent = getIntent();
        owner_name =intent.getStringExtra("owner_name");
        //CHAT_NAME = intent.getStringExtra("chat_name");
        pref = getSharedPreferences("pref", AppCompatActivity.MODE_PRIVATE);

        USER_NAME = pref.getString("user_name",null);

        if(owner_name.compareTo(USER_NAME) == -1) {
            CHAT_NAME = owner_name+"-"+USER_NAME;
        }else{
            CHAT_NAME = USER_NAME+"-"+owner_name;
        }

        chatroom_title = (TextView)findViewById(R.id.chat_username);
        chatroom_title.setText(owner_name);
        // 채팅 방 입장
        openChat(CHAT_NAME);

        // 메시지 전송 버튼에 대한 클릭 리스너 지정
        chat_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chat_edit.getText().toString().equals(""))
                    return;

                ChatDTO chat = new ChatDTO(USER_NAME, chat_edit.getText().toString()); //ChatDTO를 이용하여 데이터를 묶는다.
                databaseReference.child("chat").child(CHAT_NAME).push().setValue(chat); // 데이터 푸쉬
                chat_edit.setText(""); //입력창 초기화

            }
        });
    }

    private void addMessage(DataSnapshot dataSnapshot, ChattingAdapter adapter) {
        ChatlistItem chatlistItem = dataSnapshot.getValue(ChatlistItem.class);
        Log.d("datasnapshot","chatistItem : "+chatlistItem.getUserName()+","+chatlistItem.getMessage());
        adapter.addItem(chatlistItem.getUserName(),chatlistItem.getMessage());
        Log.d("datasnapshot","chatistItem : "+adapter.getCount());
    }

   /* private void removeMessage(DataSnapshot dataSnapshot, ArrayAdapter<String> adapter) {
        ChatDTO chatDTO = dataSnapshot.getValue(ChatDTO.class);
        adapter.remove(chatDTO.getUserName() + " : " + chatDTO.getMessage());
    }*/

    private void openChat(String chatName) {
        // 리스트 어댑터 생성 및 세팅
        final ChattingAdapter adapter = new ChattingAdapter(this,mItems);

              /*  = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        chat_view.setAdapter(adapter);*/

        // 데이터 받아오기 및 어댑터 데이터 추가 및 삭제 등..리스너 관리
        databaseReference.child("chat").child(chatName).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("datasnapshot","data : "+dataSnapshot);
                addMessage(dataSnapshot, adapter);
                adapter.notifyDataSetChanged();
                Log.e("LOG", "s:"+s);
                try{
                    chat_view.setAdapter(adapter);
                }catch (NullPointerException ne){
                    ne.printStackTrace();
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //removeMessage(dataSnapshot, adapter);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });
    }
}