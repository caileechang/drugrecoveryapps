package com.example.drugrecoveryapp.educationResources;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drugrecoveryapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class BookmarkActivity extends AppCompatActivity {
    private ArrayList<Drug> collectionList = new ArrayList<>();
    private RecyclerView rvCollection;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        // Find the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_Collection);

        // Set the Toolbar as the ActionBar
        setSupportActionBar(toolbar);

        // Enable the back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Find and initialize views
        rvCollection = findViewById(R.id.rvCollection);

        // Set layout manager to position the items
        rvCollection.setLayoutManager(new LinearLayoutManager(this));

        // Assuming you have a user ID (replace "userId" with the actual user ID)
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Assuming you have a reference to your Realtime Database (replace "databaseReference" with your actual database reference)
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://recoveryhope-default-rtdb.firebaseio.com/");

        if (currentUser != null) {
            DatabaseReference userIdRef = databaseReference.child("Users").child(currentUser.getUid()).child("collection");

            db.collection("Resources").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String documentID = document.getId();

                        // Check if the document ID is present in the user's collection
                        userIdRef.child(documentID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    // Document ID is present in the user's collection
                                    Log.d(TAG, documentID + " => " + document.getData());

                                    // Create Drug object and add it to the list
                                    Drug drug = new Drug(documentID, document.getString("Short Description"), true);
                                    drug.setBookmark(true);
                                    drug.setDocumentID(document.getId());
                                    collectionList.add(drug);

                                    // Create the adapter and set it to the recyclerview
                                    DrugsAdapter adapter = new DrugsAdapter(collectionList);
                                    rvCollection.setAdapter(adapter);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Handle error, if needed
                            }
                        });
                    }
                } else {
                    Log.d(TAG, "Error getting documents:", task.getException());
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();  // Go back when the back button is pressed
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
