package com.example.drugrecoveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.drugrecoveryapp.adapter.SearchFriendAdapter;
import com.example.drugrecoveryapp.entity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class FindFriendsActivity extends AppCompatActivity {

    private EditText SearchInputText;

    private SearchFriendAdapter searchFriendAdapter;
    private List<User> userList;

    private DatabaseReference usersRef;
    String currentUserUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);

        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        currentUserUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        userList = new ArrayList<>();
        searchFriendAdapter = new SearchFriendAdapter(userList);
        RecyclerView searchResultList = (RecyclerView) findViewById(R.id.search_result_list);
        searchResultList.setHasFixedSize(true);
        searchResultList.setLayoutManager(new LinearLayoutManager(this));
        searchResultList.setAdapter(searchFriendAdapter);

        ImageButton searchButton = (ImageButton) findViewById(R.id.search_people_friends_button);
        SearchInputText = (EditText) findViewById(R.id.search_box_input);
        Button btnBackSearchFriends=findViewById(R.id.btnBackSearchFriends);
        btnBackSearchFriends.setOnClickListener(v -> finish());

        searchButton.setOnClickListener(v -> {
            // retrieve text from searchinputtext
            String searchInputText = SearchInputText.getText().toString();
            SearchPeopleAndFriends(searchInputText);
        });
    }

    private void SearchPeopleAndFriends(String searchInputText) {
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //Retrieves data from the snapshot and creates a User object by converting the retrieved data into an instance of the User class.
                    User user = snapshot.getValue(User.class);

                    //Exclude current user in the search result
                    if (!currentUserUid.equals(user.getUid())) {

                        //Search using username,email,id,full name or phone number (not case-sensitive)
                        if (user.getUsername().toLowerCase().contains(searchInputText.toLowerCase()) || user.getEmail().toLowerCase().contains(searchInputText.toLowerCase())||user.getUid().toLowerCase().contains(searchInputText.toLowerCase())) {
                            userList.add(user);
                        }
                    }
                }
                // Sort userList based on username
                Collections.sort(userList, new Comparator<User>() {
                    @Override
                    public int compare(User user1, User user2) {
                        return user1.getUsername().compareToIgnoreCase(user2.getUsername());
                    }
                });
                searchFriendAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}