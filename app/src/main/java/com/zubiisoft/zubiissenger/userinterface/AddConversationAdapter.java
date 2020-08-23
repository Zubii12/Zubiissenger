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
        final Conversation mCurrent = mAddConversationList.get(position);

        RelativeLayout conversationItemView = holder.mAddConversationItemView;
        for (int conv = 0; conv < mAddConversationList.size(); conv++) {
            for (int i = 0; i < conversationItemView.getChildCount(); i++) {
                if (conversationItemView.getChildAt(i) instanceof ImageView){
                    Log.d(TAG, "onBindViewHolder: ImageView - avatar - found");
                } else if (conversationItemView.getChildAt(i) instanceof TextView) {
                    if (conversationItemView.getChildAt(i) ==
                            conversationItemView.getChildAt(i).
                                    findViewById(R.id.name_textView)) {
                        ((TextView) conversationItemView.getChildAt(i)).
                                setText(mCurrent.getName());
                    } else if (conversationItemView.getChildAt(i)
                            == conversationItemView.getChildAt(i).
                            findViewById(R.id.lastMessage_textView)) {
                        ((TextView) conversationItemView.getChildAt(i)).
                                setText(mCurrent.getLastMessage());
                    }
                }
            }
        }

        conversationItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("friendUid", mCurrent.getWith());
                Log.d(TAG, "onClick: " + mCurrent.getWith());
                intent.putExtra("name", mCurrent.getName());
                intent.putExtra("avatar", mCurrent.getAvatarImage());
                intent.putExtra("lastMessage", mCurrent.getLastMessage());

                mListener.onIntentCallback(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mAddConversationList.size();
    }

    public static class AddConversationViewHolder extends RecyclerView.ViewHolder {
        public final RelativeLayout mAddConversationItemView;
        final AddConversationAdapter mAdapter;

        public AddConversationViewHolder(@NonNull View itemView,
                                         AddConversationAdapter adapter) {
            super(itemView);
            mAddConversationItemView = itemView.findViewById
                    (R.id.conversationItem_relativeLayout);
            mAdapter = adapter;
        }
    }
}
