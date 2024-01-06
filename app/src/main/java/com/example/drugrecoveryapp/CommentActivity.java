package com.example.drugrecoveryapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.drugrecoveryapp.databinding.ActivityCommentBinding;
import com.example.drugrecoveryapp.entity.Comment;
import com.example.drugrecoveryapp.entity.CommentAdapter;
import com.example.drugrecoveryapp.entity.Notification;
import com.example.drugrecoveryapp.entity.Post;
import com.example.drugrecoveryapp.entity.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class CommentActivity extends AppCompatActivity {

    ActivityCommentBinding binding;
    Intent intent;
    String postId;
    String postedBy;
    String postLike;
    FirebaseDatabase database;
    FirebaseAuth auth;
    ArrayList<Comment> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        intent = getIntent();
        Button btnBackCreatePosts = findViewById(R.id.btnBackCreatePosts);
        btnBackCreatePosts.setOnClickListener(v -> finish());
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        postId = intent.getStringExtra("postId");
        postedBy = intent.getStringExtra("postedBy");
//        postLike = intent.getStringExtra("postLike");
//        System.out.println("showbug" + postLike);
//        Log.d("show postlike", postLike);
//        Toast.makeText(this,"Post ID:" + postId, Toast.LENGTH_SHORTofile).show();
//        Toast.makeText(this,"User ID:" + postedBy, Toast.LENGTH_SHORT).show();



        database.getReference()
                .child("posts")
                .child(postId).addValueEventListener(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Post post = snapshot.getValue(Post.class);
                        if (post != null) {
                            // Log the post image URL for debugging
                            Log.d("DEBUG", "Post image URL: " + post.getPostImage());

                            if (post.getPostImage() != null) {
                                if (Picasso.get() == null) {
                                    Picasso.Builder builder = new Picasso.Builder(CommentActivity.this);
                                    builder.downloader(new OkHttp3Downloader(CommentActivity.this, Integer.MAX_VALUE));
                                    Picasso picasso = builder.build();
                                    Picasso.setSingletonInstance(picasso);
                                }
                                // Load the post image using Picasso
                                Picasso.get()
                                        .load(post.getPostImage())
                                        .placeholder(R.drawable.placeholder)
                                        .error(R.drawable.human_avatar_create_posts)
                                        .into(binding.postImage);
                            } else {
                                // Handle the case where post.getPostImage() is null
                                Log.e("DEBUG", "Post image URL is null");
                            }

                            // Set other values (description, like count, comment count, title, etc.)
                            binding.description.setText(post.getPostDescription());
                            binding.like.setText(post.getPostLike() + "");
                            binding.comment.setText(post.getCommentCount() + "");
                            binding.title.setText(post.getPostTitle());
                        } else {
                            Log.e("DEBUG", "Post object is null");
                            // Handle the situation where the post object is null
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        FirebaseDatabase.getInstance().getReference()
                .child("posts")
                .child(postId)
                .child("likes")
                .child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {
                            binding.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_heart_2, 0, 0, 0);

                        } else {
//                            binding.like.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    FirebaseDatabase.getInstance().getReference()
//                                            .child("posts")
//                                            .child(postId)
//                                            .child("likes")
//                                            .child(FirebaseAuth.getInstance().getUid())
//                                            .setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                @Override
//                                                public void onSuccess(Void unused) {
//                                                    FirebaseDatabase.getInstance().getReference()
//                                                            .child("posts")
//                                                            .child(postId)
//                                                            .child("postLike")
//                                                            .setValue((FirebaseDatabase.getInstance().getReference().child("posts").child(postId).get("postLike").toString()) + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                                @Override
//                                                                public void onSuccess(Void unused) {
//                                                                    binding.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_heart_2, 0, 0, 0);
//
//                                                                    Notification notification = new Notification();
//                                                                    notification.setNotificationBy(FirebaseAuth.getInstance().getUid());
//                                                                    notification.setNotificationAt(new Date().getTime());
//                                                                    notification.setPostID(postId);
//                                                                    notification.setNotificationBy(postedBy);
//                                                                    notification.setType("like");
//
//                                                                    FirebaseDatabase.getInstance().getReference()
//                                                                            .child("notification")
//                                                                            .child(postedBy)
//                                                                            .push()
//                                                                            .setValue(notification);
//
//                                                                }
//                                                            });
//                                                }
//                                            });
//
//                                }
//                            });
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        database.getReference()
                .child("Users")
                .child(postedBy).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        String profilePicture = user.getProfilePicture();
                        if (profilePicture != null) {
                            if (isBase64(profilePicture)) {
                                // Convert Base64 string to Bitmap
                                Bitmap bitmap = base64ToBitmap(profilePicture);
                                // Set the user DP (Profile Picture) directly to IVUserProfilePicture
                                binding.profileImage.setImageBitmap(bitmap);
                            } else {
                                Picasso.get().setIndicatorsEnabled(true);  // Enables debug indicators
                                Picasso.get().setLoggingEnabled(true);      // Enables debug logging

                                // Load the image directly with Picasso
                                Picasso.get()
                                        .load(user.getProfilePicture())
                                        .placeholder(R.drawable.placeholder)
                                        .error(R.drawable.human_avatar_create_posts) // Provide an error image placeholder
                                        .into(binding.profileImage);

                            }
                        } else {
                            // Handle the case where profilePicture is null
                            Log.e("ProfilePicture", "Profile picture URL is null");
                        }


                        binding.name.setText(user.getUsername());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.commentPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Comment comment = new Comment();
                comment.setCommentBody(binding.commentET.getText().toString());
                comment.setCommentedAt(new Date().getTime());
                comment.setCommentedBy(FirebaseAuth.getInstance().getUid());

                database.getReference()
                        .child("posts")
                        .child(postId)
                        .child("comments")
                        .push()
                        .setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                database.getReference()
                                        .child("posts")
                                        .child(postId)
                                        .child("commentCount").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                int commentCount = 0;
                                                if (snapshot.exists()) {
                                                    commentCount = snapshot.getValue(Integer.class);
                                                }
                                                database.getReference()
                                                        .child("posts")
                                                        .child(postId)
                                                        .child("commentCount")
                                                        .setValue(commentCount + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                binding.commentET.setText("");
                                                                Toast.makeText(CommentActivity.this, "Commented", Toast.LENGTH_SHORT).show();

                                                                Notification notification = new Notification();
                                                                notification.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                                                notification.setNotificationAt(new Date().getTime());
                                                                notification.setPostID(postId);
                                                                notification.setPostedBy(postedBy);
                                                                notification.setType("comment");

                                                                FirebaseDatabase.getInstance().getReference()
                                                                        .child("notification")
                                                                        .child(postedBy)
                                                                        .push()
                                                                        .setValue(notification);

                                                            }
                                                        });
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                            }
                        });

            }
        });

        CommentAdapter adapter = new CommentAdapter(this, list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.commentRv.setLayoutManager(layoutManager);
        binding.commentRv.setAdapter(adapter);

        database.getReference()
                .child("posts")
                .child(postId)
                .child("comments").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Comment comment = dataSnapshot.getValue(Comment.class);
                            list.add(comment);
                        }
                        adapter.notifyDataSetChanged();

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
}