package com.example.drugrecoveryapp;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class ViewAccActivity extends AppCompatActivity {

    private TextView username, email,  gender, country, topUsername;


    private String selectedUserId, currentUserId, CURRENT_STATE, saveCurrentDate;

    private Button SendFriendReqButton, DeclineFriendReqButton;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
    private DatabaseReference selectedUserRef;
    private DatabaseReference currentUserRef;
    private DatabaseReference FriendRequestRef = FirebaseDatabase.getInstance().getReference().child("FriendRequests");
    private DatabaseReference FriendsRef = FirebaseDatabase.getInstance().getReference().child("Friends");;

private Button btnStartAChatButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_acc);


        // Retrieve current user's ID using method in Firebase library
        currentUserId = mAuth.getCurrentUser().getUid();

        // Retrieve selected user's ID passed from the previous activity
        selectedUserId = getIntent().getStringExtra("uid");


        // Get reference to the  selected user by ID
        selectedUserRef = userRef.child(selectedUserId);
        currentUserRef = userRef.child(currentUserId);

        // Initialization of TextView object
        topUsername=findViewById(R.id.top_username);
        username = findViewById(R.id.UsernameDisplay);

        email = findViewById(R.id.emailDisplay);
//        phone = findViewById(R.id.phDisplay);

        gender = findViewById(R.id.genderDisplay);
        country = findViewById(R.id.countryDisplay);
        btnStartAChatButton=findViewById(R.id.btnStartAChatButton);


        SendFriendReqButton = findViewById(R.id.send_friend_request_btn);
        DeclineFriendReqButton = findViewById(R.id.decline_friend_request_btn);
        CURRENT_STATE = "not_friends";


        // create list to store user's friend list
        List<String> currentUserFriendList = new ArrayList<>();
        List<String> selectedUserFriendList = new ArrayList<>();
        Toast.makeText(this, "User ID: "+ selectedUserId, Toast.LENGTH_SHORT).show();

        // retrieve friend list of users
        FriendsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get current user friend list
                DataSnapshot currentUserSnapshot = dataSnapshot.child(currentUserId);
                if (currentUserSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : currentUserSnapshot.getChildren()) {
                        String currentUserFriendId = userSnapshot.getKey();
                        currentUserFriendList.add(currentUserFriendId);
                    }
                }

                // Get selected user friend list
                DataSnapshot selectedUserSnapshot = dataSnapshot.child(selectedUserId);
                if (selectedUserSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : selectedUserSnapshot.getChildren()) {
                        String selectedUserFriendId = userSnapshot.getKey();
                        selectedUserFriendList.add(selectedUserFriendId);
                    }
                }

                // retrieve the selected user's profile data from database
                selectedUserRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {

                            String selectedUsername = snapshot.child("username").getValue().toString();

                            String selectedUserEmail = snapshot.child("email").getValue().toString();
//                            String selectedUserPhoneNumber = snapshot.child("phone_number").getValue().toString();
                            String selectedGender = snapshot.child("gender").getValue().toString();
                            String selectedCountry = snapshot.child("countryName").getValue().toString();




                            // Display the user's profile data in the UI
                            topUsername.setText("@"+selectedUsername);
                            username.setText(selectedUsername);

                            email.setText(selectedUserEmail);



                            // set the profile data that can only be seen if both user are friends
                            if(currentUserFriendList.contains(selectedUserId)){

//                                phone.setText(selectedUserPhoneNumber);

                                gender.setText(selectedGender);
                                country.setText(selectedCountry);



                            }else{

                                // profile data will set as "-" if both user are not friend

//                                phone.setText("-");

                                gender.setText("-");
                                country.setText("-");

                            }


                            MaintananceofButtion();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle the error
                    }
                });




            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        DeclineFriendReqButton.setVisibility(View.INVISIBLE);
        DeclineFriendReqButton.setEnabled(false);

        if(!currentUserId.equals(selectedUserId)){
            FriendRequestRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.hasChild(selectedUserId)) {
                        if (snapshot.child(selectedUserId).child("request_type").getValue().equals("sent")) {
                            CURRENT_STATE = "request_sent";
                            SendFriendReqButton.setText("Cancel Friend Request");
                            DeclineFriendReqButton.setVisibility(View.INVISIBLE);
                            DeclineFriendReqButton.setEnabled(false);
                        } else if (snapshot.child(selectedUserId).child("request_type").getValue().equals("received")) {
                            CURRENT_STATE = "request_received";
                            SendFriendReqButton.setText("Accept Friend Request");

                        }
                    }
                    else {
                        FriendsRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.hasChild(selectedUserId)) {
                                    CURRENT_STATE = "friend";
                                    SendFriendReqButton.setText("Unfriend this Person");
                                    DeclineFriendReqButton.setVisibility(View.INVISIBLE);
                                    DeclineFriendReqButton.setEnabled(false);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle error
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                }
            });
            SendFriendReqButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SendFriendReqButton.setEnabled(false);
                    if(CURRENT_STATE.equals("not_friends")){
                        SendFriendRequestToaPerson();
                    }
                    if(CURRENT_STATE.equals("request_sent")){
                        CancelFriendRequest();
                    }
                    if(CURRENT_STATE.equals("request_received")){
                        AcceptFriendRequest();
                    }
                    if(CURRENT_STATE.equals("friend")){
                        UnfriendAnExistingFriend();
                    }
                }
            });
        }else{
            DeclineFriendReqButton.setVisibility(View.INVISIBLE);
            SendFriendReqButton.setVisibility(View.INVISIBLE);
        }
        btnStartAChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewAccActivity.this, ConversationActivity.class);
                startActivity(intent);
            }
        });

    }



    private void UnfriendAnExistingFriend() {
        // selected user will be removed from the current user’s friend list
        FriendsRef.child(currentUserId).child(selectedUserId)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            // removes the current user from the selected user’s friend list
                            FriendsRef.child(selectedUserId).child(currentUserId)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                SendFriendReqButton.setEnabled(true); // enables the “Send Friend Request” button
                                                CURRENT_STATE = "not_friend"; // set CURRENT_STATE to not friends
                                                SendFriendReqButton.setText("Send Friend Request"); // updates the button text to “Send Friend Request”

                                                // hides the “Decline Friend Request” button
                                                DeclineFriendReqButton.setVisibility(View.INVISIBLE);
                                                DeclineFriendReqButton.setEnabled(false);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void AcceptFriendRequest() {
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        // adds the friendship connection to the current user’s friend list by setting the friendship date
        FriendsRef.child(currentUserId).child(selectedUserId).child("date").setValue(saveCurrentDate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    // adds the current user to the selected user's friend list with the same friendship date
                    FriendsRef.child(selectedUserId).child(currentUserId).child("date").setValue(saveCurrentDate).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                // removes the friend request entry from the database for both users
                                FriendRequestRef.child(currentUserId).child(selectedUserId)
                                        .removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    FriendRequestRef.child(selectedUserId).child(currentUserId)
                                                            .removeValue()
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful()){
                                                                        //  enables the “Send Friend Request” button
                                                                        //  sets the “CURRENT_STATE” to “friend”
                                                                        //  updates the button text to “Unfriend this Person”
                                                                        //  hides the “Decline Friend Request” button.
                                                                        SendFriendReqButton.setEnabled(true);
                                                                        CURRENT_STATE = "friend";
                                                                        SendFriendReqButton.setText("Unfriend this Person");

                                                                        DeclineFriendReqButton.setVisibility(View.INVISIBLE);
                                                                        DeclineFriendReqButton.setEnabled(false);
                                                                    }
                                                                }
                                                            });
                                                }
                                            }
                                        });
                            }

                        }
                    });
                }

            }
        });

    }

    private void CancelFriendRequest() {
        // removes the friend request entry from the database for both the current user and the selected user
        FriendRequestRef.child(currentUserId).child(selectedUserId)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            FriendRequestRef.child(selectedUserId).child(currentUserId)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                //  enables the “Send Friend Request” button
                                                //  sets the “CURRENT_STATE” to ”not friend”
                                                //  button text will be updated to “Send Friend Request”
                                                //  “Decline Friend Request” button will be disabled and hided
                                                SendFriendReqButton.setEnabled(true);
                                                CURRENT_STATE = "not_friend";
                                                SendFriendReqButton.setText("Send Friend Request");

                                                DeclineFriendReqButton.setVisibility(View.INVISIBLE);
                                                DeclineFriendReqButton.setEnabled(false);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void MaintananceofButtion() {
        // maintaining the state and behaviour of the buttons based on the existing friend relationship between the current user and the selected user

        FriendRequestRef.child(currentUserId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild(selectedUserId)){
                            String request_type = snapshot.child(selectedUserId).child("request_type").getValue().toString();

                            if(request_type.equals("sent")){
                                CURRENT_STATE = "request_sent";
                                SendFriendReqButton.setText("Cancel Friend Request");

                                DeclineFriendReqButton.setVisibility(View.INVISIBLE);
                                DeclineFriendReqButton.setEnabled(false);
                            } else if (request_type.equals("received")) {
                                CURRENT_STATE = "request_received";
                                SendFriendReqButton.setText("Accept Friend Request");

                                DeclineFriendReqButton.setVisibility(View.VISIBLE);
                                DeclineFriendReqButton.setEnabled(true);

                                DeclineFriendReqButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        CancelFriendRequest();
                                    }
                                });

                            }
                            else {
                                FriendsRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.hasChild(selectedUserId)){
                                            CURRENT_STATE = "friends";
                                            SendFriendReqButton.setText("Unfriend this Person");

                                            DeclineFriendReqButton.setVisibility(View.INVISIBLE);
                                            DeclineFriendReqButton.setEnabled(false);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void SendFriendRequestToaPerson() {
        // It adds an entry to the friend request data in the database for the current user and the selected user
        FriendRequestRef.child(currentUserId).child(selectedUserId)
                .child("request_type").setValue("sent")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            FriendRequestRef.child(selectedUserId).child(currentUserId)
                                    .child("request_type").setValue("received")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                // enables the “Send Friend Request” button
                                                // sets the “CURRENT_STATE” to “request_sent”
                                                // button text will be changed to “Cancel Friend Request”
                                                // the “Decline Friend Request” will be hide
                                                SendFriendReqButton.setEnabled(true);
                                                CURRENT_STATE = "request_sent";
                                                SendFriendReqButton.setText("Cancel Friend Request");

                                                DeclineFriendReqButton.setVisibility(View.INVISIBLE);
                                                DeclineFriendReqButton.setEnabled(false);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }







}