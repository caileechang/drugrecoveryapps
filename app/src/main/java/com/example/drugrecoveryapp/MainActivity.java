package com.example.drugrecoveryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.animation.AlphaAnimation;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.drugrecoveryapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    private Toolbar mToolbar;

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Automatic replace fragment to the respective fragment
        // based on the targetFragment when it redirect from the traceback function
        String targetFragment = getIntent().getStringExtra("targetFragment");

        if (targetFragment != null) {
            if (targetFragment.equals("EducationalContentFragment")) {
                targetFragment = null;
                replaceFragment(new EducationalContentFragment());
            } else if (targetFragment.equals("MotivationFragment")) {
                targetFragment = null;
                replaceFragment(new MotivationFragment());
            } else if (targetFragment.equals("ForumFragment")) {
                targetFragment = null;
                replaceFragment(new ForumFragment());
            } else if (targetFragment.equals("ProfileFragment")) {
                targetFragment = null;
                replaceFragment(new PersonProfileFragment());
            }
        } else {
            replaceFragment(new ReccoveryTrackFragment());
        }

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if(id == R.id.reccoveryTrackFragment){
                replaceFragment(new ReccoveryTrackFragment());
            } else if (id == R.id.educationalContentFragment) {
                replaceFragment(new EducationalContentFragment());
            } else if (id == R.id.motivationFragment) {
                replaceFragment(new MotivationFragment());
            } else if (id == R.id.forumFragment) {
                replaceFragment(new ForumFragment());
            } else if (id == R.id.personProfileFragment) {
                replaceFragment(new ForumFragment());
            }

            return true;
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment);
        fragmentTransaction.commit();

    }

}