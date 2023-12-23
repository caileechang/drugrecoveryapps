package com.example.drugrecoveryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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

//        Intent intent = getIntent();
//        if (intent.hasExtra("selectedFragment")) {
//            int selectedFragmentId = intent.getIntExtra("selectedFragment", 0);
//            if (selectedFragmentId != 0) {
//                NavController navController = Navigation.findNavController(this, R.id.NHFMain);
//                navController.navigate(selectedFragmentId);
//            }
//        }

        // Automatic replace fragment to the respective fragment
        // based on the targetFragment when it redirect from the traceback function
        String targetFragment = getIntent().getStringExtra("targetFragment");

        if (targetFragment != null) {
            if (targetFragment.equals("Educational Content")) {
                targetFragment = null;
                replaceFragment(new EducationalContentFragment());
            } else if (targetFragment.equals("Motivation")) {
                targetFragment = null;
                replaceFragment(new MotivationFragment());
            } else if (targetFragment.equals("Forum")) {
                targetFragment = null;
                replaceFragment(new ForumFragment());
            } else if (targetFragment.equals("Profile")) {
                targetFragment = null;
                replaceFragment(new PersonProfileFragment());
            }
        } else {
            replaceFragment(new ReccoveryTrackFragment());
        }

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if(id == R.id.reccoveryMenu){
                replaceFragment(new ReccoveryTrackFragment());
            } else if (id == R.id.educationalMenu) {
                replaceFragment(new EducationalContentFragment());
            } else if (id == R.id.motivationMenu) {
                replaceFragment(new MotivationFragment());
            } else if (id == R.id.forumMenu) {
                replaceFragment(new ForumFragment());
            } else if (id == R.id.personProfileMenu) {
                replaceFragment(new PersonProfileFragment());
            }

            return true;
        });
    }

//    private void handleLogout() {
//        Intent loginIntent = new Intent(this, Login.class);
//        startActivity(loginIntent);
//
//        // Finish the current activity to prevent going back to it after logout
//        finish();
//    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment);
        fragmentTransaction.commit();

    }

}