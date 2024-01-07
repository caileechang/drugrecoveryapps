package com.example.drugrecoveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.drugrecoveryapp.adapter.RequestAdapter;
import com.example.drugrecoveryapp.entity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Request_Activity extends AppCompatActivity {

    private RecyclerView requestRecyclerView;
    private RequestAdapter requestAdapter;
    private DatabaseReference friendsRef;
    private DatabaseReference usersRef;
    private String currentUserId;
    private List<User> requestList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        // Find the Toolbar
        Toolbar toolbar = findViewById(R.id.TBRequest);

        // Set the Toolbar as the ActionBar
        setSupportActionBar(toolbar);

        // Enable the back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        // Initialize RecyclerView
        requestRecyclerView = findViewById(R.id.rv_request_list);
        requestRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        requestAdapter = new RequestAdapter(requestList, this);
        requestRecyclerView.setAdapter(requestAdapter);

        // Get the current user ID
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Get the Firebase references
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        // Retrieve the friend request list of the current user from the database
        retrieveFriendRequest();
    }

    private void retrieveFriendRequest() {

        // Get the reference to the FriendRequests node for the current user
        DatabaseReference friendRequestRef = FirebaseDatabase.getInstance().getReference().child("FriendRequests").child(currentUserId);

        friendRequestRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                requestList.clear();
                for (DataSnapshot friendRequestSnapshot : dataSnapshot.getChildren()) {
                    // Get the friend request UID and request type
                    String friendRequestUid = friendRequestSnapshot.getKey();
                    String request_type = friendRequestSnapshot.child("request_type").getValue(String.class);

                    if (request_type.equals("received")) {
                        // If the request type is "received", retrieve the user information
                        usersRef.child(friendRequestUid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                User request = dataSnapshot.getValue(User.class);
                                requestList.add(request);
                                requestAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Handle the error
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
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