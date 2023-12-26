package com.example.drugrecoveryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.drugrecoveryapp.entity.Post;
import com.example.drugrecoveryapp.entity.PostAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CommunityForumActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private RecyclerView recyclerViewPosts;
    private PostAdapter postAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_forum);

        Button btnBackCommunityForum = findViewById(R.id.btnBackCommunityForum);
        btnBackCommunityForum.setOnClickListener(v -> finish());

        databaseReference = FirebaseDatabase.getInstance().getReference("posts");

        recyclerViewPosts = findViewById(R.id.recyclerViewPosts);
        recyclerViewPosts.setLayoutManager(new LinearLayoutManager(this));
        postAdapter = new PostAdapter(new ArrayList<>());
        recyclerViewPosts.setAdapter(postAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Post> postList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Post post = dataSnapshot.getValue(Post.class);
                    postList.add(post);
                }

                postAdapter.setList(postList);
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    public void btnCreatePostsOnClick(View view) {
        Intent intent = new Intent(getApplicationContext(), CreatePostsActivity.class);
        startActivity(intent);
    }
}
