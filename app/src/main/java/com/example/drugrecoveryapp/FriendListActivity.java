package com.example.drugrecoveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.drugrecoveryapp.adapter.FriendsAdapter;
import com.example.drugrecoveryapp.entity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FriendListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FriendsAdapter adapter;
    private List<User> friendList;
    private DatabaseReference usersRef;
    DatabaseReference friendsRef;
    private FirebaseAuth mAuth;
    String currentUser;
    TextView numberOfFriends;
    int friendCount;

    public FriendListActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        // Initialize Firebase references
        friendsRef = FirebaseDatabase.getInstance().getReference().child("Friends");
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        recyclerView = findViewById(R.id.my_friend_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        friendList = new ArrayList<>();
        adapter = new FriendsAdapter(friendList, this);
        recyclerView.setAdapter(adapter);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid();

        numberOfFriends = findViewById(R.id.numberOfFriends);

        // Retrieve the friend list of the current user from the database
        retrieveFriendList();

        friendsRef.child(currentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    friendCount = (int) snapshot.getChildrenCount();
                    numberOfFriends.setText("(" + Integer.toString(friendCount) + ")");

                } else
                    numberOfFriends.setText("(0)");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error
            }
        });

    }

    // Method to retrieve the friend list of the current user from the database
    private void retrieveFriendList() {

        friendsRef.child(currentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                friendList.clear();

                // Iterate over each friend UID in the snapshot
                for (DataSnapshot friendSnapshot : dataSnapshot.getChildren()) {
                    String friendUid = friendSnapshot.getKey();

                    usersRef.child(friendUid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // Retrieve the friend user object from the database
                            User friend = dataSnapshot.getValue(User.class);
                            friendList.add(friend);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle the error
                        }
                    });
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
            }
        });
    }
}