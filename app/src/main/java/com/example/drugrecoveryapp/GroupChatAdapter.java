package com.example.drugrecoveryapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.drugrecoveryapp.entity.MessageModel;
import com.example.drugrecoveryapp.entity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class GroupChatAdapter extends ArrayAdapter<MessageModel> {

    private final Context context;
    private final List<MessageModel> messagesList;
    private final DatabaseReference usersRef;

    public GroupChatAdapter(Context context, List<MessageModel> messagesList) {
        super(context, 0, messagesList);
        this.context = context;
        this.messagesList = messagesList;
        this.usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.item_group_chat, parent, false);
        }

        MessageModel currentMessage = getItem(position);

        if (currentMessage != null) {
            TextView senderTextView = listItemView.findViewById(R.id.senderTextView);
            TextView messageTextView = listItemView.findViewById(R.id.messageTextView);
            TextView timeTextView = listItemView.findViewById(R.id.timeTextView);

            // Fetch the sender's name based on their UID
            fetchSenderName(currentMessage.getSenderId(), senderTextView);

            messageTextView.setText(currentMessage.getMessage());
            String formattedTime = formatDate(currentMessage.getTimestamp());
            timeTextView.setText(formattedTime);
        }

        return listItemView;
    }

    private void fetchSenderName(String senderId, TextView senderTextView) {
        usersRef.child(senderId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User sender = dataSnapshot.getValue(User.class);
                    if (sender != null) {
                        // Set the sender's name in the TextView
                        senderTextView.setText(sender.getUsername());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

    private String formatDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault());
        return sdf.format(timestamp);
    }
}
