package com.example.drugrecoveryapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CommunityForumActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_forum);

        Button btnBackCommunityForum = findViewById(R.id.btnBackCommunityForum);
        btnBackCommunityForum.setOnClickListener(v -> finish());

    }
    public void btnCreatePostsOnClick(View view){
        Intent intent=new Intent(getApplicationContext(),CreatePostsActivity.class);
        startActivity(intent);
    }


}

