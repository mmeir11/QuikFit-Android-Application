package com.evyatartzik.android2_project.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.evyatartzik.android2_project.Models.ChatMessage;
import com.evyatartzik.android2_project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {


    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private Context mContext;
    private List<ChatMessage> mChatMessageList;

    FirebaseUser fuser;

    public MessageAdapter(Context mContext, List<ChatMessage> mChatMessageList) {
        this.mContext = mContext;
        this.mChatMessageList = mChatMessageList;
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

        ChatMessage chatMessage = mChatMessageList.get(i);
        viewHolder.show_message.setText(chatMessage.getMessage());
        viewHolder.name.setText(chatMessage.getName());
        viewHolder.date.setText(chatMessage.getDate());
        viewHolder.time.setText(chatMessage.getTime());

    }

    @Override
    public int getItemCount() {
        return mChatMessageList.size();
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
        if(mChatMessageList.get(position).getUuid().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        }
        else
            return MSG_TYPE_LEFT;
    }
}
