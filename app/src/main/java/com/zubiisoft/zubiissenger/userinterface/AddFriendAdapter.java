package com.zubiisoft.zubiissenger.userinterface;

import android.content.Context;
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
        void setNameCallback(User user);
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

        final User mCurrent = mAddFriendList.get(position);

        final RelativeLayout friendItemView = holder.mAddFriendItemView;

        for (int conv = 0; conv < mAddFriendList.size(); conv++) {
            for (int i = 0; i < friendItemView.getChildCount(); i++) {
                if (friendItemView.getChildAt(i) instanceof ImageView){
                    //Log.d(TAG, "onBindViewHolder: ImageView - avatar - found");
                } else if (friendItemView.getChildAt(i) instanceof TextView) {
                    if (friendItemView.getChildAt(i) == friendItemView.getChildAt(i).
                            findViewById(R.id.name_textView)) {
                        String name = mCurrent.getFirstName() + mCurrent.getLastName();
                        ((TextView) friendItemView.getChildAt(i)).setText(name);
                    } else if (friendItemView.getChildAt(i) ==
                            friendItemView.getChildAt(i).
                            findViewById(R.id.lastMessage_textView)) {
                        ((TextView) friendItemView.getChildAt(i)).
                        //       setText(mCurrent.getLastMessage());
                        setVisibility(View.INVISIBLE);

                    }
                }
            }
        }


        friendItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mListener.setNameCallback(mAddFriendList.get(position));
                Database mDatabase = MyApplication.getDatabase();
                String uidFriend = mCurrent.getUid();
                String uidUser = MyApplication.getAuth().getCurrentUser().getUid();
                mDatabase.writeFriendAnSpecificUserInDatabase(uidUser, uidFriend);

                mAddFriendList.remove(mCurrent);

                holder.mAdapter.notifyItemRemoved(position);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mAddFriendList.size();
    }

    public static class AddFriendViewHolder extends RecyclerView.ViewHolder{
        public final RelativeLayout mAddFriendItemView;

        protected ImageView avatar;
        protected TextView name;

        protected final AddFriendAdapter mAdapter;

        public AddFriendViewHolder(@NonNull View itemView, AddFriendAdapter adapter) {
            super(itemView);
            mAddFriendItemView = itemView.findViewById(R.id.conversationItem_relativeLayout);
            mAdapter = adapter;

            avatar = itemView.findViewById(R.id.avatar_imageView);
            name = itemView.findViewById(R.id.name_textView);
        }
    }
}
