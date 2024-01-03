package com.example.drugrecoveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Reward extends AppCompatActivity {

    private Button btn1, btn2, btn3;
    private DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
    private DatabaseReference currentUserRef;
    private String currentUserId;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);

        btn1 = findViewById(R.id.button1);
        btn1.setEnabled(false);
        btn2 = findViewById(R.id.button2);
        btn2.setEnabled(false);
        btn3 = findViewById(R.id.button3);
        btn3.setEnabled(false);

        currentUserId = mAuth.getCurrentUser().getUid();

        currentUserRef = userRef.child(currentUserId);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(currentUserId).hasChild("totalTime")) {
                    String totalRecoveryTime = snapshot.child(currentUserId).child("totalTime").getValue().toString();
                    Long totalTime = Long.parseLong(totalRecoveryTime);

                    if(totalTime > 4){
                        btn1.setText("Tap to view");
                        btn1.setEnabled(true);
                    }
                    if(totalTime > 48){
                        btn2.setText("Tap to view");
                        btn2.setEnabled(true);
                    }
                    if(totalTime > 72){
                        btn3.setText("Tap to view");
                        btn3.setEnabled(true);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }
}