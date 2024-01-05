package com.example.drugrecoveryapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drugrecoveryapp.R;
import com.example.drugrecoveryapp.entity.GroupChatActivity;

import java.util.List;

public class DiscussionGroupAdapter extends RecyclerView.Adapter<DiscussionGroupAdapter.ViewHolder> {

    private final List<String> discussionGroups;
    private final Context context;

    public DiscussionGroupAdapter(Context context, List<String> discussionGroups) {
        this.context = context;
        this.discussionGroups = discussionGroups;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_discussion_group, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String groupName = discussionGroups.get(position);

        // Set data to views
        holder.groupName.setText(groupName);

        // Set click listener for the join button
        holder.joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GroupChatActivity.class);
                intent.putExtra("groupId", groupName);
                intent.putExtra("groupName", groupName);
                context.startActivity(intent);
                Toast.makeText(context, "Joined group: " + groupName, Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return discussionGroups.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView groupName;
        public Button joinButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            groupName = itemView.findViewById(R.id.GroupName);
            joinButton = itemView.findViewById(R.id.Join);
        }
    }
}
