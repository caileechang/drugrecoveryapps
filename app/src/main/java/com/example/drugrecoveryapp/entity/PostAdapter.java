package com.example.drugrecoveryapp.entity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drugrecoveryapp.CommentActivity;
import com.example.drugrecoveryapp.R;
import com.example.drugrecoveryapp.databinding.PostSampleBinding;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.viewHolder> {

    private List<Post> list;

    public List<Post> getList() {
        return list;
    }

    public void setList(List<Post> list) {
        Collections.sort(list, new Comparator<Post>() {
            @Override
            public int compare(Post post1, Post post2) {
                return Long.compare(post2.getPostedAt(), post1.getPostedAt());
            }
        });
        this.list = list;
        notifyDataSetChanged();
    }

    public PostAdapter(List<Post> postList) {
        this.list = postList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_sample, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Post post = list.get(position);
        Picasso.get()
                .load(post.getPostImage())
                .placeholder(R.drawable.placeholder)
                .into(holder.binding.postImage);
        holder.binding.like.setText(post.getPostLike() + "");
        holder.binding.comment.setText(post.getCommentCount() + "");
        holder.binding.tittle.setText(post.getPostTitle());

        String description = post.getPostDescription();
        if (description.equals("")) {
            holder.binding.description.setVisibility(View.GONE);
        } else {
            holder.binding.description.setText(post.getPostDescription());
        }
        String time = TimeAgo.using(post.getPostedAt());
        holder.binding.dateTime.setText(time);
        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(post.getPostBy())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        Picasso.get()
                                .load(user.getProfilePicture())
                                .placeholder(R.drawable.placeholder)
                                .into(holder.binding.profileImage);
                        holder.binding.name.setText(user.getUsername());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        FirebaseDatabase.getInstance().getReference()
                .child("posts")
                .child(post.getPostId())
                .child("likes")
                .child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            holder.binding.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_heart_2, 0, 0, 0);

                        } else {
                            holder.binding.like.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("posts")
                                            .child(post.getPostId())
                                            .child("likes")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    FirebaseDatabase.getInstance().getReference()
                                                            .child("posts")
                                                            .child(post.getPostId())
                                                            .child("postLike")
                                                            .setValue(post.getPostLike() + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    holder.binding.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_heart_2, 0, 0, 0);

                                                                    Notification notification = new Notification();
                                                                    notification.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                                                    notification.setNotificationAt(new Date().getTime());
                                                                    notification.setPostID(post.getPostId());
                                                                    notification.setNotificationBy(post.getPostBy());
                                                                    notification.setType("like");

                                                                    FirebaseDatabase.getInstance().getReference()
                                                                            .child("notification")
                                                                            .child(post.getPostBy())
                                                                            .push()
                                                                            .setValue(notification);

                                                                }
                                                            });
                                                }
                                            });

                                }
                            });
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        holder.binding.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), CommentActivity.class);
                intent.putExtra("postId", post.getPostId());
                intent.putExtra("postedBy", post.getPostBy());
                intent.putExtra("postLike", post.getPostLike());
//                Toast.makeText(context, post.getPostId()+"", Toast.LENGTH_SHORT).show();
//                Toast.makeText(context, post.getPostBy()+"", Toast.LENGTH_SHORT).show();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.itemView.getContext().startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        PostSampleBinding binding;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = PostSampleBinding.bind(itemView);
        }
    }

}
