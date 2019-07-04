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

import java.util.List;

public class UserPrefAdapter extends RecyclerView.Adapter<UserPrefAdapter.UserPreferencesViewHolder> {

    private List<UserPreferences> preferences;
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
        TextView populationTv;
        ImageView flagIv;
        CheckBox goodCb;

        public UserPreferencesViewHolder(View itemView) {
            super(itemView);

            nameTv = itemView.findViewById(R.id.text_pref);
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
