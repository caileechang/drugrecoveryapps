package com.example.drugrecoveryapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateUtils;

import com.example.drugrecoveryapp.entity.MessageModel;
import com.example.drugrecoveryapp.entity.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChatRoomsAdapter extends RecyclerView.Adapter<ChatRoomsAdapter.MyViewHolder> {

    private Context context;
    private List<User> userList;
    private List<String> userID;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    private List<MessageModel> chatMessages;

    public ChatRoomsAdapter(Context context) {
        this.context = context;
        userList = new ArrayList<>();
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
        notifyDataSetChanged();
    }

    public void add(User user) {
        userList.add(user);
        notifyDataSetChanged();
    }
    public void setChatMessages(List<MessageModel> chatMessages) {
        this.chatMessages = chatMessages;
        notifyDataSetChanged();
    }
    public void clear() {
        userList.clear();
        notifyDataSetChanged();
    }


    public void addID(String id) {
        userID.add(id);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_room, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user = userList.get(position);
        holder.name.setText(user.getUsername());
        holder.lastMessage.setText(user.getLastMessage());
        holder.timestamp.setText(DateUtils.getRelativeTimeSpanString(user.getLastMessageTimestamp(),
                System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS));

        StorageReference storageReference = FirebaseStorage.getInstance().getReference("ProfilePicture/" + user.getProfilePicture());
        try {
            File localfile = File.createTempFile("Temp File", ".jpg");
            storageReference.getFile(localfile)
                    .addOnCompleteListener(task -> {
                        Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                        Picasso.get().load(user.getProfilePicture()).into(holder.profile_pic);
                    }).addOnFailureListener(e -> {
                        // Handle failure
                    });
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        holder.itemView.setOnClickListener(view -> {
            view.startAnimation(buttonClick);
            Intent intent = new Intent(context, ConversationActivity.class);
            intent.putExtra("id", user.getUid());
            intent.putExtra("name", user.getUsername());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name, lastMessage, timestamp;
        private ImageView profile_pic;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.chatRoomUserName);
            lastMessage = itemView.findViewById(R.id.chatRoomLastMessage);
            timestamp = itemView.findViewById(R.id.chatRoomTimestamp);
            profile_pic = itemView.findViewById(R.id.IVUserProfilePicture);
        }
    }
}