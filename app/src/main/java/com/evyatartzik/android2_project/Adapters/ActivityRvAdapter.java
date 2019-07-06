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
import com.evyatartzik.android2_project.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityRvAdapter extends RecyclerView.Adapter<ActivityRvAdapter.MyViewHolder>{

    private Context mContext;
    private ArrayList<Activity> mData;
    private ObjectListener listener;

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

        myViewHolder.profilepic.setImageResource(R.drawable.avatar);
        myViewHolder.titleActivity.setText(activity.getTitle());
        myViewHolder.amountNumber.setText(activity.getAmountOfParticipents()+"");
        myViewHolder.amountMaxnumber.setText(activity.getMaxParticipents()+"");
        myViewHolder.typeActivity.setText(activity.getType());
        myViewHolder.location.setText(activity.getLocation());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        CircleImageView profilepic;
        TextView titleActivity;
        TextView amountNumber;
        TextView amountMaxnumber;
        TextView typeActivity;
        TextView location;

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
