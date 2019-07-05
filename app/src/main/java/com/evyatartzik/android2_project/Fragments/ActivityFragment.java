package com.evyatartzik.android2_project.Fragments;

import android.app.ActivityOptions;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.evyatartzik.android2_project.Adapters.ActivityRvAdapter;
import com.evyatartzik.android2_project.Models.Activity;
import com.evyatartzik.android2_project.Models.GroupChatActivity;
import com.evyatartzik.android2_project.Models.User;
import com.evyatartzik.android2_project.R;
import com.evyatartzik.android2_project.UI.LoginRegister;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ActivityFragment extends Fragment implements View.OnClickListener {

    View root;
    Activity activity;


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


            Intent intent = new Intent(getContext(), GroupChatActivity.class);
            intent.putExtra("groupName", activity.getTitle());
            startActivity(intent);
        }
    }



}
