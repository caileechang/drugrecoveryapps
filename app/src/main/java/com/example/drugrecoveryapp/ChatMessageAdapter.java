package com.example.drugrecoveryapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drugrecoveryapp.entity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
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

    private void loadProfilePicture(String userId, ImageView userProfileImageView) {
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users");
        userReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        // Assuming you have a method to get the profile picture URL from the User object
                        String profilePictureUrl = user.getProfilePicture();

                        if (profilePictureUrl != null) {
                            if (isBase64(profilePictureUrl)) {
                                Bitmap bitmap = base64ToBitmap(profilePictureUrl);
                                userProfileImageView.setImageBitmap(bitmap);
                            } else {
                                Picasso.get().load(profilePictureUrl)
                                        .placeholder(R.drawable.placeholder)
                                        .error(R.drawable.human_avatar_create_posts)
                                        .into(userProfileImageView);
                            }
                        } else {
                            // Handle the case where profilePictureUrl is null
                            Log.e("ProfilePicture", "Profile picture URL is null");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });
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

    private void loadProfilePicture(User user, ImageView imageView) {
        String profilePicture = user.getProfilePicture();


        if (profilePicture != null) {
            if (isBase64(profilePicture)) {
                Bitmap bitmap = base64ToBitmap(profilePicture);
                imageView.setImageBitmap(bitmap);
            } else {
                Picasso.get().load(profilePicture)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.human_avatar_create_posts)
                        .into(imageView);
            }
        } else {
            // Handle the case where profilePicture is null
            Log.e("ProfilePicture", "Profile picture URL is null");
        }
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
}