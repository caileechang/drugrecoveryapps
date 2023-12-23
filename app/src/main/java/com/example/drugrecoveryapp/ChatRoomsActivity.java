package com.example.drugrecoveryapp;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ChatRoomsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_rooms);

        Button btnBackChatRoom = findViewById(R.id.btnBackChatRoom);
        btnBackChatRoom.setOnClickListener(v -> finish());
    }
}