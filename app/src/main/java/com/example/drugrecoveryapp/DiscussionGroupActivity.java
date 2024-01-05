package com.example.drugrecoveryapp;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
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
        Button btnBackDiscussionGroup = findViewById(R.id.btnBackDiscussionGroups);
        btnBackDiscussionGroup.setOnClickListener(v -> finish());

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
}
