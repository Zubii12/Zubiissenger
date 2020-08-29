package com.zubiisoft.zubiissenger.userinterface;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.zubiisoft.zubiissenger.R;

import java.util.ArrayList;
import java.util.LinkedList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private static final String TAG = "MessageAdapter";

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private LinkedList<ArrayList<String>> mMessageList;

    private final LayoutInflater mInflater;

    private String mUid;

    public MessageAdapter(Context context, LinkedList<ArrayList<String>> messages, String uid) {
        mInflater = LayoutInflater.from(context);
        mMessageList = messages;
        mUid = uid;
    }

    @NonNull
    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                               int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = mInflater.inflate
                    (R.layout.right_message_item, parent, false);

            return new MessageViewHolder(view, this);
        } else  {
            View view = mInflater.inflate
                    (R.layout.left_message_item, parent, false);

            return new MessageViewHolder(view, this);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MessageViewHolder holder,
                                 int position) {
        ArrayList<String> current = mMessageList.get(position);

        RelativeLayout messageItemView = holder.mMessageItemView;

        holder.mMessage.setText(current.get(2));
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (!(mMessageList == null)) {
            if (mMessageList.get(position).get(1).equals(mUid)) {
                return MSG_TYPE_RIGHT;
            } else {
                return MSG_TYPE_LEFT;
            }
        } else {
            return position;
        }
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        protected final RelativeLayout mMessageItemView;
        protected ImageView mAvatar;
        protected TextView mMessage;
        protected final MessageAdapter mAdapter;


        public MessageViewHolder(@NonNull View itemView, MessageAdapter adapter) {
            super(itemView);
            mMessageItemView = itemView.findViewById(R.id.message_relativeLayout);
            mAvatar = itemView.findViewById(R.id.avatar_imageView);
            mMessage = itemView.findViewById(R.id.message_textView);
            mAdapter = adapter;
        }
    }
}
