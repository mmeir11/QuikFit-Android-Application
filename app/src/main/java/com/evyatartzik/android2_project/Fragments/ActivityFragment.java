package com.evyatartzik.android2_project.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.evyatartzik.android2_project.Interfaces.FragmentToActivity;
import com.evyatartzik.android2_project.Models.Activity;
import com.evyatartzik.android2_project.UI.ChatActivity;
import com.evyatartzik.android2_project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import de.hdodenhof.circleimageview.CircleImageView;


public class ActivityFragment extends Fragment implements View.OnClickListener, Switch.OnCheckedChangeListener {

    View root;
    Activity activity;
    private FragmentToActivity callback;
    TextView numParticipantsTv;
    CircleImageView activityImage;


    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();

    private FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance();


//    ArrayList<Activity>  activitiesArrayList= new ArrayList<>();



    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {


        root =  inflater.inflate(R.layout.activity_fragment, container, false);

        TextView titleTv = root.findViewById(R.id.title_activity);
        TextView typeTv = root.findViewById(R.id.type_activity);
        TextView locationTv = root.findViewById(R.id.location_activity);
        TextView dateTv = root.findViewById(R.id.date_activity);
        TextView timeTv = root.findViewById(R.id.time_activity);
        /*TextView*/ numParticipantsTv = root.findViewById(R.id.numParticipants);
        TextView maxNumParticipantsTv = root.findViewById(R.id.maxNumParticipants);
        Switch confirmArravSwtich = root.findViewById(R.id.confirmArravSwtich);
        Button chatBtn = root.findViewById(R.id.chatBtn);
        activityImage = root.findViewById(R.id.image_activity);





        activity = (Activity)getArguments().getSerializable("activity");

        titleTv.setText(" " +activity.getTitle());
        typeTv.setText(" " +activity.getType()+ " " );
        locationTv.setText(" " + activity.getLocation());
        dateTv.setText(" " + activity.getDate());
        timeTv.setText(" " + activity.getTime());
        setImageOfActivity(" " + activity.getType());
        numParticipantsTv.setText(" " + activity.getAmountOfParticipents() + "");
        maxNumParticipantsTv.setText(" " + activity.getMaxParticipents() + "");

        activity.isInActivity(currentUser.getUid());
        confirmArravSwtich.setChecked(activity.getConfirm());
        confirmArravSwtich.setOnCheckedChangeListener(this);
        chatBtn.setOnClickListener(this);




        return root;
    }

    private void setImageOfActivity(String name) {
        String str = name.trim();
        if(str.equals("Tennis"))
        {
            activityImage.setImageResource(R.drawable.tennis_icon);
            return;
        }
        if(str.equals("Basketball"))
        {
            activityImage.setImageResource(R.drawable.basketball_icon);
            return;
        }
        if(str.equals("Soccer"))
        {
            activityImage.setImageResource(R.drawable.soccer_icon);
            return;
        }
        if(str.equals("Volley"))
        {
            activityImage.setImageResource(R.drawable.vallay_icon);
            return;
        }
        if(str.equals("Weightlifting"))
        {
            activityImage.setImageResource(R.drawable.weights_icon);
            return;
        }
        if(str.equals("Water sports"))
        {
            activityImage.setImageResource(R.drawable.watar_sport_icon);
            return;
        }
        if(str.equals("Bowling"))
        {
            activityImage.setImageResource(R.drawable.booling_icon);
            return;
        }
        if(str.equals("Running"))
        {
            activityImage.setImageResource(R.drawable.runnig_icon);
            return;
        }
        if(str.equals("Football"))
        {
            activityImage.setImageResource(R.drawable.football_icon);
            return;
        }
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
        callback.finish_task(1,"","");
        super.onPause();
    }



    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        activity.setConfirm(isChecked);
        String currentUserId = currentUser.getUid();

        if(buttonView.getId() == R.id.confirmArravSwtich){
//======================================================================

            if(isChecked){

                if(activity.getAmountOfParticipents() <= activity.getMaxParticipents()) {

                    activity.addParticipents(currentUserId);
                    firebaseMessaging.subscribeToTopic(activity.getTitle());
                    callback.finish_task(4,activity.getTitle(),activity.getDate());

                }

                else
                    Toast.makeText(getContext(), R.string.Activity_full, Toast.LENGTH_SHORT).show();
            }else{
                activity.removeParticipents(currentUserId);
                firebaseMessaging.unsubscribeFromTopic(activity.getTitle());

            }
        }
        numParticipantsTv.setText(activity.getAmountOfParticipents() + "");
        changeCurrentActivityInDatabase();
    }



    private void changeCurrentActivityInDatabase()
    {
        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference("database/activity");

        mReference.child(activity.getTitle()).child("usersIDList").setValue(activity.getUsersIDList());
        mReference.child(activity.getTitle()).child("amountOfParticipents").setValue(activity.getAmountOfParticipents());


    }


}
