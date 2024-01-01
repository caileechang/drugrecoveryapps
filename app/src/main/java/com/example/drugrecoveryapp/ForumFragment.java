package com.example.drugrecoveryapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForumFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ForumFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ForumFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ForumFragment newInstance(String param1, String param2) {
        ForumFragment fragment = new ForumFragment();
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

        View view = inflater.inflate(R.layout.fragment_forum,
                container, false);
        Button community_forum_btn = (Button) view.findViewById(R.id.community_forum_btn);
        Button discussion_groups_btn = (Button) view.findViewById(R.id.discussion_groups_btn);
        Button chat_room_btn = (Button) view.findViewById(R.id.chat_room_btn);
        Button counsellor_contact_btn = (Button) view.findViewById(R.id.counsellor_contact_btn);
        community_forum_btn.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CommunityForumActivity.class);
            startActivity(intent);
        });
        chat_room_btn.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ChatRoomsActivity.class);
            startActivity(intent);
        });

        discussion_groups_btn.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), DiscussionGroupActiivty.class);
            startActivity(intent);
        });

        counsellor_contact_btn.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CounsellorContactsActivity.class);
            startActivity(intent);
        });
        return view;


    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        Button BtnFindFriend = view.findViewById(R.id.search_friend_btn);
        BtnFindFriend.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.NHFMain);
            navController.navigate(R.id.destSearchFriend);
        });


    }

}