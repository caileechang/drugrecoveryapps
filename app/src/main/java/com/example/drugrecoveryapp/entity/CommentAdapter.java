package com.example.drugrecoveryapp.entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drugrecoveryapp.R;
import com.example.drugrecoveryapp.databinding.CommentSampleBinding;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.viewHolder> {

    Context context;
    ArrayList<Comment> list;

    public CommentAdapter(Context context, ArrayList<Comment> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_sample, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Comment comment = list.get(position);
        String time = TimeAgo.using(comment.getCommentedAt());
        holder.binding.time.setText(time);

        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(comment.getCommentedBy()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);

                        String profilePicture = user.getProfilePicture();
                        if (profilePicture != null) {
                            if (isBase64(profilePicture)) {
                                // Convert Base64 string to Bitmap
                                Bitmap bitmap = base64ToBitmap(profilePicture);
                                // Set the user DP (Profile Picture) directly to IVUserProfilePicture
                                holder.binding.profileImage.setImageBitmap(bitmap);
                            } else {
                                Picasso.get().setIndicatorsEnabled(true);  // Enables debug indicators
                                Picasso.get().setLoggingEnabled(true);      // Enables debug logging

                                // Load the image directly with Picasso
                                Picasso.get()
                                        .load(user.getProfilePicture())
                                        .placeholder(R.drawable.placeholder)
                                        .error(R.drawable.human_avatar_create_posts) // Provide an error image placeholder
                                        .into(holder.binding.profileImage);

                            }
                        } else {
                            // Handle the case where profilePicture is null
                            Log.e("ProfilePicture", "Profile picture URL is null");
                        }

                        holder.binding.comment.setText(Html.fromHtml("<b>" + user.getUsername() + "</b>" + "  " + comment.getCommentBody()));
                        Log.d("Debug", "Error");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

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
        if (base64Image == null) {
            return null;  // Handle null strings according to your use case
        }
        byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {

        CommentSampleBinding binding;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CommentSampleBinding.bind(itemView);
        }
    }

}
