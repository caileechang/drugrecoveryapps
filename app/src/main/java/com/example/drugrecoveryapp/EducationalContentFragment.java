package com.example.drugrecoveryapp;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.MotionEffect;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.drugrecoveryapp.educationResources.Drug;
import com.example.drugrecoveryapp.educationResources.DrugsAdapter;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EducationalContentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EducationalContentFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<Drug> drugArrayList = new ArrayList<Drug>();
    private RecyclerView rvDrug;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public EducationalContentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EducationalContentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EducationalContentFragment newInstance(String param1, String param2) {
        EducationalContentFragment fragment = new EducationalContentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_educational_content, container, false);
        rvDrug = rootView.findViewById(R.id.rvCollection);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://recoveryhope-default-rtdb.firebaseio.com/");

        if (currentUser != null) {
            DatabaseReference userIdRef = databaseReference.child("Users").child(currentUser.getUid()).child("collection");

            db.collection("Resources").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String documentID = document.getId();

                        userIdRef.child(documentID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Log.d(TAG, documentID + " => " + document.getData());
                                Drug drug = new Drug(documentID, document.getString("Short Description"), true);
                                drug.setDocumentID(document.getId());
                                if (dataSnapshot.exists()) {
                                    drug.setBookmark(true);
                                }

                                drugArrayList.add(drug);

                                DrugsAdapter adapter = new DrugsAdapter(drugArrayList);
                                rvDrug.setAdapter(adapter);
                                rvDrug.setLayoutManager(new LinearLayoutManager(getActivity()));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.e(TAG, "Error reading data from Firebase: " + databaseError.getMessage());
                            }
                        });
                    }
                } else {
                    Log.e(TAG, "Error getting documents from Firestore: " + task.getException());
                }
            });
        }

        //Search Engine Feature
        EditText searchKeyword = rootView.findViewById(R.id.search_keyword);
        Button BtnSearch = rootView.findViewById(R.id.BtnSearch);
        BtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keyword = searchKeyword.getText().toString();
                drugArrayList.clear();
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();

                // Assuming you have a reference to your Realtime Database (replace "databaseReference" with your actual database reference)
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://recoveryhope-default-rtdb.firebaseio.com/");
                if (currentUser != null) {
                    DatabaseReference userIdRef = databaseReference.child("Users").child(currentUser.getUid()).child("collection");

                    db.collection("Resources").get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String drugName = document.getId().toString();
                                String shortDescription = document.getString("Short Description");
                                if (shortDescription != null && shortDescription.toLowerCase().contains(keyword) || drugName.toLowerCase().contains(keyword)) {

                                    Log.d(TAG, document.getId() + "=>" + document.getData());
                                    Drug drug = new Drug(document.getId(), shortDescription, true);
                                    // Assuming you have a user ID (replace "userId" with the actual user ID)

                                    String documentID = document.getId();
                                    userIdRef.child(documentID).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                // The document ID exists under the current user's collection
                                                drug.setBookmark(true);
                                                drug.setDocumentID(document.getId());
                                                drugArrayList.add(drug);
                                            } else {
                                                // The document ID does not exist under the current user's collection
                                                drug.setBookmark(false);
                                                drug.setDocumentID(document.getId());
                                                drugArrayList.add(drug);
                                            }

                                            // Create the adapter and set it to the recyclerview
                                            DrugsAdapter adapter = new DrugsAdapter(drugArrayList);
                                            rvDrug.setAdapter(adapter);

                                            // Set layout manager to position the items
                                            rvDrug.setLayoutManager(new LinearLayoutManager(getActivity()));
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            // Handle error
                                        }
                                    });
                                } else {
                                    Log.d(MotionEffect.TAG, "Error getting documents:", task.getException());
                                }

                            }
                        }



                    });
                }
            }
        });

        return rootView;
    }

}