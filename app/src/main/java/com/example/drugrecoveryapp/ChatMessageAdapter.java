package com.example.drugrecoveryapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ViewHolder> {

    private List<ChatMessage> chatMessages;

    public ChatMessageAdapter(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_room, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatMessage chatMessage = chatMessages.get(position);

        // Fetch sender's username from Firebase using UID
        fetchUsername(chatMessage.getSenderUid(), holder.senderUsername);

        holder.messageTextView.setText(chatMessage.getMessage());
        // Display date and time
        Date date = new Date(chatMessage.getTimestamp());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        holder.timestamp.setText(sdf.format(date));

        // Load sender's profile picture
        loadProfilePicture(chatMessage.getSenderUid(), holder.userProfileImageView);

        // Adjust layout based on sender or receiver
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (chatMessage.getSenderUid().equals(currentUser.getUid())) {
            holder.messageTextView.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.black));
            holder.main.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.light_blue_1));
        } else {
            holder.main.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.light_grey_1));
            holder.messageTextView.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.black));
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        TextView senderUsername;
        LinearLayout main;
        TextView timestamp;
        ImageView userProfileImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.chatRoomLastMessage);
            senderUsername = itemView.findViewById(R.id.chatRoomUserName);
            main = itemView.findViewById(R.id.mainMessageLayout);
            timestamp = itemView.findViewById(R.id.chatRoomTimestamp);
            userProfileImageView = itemView.findViewById(R.id.IVUserProfilePicture);
        }
    }

    private void fetchUsername(String uid, TextView usernameTextView) {
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users");
        userReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String username = dataSnapshot.child("username").getValue(String.class);
                    usernameTextView.setText(username);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });
    }

    private void loadProfilePicture(String userId, ImageView imageView) {
        // Retrieve the user's profile picture URL from your database or storage
        String profilePictureUrl = getProfilePictureUrl(userId);

        // Use Picasso to load the image into the ImageView
        Picasso.get().load(profilePictureUrl)
                .placeholder(R.drawable.placeholder) // Placeholder image while loading
                .into(imageView);
    }
    private String getProfilePictureUrl(String userId) {
        // Implement the logic to get the profile picture URL for the user with userId
        // For example, retrieve it from Firebase database
        // Return the URL string
        return "https://example.com/profile_picture.jpg"; // Replace this with your actual logic
    }
}
