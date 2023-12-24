package com.example.drugrecoveryapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import com.example.drugrecoveryapp.PersonProfileFragment;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.drugrecoveryapp.entity.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.Intent;
import android.provider.MediaStore;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class EditProfile extends AppCompatActivity {

    private TextView usernameTextView;
    private TextView emailTextView;
    private RadioGroup genderRadioGroup;
    private TextView countryTextView;
    private Button updateBtn;

    private String currentUserID;
    private User user; // Declare User object

    private ImageView cameraIcon;
    private ImageView profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize UI components
        usernameTextView = findViewById(R.id.usernameBox);
        emailTextView = findViewById(R.id.emailBox);
        genderRadioGroup = findViewById(R.id.editGenderUpdate);
        countryTextView = findViewById(R.id.CountryBox);
        updateBtn = findViewById(R.id.UpdateBtn);
        profileImageView = findViewById(R.id.profilePhoto);

        // Get the current user's UID
        currentUserID = FirebaseAuth.getInstance().getUid();

        // Get a reference to the "Users" node in the Firebase Realtime Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        // Fetch user profile data from the database
        databaseReference.child(currentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Check if the snapshot exists
                if (snapshot.exists()) {
                    // Retrieve user data
                    user = snapshot.getValue(User.class);

                    // Check if user is not null
                    if (user != null) {
                        // Update UI with retrieved user data
                        usernameTextView.setText(user.getUsername());
                        emailTextView.setText(user.getEmail());
                        updateGenderRadioGroup(user.getGender());
                        countryTextView.setText(user.getCountryName());
                        
                        loadProfilePicture(user.getProfilePicture());
                    } else {
                        // Handle the case where user data is null
                        handleNullUserDataError();
                    }
                } else {
                    // Handle the case where the snapshot doesn't exist
                    handleSnapshotNotFoundError();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error during database operation
                handleDatabaseError(error);
            }
        });

        cameraIcon = findViewById(R.id.camera);
        cameraIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the gallery to choose a photo
                openGallery();
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the method to update user information
                updateUserInfo();
            }
        });
    }

    // Add this method to load and display the profile picture


    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            // Handle the selected image from the gallery
            Uri selectedImageUri = data.getData();
            Bitmap bitmap = decodeUri(selectedImageUri);

            // Convert the Bitmap to a Base64 string (you can use your preferred method)
            String base64Image = bitmapToBase64(bitmap);

            // Update the user object with the new profile picture information
            user.setProfilePicture(base64Image);

            // Update the profile picture in the database
            DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
            userReference.child("profilePicture").setValue(base64Image);

//            ImageView profileImageView=findViewById(R.id.profilePhoto);
            profileImageView.setImageBitmap(bitmap);
            // Optionally, update the UI to display the selected image
            // imageView.setImageBitmap(bitmap);
        }
    }

    // Decode the selected image URI to a Bitmap
    private Bitmap decodeUri(Uri selectedImage) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(selectedImage);
            return BitmapFactory.decodeStream(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Convert Bitmap to Base64 string
    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void updateUserInfo() {
        // Retrieve input values from UI elements
        String newUsername = usernameTextView.getText().toString();
        String newEmail = emailTextView.getText().toString();
        String newGender = getSelectedGender();
        String newCountry = countryTextView.getText().toString();

        // Update the user object with the edited information
        user.setUsername(newUsername);
        user.setEmail(newEmail);
        user.setGender(newGender);
        user.setCountryName(newCountry);

        // Check if the profile picture has been updated
        if (user.getProfilePicture() != null && !user.getProfilePicture().isEmpty()) {
            // Update the profile picture in the database
            DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
            userReference.child("profilePicture").setValue(user.getProfilePicture());
        }

        // Get a reference to the "Users" node in the Firebase Realtime Database
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);

        // Update the user data in the database and handle the completion
        userReference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(EditProfile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    finish();

                    // U are directing from a fragment coiner viewer to an activity
                    // so when u update ur profile （activity), after updating, then should end the activity
                    // use finish(), then back to the fragment

                    // Navigate to PersonProfileFragment
//                    // Navigate to PersonProfileFragment without specifying a container
                    /*Intent intent = new Intent(EditProfile.this, PersonProfileFragment.class);
                    intent.putExtra("selectedFragment", R.id.personProfileFragment);
                    startActivity(intent);
                    finish();*/

//                    FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.replace(R.id.mainContainer,new PersonProfileFragment()).commit();

//                    // Create a FragmentManager
//                    FragmentManager fragmentManager = getSupportFragmentManager();
//
//                    // Begin a FragmentTransaction
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//                    // Replace the current fragment with PersonProfileFragment
//                    fragmentTransaction.replace(R.id.mainContainer, new PersonProfileFragment());
//
//                    // Add the transaction to the back stack (optional)
//                    // This allows the user to navigate back to the previous fragment
//                    fragmentTransaction.addToBackStack(null);
//
//                    // Commit the transaction
//                    fragmentTransaction.commit();
                } else {
                    Toast.makeText(EditProfile.this, "Error updating profile", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String getSelectedGender() {
        // Retrieve the selected gender from the RadioGroup
        RadioButton selectedRadioButton = findViewById(genderRadioGroup.getCheckedRadioButtonId());
        return (selectedRadioButton != null) ? selectedRadioButton.getText().toString() : "";
    }

//    private void navigateToPersonProfileFragment() {
//        // Navigate using NavController (assuming you have a NavController instance)
//        NavController navController = Navigation.findNavController(this, R.id.NHFMain);
//        navController.navigate(R.id.personProfileFragment);
//        finish(); // Optional: finish the current activity if needed
//    }


    private void updateGenderRadioGroup(String gender) {
        // Find the RadioButton based on the gender and set it as checked
        if (gender != null) {
            switch (gender) {
                case "Male":
                    genderRadioGroup.check(R.id.male);
                    break;
                case "Female":
                    genderRadioGroup.check(R.id.female);
                    break;
            }
        }
    }

    private void handleNullUserDataError() {
        Toast.makeText(EditProfile.this, "User data is null", Toast.LENGTH_SHORT).show();
    }

    private void handleSnapshotNotFoundError() {
        Toast.makeText(EditProfile.this, "User data snapshot not found", Toast.LENGTH_SHORT).show();
    }

    private void handleDatabaseError(DatabaseError error) {
        Toast.makeText(EditProfile.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void loadProfilePicture(String base64Image) {
        if (base64Image != null && !base64Image.isEmpty()) {
            // Convert Base64 string to Bitmap
            Bitmap bitmap = base64ToBitmap(base64Image);
            // Display the profile picture in the ImageView
            profileImageView.setImageBitmap(bitmap);
        }
    }

    private Bitmap base64ToBitmap(String base64Image) {
        byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

}
