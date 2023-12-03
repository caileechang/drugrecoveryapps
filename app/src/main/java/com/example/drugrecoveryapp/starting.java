package com.example.drugrecoveryapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class starting extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // launch activity(page)
        setContentView(R.layout.activity_starting);
        // find the element by its id
        Button startbutton = findViewById(R.id.startbutton);
        // Objects LoginBtn to run
        startbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                DatabaseReference transactionReference = FirebaseDatabase.getInstance().getReference();
//                transactionReference.child("User").setValue("hello");

                view.startAnimation(buttonClick);
                Intent intent = new Intent(starting.this, Login.class);
                startActivity(intent);
            }
        });

    }
}