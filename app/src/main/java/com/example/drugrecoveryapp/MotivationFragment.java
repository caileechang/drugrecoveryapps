package com.example.drugrecoveryapp;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import android.provider.MediaStore.Images;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MotivationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MotivationFragment extends Fragment {
    private int currentPageLayoutId;
    private View page1, page2, page3, page4; // Reference to different "pages"
    private int currentPage = 1; // Track the current page number

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Object MediaStore;

    public MotivationFragment(int layoutId) {
        this.currentPageLayoutId = R.id.motivationFragment;
    }
    public MotivationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MotivationFragment.
     */
    // TODO: Rename and change types and number of parameters
    // newInstance method if needed
    public static MotivationFragment newInstance(String param1, String param2) {
        MotivationFragment fragment = new MotivationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);

        fragment.setArguments(args);
        return fragment;
    }



        @SuppressLint("MissingInflatedId")
        @Override

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_motivation, container, false);
            // Find the views representing different "pages"
            page1 = view.findViewById(R.id.motivationMenu);
            page2 = view.findViewById(R.id.motivation_2);
            page3 = view.findViewById(R.id.motivation_3);
            page4 = view.findViewById(R.id.affirmation);

            // Initially, show the first page and hide the others
            showPage(1);

            // Find the button to switch between pages
            Button switchPageButton = view.findViewById(R.id.switchPageButton);
            switchPageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toggle between pages on button click
                    currentPage = (currentPage % 4) + 1; // Cycle between 1 to 4
                    showPage(currentPage);
                }
            });

            // Find and set onClickListener for the saveButton
            Button saveButton = view.findViewById(R.id.saveButton);
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Convert the drawable resource to a Bitmap (change this according to your image source)
                    BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.motivation1);
                    Bitmap imageBitmap = drawable.getBitmap();

                    // Call saveImageToGallery with the obtained Bitmap
                    saveImageToGallery(imageBitmap);
                }
            });

            return view;
        }

        private void showPage(int pageNumber) {
            // Hide all pages
            page1.setVisibility(View.GONE);
            page2.setVisibility(View.GONE);
            page3.setVisibility(View.GONE);
            page4.setVisibility(View.GONE);

            // Show the desired page based on the page number
            switch (pageNumber) {
                case 1:
                    page1.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    page2.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    page3.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    page4.setVisibility(View.VISIBLE);
                    break;
            }
        }

    private void saveImageToGallery(Bitmap imageBitmap) {
        if (getContext() == null) {
            // Handle the case where the context is not available
            return;
        }

        String savedImageURL = Images.Media.insertImage(
                getContext().getContentResolver(),
                imageBitmap,
                "Image Title",
                "Image Description"
        );

        if (savedImageURL != null) {
            // Image saved successfully
            Toast.makeText(getContext(), "Image saved to Gallery", Toast.LENGTH_SHORT).show();
        } else {
            // Failed to save image
            Toast.makeText(getContext(), "Failed to save image", Toast.LENGTH_SHORT).show();
        }
    }
}


