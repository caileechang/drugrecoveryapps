package com.example.drugrecoveryapp;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;


import androidx.appcompat.widget.Toolbar;

import com.example.drugrecoveryapp.adapter.RewardAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Reward extends AppCompatActivity {

    private Button btn1, btn2, btn3;
    private DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
    private DatabaseReference currentUserRef;
    private String currentUserId;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);

        Toolbar TBReward = findViewById(R.id.TBReward);
        setSupportActionBar(TBReward);

        // Enable the back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        actionBar.setTitle("Your Rewards");

        currentUserId = mAuth.getCurrentUser().getUid();

        currentUserRef = userRef.child(currentUserId);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(currentUserId).hasChild("totalTime")) {
                    String totalRecoveryTime = snapshot.child(currentUserId).child("totalTime").getValue().toString();
                    Long totalTime = Long.parseLong(totalRecoveryTime);
                    GridView gridViewReward = findViewById(R.id.GVReward);
                    RewardAdapter adapter = new RewardAdapter(Reward.this, 12, totalTime);
                    gridViewReward.setAdapter(adapter);
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
