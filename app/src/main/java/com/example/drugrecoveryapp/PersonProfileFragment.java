package com.example.drugrecoveryapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters

    private View root;
    private String mParam1;
    private String mParam2;

    public PersonProfileFragment() {

        super(R.layout.fragment_person_profile);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PersonProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PersonProfileFragment newInstance(String param1, String param2) {
        PersonProfileFragment fragment = new PersonProfileFragment();
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
        root = inflater.inflate(R.layout.fragment_person_profile, container, false);

        // Get reference to UI elements
        TextView username = root.findViewById(R.id.P_UsernameDisplay);
        TextView email = root.findViewById(R.id.P_emailDisplay);
//        TextView phoneNumber = root.findViewById(R.id.P_phDisplay);
        TextView country = root.findViewById(R.id.P_countryDisplay);
        TextView gender = root.findViewById(R.id.P_genderDisplay);


        // Get Firebase instance and reference
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        String uid = auth.getUid();

        // fetch user profile data from database
        databaseReference.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // update UI with retrieved user data
                username.setText(snapshot.child("username").getValue(String.class));
                email.setText(snapshot.child("email").getValue(String.class));
//                phoneNumber.setText(snapshot.child("phone_number").getValue(String.class));
                country.setText(snapshot.child("countryName").getValue(String.class));
                gender.setText(snapshot.child("gender").getValue(String.class));

                //Set User DP (After user has
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return root;
    }



    public void onViewCreated(View view, Bundle savedInstanceState){
        Button BtnFriend = view.findViewById(R.id.friend_btn);
        BtnFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.NHFMain);
                navController.navigate(R.id.destfriendActivity);
            }
        });
        Button edit=view.findViewById(R.id.buttonEditProfile);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.NHFMain);
                navController.navigate(R.id.editProfile);
            }
        });

        Button BtnRequest = view.findViewById(R.id.request_btn);
        BtnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.NHFMain);
                navController.navigate(R.id.destrequestActivity);
            }
        });
    }
    //Creating ActionBar Menu

//    HV
//    public boolean onCreateOptionsMenu(Menu menu){
//        getMenuInflater().inflate(R.menu.menu_view_acc, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NotNull MenuItem item){
//        int id=item.getItemId();
//
//        return super.onOptionsItemSelected(item);
//    }



}

