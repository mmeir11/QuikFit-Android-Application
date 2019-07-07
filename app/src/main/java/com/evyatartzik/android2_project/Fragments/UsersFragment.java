package com.evyatartzik.android2_project.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evyatartzik.android2_project.Adapters.UsersChatAdapter;
import com.evyatartzik.android2_project.Models.User;
import com.evyatartzik.android2_project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class UsersFragment extends Fragment {


    private View root;
    private RecyclerView recyclerViewUsers;

    private UsersChatAdapter usersChatAdapter;
    private ArrayList<User> userList;

    FirebaseUser firebaseUser;
    DatabaseReference reference;



    public UsersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root =  inflater.inflate(R.layout.fragment_users, container, false);

        recyclerViewUsers = root.findViewById(R.id.recycler_view_users);
        recyclerViewUsers.setHasFixedSize(true);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(getContext()));

        userList = new ArrayList<>();



        return root;
    }


}
