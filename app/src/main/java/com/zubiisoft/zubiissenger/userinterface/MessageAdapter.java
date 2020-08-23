package com.zubiisoft.zubiissenger.userinterface;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zubiisoft.zubiissenger.MyApplication;
import com.zubiisoft.zubiissenger.R;
import com.zubiisoft.zubiissenger.database.Database;
import com.zubiisoft.zubiissenger.entity.User;

import java.util.ArrayList;
import java.util.LinkedList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private static final String TAG = "MessageAdapter";

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private LinkedList<ArrayList<String>> mMessageList;

    private final LayoutInflater mInflater;

    private Database mDatabase = MyApplication.getDatabase();
    private FirebaseAuth mAuth = MyApplication.getAuth();
    private FirebaseUser mUser = mAuth.getCurrentUser();
    private String mUid = mUser.getUid();

    public MessageAdapter(Context context, LinkedList<ArrayList<String>> messages) {
        mInflater = LayoutInflater.from(context);
        mMessageList = messages;
    }

    @NonNull
    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                               int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = mInflater.inflate
                    (R.layout.right_message_item, parent, false);

            return new MessageViewHolder(view, this);
        } else if (viewType == MSG_TYPE_LEFT) {
            View view = mInflater.inflate
                    (R.layout.left_message_item, parent, false);

            return new MessageViewHolder(view, this);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MessageViewHolder holder,
                                 int position) {
        ArrayList<String> mCurrent = mMessageList.get(position);

        RelativeLayout messageItemView = holder.mMessageItemView;

        TextView textview = messageItemView.findViewById(R.id.message_textView);
        textview.setText(mCurrent.get(2));

    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mMessageList.get(position).get(1).equals(mUid)) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public final RelativeLayout mMessageItemView;
        final MessageAdapter mAdapter;

        public MessageViewHolder(@NonNull View itemView, MessageAdapter adapter) {
            super(itemView);
            mMessageItemView = itemView.findViewById(R.id.message_relativeLayout);
            mAdapter = adapter;
        }
    }
}
