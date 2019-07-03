package com.evyatartzik.android2_project.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.evyatartzik.android2_project.Models.Activity;
import com.evyatartzik.android2_project.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityRvAdapter extends RecyclerView.Adapter<ActivityRvAdapter.MyViewHolder>{

    private Context mContext;
    private ArrayList<Activity> mData;
    private ObjectListener listener;

    public interface ObjectListener{
        void onObjectClicked(int pos,View view);
    }

    public void setListener(ObjectListener listener) {
        this.listener = listener;
    }

    public ActivityRvAdapter(Context mContext, ArrayList<Activity> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.activity_card_layout,viewGroup,false);
        MyViewHolder vHolder = new MyViewHolder(view);

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        //Fill card view item Example: "myViewHolder.profilepic.setImageBitmap(mData.get(pos).getMyBitMap());"

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        /* create the same components as card view , Example: */

//        CircleImageView profilepic;
//        TextView contactname;
//        CardView CardView_Layout;
//        TextView Amount_number;
//        TextView Amount_Type;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            /* Get references to card view layout items , Example: */

//            profilepic = (CircleImageView) itemView.findViewById(R.id.profile_image);
//            contactname = (TextView) itemView.findViewById(R.id.text_test);
//            CardView_Layout = (CardView) itemView.findViewById(R.id.card_view_id);
//            Amount_number = itemView.findViewById(R.id.amount_number);
//            Amount_Type = itemView.findViewById(R.id.amount_type);


            /* Set on click listner to card view items Example: */

//            CardView_Layout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(listener != null)
//                        listener.onObjectClicked(getAdapterPosition(),v);
//
//                }
//            });


        }
    }
}
