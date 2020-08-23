package com.zubiisoft.zubiissenger.userinterface;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.zubiisoft.zubiissenger.MyApplication;
import com.zubiisoft.zubiissenger.R;
import com.zubiisoft.zubiissenger.database.Database;
import com.zubiisoft.zubiissenger.entity.Conversation;
import com.zubiisoft.zubiissenger.entity.User;

import java.util.ArrayList;
import java.util.LinkedList;

public class AddConversationActivity extends AppCompatActivity implements
        AddConversationAdapter.IntentCallback {

    // Tag for log chat.
    private static final String TAG = "AddConversationActivity";

    // Current context.
    private Context mContext;

    // RecyclerView.
    private RecyclerView mRecyclerView;

    // Adapter for RecyclerView.
    private AddConversationAdapter mAddConversationAdapter;

    // List to store all new conversations.
    private final LinkedList<Conversation> mAddConversationList = new LinkedList<>();

    private Database mDatabase;

    private String mUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_conversation);

        // Get an instance of current context.
        mContext = getApplicationContext();

        mDatabase = MyApplication.getDatabase();

        mUid = MyApplication.getAuth().getCurrentUser().getUid();

        getSupportActionBar().setTitle("Add new conversation");

        // Get an instance of recycler view.
        mRecyclerView = findViewById(R.id.addConversation_recyclerView);

        setConversationInRecyclerView(mUid);

        mAddConversationAdapter =
                new AddConversationAdapter(mContext, mAddConversationList, this);

        mRecyclerView.setAdapter(mAddConversationAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

    }

    private void finishActivity(String friendUid, String name, String avatar, String lastMessage) {
        Intent intent = new Intent();
        intent.putExtra("friendUid", friendUid);
        Log.d(TAG, "finishActivity: " + friendUid);
        intent.putExtra("name", name);
        intent.putExtra("avatar", avatar);
        intent.putExtra("lastMessage", lastMessage);
        Log.d(TAG, "finishActivity: " + intent.getStringExtra("friendUid"));

        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onIntentCallback(Intent intent) {

        String friendUid = intent.getStringExtra("friendUid");
        String name = intent.getStringExtra("name");
        String avatar = intent.getStringExtra("avatar");
        String lastMessage = intent.getStringExtra("lastMessage");

        finishActivity(friendUid, name, avatar, lastMessage);
    }

    private void setConversationInRecyclerView(final String uid) {
        mDatabase.readAllFriendsAtSpecificUserFromDatabase(new Database.FriendCallbackDb() {
            @Override
            public void onFriendCallbackDb(ArrayList<String> friends) {

                for (final String friendUid : friends) {
                    mDatabase.readUserFromDatabase(new Database.UserCallbackDb() {
                        @Override
                        public void onUserCallbackDb(User user) {
                            if (user != null) {
                                Conversation conversation = new Conversation(
                                        user.getAvatar(),
                                        user.getFirstName(),
                                        "",
                                        friendUid);

                                mAddConversationList.addLast(conversation);
                                mRecyclerView.getAdapter().notifyItemChanged
                                        (mAddConversationList.size() + 1);
                            }
                        }

                        @Override
                        public void onUserCallbackDb(ArrayList<User> users) {

                        }
                    }, friendUid);
                }
            }
        }, uid);
    }
}