package com.example.drugrecoveryapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.drugrecoveryapp.databinding.ActivityConversationBinding;
import com.example.drugrecoveryapp.entity.ConversationAdapter;
import com.example.drugrecoveryapp.entity.MessageModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ConversationActivity extends AppCompatActivity {

    ActivityConversationBinding binding;
    String receiverId;
    DatabaseReference databaseReferenceSender;
    DatabaseReference databaseReferenceReceiver;
    String senderRoom, receiverRoom, receiverName;
    ConversationAdapter conversationAdapter;
    FirebaseUser currentUser;
    String key;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConversationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        receiverId = getIntent().getStringExtra("id");
        receiverName = getIntent().getStringExtra("name");
        senderRoom = FirebaseAuth.getInstance().getUid() + receiverId;
        receiverRoom = receiverId + FirebaseAuth.getInstance().getUid();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReferenceSender = FirebaseDatabase.getInstance().getReference("chats").child(senderRoom);
        databaseReferenceReceiver = FirebaseDatabase.getInstance().getReference("chats").child(receiverRoom);

        conversationAdapter = new ConversationAdapter(this);
        binding.conversationRecycler.setLayoutManager(new LinearLayoutManager(this));

        // Find the Toolbar
        Toolbar toolbar = findViewById(R.id.TBChat);

        // Set the Toolbar as the ActionBar
        setSupportActionBar(toolbar);




        // Set the text in the TextView to the receiver's name or ID
        if (receiverName != null && !receiverName.isEmpty()) {
            // Enable the back button
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(receiverName);
                actionBar.setDisplayHomeAsUpEnabled(true);
                toolbar.setTitleTextColor(Color.WHITE);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_keyboard_backspace_24); // Set your white back arrow icon here
            }

        } else {
            // If receiverName is not available, display receiverId
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(receiverId);
                actionBar.setDisplayHomeAsUpEnabled(true);
                toolbar.setTitleTextColor(Color.WHITE);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_keyboard_backspace_24); // Set your white back arrow icon here

            }
        }

        // Listen for new messages in real-time
        databaseReferenceSender.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                conversationAdapter.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MessageModel messageModel = dataSnapshot.getValue(MessageModel.class);
                    conversationAdapter.add(messageModel);
                    conversationAdapter.notifyItemInserted(conversationAdapter.getItemCount() - 1);
                }
                binding.conversationRecycler.setAdapter(conversationAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });

        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                String message = binding.messageEdit.getText().toString();
                binding.messageEdit.setText("");
                if (message.trim().length() > 0) {
                    sendMessage(message);
                }
            }
        });
    }

    private void sendMessage(String message) {
        key = databaseReferenceSender.push().getKey(); // Generate a unique key for the message
        MessageModel messageModel = new MessageModel(
                key,
                currentUser.getUid(),
                message,
                System.currentTimeMillis()
        );

        // Update sender's message list
        databaseReferenceSender.child(key).setValue(messageModel);

        DatabaseReference receiverReference = FirebaseDatabase.getInstance().getReference("chats").child(receiverRoom);
        if (receiverReference != null) {
            receiverReference.child(key).setValue(messageModel);
        }

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
