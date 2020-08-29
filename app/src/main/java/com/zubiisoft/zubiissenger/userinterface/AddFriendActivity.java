package com.zubiisoft.zubiissenger.userinterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
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

        initInstances();
        initRecyclerView();

        setUsersInRecyclerView();
    }

    @Override
    public void setNameCallback(@NonNull Intent intent) {
        String friendUid = intent.getStringExtra("friendUid");
        mDatabase.writeFriendAnSpecificUserInDatabase(mUid, friendUid);
        Toast.makeText(mContext, "Friend successfully added ", Toast.LENGTH_SHORT).show();
    }

    private void initInstances() {
        mUid = MyApplication.getAuth().getCurrentUser().getUid();

        // Get the current context.
        mContext = getApplicationContext();

        // Get an instance of Database.
        mDatabase = MyApplication.getDatabase();

        // Get the RecyclerView.
        mRecyclerView = findViewById(R.id.addFriend_recyclerView);

    }

    private void initRecyclerView() {
        // Create the adapter.
        mAddFriendAdapter = new AddFriendAdapter(mContext, mUsersList, this);

        // Set the adapter to the recycler view.
        mRecyclerView.setAdapter(mAddFriendAdapter);

        // Set the default layout for recycler view..
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    private void setUsersInRecyclerView() {
        mDatabase.readAllUsersAndFriendsAtSpecificUserFromDatabase(new Database.UserAndFriendCallback() {
            @Override
            public void onUserAndFriendCallback(ArrayList<User> users, ArrayList<String> friends) {
                clearConversationList();

                for (User user : users) {
                    if (!friends.contains(user.getUid())) {
                        mUsersList.addLast(user);
                        mRecyclerView.getAdapter().notifyDataSetChanged();
                    }
                }
            }
        }, mUid);
    }
    /**
     * Clear the data from mUsersList and notify the adapter.
     */
    private void clearConversationList() {
        mUsersList.clear();
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

}