package com.example.drugrecoveryapp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.drugrecoveryapp.entity.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText UserName,UserEmail,UserPassword,UserConfrimPassword;
    private ProgressDialog loadingBar;
    private Button CreateAccountButton;
    private User user;

    private TextView bottomLogin;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth= FirebaseAuth.getInstance();

        UserName=(EditText) findViewById(R.id.register_username);
        UserEmail=(EditText) findViewById(R.id.register_email);
//        UserPhoneNumber=(EditText) findViewById(R.id.register_phonenumber) ;
        UserPassword=(EditText) findViewById(R.id.register_password);
        UserConfrimPassword=(EditText) findViewById(R.id.register_cofirmpassword);
        CreateAccountButton=(Button) findViewById(R.id.register_create_account);

        loadingBar=new ProgressDialog(this);
        CreateAccountButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //method of creating new account
                CreateNewAccount();
            }
        });

        // Show Hide Password for UserPassword
        ImageView imageViewShowHidePwd = findViewById(R.id.imageView_show_hide_pwd2);
        imageViewShowHidePwd.setImageResource(R.drawable.closed_eye);

        imageViewShowHidePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    UserPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imageViewShowHidePwd.setImageResource(R.drawable.closed_eye);
                } else {
                    UserPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageViewShowHidePwd.setImageResource(R.drawable.open_eye);
                }
            }
        });


// Show Hide Password for UserConfirmPassword
        ImageView imageViewShowHidePwdConfirm = findViewById(R.id.imageView_show_hide_pwd3);
        imageViewShowHidePwdConfirm.setImageResource(R.drawable.closed_eye);

        imageViewShowHidePwdConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserConfrimPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    UserConfrimPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imageViewShowHidePwdConfirm.setImageResource(R.drawable.closed_eye);
                } else {
                    UserConfrimPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageViewShowHidePwdConfirm.setImageResource(R.drawable.open_eye);
                }
            }
        });

        bottomLogin = findViewById(R.id.bottomLogin);
        bottomLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public void CreateNewAccount(){

        //get text from user input
        String username=UserName.getText().toString();
        String email=UserEmail.getText().toString();
//        String phoneNumber=UserPhoneNumber.getText().toString();
        String password=UserPassword.getText().toString();
        String confirmPassword=UserConfrimPassword.getText().toString();
//        String mobileRegex = "\\+[0-9]{2}[6-9][0-9]{9}";
//        Matcher mobileMatcher;
//        Pattern mobilePattern=Pattern.compile(mobileRegex);
//        mobileMatcher=mobilePattern.matcher(phoneNumber);

        //prompt user to key in the information if the column is still empty
        if(TextUtils.isEmpty(username)){
            Toast.makeText(Register.this,"Please insert your username...",Toast.LENGTH_LONG).show();
            UserName.setError("Username is required");
            UserName.requestFocus();

        }else if(TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please insert your email...", Toast.LENGTH_LONG).show();
            UserEmail.setError("Email is required");
            UserEmail.requestFocus();
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(Register.this, "Please re-enter your email", Toast.LENGTH_LONG).show();
            UserEmail.setError("Valid email is required");
            UserEmail.requestFocus();

//        }else if(TextUtils.isEmpty(phoneNumber)) {
//            Toast.makeText(this, "Please insert your phone number...", Toast.LENGTH_SHORT).show();
//            UserPhoneNumber.setError("Valid phone number is required");
//            UserPhoneNumber.requestFocus();
//
//        }else if(phoneNumber.length()!=12){
//            Toast.makeText(Register.this, "Please re-enter your mobile no.", Toast.LENGTH_LONG).show();
//            UserPhoneNumber.setError("Mobile No. should be 12 digits (including +countrycode)");
//            UserPhoneNumber.requestFocus();

//        }else if(!mobileMatcher.find()){
//            Toast.makeText(Register.this, "Please re-enter your mobile no.", Toast.LENGTH_LONG).show();
//            UserPhoneNumber.setError("Mobile No. is not valid");
//            UserPhoneNumber.requestFocus();

        }else if(TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please insert your password...", Toast.LENGTH_LONG).show();
            UserPassword.setError("Password is required");
            UserPassword.requestFocus();

        }else if(password.length()<6){
            Toast.makeText(Register.this,"Password should be at least 6 digits",Toast.LENGTH_LONG).show();
            UserPassword.setError("Password too weak");
            UserPassword.requestFocus();

        }else if(TextUtils.isEmpty(confirmPassword)){
            Toast.makeText(this,"Please insert your password...",Toast.LENGTH_LONG).show();
            UserConfrimPassword.setError("Password is required");
            UserConfrimPassword.requestFocus();

        }else if(!(password.equals(confirmPassword))) {
            Toast.makeText(this, "Your password do not match with your confirm password...", Toast.LENGTH_LONG).show();
            UserConfrimPassword.setError("Password Confirmation is required");
            UserConfrimPassword.requestFocus();

            //Clear the entered passwords
            UserPassword.clearComposingText();
            UserConfrimPassword.clearComposingText();

        }else{
            //all the columns are filled,start creating new account

            //loading bar message
            loadingBar.setTitle("Creating New Account...");
            loadingBar.setMessage("Please wait, we are creating your new account...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);
            //use email and password to create
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    // Tell the user whether they were successful in creating an account or not
                    if (task.isSuccessful()) {
                        Toast.makeText(Register.this, "You are authenticated successfully...", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();

                        // Create a new User object with the provided information
                        user = new User(FirebaseAuth.getInstance().getUid(), username, email, "", "");

                        // Store the user object in the "Users" node of the Firebase Realtime Database
                        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).setValue(user);

                        // Start the Setup_Activity
                        Intent mainIntent = new Intent(Register.this, Setup.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(mainIntent);
                        finish();
                    } else {
                        // Display the error message to the user
                        String message = task.getException().getMessage();
                        Toast.makeText(Register.this, "Error Occurred: " + message, Toast.LENGTH_SHORT);
                        loadingBar.dismiss();
                    }
                }

            });
        }

    }


}
