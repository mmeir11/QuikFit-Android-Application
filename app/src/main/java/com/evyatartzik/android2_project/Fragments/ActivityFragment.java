package com.evyatartzik.android2_project.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.evyatartzik.android2_project.Interfaces.FragmentToActivity;
import com.evyatartzik.android2_project.Models.Activity;
import com.evyatartzik.android2_project.Models.ChatActivity;
import com.evyatartzik.android2_project.R;


public class ActivityFragment extends Fragment implements View.OnClickListener {

    View root;
    Activity activity;
    private FragmentToActivity callback;


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {


        root =  inflater.inflate(R.layout.activity_fragment, container, false);

        TextView titleTv = root.findViewById(R.id.title_activity);
        TextView typeTv = root.findViewById(R.id.type_activity);
        TextView locationTv = root.findViewById(R.id.location_activity);
        TextView dateTv = root.findViewById(R.id.date_activity);
        TextView timeTv = root.findViewById(R.id.time_activity);
        TextView numParticipantsTv = root.findViewById(R.id.numParticipants);
        TextView maxNumParticipantsTv = root.findViewById(R.id.maxNumParticipants);
        Switch confirmArravSwtich = root.findViewById(R.id.confirmArravSwtich);
        Button chatBtn = root.findViewById(R.id.chatBtn);

        activity = (Activity)getArguments().getSerializable("activity");

        titleTv.setText(activity.getTitle());
        typeTv.setText(activity.getType());
        locationTv.setText(activity.getLocation());
        dateTv.setText(activity.getDate());
        timeTv.setText(activity.getTime());
        numParticipantsTv.setText(activity.getAmountOfParticipents() + "");
        maxNumParticipantsTv.setText(activity.getMaxParticipents() + "");
        confirmArravSwtich.setChecked(activity.getConfirm());
        chatBtn.setOnClickListener(this);



        return root;
    }


    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.chatBtn) {

            Intent intent = new Intent(getContext(), ChatActivity.class);
            intent.putExtra("groupName", activity.getTitle());
            startActivity(intent);
        }
    }

    public void setOnActivityOpenListener(FragmentToActivity callback) {
        this.callback = callback;
    }

    @Override
    public void onPause() {
        callback.finish_task();
        super.onPause();
    }
}
