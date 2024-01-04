package com.example.drugrecoveryapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.drugrecoveryapp.entity.GroupChatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class DiscussionGroupActiivty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion_group_actiivty);

        setupGroupListView();
    }

    private void setupGroupListView() {
        ListView listViewGroups = findViewById(R.id.listViewGroups);

        // Example list of discussion groups
        ArrayList<String> discussionGroups = new ArrayList<>(Arrays.asList(
                "Serenity Seekers: Meditation Meetup", "Curious Conversations Collection",
                "Together We Share: A Support Circle"
        ));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, discussionGroups
        );

        listViewGroups.setAdapter(adapter);

        listViewGroups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
                String selectedGroup = discussionGroups.get(position);

                // Open GroupChatActivity and pass the group name
                Intent intent = new Intent(DiscussionGroupActiivty.this, GroupChatActivity.class);
                intent.putExtra("groupId", selectedGroup);
                intent.putExtra("groupName", selectedGroup);
                startActivity(intent);

                // Perform an action when a group is clicked
                Toast.makeText(DiscussionGroupActiivty.this, "Joined group: " + selectedGroup, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
