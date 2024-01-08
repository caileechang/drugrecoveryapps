package com.example.drugrecoveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Report extends AppCompatActivity {

    TextView username, gender, email, country, date, totalTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        Toolbar TBReward = findViewById(R.id.TBReport);
        setSupportActionBar(TBReward);

        // Enable the back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            TBReward.setTitleTextColor(Color.WHITE);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_keyboard_backspace_24); // Set your white back arrow icon here

        }
        actionBar.setTitle("My Report");

        username = findViewById(R.id.R_usernameDisplay);
        email = findViewById(R.id.R_emailDisplay);
        gender = findViewById(R.id.R_genderDisplay);
        country = findViewById(R.id.R_countryDisplay);
        date = findViewById(R.id.R_startDateDisplay);
        totalTime = findViewById(R.id.R_totalTimeDisplay);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        String uid = auth.getCurrentUser().getUid();

        databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                username.setText(snapshot.child("username").getValue(String.class));
                email.setText(snapshot.child("email").getValue(String.class));
                country.setText(snapshot.child("countryName").getValue(String.class));
                gender.setText(snapshot.child("gender").getValue(String.class));
                date.setText(snapshot.child("date").getValue(String.class));

                Long totalRecoveryTime = snapshot.child("totalTime").getValue(Long.class);
                if (totalRecoveryTime != null) {
                    totalTime.setText(String.valueOf(totalRecoveryTime) + " hours");
                } else {
                    // Handle the case where "totalTime" is not available
                    totalTime.setText("no");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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