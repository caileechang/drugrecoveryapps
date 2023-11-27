//package com.example.drugrecoveryapp;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.ImageButton;
//
//import com.example.drugrecoveryapp.adapter.SearchFriendAdapter;
//import com.example.drugrecoveryapp.entity.User;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.List;
//
//public class FindFriendsActivity extends AppCompatActivity {
//
//    private ImageButton SearchButton;
//    private EditText SearchInputText;
//
//    private RecyclerView SearchResultList;
//
//    private SearchFriendAdapter searchFriendAdapter;
//    private List<User> userList;
//
//    private DatabaseReference usersRef;
//    String currentUserUid;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_find_friends);
//
//        usersRef = FirebaseDatabase.getInstance().getReference("Users");
//        currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//
//        userList = new ArrayList<>();
//        searchFriendAdapter = new SearchFriendAdapter(userList);
//        SearchResultList = (RecyclerView) findViewById(R.id.search_result_list);
//        SearchResultList.setHasFixedSize(true);
//        SearchResultList.setLayoutManager(new LinearLayoutManager(this));
//        SearchResultList.setAdapter(searchFriendAdapter);
//
//        SearchButton = (ImageButton) findViewById(R.id.search_people_friends_button);
//        SearchInputText = (EditText) findViewById(R.id.search_box_input);
//
//        SearchButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // retrieve text from searchinputtext
//                String searchInputText = SearchInputText.getText().toString();
//                SearchPeopleAndFriends(searchInputText);
//            }
//        });
//    }
//
//    private void SearchPeopleAndFriends(String searchInputText) {
//        usersRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                userList.clear();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    //Retrieves data from the snapshot and creates a User object by converting the retrieved data into an instance of the User class.
//                    User user = snapshot.getValue(User.class);
//
//                    //Exclude current user in the search result
//                    if (!currentUserUid.equals(user.getUid())) {
//
//                        //Search using username,email,id,full name or phone number (not case-sensitive)
//                        if (user.getUsername().toLowerCase().contains(searchInputText.toLowerCase()) || user.getEmail().toLowerCase().contains(searchInputText.toLowerCase())||user.getUid().toLowerCase().contains(searchInputText.toLowerCase())||user.getFullName().toLowerCase().contains(searchInputText.toLowerCase())||user.getPhone_number().contains(searchInputText)) {
//                            userList.add(user);
//                        }
//                    }
//                }
//                // Sort userList based on username
//                Collections.sort(userList, new Comparator<User>() {
//                    @Override
//                    public int compare(User user1, User user2) {
//                        return user1.getUsername().compareToIgnoreCase(user2.getUsername());
//                    }
//                });
//                searchFriendAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//    }
//}