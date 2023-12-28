package com.example.drugrecoveryapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    // private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    //private Toolbar mToolbar;
    //ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set up App Bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavHostFragment host = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        NavController navController = host.getNavController();

        //Set up bottom Navigation bar
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        NavigationUI.setupWithNavController(bottomNav, navController);

        // Set up a destination changed listener to update the toolbar title
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(NavController controller, NavDestination destination, Bundle arguments) {
                // Update the toolbar title based on the destination label
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null) {
                    actionBar.setTitle(destination.getLabel());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_acc, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            Navigation.findNavController(this, R.id.fragment_container).navigate(item.getItemId());
            return true;
        } catch (Exception ex) {
            return super.onOptionsItemSelected(item);
        }
    }
}


        /*
        //setupNavMenu(navController);

       // binding = ActivityMainBinding.inflate(getLayoutInflater());
       // setContentView(binding.getRoot());

/*
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
                replaceFragment(new EducationalContentFragment(),"Education Resources");

            } else if (targetFragment.equals("Motivation")) {
                targetFragment = null;
                replaceFragment(new MotivationFragment(), "Motivation Quotes");
            } else if (targetFragment.equals("Forum")) {
                targetFragment = null;
                replaceFragment(new ForumFragment(),"Community Forum");
            } else if (targetFragment.equals("Profile")) {
                targetFragment = null;
                replaceFragment(new PersonProfileFragment(),"Profile");
            }
        } else {
            replaceFragment(new ReccoveryTrackFragment(),"Home");
        }

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if(id == R.id.reccoveryMenu){
                replaceFragment(new ReccoveryTrackFragment(),"Home");
            } else if (id == R.id.educationalMenu) {
                replaceFragment(new EducationalContentFragment(),"Education Resources");
            } else if (id == R.id.motivationMenu) {
                replaceFragment(new MotivationFragment(),"Motivation Quotes");
            } else if (id == R.id.forumMenu) {
                replaceFragment(new ForumFragment(), "Community Forum");
            } else if (id == R.id.personProfileMenu) {
                replaceFragment(new PersonProfileFragment(),"Profile");
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

    private void replaceFragment(Fragment fragment, String title){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment);
        fragmentTransaction.commit();

        // Set the title for the Toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

    }



 */
