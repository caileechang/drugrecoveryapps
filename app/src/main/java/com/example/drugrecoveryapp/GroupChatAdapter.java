package com.example.drugrecoveryapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
            ImageView userProfilePicture = listItemView.findViewById(R.id.IVUserProfilePicture);

            // Fetch the sender's name and profile picture based on their UID
            fetchSenderDetails(currentMessage.getSenderId(), senderTextView, userProfilePicture);

            messageTextView.setText(currentMessage.getMessage());
            String formattedTime = formatDate(currentMessage.getTimestamp());
            timeTextView.setText(formattedTime);
        }

        return listItemView;
    }

    private void fetchSenderDetails(String senderId, TextView senderTextView, ImageView userProfilePicture) {
        usersRef.child(senderId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User sender = dataSnapshot.getValue(User.class);
                    if (sender != null) {
                        // Set the sender's name in the TextView
                        senderTextView.setText(sender.getUsername());

                        // Check if the user has a profile picture
                        if (sender.getProfilePicture() != null && isBase64(sender.getProfilePicture())) {
                            // Decode base64 and set the profile picture
                            Bitmap profilePictureBitmap = base64ToBitmap(sender.getProfilePicture());
                            userProfilePicture.setImageBitmap(profilePictureBitmap);
                        } else {
                            // Set a default profile picture if the user doesn't have one
                            userProfilePicture.setImageResource(R.drawable.human_avatar_create_posts);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
                Log.e("GroupChatAdapter", "Error fetching sender details: " + error.getMessage());
            }
        });
    }

    private boolean isBase64(String str) {
        if (str == null) {
            return false;  // Handle null strings according to your use case
        }
        try {
            Base64.decode(str, Base64.DEFAULT);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private Bitmap base64ToBitmap(String base64Image) {
        byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    private String formatDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault());
        return sdf.format(timestamp);
    }
}
