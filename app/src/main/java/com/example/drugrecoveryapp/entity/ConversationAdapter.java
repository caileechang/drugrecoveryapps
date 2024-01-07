package com.example.drugrecoveryapp.entity;

import android.content.Context;
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

import com.example.drugrecoveryapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.MyViewHolder> {

    private Context context;
    private List<MessageModel> messageModelList;
    private DatabaseReference userReference;

    public ConversationAdapter(Context context) {
        this.context = context;
        messageModelList = new ArrayList<>();
        userReference = FirebaseDatabase.getInstance().getReference("Users");
    }

    public void add(MessageModel messageModel) {
        messageModelList.add(messageModel);
        notifyItemInserted(messageModelList.size() - 1);
    }

    public void clear() {
        messageModelList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationAdapter.MyViewHolder holder, int position) {
        MessageModel messageModel = messageModelList.get(position);

        // Fetch sender's username from Firebase using UID
        fetchUsername(messageModel.getSenderId(), holder.senderUsername);

        holder.msg.setText(messageModel.getMessage());
        loadProfilePicture(messageModel.getSenderId(), holder.userProfileImageView);
        // Display date and time
        Date date = new Date(messageModel.getTimestamp());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        holder.timestamp.setText(sdf.format(date));

        // Adjust layout based on sender or receiver
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (messageModel.getSenderId().equals(currentUser.getUid())) {
            holder.msg.setTextColor(context.getResources().getColor(R.color.black));
            holder.main.setBackgroundColor(context.getResources().getColor(R.color.light_blue_1));
        } else {
            holder.main.setBackgroundColor(context.getResources().getColor(R.color.light_grey_1));
            holder.msg.setTextColor(context.getResources().getColor(R.color.black));
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
                                        .into(userProfileImageView);
                                Picasso.get().load(profilePictureUrl)
                                        .placeholder(R.drawable.placeholder) // Placeholder image while loading
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


    @Override
    public int getItemCount() {
        return messageModelList.size();
    }

    private void fetchUsername(String uid, TextView usernameTextView) {
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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView msg;
        private LinearLayout main;
        private TextView timestamp;
        private TextView senderUsername;
private ImageView userProfileImageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.message);
            main = itemView.findViewById(R.id.mainMessageLayout);
            timestamp = itemView.findViewById(R.id.timestamp);
            senderUsername = itemView.findViewById(R.id.senderUsername);
            userProfileImageView = itemView.findViewById(R.id.IVUserProfilePicture);

        }
    }
}
