package com.example.share.Chatting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.share.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import java.util.ArrayList;

public class ChatListActivity extends AppCompatActivity {

    private ListView chatlist_view;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private ArrayList<ChatroomlistItem> mItems;
    private SharedPreferences pref;

    //mongoDB
    private String MongoDB_IP = "15.164.51.129";
    private int MongoDB_PORT = 27017;
    private String DB_NAME = "local";
    private String ITEM_COLLECTION = "items";
    private String USER_COLLECTION= "users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        chatlist_view = (ListView)findViewById(R.id.chatroomlistview);
        mItems = new ArrayList<ChatroomlistItem>();
        showChatList();

    }

    private void showChatList() {
        // 리스트 어댑터 생성 및 세팅
        final ChatroomAdapter adapter
                = new ChatroomAdapter(this, mItems);

        // 데이터 받아오기 및 어댑터 데이터 추가 및 삭제 등..리스너 관리

        databaseReference.child("chat").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("LOG", "dataSnapshot.getKey() : " + dataSnapshot.getKey());
                ChatroomlistItem chatroomlistitem = dataSnapshot.getValue(ChatroomlistItem.class);
                Log.d("jihye","add 전 : "+adapter.getCount());

                int idx = dataSnapshot.getKey().indexOf("-");

                String email1 = dataSnapshot.getKey().substring(0,idx).concat(".com");
                String email2 = dataSnapshot.getKey().substring(idx+1).concat(".com");

                MongoClient mongoClient = new MongoClient(new ServerAddress(MongoDB_IP, MongoDB_PORT)); // failed here?
                DB db = mongoClient.getDB(DB_NAME);
                DBCollection collection = db.getCollection( ITEM_COLLECTION);
                DBCollection collection2 = db.getCollection(USER_COLLECTION);
                BasicDBObject query2 = new BasicDBObject();

                pref = getSharedPreferences("pref", AppCompatActivity.MODE_PRIVATE);
                String user_email = pref.getString("user_email",null);

                if(user_email.equals(email1) ){
                    query2.put("email", email2);
                    DBObject dbObj = collection2.findOne(query2);
                    adapter.add(dbObj.get("name").toString(),chatroomlistitem.getMessage());
                    Log.d("jihye","add 후 : "+adapter.getCount());
                    adapter.notifyDataSetChanged();
                    chatlist_view.setAdapter(adapter);
                }else if(user_email.equals(email2)){
                    query2.put("email", email1);
                    DBObject dbObj = collection2.findOne(query2);
                    adapter.add(dbObj.get("name").toString(),chatroomlistitem.getMessage());
                    Log.d("jihye","add 후 : "+adapter.getCount());
                    adapter.notifyDataSetChanged();
                    chatlist_view.setAdapter(adapter);
                }


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void addChatroom(DataSnapshot datasnapshot, ChatroomAdapter adapter){
        ChatroomlistItem chatroomlist = datasnapshot.getValue(ChatroomlistItem.class);
        adapter.add(chatroomlist.getroomName(),chatroomlist.getMessage());
    }
}