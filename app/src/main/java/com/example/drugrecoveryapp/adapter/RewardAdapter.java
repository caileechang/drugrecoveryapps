package com.example.drugrecoveryapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.drugrecoveryapp.R;
import com.example.drugrecoveryapp.Reward;
import com.example.drugrecoveryapp.RewardDetails;

public class RewardAdapter extends BaseAdapter {
    Context context;
    private Activity activity;

    int logos;
    LayoutInflater inflater;
    Long totalTime;
    public RewardAdapter(Activity activity, int num, long totalTime) {
        this.context = activity.getApplicationContext();
        this.activity = activity;
       // this.context = applicationContext;
        this.logos = num;
        inflater = (LayoutInflater.from(context));
        this.totalTime = totalTime;
    }
    @Override
    public int getCount() {
        return logos;
    }
    @Override
    public Object getItem(int i) {
        return null;
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.item_rewward, viewGroup, false);
        }

        ImageView imageViewReward = view.findViewById(R.id.IVReward);
        TextView textViewReward = view.findViewById(R.id.TVReward);
        Button buttonReward = view.findViewById(R.id.BtnLocked);
        imageViewReward.setImageResource(R.drawable.reward_color);

        //view = inflter.inflate(R.layout.item_rewward, null); // inflate the layout
        //ImageView icon = (ImageView) view.findViewById(R.id.icon); // get the reference of ImageView
        //icon.setImageResource(logos[i]); // set logo images

        textViewReward.setText((i + 1)+" Day Sober");

        if(totalTime/24+1 > i){
            buttonReward.setText("Tap to view");
            buttonReward.setEnabled(true);
            buttonReward.setBackgroundColor(Color.parseColor("#FF68B8B8"));

            buttonReward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Use the activity to start the new activity with FLAG_ACTIVITY_NEW_TASK
                    Intent intent = new Intent(activity, RewardDetails.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                }
            });
        }


        return view;
    }
}
