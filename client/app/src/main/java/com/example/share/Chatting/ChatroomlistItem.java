package com.example.share.Chatting;

import android.graphics.drawable.Drawable;

public class ChatroomlistItem {

    private Drawable icon;
    private String roomName;
    private String message;

    public ChatroomlistItem() {}

    public ChatroomlistItem(String roomName, String message) {
        this.roomName = roomName;
        this.message = message;

    }

    public void setUserName(String roomName) {
        this.roomName = roomName;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getroomName() {
        return roomName;
    }

    public String getMessage() {
        return message;
    }
    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
}
