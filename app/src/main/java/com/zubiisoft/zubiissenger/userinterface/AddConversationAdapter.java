package com.zubiisoft.zubiissenger.userinterface;

import android.content.Context;
import android.content.Intent;
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
import com.zubiisoft.zubiissenger.entity.Conversation;

import java.util.LinkedList;

public class AddConversationAdapter extends
        RecyclerView.Adapter<AddConversationAdapter.AddConversationViewHolder> {

    private static final String TAG = "AddConversationAdapter";

    private LinkedList<Conversation> mAddConversationList;

    private final LayoutInflater mInflater;

    private Context mContext;

    public interface IntentCallback {
        void onIntentCallback(Intent intent);
    }

    private IntentCallback mListener;

    /**
     *
     * @param context
     * @param conversations
     */
    public AddConversationAdapter(Context context, LinkedList<Conversation> conversations,
                                  IntentCallback intentCallback) {
        mInflater = LayoutInflater.from(context);
        mAddConversationList = conversations;
        mContext = context;
        mListener = intentCallback;
    }

    @NonNull
    @Override
    public AddConversationAdapter.AddConversationViewHolder onCreateViewHolder
            (@NonNull ViewGroup parent, int viewType) {

        View view = mInflater.inflate
                (R.layout.conversation_item, parent, false);
        return new AddConversationViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull AddConversationViewHolder holder, int position) {
        final Conversation current = mAddConversationList.get(position);

        RelativeLayout conversationItemView = holder.mAddConversationItemView;

        //holder.mAvatar TODO
        holder.mName.setText(current.getName());

        conversationItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("friendUid", current.getWith());
                intent.putExtra("name", current.getName());
                intent.putExtra("avatar", current.getAvatarImage());
                intent.putExtra("lastMessage", current.getLastMessage());

                mListener.onIntentCallback(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mAddConversationList.size();
    }

    public static class AddConversationViewHolder extends RecyclerView.ViewHolder {
        protected final RelativeLayout mAddConversationItemView;
        protected ImageView mAvatar;
        protected TextView mName;
        protected final AddConversationAdapter mAdapter;

        public AddConversationViewHolder(@NonNull View itemView,
                                         AddConversationAdapter adapter) {
            super(itemView);
            mAddConversationItemView =
                    itemView.findViewById(R.id.conversationItem_relativeLayout);
            mAvatar = itemView.findViewById(R.id.avatar_imageView);
            mName = itemView.findViewById(R.id.name_textView);
            mAdapter = adapter;
        }
    }
}
