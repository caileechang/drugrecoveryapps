package com.example.drugrecoveryapp;

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
        btnChat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Button btnChat2 = findViewById(R.id.btnChat1);
        btnChat2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}