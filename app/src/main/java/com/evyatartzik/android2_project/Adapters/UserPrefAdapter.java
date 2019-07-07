package com.evyatartzik.android2_project.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.evyatartzik.android2_project.Models.UserPreferences;
import com.evyatartzik.android2_project.R;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserPrefAdapter extends RecyclerView.Adapter<UserPrefAdapter.UserPreferencesViewHolder> {

    private List<UserPreferences> preferences;
    private HashMap<String,Integer> preferencesPhotos;
    //private MyUserPrefListener listener;

/*    public interface MyUserPrefListener {
        void onUserPreferencesClicked(int position, View view);
        void onUserPreferencesLongClicked(int position,View view);
    }*/

/*
    public void setListener(MyUserPrefListener listener) {
        this.listener = listener;
    }
*/

    public UserPrefAdapter(List<UserPreferences> preferences) {
        this.preferences = preferences;
    }


    public class UserPreferencesViewHolder extends RecyclerView.ViewHolder {

        TextView nameTv;
        CircleImageView activityTypeImage;
        TextView populationTv;
        ImageView flagIv;
        CheckBox goodCb;

        public UserPreferencesViewHolder(View itemView) {
            super(itemView);

            nameTv = itemView.findViewById(R.id.text_pref);
            activityTypeImage = itemView.findViewById(R.id.profile_image);
/*
            populationTv = itemView.findViewById(R.id.UserPreferences_population);
            flagIv = itemView.findViewById(R.id.UserPreferences_flag);
            goodCb = itemView.findViewById(R.id.UserPreferences_checkbox);
*/

/*
            goodCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    UserPreferences UserPreferences = preferences.get(getAdapterPosition());
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(listener!=null)
                        listener.onUserPreferencesClicked(getAdapterPosition(),view);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(listener!=null)
                        listener.onUserPreferencesLongClicked(getAdapterPosition(),view);
                    return false;
                }
            });
*/
        }
    }

    @Override
    public UserPreferencesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.userpref_cell,parent,false);
        UserPreferencesViewHolder UserPreferencesViewHolder = new UserPreferencesViewHolder(view);

        return UserPreferencesViewHolder;
    }

    @Override
    public void onBindViewHolder(UserPreferencesViewHolder holder, int position) {
        UserPreferences UserPreferences = preferences.get(position);
        holder.nameTv.setText(UserPreferences.getName());
        String name = UserPreferences.getName();
        switch (name){

            case "Basketball":
                holder.activityTypeImage.setImageResource(R.drawable.basketball_icon);
                break;
            case "Soccer":
                holder.activityTypeImage.setImageResource(R.drawable.football_icon);
                break;
            case "Volley":
                holder.activityTypeImage.setImageResource(R.drawable.vallay_icon);
                break;
            case "Weightlifting":
                holder.activityTypeImage.setImageResource(R.drawable.weights_icon);
                break;
            case "Water sports":
                holder.activityTypeImage.setImageResource(R.drawable.watar_sport_icon);
                break;
            case "Tennis":
                holder.activityTypeImage.setImageResource(R.drawable.tennis_icon);
                break;
            case "Football":
                holder.activityTypeImage.setImageResource(R.drawable.football_icon);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return preferences.size();
    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
