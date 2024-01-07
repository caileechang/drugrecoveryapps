package com.example.drugrecoveryapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiscussionGroupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion_group_actiivty);
        // Find the Toolbar
        Toolbar toolbar = findViewById(R.id.TBDiscussionGroup);

        // Set the Toolbar as the ActionBar
        setSupportActionBar(toolbar);

        // Enable the back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        setupGroupRecyclerView();
    }

    private void setupGroupRecyclerView() {
        RecyclerView recyclerViewGroups = findViewById(R.id.discussiongroupRecycleView);

        // Example list of discussion groups
        List<String> discussionGroups = new ArrayList<>(Arrays.asList(
                "Serenity Seekers: Meditation Meetup",
                "Curious Conversations Collection",
                "Together We Share: A Support Circle"
        ));

        DiscussionGroupAdapter adapter = new DiscussionGroupAdapter(this, discussionGroups);
        recyclerViewGroups.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewGroups.setAdapter(adapter);
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
