package com.evyatartzik.android2_project.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.evyatartzik.android2_project.Fragments.SearchFragment;
import com.evyatartzik.android2_project.Models.Activity;
import com.evyatartzik.android2_project.Models.User;
import com.evyatartzik.android2_project.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersRvAdapter extends RecyclerView.Adapter<UsersRvAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<User> mData;
    private UsersRvAdapter.UserObjectListener listener;

    public interface UserObjectListener{
        public void onUserObjectClicked(int pos, View view);
    }

    public void setListener(UsersRvAdapter.UserObjectListener listener) {
        this.listener = listener;
    }



    public UsersRvAdapter(Context mContext, ArrayList<User> mData) {
        this.mContext = mContext;
        this.mData = mData;

    }

    @NonNull
    @Override
    public UsersRvAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.user_card,viewGroup,false);
        UsersRvAdapter.MyViewHolder vHolder = new UsersRvAdapter.MyViewHolder(view);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UsersRvAdapter.MyViewHolder myViewHolder, int i) {

        User user = mData.get(i);
        downloadImageAndSetImageView(myViewHolder.profilepic,user.getProfile_pic_path());
        myViewHolder.userAddress.setText(user.getLocation_string());
        myViewHolder.userName.setText(user.getName());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        CircleImageView profilepic;
        TextView userName;
        TextView userAddress;


        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);

            profilepic = itemView.findViewById(R.id.profile_image);
            userName = itemView.findViewById(R.id.user_name);
            userAddress = itemView.findViewById(R.id.user_location);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
//                        if( listener.getClass()!= SearchFragment.class)
                            listener.onUserObjectClicked(getAdapterPosition(), v);
                    }
                }
            });

        }

    }

    public void downloadImageAndSetImageView(ImageView imageView, String url){

        Picasso.get().load(url).into(imageView);
    }
}
