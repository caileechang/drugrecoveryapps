package com.example.drugrecoveryapp;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ReccoveryTrackFragment extends Fragment {

    private TextView timerTextView;
    private ProgressBar progressBar;
    private Button startButton;
    private Button startOverButton;

    // used for service connection and binding
    // timerService is an instance of the backgroundTimerService that will be used to interact with the service
    private BackgroundTimerService timerService;

    // a flag to track whether the fragment is currently bound to the service
    private boolean isServiceBound = false;

    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;
    private long totalTimeInMillis; // Total time in milliseconds
    private long elapsedTimeInMillis; // Elapsed time since starting the recovery journey
    private long totalRecoveryTime; // Total recovery time stored for the user

    private static final long MILESTONE_ONE_DAY = 24 * 60 * 60 * 1000; // One day milestone
    private static final long MILESTONE_TWO_DAYS = 2 * MILESTONE_ONE_DAY; // Two days milestone

    private static final String PREFS_NAME = "RecoveryPrefs";
    private static final String KEY_TOTAL_RECOVERY_TIME = "TotalRecoveryTime";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reccovery_track, container, false);

        timerTextView = view.findViewById(R.id.timerTextView);
        progressBar = view.findViewById(R.id.progressBar);
        startButton = view.findViewById(R.id.startButton);
        startOverButton = view.findViewById(R.id.startOverButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecovery();
            }
        });

        startOverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startOver();
            }
        });

        // Retrieve total recovery time for the user
        totalRecoveryTime = loadTotalRecoveryTime();

        // Update UI with the stored total recovery time
        updateProgressBars();

        return view;
    }

    // initiates the process of binding to the background service
    private void bindTimerService() {
        Intent intent = new Intent(requireContext(), BackgroundTimerService.class);
        // initiate the binding process and automatically creates the service as part of the binding
        requireContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        isServiceBound = true;
    }

    private void unbindTimerService() {
        if (isServiceBound) {
            requireContext().unbindService(serviceConnection);
            isServiceBound = false;
        }
    }

    private void startRecovery() {
        stopRecoveryTimer();
        // Calculate total time (you may customize this logic based on your requirements)
        totalTimeInMillis = calculateTotalTime();

        // Initialize CountDownTimer
        countDownTimer = new CountDownTimer(totalTimeInMillis - totalRecoveryTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                elapsedTimeInMillis = totalTimeInMillis - millisUntilFinished;
                updateTimerUI();
                updateProgressBars();
                checkMilestones();
            }

            @Override
            public void onFinish() {
                // Handle timer finish (optional)
            }
        };

        // Start the timer
        countDownTimer.start();

        // Update UI as needed (e.g., hide start button)
        startButton.setVisibility(View.GONE);
    }

    private void startOver() {
        // Reset total recovery time and restart the timer
        totalRecoveryTime = 0;
        saveTotalRecoveryTime(totalRecoveryTime);
        startRecovery();
        showStartOverConfirmationDialog();
    }

    private void updateTimerUI() {
        // Update timer TextView with the formatted time
        String formattedTime = formatTime(elapsedTimeInMillis);
        timerTextView.setText(formattedTime);
    }

    private void updateProgressBars() {
        // Update progress bars based on elapsed time
        int progress = (int) ((float) (elapsedTimeInMillis + totalRecoveryTime) / totalTimeInMillis * 100);
        progressBar.setProgress(progress);
    }

    private void startNewRecovery() {
        // Stop the existing timer if running
        stopRecoveryTimer();

        // Reset timer values
        // You might need to reset other variables related to your timer, such as progress bar, etc.
        // ...

        // Start a new recovery timer
        startRecovery();
    }

    private void stopRecoveryTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            isTimerRunning = false;
        }
    }
    private void showStartOverConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Start Over Confirmation");
        builder.setMessage("Are you sure you want to start over?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked Yes, stop the old timer and start over
                stopRecoveryTimer();
                totalRecoveryTime = 0;
                saveTotalRecoveryTime(totalRecoveryTime);
                startRecovery();
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

    //a connection that handles the binding and unbinding of the service
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // localBinder is the inner class within backgroundTimerService that provides the service instance
            BackgroundTimerService.LocalBinder binder = (BackgroundTimerService.LocalBinder) service;
            //retrieve the service instance
            timerService = binder.getService();
            // Perform operations related to the service, if needed
        }

        // called if the connection to the service is unexpectedly disconnected
        @Override
        public void onServiceDisconnected(ComponentName name) {
            timerService = null;
        }
    };

    // called when the fragment becomes visible
    @Override
    public void onStart() {
        super.onStart();
        bindTimerService();
    }

    // Called when the fragment is no longer visible
    @Override
    public void onStop() {
        super.onStop();
//        unbindTimerService();
    }



    private void checkMilestones() {
        // Check if milestones are reached and unlock awards
        if ((elapsedTimeInMillis + totalRecoveryTime) >= MILESTONE_ONE_DAY) {
            unlockAward("One Day Sober");
        }
        if ((elapsedTimeInMillis + totalRecoveryTime) >= MILESTONE_TWO_DAYS) {
            unlockAward("Two Days Sober");
        }
        // Add more milestones and awards as needed
    }

    private void unlockAward(String award) {
        // Implement logic to unlock the award (e.g., show a message, update UI)
    }

    // Add other utility methods as needed

    private String formatTime(long millis) {
        // Format time in hours, minutes, seconds (you can customize this)
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        return String.format("%02d:%02d:%02d", hours % 24, minutes % 60, seconds % 60);
    }

    private long calculateTotalTime() {
        // Implement logic to calculate the total time based on user preferences
        // For example, you might want to load the total time from SharedPreferences
        return 7 * 24 * 60 * 60 * 1000; // 7 days as an example
    }

    private void saveTotalRecoveryTime(long time) {
        // Save the total recovery time for the user
        SharedPreferences preferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(KEY_TOTAL_RECOVERY_TIME, time);
        editor.apply();
    }

    private long loadTotalRecoveryTime() {
        // Load the total recovery time for the user
        SharedPreferences preferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return preferences.getLong(KEY_TOTAL_RECOVERY_TIME, 0);
    }
}
