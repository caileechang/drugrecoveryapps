//package com.example.drugrecoveryapp.adapter;
//
//import android.content.Context;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.drugrecoveryapp.R;
//import com.example.drugrecoveryapp.ViewAccActivity;
//import com.example.drugrecoveryapp.entity.User;
//import java.util.List;
//public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendViewHolder> {
//    private List<User> friendList;
//    private Context context;
//
//    public FriendsAdapter(List<User> friendList, Context context) {
//        this.friendList = friendList;
//        this.context = context;
//    }
//
//    @NonNull
//    @Override
//    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.friend_display, parent, false);
//        return new FriendViewHolder(view);
//    }
//
//    @Override
//    // binding the data to each item view in the RecyclerView
//    // retrieves the “User” object from the “userList” based on the given position
//    // sets the corresponding values to the “TextVIew” fields
//    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
//        User friend = friendList.get(position);
//        holder.username.setText(friend.getUsername());
//        holder.fullname.setText(friend.getFullName());
//        holder.email.setText(friend.getEmail());
//        holder.phonenumber.setText(friend.getPhone_number());
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, ViewAccActivity.class);
//                intent.putExtra("uid", friend.getUid()); // Pass the user object
//                context.startActivity(intent);
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return friendList.size();
//    }
//
//    public class FriendViewHolder extends RecyclerView.ViewHolder {
//        private TextView username;
//        private TextView fullname;
//        private TextView email;
//        private TextView phonenumber;
//
//        public FriendViewHolder(@NonNull View itemView) {
//            super(itemView);
//            username = itemView.findViewById(R.id.friend_username);
//            fullname=itemView.findViewById(R.id.friend_fullname_display);
//            email=itemView.findViewById(R.id.friend_mutualFriendDisplay);
//            phonenumber=itemView.findViewById(R.id.friend_phDisplay);
//        }
//    }
//}
