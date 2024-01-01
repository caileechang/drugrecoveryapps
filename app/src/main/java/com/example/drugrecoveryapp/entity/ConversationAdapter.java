package com.example.drugrecoveryapp.entity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.message);
            main = itemView.findViewById(R.id.mainMessageLayout);
            timestamp = itemView.findViewById(R.id.timestamp);
            senderUsername = itemView.findViewById(R.id.senderUsername);
        }
    }
}
