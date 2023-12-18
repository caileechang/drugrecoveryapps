package com.example.drugrecoveryapp;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CounsellorContactsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counsellor_contacts);
        Button btnBackCounsellorContacts = findViewById(R.id.btnBackCounsellorContacts);
        btnBackCounsellorContacts.setOnClickListener(v -> finish());
    }
}