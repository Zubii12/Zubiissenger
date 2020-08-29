package com.zubiisoft.zubiissenger.userinterface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zubiisoft.zubiissenger.MyApplication;
import com.zubiisoft.zubiissenger.R;
import com.zubiisoft.zubiissenger.database.Database;
import com.zubiisoft.zubiissenger.entity.ChatMessage;
import com.zubiisoft.zubiissenger.entity.Conversation;
import com.zubiisoft.zubiissenger.entity.User;
import com.zubiisoft.zubiissenger.utils.Utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;


/**
 * ConversationActivity. Show all available conversations.
 *
 */
public class ConversationActivity extends AppCompatActivity {

    // Tag for log chat.
    private static final String TAG = "ConversationActivity";

    public static final int REQUEST_CODE = 1;

    // LinkedList to store all conversation available.
    private final LinkedList<Conversation> mConversationList = new LinkedList<>();

    // Recycler view to show all conversations.
    private RecyclerView mRecyclerView;

    // Adapter...
    private ConversationAdapter mConversationAdapter;

    // Instance of Database.
    private Database mDatabase;

    // Instance of context.
    private Context mContext;

    // Instance of context.
    private ActionBar mActionBar;

    private String mUid;

    private FloatingActionButton floatingActionButton;

    /**
     * Create the content view, inflate the activity UI.
     *
     * @param savedInstanceState Saved instance state bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        //Init methods.
        initInstances();
        initRecyclerView();

        // Set the conversations in recycler view.
        setConversationsInRecyclerView(mUid);

        // Set the user information for the activity.
        setUserInformation(mUid);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AddConversationActivity.class);
                startActivityForResult(intent, REQUEST_CODE);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE  && resultCode  == RESULT_OK) {
            String receiver = data.getStringExtra("friendUid");
            String name = data.getStringExtra("name");
            String lastMessage = data.getStringExtra("lastMessage");
            String avatar = data.getStringExtra("avatar");

            ArrayList<ArrayList<String>> messages = new ArrayList<>();

            ChatMessage chatMessage = new ChatMessage(Utils.getRandomUid(), mUid, receiver, messages);

            mDatabase.writeIdChatAtSpecificUserInDatabase(chatMessage);
            mDatabase.writeChatMessageInDatabase(chatMessage);
        }

    }

    /**
     * Inflates the menu, and adds items to the action bar if it is present.
     *
     * @param menu Menu to inflate.
     * @return Returns true if the menu inflated.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_conversation, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Handles app bar item clicks.
     *
     * @param item Item clicked.
     * @return True if one of the defined items was clicked.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.addNewFriend_item:
                startActivity(new Intent(mContext, AddFriendActivity.class));
                break;
            case R.id.signOut_item:
                MyApplication.getAuth().signOut();
                startActivity(new Intent(mContext, MainActivity.class));
                break;
            default:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initInstances() {
        // It doesn't work in API 23 when I give it as a parameter for RecyclerView
        //mContext = getApplicationContext();

        mContext = this;

        // Get an instance of Database.
        mDatabase = MyApplication.getDatabase();

        // Get an instance of Actionbar.
        mActionBar = getSupportActionBar();

        // User id.
        mUid = MyApplication.getAuth().getCurrentUser().getUid();

        // Get the RecyclerView.
        mRecyclerView = findViewById(R.id.conversations_recyclerView);

        // Get an instance of FloatingActionButton.
        floatingActionButton = findViewById(R.id.addConversation_floatingActionButton);
    }

    private void initRecyclerView() {
        mConversationAdapter = new ConversationAdapter(mContext, mConversationList);

        mRecyclerView.setAdapter(mConversationAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

    }

    /**
     * Clear the data from mConversationList and notify the adapter.
     */
    private void clearConversationList() {
        mConversationList.clear();
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    /**
     *
     * @param uid
     */
    private void setUserInformation(String uid) {
        mDatabase.readUserFromDatabase(new Database.UserCallbackDb() {
            @Override
            public void onUserCallbackDb(final User user) {
                mActionBar.setTitle(user.getFirstName());
            }

            @Override
            public void onUserCallbackDb(ArrayList<User> users) {

            }
        }, uid);

    }

    /**
     *
     * @param uid
     */
    private void setConversationsInRecyclerView(final String uid) {
        mDatabase.readChatsAtSpecificUserFromDatabase(new Database.ChatsCallbackDb() {
            @Override
            public void onChatsCallbackDb(ArrayList<ChatMessage> chatMessages, ArrayList<User> conversationsWithFriends) {
                clearConversationList();
                for (final User user : conversationsWithFriends) {
                    mDatabase.readIdChatAtSpecificUserAndFriendFromDatabase(new Database.IdChatCallbackDb() {
                        @Override
                        public void idChatCallbackDb(String idChat) {
                            clearConversationList();
                            mDatabase.readLastMessageAtSpecificIdChatFromDatabase(new Database.MessageCallbackDb() {
                                @Override
                                public void onMessageCallbackDb(String message) {
                                    mConversationList.addLast(new Conversation(
                                            user.getAvatar(),
                                            user.getFirstName(),
                                            message,
                                            user.getUid()));
                                    mRecyclerView.getAdapter().notifyDataSetChanged();
                                }
                            }, idChat);
                        }
                    }, mUid, user.getUid());
                }
            }
        }, uid);
    }
}