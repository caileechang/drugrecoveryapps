//package com.example.drugrecoveryapp.adapter;
//
//import android.content.Context;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//
//import com.example.drugrecoveryapp.R;
//import com.example.drugrecoveryapp.ViewAccActivity;
//import com.example.drugrecoveryapp.entity.User;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.List;
//
//public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.FriendRequestViewHolder> {
//
//    private List<User> requestList;
//    private Context context;
//    private String saveCurrentDate;
//
//    public RequestAdapter(List<User> requestList, Context context) {
//        this.requestList=requestList;
//        this.context = context;
//    }
//
//    @NonNull
//    @Override
//    public RequestAdapter.FriendRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        // inflates the layout for each item in the RecyclerView using the LayoutInflater and returns a new instance of the FriendRequestViewHolder
//        View view = LayoutInflater.from(context).inflate(R.layout.request_display, parent, false);
//        return new RequestAdapter.FriendRequestViewHolder(view);
//    }
//
//    // retrieves the User object at the given position from the requestList
//    // sets the corresponding data to the respective TextViews in the ViewHolder
//    @Override
//    public void onBindViewHolder(@NonNull FriendRequestViewHolder holder, int position) {
//        Calendar calForDate = Calendar.getInstance();
//        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
//        saveCurrentDate = currentDate.format(calForDate.getTime());
//        User request = requestList.get(position);
//        holder.username.setText(request.getUsername());
//        holder.fullname.setText(request.getFullName());
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            // When an item is clicked, it navigates to the ViewAccActivity_Scrollview activity
//            // uid is passed as an extra with the intent to view the user's account details
//            public void onClick(View v) {
//                Intent intent = new Intent(context, ViewAccActivity.class);
//                intent.putExtra("uid", request.getUid()); // Pass the user object
//                context.startActivity(intent);
//            }
//        });
//
//        // when accept button is clicked, it updates the "Friends" node in the Firebase Realtime Database for both the current user and the requesting user and adding each other as friends with the current date
//        // removes the friend request from the "FriendRequests" node for both users
//        holder.accept.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseDatabase.getInstance().getReference("Friends").child(FirebaseAuth.getInstance().getUid()).child(request.userid).child("date").setValue(saveCurrentDate);
//                FirebaseDatabase.getInstance().getReference("Friends").child(request.userid).child(FirebaseAuth.getInstance().getUid()).child("date").setValue(saveCurrentDate);
//                FirebaseDatabase.getInstance().getReference("FriendRequests").child(FirebaseAuth.getInstance().getUid()).child(request.getUid()).removeValue();
//                FirebaseDatabase.getInstance().getReference("FriendRequests").child(request.getUid()).child(FirebaseAuth.getInstance().getUid()).removeValue();
//            }
//        });
//
//        // When the button is clicked, it removes the friend request from the "FriendRequests" node for both the current user and the requesting user
//        holder.delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseDatabase.getInstance().getReference("FriendRequests").child(FirebaseAuth.getInstance().getUid()).child(request.getUid()).removeValue();
//                FirebaseDatabase.getInstance().getReference("FriendRequests").child(request.getUid()).child(FirebaseAuth.getInstance().getUid()).removeValue();
//            }
//        });
//
//
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return requestList.size();
//    }
//
//    public class FriendRequestViewHolder extends RecyclerView.ViewHolder {
//        // represents a single item view within the RecyclerView
//        // holds references to the TextViews (username and fullname) as well as the accept and delete buttons (accept and delete)
//        private TextView username;
//        private TextView fullname;
//
//        private Button accept;
//        private Button delete;
//
//        public FriendRequestViewHolder(@NonNull View itemView) {
//            super(itemView);
//            username = itemView.findViewById(R.id.friend_username);
//            fullname=itemView.findViewById(R.id.friend_fullname_display);
//            accept = itemView.findViewById(R.id.accept_button);
//            delete = itemView.findViewById(R.id.delete_button);
//        }
//    }
//
//}
