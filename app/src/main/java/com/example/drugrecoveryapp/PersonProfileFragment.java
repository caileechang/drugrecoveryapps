package com.example.drugrecoveryapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drugrecoveryapp.entity.Post;
import com.example.drugrecoveryapp.entity.PostAdapter;
import com.example.drugrecoveryapp.entity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;

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

        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_person_profile, container, false);

        // Get reference to UI elements
        TextView username = root.findViewById(R.id.P_UsernameDisplay);
        TextView email = root.findViewById(R.id.P_emailDisplay);
        TextView country = root.findViewById(R.id.P_countryDisplay);
        TextView gender = root.findViewById(R.id.P_genderDisplay);
        recyclerView = root.findViewById(R.id.P_posts_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        postAdapter = new PostAdapter(new ArrayList<>());
        recyclerView.setAdapter(postAdapter);


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
                country.setText(snapshot.child("countryName").getValue(String.class));
                gender.setText(snapshot.child("gender").getValue(String.class));

                // Retrieve updated profile picture information
                String base64Image = snapshot.child("profilePicture").getValue(String.class);

                if (base64Image != null && !base64Image.isEmpty()) {
                    // Convert Base64 string to Bitmap (you can use your preferred method)
                    Bitmap bitmap = base64ToBitmap(base64Image);

                    // Set the user DP (Profile Picture)
                    setProfilePicture(bitmap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Fetch and display post data
        fetchPostData();
        return root;
    }

    private void fetchPostData() {
        DatabaseReference postsReference = FirebaseDatabase.getInstance().getReference("posts");

        // Assuming you have the user's ID
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getUid();

        postsReference.orderByChild("postBy").equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Post> postList = new ArrayList<>();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    if (post != null) {
                        postList.add(post);
                    }
                }

                // Sort the list based on postedAt (if not already sorted)
                Collections.sort(postList, new Comparator<Post>() {
                    @Override
                    public int compare(Post post1, Post post2) {
                        return Long.compare(post2.getPostedAt(), post1.getPostedAt());
                    }
                });

                // Assuming you have a postAdapter field in your class
                // and it's initialized properly
                if (postAdapter != null) {
                    postAdapter.setList(postList);
                } else {
                    // Initialize and set the adapter if not already done
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    postAdapter = new PostAdapter(postList);
                    recyclerView.setAdapter(postAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error fetching posts: " + error.getMessage());
            }
        });
    }

    private Bitmap base64ToBitmap(String base64Image) {
        byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    private void setProfilePicture(Bitmap bitmap) {
        ImageView profileImageView = root.findViewById(R.id.profilePhotoPerson);
        profileImageView.setImageBitmap(bitmap);
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

        Button BtnLogOut = view.findViewById(R.id.buttonLogOut);
        BtnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                if (getActivity() != null) {
                    getActivity().finish();
                }
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

}


