package com.evyatartzik.android2_project.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.evyatartzik.android2_project.Models.UserPreferences;
import com.evyatartzik.android2_project.R;
import com.evyatartzik.android2_project.UI.LoginRegister;

import java.util.List;

import static java.security.AccessController.getContext;

public class UserPreferencesAdapter extends RecyclerView.Adapter<UserPreferencesAdapter.UserPrefViewHold> {

    private List<UserPreferences> userPreferences;
    private MyUserPrefListener listener;

    public interface MyUserPrefListener {
        void onCardClicked(int position, View view);
        void onCheckSelected(int position,UserPreferences userPreference);
    }

    public void setListener(MyUserPrefListener listener) {
        this.listener = listener;
    }

    public UserPreferencesAdapter(List<UserPreferences> userPreferences) {
        this.userPreferences = userPreferences;
    }


    public class UserPrefViewHold extends RecyclerView.ViewHolder {

        Context context = GlobalApplication.getAppContext();

        TextView Title;
        CheckBox checkBox;
        public UserPrefViewHold(View itemView) {
            super(itemView);

            Title = itemView.findViewById(R.id.Sport_Title_TV);
            checkBox = itemView.findViewById(R.id.card_CB);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(listener!=null)
                        listener.onCardClicked(getAdapterPosition(),view);
                }
            });
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    UserPreferences userPreference= userPreferences.get(getAdapterPosition());
                    userPreference.setCheck(buttonView.isChecked());
                    //Toast.makeText(context,userPreference.getName(), Toast.LENGTH_LONG).show();
                    if(listener!=null)
                        listener.onCheckSelected(getAdapterPosition(),userPreference);
                }
            });


        }
    }

    @Override
    public UserPrefViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_user_prefrences,parent,false);
        UserPrefViewHold userPrefViewHold = new UserPrefViewHold(view);
        return userPrefViewHold;
    }

    @Override
    public void onBindViewHolder(UserPrefViewHold holder, int position) {
        UserPreferences userPreferences = this.userPreferences.get(position);
        holder.Title.setText(userPreferences.getName());
    }

    @Override
    public int getItemCount() {
        return userPreferences.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
