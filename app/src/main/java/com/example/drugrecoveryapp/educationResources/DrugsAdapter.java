package com.example.drugrecoveryapp.educationResources;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drugrecoveryapp.R;

import java.util.List;

public class DrugsAdapter extends
        RecyclerView.Adapter<DrugsAdapter.ViewHolder> {
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView drugNameTV;
        public TextView drugDescriptionTV;
        public Button readMoreButton;

        public ImageView drugImage;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the contextL from any ViewHolder instance.
            super(itemView);

            drugNameTV = (TextView) itemView.findViewById(R.id.drug_name);
            drugDescriptionTV = (TextView)itemView.findViewById(R.id.drug_description);
            readMoreButton = (Button) itemView.findViewById(R.id.read_more);
            drugImage = (ImageView) itemView.findViewById(R.id.drug_image);
        }


    }
    private List<Drug> drugList;

    // Pass in the contact array into the constructor
    public DrugsAdapter(List<Drug> contacts) {
        drugList = contacts;
    }

    @NonNull
    @Override
    public DrugsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_drug, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data model based on position
        Drug drug = drugList.get(position);
        // Set item views based on your views and data model
        TextView drugNameTV = holder.drugNameTV;
        drugNameTV.setText(drug.getName());
        TextView drugDescriptionTV = holder.drugDescriptionTV;
        drugDescriptionTV.setText(drug.getDrugDescription());
        Button button = holder.readMoreButton;
        button.setText("Read More");
        button.setEnabled(drug.isReadMore());
        ImageView imageView = holder.drugImage;
        imageView.setImageResource(R.drawable.drug);


    }

    @Override
    public int getItemCount() {
        return drugList.size();
    }


}
