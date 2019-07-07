package com.evyatartzik.android2_project.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.evyatartzik.android2_project.Models.Activity;
import com.evyatartzik.android2_project.Models.Chat;
import com.evyatartzik.android2_project.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsListRvAdapter extends RecyclerView.Adapter<ChatsListRvAdapter.MyViewHolder>{

    private Context mContext;
    private ArrayList<Chat> chatArrayList;
    private ObjectListener listener;

    public interface ObjectListener{
       public void onActivityObjectClicked(int pos, View view);
    }

    public void setListener(ObjectListener listener) {
        this.listener = listener;
    }



    public ChatsListRvAdapter(Context mContext, ArrayList<Chat> chatArrayList) {
        this.mContext = mContext;
        this.chatArrayList = chatArrayList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.chat_cell,viewGroup,false);
        MyViewHolder vHolder = new MyViewHolder(view);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        Chat chat = chatArrayList.get(i);

        myViewHolder.imageChat.setImageResource(R.drawable.avatar);
        myViewHolder.titleChat.setText(chat.getTitle());
    }

    @Override
    public int getItemCount() {
        return chatArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        CircleImageView imageChat;
        TextView titleChat;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);

            imageChat = itemView.findViewById(R.id.imageChat);
            titleChat = itemView.findViewById(R.id.titleChat);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                            listener.onActivityObjectClicked(getAdapterPosition(), v);
                    }
                }
            });

        }

    }
}
