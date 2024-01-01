package com.example.drugrecoveryapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Locale;

import com.example.drugrecoveryapp.entity.User;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;



public class Setup extends AppCompatActivity {
    private RadioGroup gender;
    private RadioButton genderSelection;
    private Button SaveInfoButton, skipBtn;
    private ImageView ProfilePicture;
    Uri imageUri;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private DatabaseReference reference;
    private ProgressDialog loadingBar;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    String currentUserID;
    private User user;

    private AutoCompleteTextView countryAutoComplete;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        // Initialize Firebase authentication, retrieve the current user ID, and get the database reference
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    user=snapshot.getValue(User.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Initialize UI elements and set up event listeners

        countryAutoComplete = findViewById(R.id.editCountry);

// Populate the AutoCompleteTextView with a list of countries
        String[] countries = getCountryList();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, countries);
        countryAutoComplete.setAdapter(adapter);

// Set threshold to 1, so suggestions appear after typing the first character
        countryAutoComplete.setThreshold(1);


        gender = (RadioGroup) findViewById(R.id.editGender);
        SaveInfoButton = (Button) findViewById(R.id.save_job);
        loadingBar = new ProgressDialog(this);

        skipBtn = findViewById(R.id.skipBtn);
        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
        // Handle the click event on the SaveInfoButton
        SaveInfoButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                SaveAccountSetupInformation();
            }


            @RequiresApi(api = Build.VERSION_CODES.O)
            // Method to save the user account setup information
            private void SaveAccountSetupInformation() {

                // Retrieve input values from UI elements

                String countryName = countryAutoComplete.getText().toString();


                genderSelection = (RadioButton) findViewById(gender.getCheckedRadioButtonId());


                if (genderSelection == null) {
                    // Handle the case where no gender is selected
                    Toast.makeText(Setup.this, "Please select your gender", Toast.LENGTH_SHORT).show();
                    return; // Exit the method to avoid further processing
                }



                // Check if any required field is empty, display a toast if true
                if (TextUtils.isEmpty(countryName) ) {
                    Toast.makeText(Setup.this, "Please insert your information...", Toast.LENGTH_SHORT).show();
                } else {
                    loadingBar.setTitle("Setting up account...");
                    loadingBar.setMessage("Please wait, we are saving your account information...");
                    loadingBar.show();
                    loadingBar.setCanceledOnTouchOutside(true);
                    // Update the user object with the entered information
                    // Update the user data in the database and handle the completion
                    user.setCountryName(countryName);
                    user.setGender(genderSelection.getText().toString());
                    reference.setValue(user)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Setup.this, "Saved", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                        SendUserToMainActivity();
                                    } else {
                                        Toast.makeText(Setup.this, "Something error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
            }
        });
    }

    private String[] getCountryList() {
        String[] locales = Locale.getISOCountries();
        ArrayList<String> countryList = new ArrayList<>();
        for (String countryCode : locales) {
            Locale obj = new Locale("", countryCode);
            countryList.add(obj.getDisplayCountry());
        }
        return countryList.toArray(new String[0]);
    }

    //after setting up the account, users will be sent to main activity
    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(Setup.this, Login.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }


}




