package com.evyatartzik.android2_project.Fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.evyatartzik.android2_project.Models.GroupChatActivity;
import com.evyatartzik.android2_project.R;
import com.evyatartzik.android2_project.UI.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupsFragment extends Fragment {

    private View groupFragmentView;
    private ListView mListView;
    private FloatingActionButton mFloatingBtn;
    private ArrayAdapter<String> mArrayAdapter;
    private ArrayList<String> mListGroup = new ArrayList<>();

    private DatabaseReference mReference;


    public GroupsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        groupFragmentView =  inflater.inflate(R.layout.fragment_groups, container, false);
//        mReference = FirebaseDatabase.getInstance().getReference().child("Groups");
        mReference = FirebaseDatabase.getInstance().getReference("database/groups");
        initFields();
        retrieveAndDisplayGroups();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String currentGroupName =  parent.getItemAtPosition(position).toString();
                Toast.makeText(getContext(), currentGroupName+"", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getContext(), GroupChatActivity.class);
                intent.putExtra("groupName", currentGroupName);
                startActivity(intent);
            }
        });


        mFloatingBtn = groupFragmentView.findViewById(R.id.fb_createGroup);
        mFloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestNewGroup();
            }
        });



        return groupFragmentView;

    }



    private void initFields() {
        mListView = groupFragmentView.findViewById(R.id.list_view);
        mArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, mListGroup);
        mListView.setAdapter(mArrayAdapter);
    }

    private void retrieveAndDisplayGroups() {
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<>();
                Iterator iterator = dataSnapshot.getChildren().iterator();
                while (iterator.hasNext()){
                    set.add(((DataSnapshot)iterator.next()).getKey());
                }

                mListGroup.clear();
                mListGroup.addAll(set);
                mArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void RequestNewGroup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.AlertDialog);
        builder.setTitle("Enter Group Name:");
        final EditText groupNameField = new EditText(getContext());
        groupNameField.setHint("e.g Coding Cafe");
        builder.setView(groupNameField);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String groupName = groupNameField.getText().toString();
                if(TextUtils.isEmpty(groupName)){
                    Toast.makeText(getContext(), "Please write group name", Toast.LENGTH_SHORT).show();
                }
                else{
                    CreateNewGroup(groupName);
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();


    }

    private void CreateNewGroup(final String groupname) {
        mReference.child(groupname).setValue(groupname)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getContext(), "Group "+groupname +" Created Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
