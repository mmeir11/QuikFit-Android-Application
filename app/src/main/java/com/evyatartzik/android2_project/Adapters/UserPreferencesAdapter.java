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

public class UserPreferencesAdapter extends RecyclerView.Adapter<UserPreferencesAdapter.CountryViewHolder> {

    private List<UserPreferences> countries;
    private MyCountryListener listener;

    public interface MyCountryListener {
        void onCountryClicked(int position,View view);
        void onCountryLongClicked(int position,View view);
    }

    public void setListener(MyCountryListener listener) {
        this.listener = listener;
    }

    public UserPreferencesAdapter(List<UserPreferences> countries) {
        this.countries = countries;
    }


    public class CountryViewHolder extends RecyclerView.ViewHolder {

        TextView nameTv;
        TextView populationTv;
        ImageView flagIv;
        CheckBox goodCb;

        public CountryViewHolder(View itemView) {
            super(itemView);

            nameTv = itemView.findViewById(R.id.country_name);
            populationTv = itemView.findViewById(R.id.country_population);
            flagIv = itemView.findViewById(R.id.country_flag);
            goodCb = itemView.findViewById(R.id.country_checkbox);

            goodCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    UserPreferences userPreferences = countries.get(getAdapterPosition());
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(listener!=null)
                        listener.onCountryClicked(getAdapterPosition(),view);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(listener!=null)
                        listener.onCountryLongClicked(getAdapterPosition(),view);
                    return false;
                }
            });
        }
    }

    @Override
    public CountryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_prefrences,parent,false);
        CountryViewHolder countryViewHolder = new CountryViewHolder(view);
        return countryViewHolder;
    }

    @Override
    public void onBindViewHolder(CountryViewHolder holder, int position) {
        UserPreferences userPreferences = countries.get(position);
        holder.nameTv.setText(userPreferences.getName());
        holder.flagIv.setImageResource(userPreferences.getResId());
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
