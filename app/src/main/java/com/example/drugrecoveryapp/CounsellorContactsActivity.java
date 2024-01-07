package com.example.drugrecoveryapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class CounsellorContactsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counsellor_contacts);
        // Find the Toolbar
        Toolbar toolbar = findViewById(R.id.TBCounsellor);

        // Set the Toolbar as the ActionBar
        setSupportActionBar(toolbar);

        // Enable the back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Handle the back button click
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}