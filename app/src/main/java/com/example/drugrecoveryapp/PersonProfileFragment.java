package com.example.drugrecoveryapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_person_profile, container, false);

//        // Get reference to UI elements
//        TextView username = root.findViewById(R.id.UsernameDisplay);
//        TextView email = root.findViewById(R.id.emailDisplay);
//        TextView phoneNumber = root.findViewById(R.id.phDisplay);
//        TextView country = root.findViewById(R.id.countryDisplay);
//        TextView gender = root.findViewById(R.id.genderDisplay);
//
//        // Get Firebase instance and reference
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
//        String uid = auth.getUid();
        return root;
    }
}