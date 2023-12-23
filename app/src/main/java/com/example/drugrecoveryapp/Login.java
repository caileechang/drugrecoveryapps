package com.example.drugrecoveryapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button LoginButton;
    private EditText UserEmail, UserPassword;
    private ProgressDialog loadingBar;

    private DatabaseReference userRef;

    private TextView bottomRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        userRef=FirebaseDatabase.getInstance().getReference().child("Users");

        UserEmail = (EditText) findViewById(R.id.input_email);
        UserPassword = (EditText) findViewById(R.id.input_password);
        LoginButton = (Button) findViewById(R.id.login_button);
        TextView forgotPassword = findViewById(R.id.forgot);
        loadingBar = new ProgressDialog(this);
        bottomRegister=findViewById(R.id.bottomRegister);

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgotPassword.class);
                startActivity(intent);
                finish();
            }
        });

        //        Show Hide Password using eye icon
        ImageView imageViewShowHidePwd=findViewById(R.id.imageView_show_hide_pwd);
        imageViewShowHidePwd.setImageResource(R.drawable.closed_eye);
        imageViewShowHidePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(UserPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    UserPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imageViewShowHidePwd.setImageResource(R.drawable.closed_eye);
                }else{
                    UserPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageViewShowHidePwd.setImageResource(R.drawable.open_eye);
                }
            }
        });

        bottomRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
                finish();
            }
        });

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });
    }

    private void Login() {
        String email = UserEmail.getText().toString();
        String password = UserPassword.getText().toString();

        //cannot leave empty space at email and password field
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please write your email...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
        } else {
            // Loading bar message
            loadingBar.setTitle("Logging in...");
            loadingBar.setMessage("Please wait, we are logging your account...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        String userId = mAuth.getCurrentUser().getUid();
                        // Check if user exists in "Users" node
                        userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    // User exists in the "Users" node
                                    Toast.makeText(Login.this, "You are logged in successfully", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // User does not exist in the "Users" node
                                    Toast.makeText(Login.this, "User not found. Login failed.", Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle error
                                loadingBar.dismiss();
                            }
                        });
                    } else {
                        String message = "Please recheck your email or password";
                        Toast.makeText(Login.this, "Error occurred: " + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }



}


