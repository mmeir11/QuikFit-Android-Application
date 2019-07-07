package com.evyatartzik.android2_project.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.evyatartzik.android2_project.Models.User;
import com.evyatartzik.android2_project.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersChatAdapter extends RecyclerView.Adapter<UsersChatAdapter.MyViewHolder> {

    private Context mContext;
    private List<User> mData;
    private UsersChatAdapter.UserObjectListener listener;

    public interface UserObjectListener{
        public void onUserObjectClicked(int pos, View view);
    }

    public void setListener(UsersChatAdapter.UserObjectListener listener) {
        this.listener = listener;
    }



    public UsersChatAdapter(Context mContext, ArrayList<User> mData) {
        this.mContext = mContext;
        this.mData = mData;

    }

    @NonNull
    @Override
    public UsersChatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.user_cell, viewGroup,false);
        UsersChatAdapter.MyViewHolder vHolder = new UsersChatAdapter.MyViewHolder(view);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UsersChatAdapter.MyViewHolder myViewHolder, int i) {

        User user = mData.get(i);

        downloadImageAndSetImageView(myViewHolder.profile_image,user.getProfile_pic_path());
        myViewHolder.userName.setText(user.getName());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        CircleImageView profile_image;
        TextView userName;


        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);

            profile_image = itemView.findViewById(R.id.profile_image);
            userName = itemView.findViewById(R.id.username);



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
