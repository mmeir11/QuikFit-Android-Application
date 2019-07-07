package com.evyatartzik.android2_project.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.evyatartzik.android2_project.Fragments.SearchFragment;
import com.evyatartzik.android2_project.Models.Activity;
import com.evyatartzik.android2_project.Models.UserPreferences;
import com.evyatartzik.android2_project.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityRvAdapter extends RecyclerView.Adapter<ActivityRvAdapter.MyViewHolder>{

    private Context mContext;
    private ArrayList<Activity> mData;
    private ObjectListener listener;
    CircleImageView activityTypeImage;


    public interface ObjectListener{
       public void onActivityObjectClicked(int pos,View view);
    }

    public void setListener(ObjectListener listener) {
        this.listener = listener;
    }



    public ActivityRvAdapter(Context mContext, ArrayList<Activity> mData) {
        this.mContext = mContext;
        this.mData = mData;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.activity_card_layout,viewGroup,false);
        MyViewHolder vHolder = new MyViewHolder(view);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        Activity activity = mData.get(i);

        setImageForActivity(myViewHolder,i);
        myViewHolder.titleActivity.setText(activity.getTitle());
        myViewHolder.amountNumber.setText(activity.getAmountOfParticipents()+"");
        myViewHolder.amountMaxnumber.setText(activity.getMaxParticipents()+"");
        myViewHolder.typeActivity.setText(activity.getType());
        myViewHolder.location.setText(activity.getLocation());


    }

    private void setImageForActivity(MyViewHolder holder, int position) {

        Activity activities = mData.get(position);

        String name = activities.getType();
        switch (name) {

            case "Basketball":
                holder.profilepic.setImageResource(R.drawable.basketball_icon);
                break;
            case "Soccer":
                holder.profilepic.setImageResource(R.drawable.soccer_icon);
                break;
            case "Volley":
                holder.profilepic.setImageResource(R.drawable.vallay_icon);
                break;
            case "Weightlifting":
                holder.profilepic.setImageResource(R.drawable.weights_icon);
                break;
            case "Water sports":
                holder.profilepic.setImageResource(R.drawable.watar_sport_icon);
                break;
            case "Tennis":
                holder.profilepic.setImageResource(R.drawable.tennis_icon);
                break;
            case "Football":
                holder.profilepic.setImageResource(R.drawable.football_icon);
                break;
            case "Running":
                holder.profilepic.setImageResource(R.drawable.runnig_icon);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView profilepic;
        public TextView titleActivity;
        public TextView amountNumber;
        public TextView amountMaxnumber;
        public TextView typeActivity;
        public TextView location;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);

            profilepic = itemView.findViewById(R.id.profileActivity);
            titleActivity = itemView.findViewById(R.id.titleActivity);
            amountNumber = itemView.findViewById(R.id.amountNumber);
            amountMaxnumber = itemView.findViewById(R.id.amountMaxNumber);
            typeActivity = itemView.findViewById(R.id.typeActivity);
            location = itemView.findViewById(R.id.locationActivity);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                            listener.onActivityObjectClicked(getAdapterPosition(), v);
                    }
                }
            });

        }

    }
}
