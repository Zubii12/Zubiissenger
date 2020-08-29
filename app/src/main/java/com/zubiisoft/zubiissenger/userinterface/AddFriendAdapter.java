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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zubiisoft.zubiissenger.MyApplication;
import com.zubiisoft.zubiissenger.R;
import com.zubiisoft.zubiissenger.database.Database;
import com.zubiisoft.zubiissenger.entity.User;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.LinkedList;

public class AddFriendAdapter extends
        RecyclerView.Adapter<AddFriendAdapter.AddFriendViewHolder> {

    private static final String TAG = "AddFriendAdapter";

    private LinkedList<User> mAddFriendList;

    private final LayoutInflater mInflater;

    private Context mContext;

    public interface NameCallback {
        void setNameCallback(Intent intent);
    }

    NameCallback mListener;

    /**
     *
     * @param context
     * @param addFriendList
     */
    public AddFriendAdapter(Context context, LinkedList<User> addFriendList,
                            NameCallback nameCallback) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mAddFriendList = addFriendList;
        mListener = nameCallback;
    }

    @NonNull
    @Override
    public AddFriendAdapter.AddFriendViewHolder onCreateViewHolder
            (@NonNull ViewGroup parent, int viewType) {

        View view = mInflater.inflate
                (R.layout.conversation_item, parent, false);

        return new AddFriendViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull final AddFriendViewHolder holder,
                                 final int position) {

        final User current = mAddFriendList.get(position);

        final RelativeLayout friendItemView = holder.mAddFriendItemView;

        //holder.mAvatar TODO
        String name = current.getFirstName() + current.getLastName();
        holder.mName.setText(name);

        friendItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("friendUid", current.getUid());

                mAddFriendList.remove(current);
                holder.mAdapter.notifyItemRemoved(position);

                mListener.setNameCallback(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAddFriendList.size();
    }

    public static class AddFriendViewHolder extends RecyclerView.ViewHolder{
        protected final RelativeLayout mAddFriendItemView;
        protected ImageView mAvatar;
        protected TextView mName;
        protected final AddFriendAdapter mAdapter;

        public AddFriendViewHolder(@NonNull View itemView, AddFriendAdapter adapter) {
            super(itemView);
            mAddFriendItemView =
                    itemView.findViewById(R.id.conversationItem_relativeLayout);
            mAvatar = itemView.findViewById(R.id.avatar_imageView);
            mName = itemView.findViewById(R.id.name_textView);
            mAdapter = adapter;
        }
    }
}
