package com.example.drugrecoveryapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CounsellorContactsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counsellor_contacts);
        Button btnBackCounsellorContacts = findViewById(R.id.btnBackCounsellorContacts);
        btnBackCounsellorContacts.setOnClickListener(v -> finish());
        Button btnChat1 = findViewById(R.id.btnChat1);
        btnChat1.setOnClickListener(v -> openChatRoom("Counselor1UID", "Counsellor Judith"));

        Button btnChat2 = findViewById(R.id.btnChat2);
        btnChat2.setOnClickListener(v -> openChatRoom("Counselor2UID", "Counsellor Samia"));
    }
    private void openChatRoom(String counselorUid, String counselorName) {
        Intent intent = new Intent(this, ConversationCounsellorActivity.class);
        intent.putExtra("counselorUid", counselorUid);
        intent.putExtra("counselorName", counselorName);
        startActivity(intent);
    }
}