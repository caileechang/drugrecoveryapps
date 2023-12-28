package com.example.drugrecoveryapp.educationResources;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.drugrecoveryapp.R;

public class ReadMoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_more);

        // Find the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_edu);

        // Set the Toolbar as the ActionBar
        setSupportActionBar(toolbar);

        // Enable the back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Retrieve data from the intent
        Intent intent = getIntent();
        actionBar.setTitle(intent.getStringExtra("drug_name"));

        // Retrieve more data as needed
        TextView heading1 = findViewById(R.id.heading1);
        TextView long_description = findViewById(R.id.long_description);
        TextView heading2 = findViewById(R.id.heading2);
        TextView effect = findViewById(R.id.effect);
        TextView immediate_effect = findViewById(R.id.immediate_effect);
        TextView longterm_effect = findViewById(R.id.long_term_effect);
        TextView heading3 = findViewById(R.id.heading3);
        TextView personal_story = findViewById(R.id.personal_story);

        // Example: Set text for the TextView using the retrieved data
        heading1.setText(intent.getStringExtra("drug_heading1"));
        long_description.setText(intent.getStringExtra("drug_description"));
        heading2.setText(intent.getStringExtra("drug_heading2"));
        effect.setText(intent.getStringExtra("drug_effect"));
        immediate_effect.setText(intent.getStringExtra("drug_immediate_effect"));
        longterm_effect.setText(intent.getStringExtra("drug_long-term_effect"));
        heading3.setText(intent.getStringExtra("drug_heading3"));
        personal_story.setText(intent.getStringExtra("drug_personal_story"));


        // Add more code as needed for your specific functionality
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Handle the back button click
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
