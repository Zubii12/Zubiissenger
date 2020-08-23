package com.zubiisoft.zubiissenger.userinterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.zubiisoft.zubiissenger.MyApplication;
import com.zubiisoft.zubiissenger.R;
import com.zubiisoft.zubiissenger.database.Database;
import com.zubiisoft.zubiissenger.entity.User;

import java.util.ArrayList;
import java.util.LinkedList;

public class AddFriendActivity extends AppCompatActivity
        implements AddFriendAdapter.NameCallback {

    // Tag for log chat.
    private static final String TAG = "AddFriendActivity";

    // Current context.
    private Context mContext;

    // Recycler view to store the users from database.
    private RecyclerView mRecyclerView;

    // Adapter for recycler view.
    private AddFriendAdapter mAddFriendAdapter;

    // Instance of Database.
    private Database mDatabase;

    // LinkedList to store all users available from database.
    private final LinkedList<User> mUsersList = new LinkedList<>();

    private String mUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        mUid = MyApplication.getAuth().getCurrentUser().getUid();

        // Get the current context.
        mContext = getApplicationContext();

        // Get an instance of Database.
        mDatabase = MyApplication.getDatabase();

        // Get the RecyclerView.
        mRecyclerView = findViewById(R.id.addFriend_recyclerView);

        setUsersInRecyclerView();

        // Create the adapter.
        mAddFriendAdapter = new AddFriendAdapter(mContext, mUsersList, this);

        // Set the adapter to the recycler view.
        mRecyclerView.setAdapter(mAddFriendAdapter);

        // Set the default layout for recycler view..
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAddFriendAdapter.getItemCount() == 0 && mUsersList.size() == 0) {
            Toast.makeText(mContext, " I haven't found any new friends ", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void setNameCallback(@NonNull User user) {
        mDatabase.writeFriendAnSpecificUserInDatabase(mUid, user.getUid());
        Toast.makeText(mContext, "Friend successfully added ", Toast.LENGTH_SHORT).show();
    }

    private void setUsersInRecyclerView() {
        mDatabase.readAllUsersAndFriendsAtSpecificUserFromDatabase(new Database.UserAndFriendCallback() {
            @Override
            public void onUserAndFriendCallback(ArrayList<User> users, ArrayList<String> friends) {
                mUsersList.clear();
                mRecyclerView.getAdapter().notifyItemRangeRemoved(0, mUsersList.size());

                for (User user : users) {
                    Log.d(TAG, "onUserAndFriendCallback: VERIF " + user.toString());
                    Log.d(TAG, "onUserAndFriendCallback: chats " + user.getChats());
                    Log.d(TAG, "onUserAndFriendCallback: " + user.getFriendList());
                    if (!friends.contains(user.getUid())) {
                        mUsersList.addLast(user);
                        mRecyclerView.getAdapter().notifyItemInserted(mUsersList.size() + 1);
                    }
                }

            }
        }, mUid);
    }

}