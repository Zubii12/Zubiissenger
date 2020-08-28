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

/**
 * Adapter for a RecyclerView.
 */
public class ConversationAdapter extends
        RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder> {

    private static final String TAG = "ConversationAdapter";

    private LinkedList<Conversation> mConversationList;
    private final LayoutInflater mInflater;
    private Context mContext;
    private String mWith;

    /**
     * Constructor for adapter.
     * @param context The current context.
     * @param conversations All conversations.
     */
    public ConversationAdapter(Context context, LinkedList<Conversation> conversations) {
        mInflater = LayoutInflater.from(context);
        mConversationList = conversations;
        mContext = context;
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to
     * represent an item.
     *
     * This new ViewHolder should be constructed with a new View that can
     * represent the items of the given type. You can either create a new View
     * manually or inflate it from an XML layout file.
     *
     * The new ViewHolder will be used to display items of the adapter using
     * onBindViewHolder(ViewHolder, int, List). Since it will be reused to
     * display different items in the data set, it is a good idea to cache
     * references to sub views of the View to avoid unnecessary findViewById()
     * calls.
     *
     * @param parent    The ViewGroup into which the new View will be added after
     *                  it is bound to an adapter position.
     * @param viewType The view type of the new View. @return A new ViewHolder
     *                 that holds a View of the given view type.
     * @return
     */
    @NonNull
    @Override
    public ConversationAdapter.ConversationViewHolder onCreateViewHolder
            (@NonNull ViewGroup parent, int viewType) {

        // Inflate an item view.
        View view = mInflater.inflate(R.layout.conversation_item, parent,
                false);

        return new ConversationViewHolder(view, this);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * This method should update the contents of the ViewHolder.itemView to
     * reflect the item at the given position.
     *
     * @param holder   The ViewHolder which should be updated to represent
     *                 the contents of the item at the given position in the
     *                 data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder
            (@NonNull ConversationAdapter.ConversationViewHolder holder, int position) {

        // Retrieve the data for that position.
        final Conversation mCurrent = mConversationList.get(position);


        // Add the data to the view holder.
        RelativeLayout conversationItemView = holder.mConversationItemView;

        for (int conv = 0; conv < mConversationList.size(); conv++) {
            for (int i = 0; i < conversationItemView.getChildCount(); i++) {
                if (conversationItemView.getChildAt(i) instanceof ImageView) {
                    // TODO
                } else if (conversationItemView.getChildAt(i) instanceof TextView) {
                    if (conversationItemView.getChildAt(i) == conversationItemView
                            .getChildAt(i).findViewById(R.id.name_textView)) {

                        ((TextView) conversationItemView.getChildAt(i)).
                                setText(mCurrent.getName());

                    } else if (conversationItemView.getChildAt(i) == conversationItemView.
                            getChildAt(i).findViewById(R.id.lastMessage_textView)) {
;
                        ((TextView) conversationItemView.getChildAt(i)).
                                setText(mCurrent.getLastMessage());
                    }
                }
            }
        }

        conversationItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MessagesActivity.class);
                intent.putExtra("with", mCurrent.getWith());
                mContext.startActivity(intent);
            }
        });

    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return mConversationList.size();
    }

    public static class ConversationViewHolder extends RecyclerView.ViewHolder {
        public final RelativeLayout mConversationItemView;
        final ConversationAdapter mAdapter;
        public TextView textView;

        public ConversationViewHolder(@NonNull View itemView,
                                      ConversationAdapter adapter) {
            super(itemView);
            mConversationItemView =
                    itemView.findViewById(R.id.conversationItem_relativeLayout);
            textView = itemView.findViewById(R.id.name_textView);
            mAdapter = adapter;

        }
    }
}
