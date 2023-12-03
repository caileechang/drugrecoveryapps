package com.example.drugrecoveryapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drugrecoveryapp.R;
import com.example.drugrecoveryapp.ViewAccActivity;
import com.example.drugrecoveryapp.entity.User;


import java.util.ArrayList;
import java.util.List;

public class SearchFriendAdapter extends RecyclerView.Adapter<SearchFriendAdapter.UserViewHolder> {
    private static List<User> userList;
    private Context context;

    public SearchFriendAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflates the layout for each item in the RecyclerView using the LayoutInflater and returns a new instance of the UserViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_display_list, parent, false);
        context= parent.getContext();
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        //retrieves the User object at the given position from the userList
        User user = userList.get(position);

        //sets the corresponding data to the respective TextViews in the ViewHolder
        holder.usernameTextView.setText(user.getUsername());
        holder.emailTextView.setText(user.getEmail());
//        holder.phonenumberTextView.setText(user.getPhone_number());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // navigate to the ViewAccActivity_Scrollview activity when it is clicked
                Intent intent = new Intent(context, ViewAccActivity.class);
                intent.putExtra("uid", user.getUid()); // uid is passed as an extra with the intent to view the user's account details
                context.startActivity(intent);
            }
        });

    }

    @Override
    //represents the number of users to be displayed in the RecyclerView
    public int getItemCount() {
        return userList.size();
    }

    // represents a single item view within the RecyclerView
    // holds references to the TextViews that display the user's username, full name, email, and phone number
    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        TextView emailTextView;
//        TextView phonenumberTextView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.all_user_profile_username);
            emailTextView = itemView.findViewById(R.id.all_user_email);
//            phonenumberTextView = itemView.findViewById(R.id.all_user_phonenumber);
        }

    }
}
