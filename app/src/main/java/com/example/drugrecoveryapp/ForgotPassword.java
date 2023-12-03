package com.example.drugrecoveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.drugrecoveryapp.databinding.ActivityForgotPasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    ActivityForgotPasswordBinding binding;
    ProgressDialog dialog;
    FirebaseAuth auth;

    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_forgot_password);
        binding=ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth=FirebaseAuth.getInstance();

        dialog=new ProgressDialog((ForgotPassword.this));
        dialog.setCancelable(false);
        dialog.setMessage("Loading....");

        binding.submitbutton.setOnClickListener(view ->{
            forgotPassword();
        });

        back = findViewById(R.id.btnBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private Boolean validateEmail(){
        String val=binding.forgotPasswordEmail.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(val.isEmpty()){
            binding.forgotPasswordEmail.setError("Field cannot be empty");
            return false;

        }else if(!val.matches(emailPattern)){
            binding.forgotPasswordEmail.setError("Invalid email address");
            return false;
        }else{
            binding.forgotPasswordEmail.setError(null);
            return true;
        }
    }
    private void forgotPassword() {

        if(!validateEmail()){
            return;
        }

        dialog.show();
        auth.sendPasswordResetEmail(binding.forgotPasswordEmail.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        dialog.dismiss();
                        if (task.isSuccessful()) {
                            startActivity(new Intent(ForgotPassword.this, Login.class));
                            finish();
                            Toast.makeText(ForgotPassword.this, "Please Check your email Address!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ForgotPassword.this, "Enter Correct email Id", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        if (e.getMessage() != null && e.getMessage().contains("There is no user record corresponding to this identifier")) {
                            // This error message indicates that the email is not registered
                            Toast.makeText(ForgotPassword.this, "Email is not registered. Please enter a registered email.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ForgotPassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}