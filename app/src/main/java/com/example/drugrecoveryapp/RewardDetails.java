package com.example.drugrecoveryapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

public class RewardDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_details);

        Toolbar TBReward = findViewById(R.id.TBRewardDetails);
        setSupportActionBar(TBReward);

        // Enable the back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            TBReward.setTitleTextColor(Color.WHITE);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_keyboard_backspace_24); // Set your white back arrow icon here
        }
        actionBar.setTitle("Your Rewards");

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