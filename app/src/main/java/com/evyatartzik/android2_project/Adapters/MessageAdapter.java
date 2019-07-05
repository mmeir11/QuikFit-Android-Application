package com.evyatartzik.android2_project.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.evyatartzik.android2_project.Models.Chat;
import com.evyatartzik.android2_project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {


    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private Context mContext;
    private List<Chat> mChatList;

    FirebaseUser fuser;

    public MessageAdapter(Context mContext, List<Chat> mChatList) {
        this.mContext = mContext;
        this.mChatList = mChatList;
    }


    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = null;

        if (viewType == MSG_TYPE_RIGHT)
            view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, viewGroup, false);
        else if (viewType == MSG_TYPE_LEFT)
            view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, viewGroup, false);

        return new MessageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder viewHolder, int i) {

        Chat chat = mChatList.get(i);
        viewHolder.show_message.setText(chat.getMessage());
        viewHolder.name.setText(chat.getName());
        viewHolder.date.setText(chat.getDate());
        viewHolder.time.setText(chat.getTime());

    }

    @Override
    public int getItemCount() {
        return mChatList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message;
        public TextView name;
        public TextView date;
        public TextView time;

        public ViewHolder(View itemView){
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            name = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);

        }

    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(mChatList.get(position).getUuid().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        }
        else
            return MSG_TYPE_LEFT;
    }
}
