package com.example.drugrecoveryapp;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.drugrecoveryapp.educationResources.Drug;
import com.example.drugrecoveryapp.educationResources.DrugsAdapter;
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

        RecyclerView rvContacts = rootView.findViewById(R.id.rvContacts);

        ArrayList<Drug> drugArrayList = new ArrayList<Drug>();
        db.collection("Resources").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Log.d(TAG, document.getId() + "=>" + document.getData());
                    Drug drug = new Drug(document.getId().toString(), document.getString("Short Description"), true);
                    drugArrayList.add(drug);
                }

                // Create the adapter and set it to the recyclerview
                DrugsAdapter adapter = new DrugsAdapter(drugArrayList);
                rvContacts.setAdapter(adapter);

                // Set layout manager to position the items
                rvContacts.setLayoutManager(new LinearLayoutManager(getActivity()));
            } else {
                Log.d(TAG, "Error getting documents:", task.getException());
            }
        });


        return rootView;
    }

}