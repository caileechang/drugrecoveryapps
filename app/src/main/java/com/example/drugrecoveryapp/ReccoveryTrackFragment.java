package com.example.drugrecoveryapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ReccoveryTrackFragment extends Fragment {

    private Button startButton;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
    private DatabaseReference currentUserRef;
    private String currentUserId, saveCurrentDateTime, CURRENT_STATE;

    private Button rewardButton;


    private static final long MILESTONE_ONE_DAY = 24 * 60 * 60 * 1000; // One day milestone
    private static final long MILESTONE_TWO_DAYS = 2 * MILESTONE_ONE_DAY; // Two days milestone

    private static final String PREFS_NAME = "RecoveryPrefs";
    private static final String KEY_TOTAL_RECOVERY_TIME = "TotalRecoveryTime";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reccovery_track, container, false);

        startButton = view.findViewById(R.id.startButton);
        rewardButton = view.findViewById(R.id.rewardButton);

        // Retrieve current user's ID using method in Firebase library
        currentUserId = mAuth.getCurrentUser().getUid();
        currentUserRef = userRef.child(currentUserId);
        CURRENT_STATE = "not_started";
        startButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startButton.setEnabled(false);
                if(CURRENT_STATE.equals("not_started")){
                    StartRecovery();
                }
                if(CURRENT_STATE.equals("started")){
                    RestartRecovery();
                }
            }
        });

        rewardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Reward.class);
                startActivity(intent);
            }
        });

        calculateTotalTime();



        return view;
    }

    private void calculateTotalTime() {
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDateTime = new SimpleDateFormat("dd-MMMM-yyyy HH:mm:ss");
        saveCurrentDateTime = currentDateTime.format(calForDate.getTime());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long hoursDifference;
                if (snapshot.child(currentUserId).hasChild("date")) {
                    String recoveryDateString = snapshot.child(currentUserId).child("date").getValue().toString();
                    Date currentDateObject = null;
                    try {
                        currentDateObject = currentDateTime.parse(saveCurrentDateTime);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    Date recoveryDateObject = null;
                    try {
                        recoveryDateObject = currentDateTime.parse(recoveryDateString);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    long timeDifference = currentDateObject.getTime() - recoveryDateObject.getTime();

                    long secondsDifference = timeDifference / 1000;
                    long minutesDifference = secondsDifference / 60;
                    hoursDifference = minutesDifference / 60;
                } else {
                    hoursDifference = 0;
                }

                userRef.child(currentUserId).child("totalTime").setValue(hoursDifference).addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getContext(), "Your total time is updated", Toast.LENGTH_SHORT).show();
                    }
                });
            }

                @Override
                public void onCancelled (@NonNull DatabaseError error){

                }
            });
        }

    private void RestartRecovery() {

        showStartOverConfirmationDialog();


    }

    private void showStartOverConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Start Over Confirmation");
        builder.setMessage("Are you sure you want to start over?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked Yes, stop the old timer and start over
                userRef.child(currentUserId).child("date")
                        .removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                CURRENT_STATE = "not_started";
                                startButton.setText("Start");
                            }
            });
        }
                });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked Cancel, do nothing
            }
        });

        builder.create().show();
    }


    private void StartRecovery() {
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDateTime = new SimpleDateFormat("dd-MMMM-yyyy HH:mm:ss");
        saveCurrentDateTime = currentDateTime.format(calForDate.getTime());

        userRef.child(currentUserId).child("date").setValue(saveCurrentDateTime).addOnCompleteListener(new OnCompleteListener<Void>() {


            @Override
            public void onComplete(@NonNull Task<Void> task) {
                startButton.setEnabled(true);
                CURRENT_STATE = "started";
                startButton.setText("Restart");
            }
        });


}
}
