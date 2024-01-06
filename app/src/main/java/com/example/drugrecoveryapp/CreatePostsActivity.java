package com.example.drugrecoveryapp;


import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.drugrecoveryapp.MainActivity;
import com.example.drugrecoveryapp.entity.Post;
import com.example.drugrecoveryapp.entity.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class CreatePostsActivity extends AppCompatActivity {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final int PICK_IMAGE_REQUEST = 1;

    public ArrayAdapter<String> arrayAdapter;

    private ImageView IVUserProfilePicture;
    private TextView TVUserID;
    private TextView TVDateTime;
    private Spinner SpinnerCategory;
    private EditText ETTitle;
    private EditText ETContent;
    private Button BtnPost;
    private Button BtnUploadImage;
    private ProgressBar PBUpload;
    private ImageView IVUpload;

    private Uri UriImage;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;

    private StorageTask uploadTask;
    public ArrayList<String> categoryArrayList=new ArrayList<>(Arrays.asList("Text Only","Text and Image"));

    @SuppressLint({"ResourceAsColor", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_posts);
        Button btnBackCreatePosts = findViewById(R.id.btnBackCreatePosts);
        btnBackCreatePosts.setOnClickListener(v -> finish());

        TVUserID = findViewById(R.id.TVUserID);
        SpinnerCategory = findViewById(R.id.SpinnerCategory);
        BtnPost = findViewById(R.id.BtnPost);
        BtnUploadImage = findViewById(R.id.BtnUploadImage);
        PBUpload = findViewById(R.id.PBUpload);
        ETTitle = findViewById(R.id.ETTitle);
        ETContent = findViewById(R.id.ETContent);
        IVUpload = findViewById(R.id.IVUpload);
        IVUserProfilePicture = findViewById(R.id.IVUserProfilePicture);
        TVDateTime=findViewById(R.id.dateTime);
        storageReference = FirebaseStorage.getInstance().getReference("posts");
        databaseReference = FirebaseDatabase.getInstance().getReference("posts");


        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        }
        else {
            Toast.makeText(getApplicationContext(), "User is not logged in, unable to retrieve user data", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(),Login.class);
            startActivity(intent);
        }

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.getKey().equalsIgnoreCase(firebaseUser.getUid())) {
                        User user = new User(dataSnapshot.child("userid").getValue().toString()
                                ,dataSnapshot.child("username").getValue().toString()
                                ,dataSnapshot.child("email").getValue().toString()
                                ,dataSnapshot.child("countryName").getValue().toString()
                                ,dataSnapshot.child("gender").getValue().toString());
                        TVUserID.setText(user.getUsername());
                        // Inside the ValueEventListener where profile picture is loaded
                        String profilePicture = user.getProfilePicture();
                        if (profilePicture != null) {
                            if (isBase64(profilePicture)) {
                                // Convert Base64 string to Bitmap
                                Bitmap bitmap = base64ToBitmap(profilePicture);
                                // Set the user DP (Profile Picture) directly to IVUserProfilePicture
                                IVUserProfilePicture.setImageBitmap(bitmap);
                            } else {
                                // Load the image directly with Picasso
                                Picasso.get()
                                        .load(profilePicture)
                                        .placeholder(R.drawable.placeholder)
                                        .error(R.drawable.human_avatar_create_posts) // Provide an error image placeholder
                                        .into(IVUserProfilePicture, new com.squareup.picasso.Callback() {
                                            @Override
                                            public void onSuccess() {
                                                Log.d("Picasso", "Image loaded successfully");
                                            }

                                            @Override
                                            public void onError(Exception e) {
                                                Log.e("Picasso", "Error loading image: " + e.getMessage());
                                            }
                                        });
                            }
                        } else {
                            // Handle the case where profilePicture is null
                            Log.e("ProfilePicture", "Profile picture URL is null");
                        }




                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        BtnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
            private void openFileChooser() {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,PICK_IMAGE_REQUEST);
            }
        });

        BtnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedCategory = SpinnerCategory.getSelectedItem().toString();

                if (selectedCategory.equals("Text Only")) {

                    uploadPostWithoutImage();
                } else if (selectedCategory.equals("Text and Image")) {
                    // Handle text and image case
                    uploadPost(UriImage, true);
                }
            }
        });

        SpinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedCategory = categoryArrayList.get(i);
                if (selectedCategory.equals("Text Only")) {
                    BtnUploadImage.setVisibility(View.GONE);

                } else if (selectedCategory.equals("Text and Image")) {
                    BtnUploadImage.setVisibility(View.VISIBLE);
                    uploadPost(UriImage,true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(CreatePostsActivity.this, "Must choose one category !", Toast.LENGTH_SHORT).show();
            }
        });

        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,categoryArrayList);
        SpinnerCategory.setAdapter(arrayAdapter);
    }

    private boolean isBase64(String str) {
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            UriImage = data.getData();
            Picasso.get().load(UriImage).into(IVUpload);
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadPost(Uri UriImage, boolean exist) {
        if (UriImage != null&& exist) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(UriImage));
            uploadTask = fileReference.putFile(UriImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            PBUpload.setProgress(0);
                                        }
                                    },500);
                                    Toast.makeText(getApplicationContext(), "Post uploaded successfully", Toast.LENGTH_SHORT).show();

                                    String postId = databaseReference.push().getKey();
                                    Post post = new Post(postId
                                            ,uri.toString()
                                            ,SpinnerCategory.getSelectedItem().toString()
                                            ,firebaseUser.getUid()
                                            ,TVUserID.getText().toString()
                                            ,ETTitle.getText().toString()
                                            ,ETContent.getText().toString()
                                            ,new Date().getTime());
                                    databaseReference.child(postId).setValue(post);
                                    //After upload successfully, back to home page
                                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            PBUpload.setProgress((int)progress);
                        }
                    });
        }else if(UriImage==null&&!exist){
            String postId = databaseReference.push().getKey();
            Post post = new Post(postId
                    , null
                    , SpinnerCategory.getSelectedItem().toString()
                    , firebaseUser.getUid()
                    , TVUserID.getText().toString()
                    , ETTitle.getText().toString()
                    , ETContent.getText().toString()
                    , new Date().getTime());

            databaseReference.child(postId).setValue(post);

            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
        else if(UriImage==null&& exist){
            Toast.makeText(getApplicationContext(), "Please Upload Image", Toast.LENGTH_SHORT).show();
        }
    }
    private void uploadPostWithoutImage() {
        String postId = databaseReference.push().getKey();
        Post post = new Post(postId
                , null // Set image URL to null for posts without images
                , SpinnerCategory.getSelectedItem().toString()
                , firebaseUser.getUid()
                , TVUserID.getText().toString()
                , ETTitle.getText().toString()
                , ETContent.getText().toString()
                , new Date().getTime());

        databaseReference.child(postId).setValue(post);

        // Optional: Display a success message
        Toast.makeText(getApplicationContext(), "Post uploaded successfully", Toast.LENGTH_SHORT).show();

        // After upload successfully, back to home page
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }


}