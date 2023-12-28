package com.example.drugrecoveryapp;

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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Object MediaStore;

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
    public static MotivationFragment newInstance(String param1, String param2) {
        MotivationFragment fragment = new MotivationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_motivation_3, container, false);

        Button saveButton = view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Convert the drawable resource to a Bitmap
                BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.motivation1);
                Bitmap imageBitmap = drawable.getBitmap();

                // Call saveImageToGallery with the obtained Bitmap
                saveImageToGallery(imageBitmap);
            }
        });

        return view;
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


