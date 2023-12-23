package com.example.drugrecoveryapp;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class DiscussionGroupActiivty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion_group_actiivty);
        Button btnBackDiscussionGroups = findViewById(R.id.btnBackDiscussionGroups);
        btnBackDiscussionGroups.setOnClickListener(v -> finish());
    }
}