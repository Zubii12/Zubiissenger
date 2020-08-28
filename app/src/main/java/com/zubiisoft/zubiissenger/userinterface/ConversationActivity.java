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

    private LinkedList<Conversation> mFriendList = new LinkedList<>();

    private String mUid;

    /**
     * Create the content view, inflate the activity UI.
     *
     * @param savedInstanceState Saved instance state bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        // It doesn't work in API 23 when I give it as a parameter for RecyclerView
        //mContext = getApplicationContext();

        mContext = this;

        // Get an instance of Database.
        mDatabase = MyApplication.getDatabase();

        // Get an instance of Actionbar.
        mActionBar = getSupportActionBar();

        // User id.
        mUid = MyApplication.getAuth().getCurrentUser().getUid();

        //
        mActionBar.setTitle(mUid);

        // Get the RecyclerView.
        mRecyclerView = findViewById(R.id.conversations_recyclerView);

        mConversationAdapter = new ConversationAdapter(mContext, mConversationList);

        mRecyclerView.setAdapter(mConversationAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        // Set the user information for the activity.
        setUserInformation(mUid);

        // Set the conversations in recycler view.
        setConversationsInRecyclerView(mUid);

        FloatingActionButton floatingActionButton =
                findViewById(R.id.addConversation_floatingActionButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AddConversationActivity.class);
                startActivityForResult(intent, REQUEST_CODE);

            }
        });

        setListenerOnMessages();
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

            ChatMessage chatMessage = new ChatMessage(getRandomUid(), mUid, receiver, messages);

            mDatabase.writeIdChatAtSpecificUserInDatabase(chatMessage);
            mDatabase.writeChatMessageInDatabase(chatMessage);

            //setConversationsInRecyclerView(mUid);
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

                mConversationList.clear();
                mRecyclerView.getAdapter().notifyItemRangeRemoved(0, mConversationList.size());


                for (final User user : conversationsWithFriends) {
                    mDatabase.readIdChatAtSpecificUserAndFriendFromDatabase(new Database.IdChatCallbackDb() {
                        @Override
                        public void idChatCallbackDb(String idChat) {

                            mConversationList.clear();
                            mRecyclerView.getAdapter().notifyItemRangeRemoved(0, mConversationList.size());

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
/*
                    for (String idChatWithFriend : user.getChats()) {
                        Log.d(TAG, "onChatsCallbackDb: + idChatWithFriend" + idChatWithFriend);
                        mDatabase.readIdChatAtSpecificUserAndFriendFromDatabase(new Database.IdChatCallbackDb() {
                            @Override
                            public void idChatCallbackDb(String idChat) {
                                mDatabase.readLastMessageAtSpecificIdChatFromDatabase(new Database.MessageCallbackDb() {
                                    @Override
                                    public void onMessageCallbackDb(String message) {
                                        Log.d(TAG, "onMessageCallbackDb: " + message);
                                        mConversationList.addLast(new Conversation(
                                                user.getAvatar(),
                                                user.getFirstName(),
                                                message,
                                                user.getUid()));
                                        //mRecyclerView.getAdapter().notifyItemInserted(mConversationList.size() + 1);
                                        mRecyclerView.getAdapter().notifyDataSetChanged();
                                    }
                                }, idChat);

                            }
                        },user.getUid(), idChatWithFriend);

                    }
*/
                    /*
                    mConversationList.addLast(new Conversation(
                            user.getAvatar(),
                            user.getFirstName(),
                            "",
                            user.getUid()));
                    mRecyclerView.getAdapter().notifyItemInserted(mConversationList.size() + 1);

                     */
                }
            }
        }, uid);
    }

    @NonNull
    private String getRandomUid() {
        int length = 28;

        String symbol = "-/.^&*_!@%=+>)";
        String capLetter = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String smallLetter = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String finalString =  capLetter + smallLetter + numbers;

        Random random = new Random();

        char[] uid = new char[length];

        for (int i = 0; i < length; i++) {
            uid[i] = finalString.charAt(random.nextInt(finalString.length()));
        }

        return String.valueOf(uid);
    }

    private void setListenerOnMessages() {
        mDatabase.listenerOnChatMessages(new Database.ChatMessageCallbackDb() {
            @Override
            public void onChatMessageCallbackDb(final ChatMessage chatMessage) {
                mDatabase.readUserFromDatabase(new Database.UserCallbackDb() {
                    @Override
                    public void onUserCallbackDb(final User user) {
                        mDatabase.readLastMessageAtSpecificIdChatFromDatabase(new Database.MessageCallbackDb() {
                            @Override
                            public void onMessageCallbackDb(String message) {
                                /*
                                 */
                            }
                        }, chatMessage.getIdChat());
                    }

                    @Override
                    public void onUserCallbackDb(ArrayList<User> users) { }
                }, chatMessage.getReceiver());
            }
        });
    }
}