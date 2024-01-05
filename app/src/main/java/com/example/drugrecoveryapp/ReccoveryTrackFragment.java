package com.example.drugrecoveryapp;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.ref.WeakReference;
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
    private Button reportButton;

    private ProgressBar PBSecond;
    private ProgressBar PBMinute;
    private ProgressBar PBHour;
    private ProgressBar PBDay;
    private ProgressBar PBMonth;
    private ProgressBar PBYear;
    private Handler handler = new Handler();

    private long secondsDifference;
    private long minutesDifference;
    private long hoursDifference;
    private long daysDifference;
    private long monthsDifference;
    private long yearsDifference;

    private static final long MILESTONE_ONE_DAY = 24 * 60 * 60 * 1000; // One day milestone
    private static final long MILESTONE_TWO_DAYS = 2 * MILESTONE_ONE_DAY; // Two days milestone

    private static final String PREFS_NAME = "RecoveryPrefs";
    private static final String KEY_TOTAL_RECOVERY_TIME = "TotalRecoveryTime";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reccovery_track, container, false);

        startButton = view.findViewById(R.id.startButton);
        reportButton = view.findViewById(R.id.ReportButton);

        PBSecond = view.findViewById(R.id.PBSecond);
        PBMinute = view.findViewById(R.id.PBMinute);
        PBHour = view.findViewById(R.id.PBHour);
        PBDay = view.findViewById(R.id.PBDay);
        PBMonth = view.findViewById(R.id.PBMonth);
        PBYear = view.findViewById(R.id.PBYear);

        // Retrieve current user's ID using method in Firebase library
        currentUserId = mAuth.getCurrentUser().getUid();
        currentUserRef = userRef.child(currentUserId);
        CURRENT_STATE = "not_started";

        buttonMaintenance();
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startButton.setEnabled(false);
                if (CURRENT_STATE.equals("not_started")) {
                    StartRecovery();
                }
                if (CURRENT_STATE.equals("started")) {
                    startButton.setText("Restart");
                    RestartRecovery();
                }
            }
        });

        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Report.class);
                startActivity(intent);
            }
        });


        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    calculateTotalTime();
                    handler.post(new Runnable() {
                        public void run() {
                            PBSecond.setProgress((int) secondsDifference%60);
                            PBMinute.setProgress((int) minutesDifference%60);
                            PBHour.setProgress((int) hoursDifference%24);
                            PBDay.setProgress((int) daysDifference%30);
                            PBMonth.setProgress((int) monthsDifference%12);
                            PBYear.setProgress((int) yearsDifference);

                        }
                    });
                    try {
                        // Sleep for 50 ms to show progress you can change it as well.
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


        return view;
    }

//    private void buttonMaintenance() {
//        userRef.child(currentUserId)
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.hasChild(currentUserId)) {
//                            DataSnapshot userSnapshot = snapshot.child(currentUserId);
//
//                            if (userSnapshot.hasChild("recovery_status")) {
//                                String status_type = userSnapshot.child("recovery_status").getValue(String.class);
//
//                                if ("started".equals(status_type)) {
//                                    CURRENT_STATE = "started";
//                                    startButton.setText("Restart");
//                                } else if ("not_started".equals(status_type)) {
//                                    CURRENT_STATE = "not_started";
//                                    startButton.setText("Start");
//                                } else {
//                                    // Handle unexpected status types
//                                }
//                            } else {
//                                CURRENT_STATE = "not_started";
//                                startButton.setText("Start");
//                            }
//                        } else {
//                            CURRENT_STATE = "not_started";
//                            startButton.setText("Start");
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        // Handle error
//                    }
//                });
//    }

    private void buttonMaintenance() {
        userRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("recovery_status")) {
                    String status_type = snapshot.child("recovery_status").getValue(String.class);

                    if ("started".equals(status_type)) {
                        CURRENT_STATE = "started";
                        startButton.setText("Restart");
                        startButton.setEnabled(true);
                    } else if ("not_started".equals(status_type)) {
                        CURRENT_STATE = "not_started";
                        startButton.setText("Start");
                        startButton.setEnabled(true);
                    } else {
                        // Handle unexpected status types
                    }
                } else {
                    CURRENT_STATE = "not_started";
                    startButton.setText("Start");
                }

            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    private void calculateTotalTime() {
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDateTime = new SimpleDateFormat("dd-MMMM-yyyy HH:mm:ss");
        saveCurrentDateTime = currentDateTime.format(calForDate.getTime());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

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

                    secondsDifference = timeDifference / 1000;
                    minutesDifference = secondsDifference / 60;
                    hoursDifference = minutesDifference / 60;
                    daysDifference = hoursDifference / 24;
                    monthsDifference = daysDifference / 30;
                    yearsDifference = monthsDifference / 12;


                } else {
                    hoursDifference = 0;
                }

                userRef.child(currentUserId).child("totalTime").setValue(hoursDifference).addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //Toast.makeText(getContext(), "Your total time is updated", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void RestartRecovery() {
        showStartOverConfirmationDialog();
        buttonMaintenance();
    }

    private void showStartOverConfirmationDialog() {
        WeakReference<FragmentActivity> activityRef = new WeakReference<>(getActivity());

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Start Over Confirmation");
        builder.setMessage("Are you sure you want to start over?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FragmentActivity activity = activityRef.get();
                if (activity != null && !activity.isFinishing()) {
                    // User clicked Yes, stop the old timer and start over
                    userRef.child(currentUserId).child("date")
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    userRef.child(currentUserId).child("recovery_status").setValue("not_started").addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            CURRENT_STATE = "not_started";
                                            startButton.setText("Start");
                                            buttonMaintenance();
                                        }
                                    });

                                }
                            });
                }
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
                userRef.child(currentUserId)
                        .child("recovery_status").setValue("started")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                startButton.setEnabled(true);
                                CURRENT_STATE = "started";
                                startButton.setText("Restart");
                            }
                        });
            }
        });


    }
}
