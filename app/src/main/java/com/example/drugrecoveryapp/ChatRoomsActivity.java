package com.example.drugrecoveryapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drugrecoveryapp.adapter.SearchFriendAdapter;
import com.example.drugrecoveryapp.entity.MessageModel;
import com.example.drugrecoveryapp.entity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ChatRoomsActivity extends AppCompatActivity {

    private RecyclerView chatRoomsRecyclerView;
    private ChatRoomsAdapter chatRoomsAdapter;
    private EditText SearchInputText;
private RecyclerView searchFriendRecyclerView;
    private SearchFriendAdapter searchFriendAdapter;
    private List<User> userList;

    private DatabaseReference usersRef;
    String currentUserUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_rooms);

        Button btnBackChatRoom = findViewById(R.id.btnBackChatRoom);
        btnBackChatRoom.setOnClickListener(v -> finish());

        chatRoomsRecyclerView = findViewById(R.id.chatRoomsRecyclerView);
        chatRoomsAdapter = new ChatRoomsAdapter(this);
        searchFriendRecyclerView = findViewById(R.id.searchFriendRecycleView);

        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        currentUserUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        userList = new ArrayList<>();
        searchFriendAdapter = new SearchFriendAdapter(userList);

        ImageButton searchButton = findViewById(R.id.search_people_friends_button);
        SearchInputText = findViewById(R.id.search_box_input);

        searchButton.setOnClickListener(v -> {
            String searchInputText = SearchInputText.getText().toString();
            SearchPeopleAndFriends(searchInputText);
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        chatRoomsRecyclerView.setHasFixedSize(true);
        chatRoomsRecyclerView.setLayoutManager(layoutManager);
        chatRoomsRecyclerView.setAdapter(chatRoomsAdapter);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        searchFriendRecyclerView.setHasFixedSize(true);
        searchFriendRecyclerView.setLayoutManager(layoutManager2);
        searchFriendRecyclerView.setAdapter(searchFriendAdapter);
        loadChatMessages();
    }


    private void SearchPeopleAndFriends(String searchInputText) {
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);

                    if (!currentUserUid.equals(user.getUid())) {
                        if (user.getUsername().toLowerCase().contains(searchInputText.toLowerCase()) ||
                                user.getEmail().toLowerCase().contains(searchInputText.toLowerCase()) ||
                                user.getUid().toLowerCase().contains(searchInputText.toLowerCase())) {
                            userList.add(user);
                        }
                    }
                }
                Collections.sort(userList, (user1, user2) -> user1.getUsername().compareToIgnoreCase(user2.getUsername()));
                searchFriendAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

    private void loadChatMessages() {
        DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference("Messages").child(currentUserUid);

        messagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<User> chatUsers = new ArrayList<>();

                for (DataSnapshot chatSnapshot : dataSnapshot.getChildren()) {
                    // Adjust the path based on your actual structure
                    String chatUserId = chatSnapshot.getKey();
                    String lastMessage = "";
                    long lastMessageTimestamp = 0;

                    for (DataSnapshot messageSnapshot : chatSnapshot.getChildren()) {
                        // Assuming "timeStamp" is the correct field name in your database
                        Long timestamp = messageSnapshot.child("timeStamp").getValue(Long.class);
                        String message = messageSnapshot.child("message").getValue(String.class);

                        if (timestamp != null && message != null) {
                            // Check if the current message is the latest one
                            if (timestamp > lastMessageTimestamp) {
                                lastMessage = message;
                                lastMessageTimestamp = timestamp;
                            }
                        }
                    }

                    // Create a User object with the chatUserId and the latest message
                    User chatUser = new User();
                    chatUser.setUserid(chatUserId);
                    chatUser.setLastMessage(lastMessage);
                    chatUser.setLastMessageTimestamp(lastMessageTimestamp);

                    // Add the chatUser to the list
                    chatUsers.add(chatUser);
                    Log.d("ChatRoomsActivity", "Number of chatUsers: " + chatUsers.size());
                }

                // Log the chatUsers to ensure it contains data
                Log.d("ChatRoomsActivity", "ChatUsers: " + chatUsers);

                // Set up the chatRoomsAdapter with the updated chatUsers list
                chatRoomsAdapter.setUserList(chatUsers);
                chatRoomsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }


}