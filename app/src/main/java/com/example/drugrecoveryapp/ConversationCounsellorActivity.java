package com.example.drugrecoveryapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ConversationCounsellorActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText messageEditText;
    private ImageView sendButton;

    private String counselorUid;
    private String counselorName;
    private String currentUserUid;

    private DatabaseReference databaseReference;
    private List<ChatMessage> chatMessageList;
    private ChatMessageAdapter chatMessageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_counsellor);
        Button btnBackChatRoom=findViewById(R.id.btnBackChatRoom);
        btnBackChatRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Get counselor details from intent
        counselorUid = getIntent().getStringExtra("counselorUid");
        counselorName = getIntent().getStringExtra("counselorName");

        // Initialize UI elements
        recyclerView = findViewById(R.id.conversation_recycler);
        messageEditText = findViewById(R.id.message_edit);
        sendButton = findViewById(R.id.sendBtn);
        // Set counselor's name in the TextView
        TextView nameChatTextView = findViewById(R.id.name_chat);
        nameChatTextView.setText(counselorName);

        // Initialize Firebase
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUserUid = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("chats");

        // Initialize chat message list and adapter
        chatMessageList = new ArrayList<>();
        chatMessageAdapter = new ChatMessageAdapter(chatMessageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatMessageAdapter);

        // Set up send button click listener
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        // Set up Firebase child event listener
        databaseReference.child(currentUserUid).child(counselorUid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                chatMessageList.add(chatMessage);
                chatMessageAdapter.notifyItemInserted(chatMessageList.size() - 1);
            }


            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                // Handle child changes
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                // Handle child removal
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {
                // Handle child movement
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }

    private void sendMessage() {
        String messageText = messageEditText.getText().toString().trim();
        if (!messageText.isEmpty()) {
            ChatMessage chatMessage = new ChatMessage(currentUserUid, messageText);
            databaseReference.child(currentUserUid).child(counselorUid).push().setValue(chatMessage);
            databaseReference.child(counselorUid).child(currentUserUid).push().setValue(chatMessage);
            messageEditText.setText("");
        }
    }
}
