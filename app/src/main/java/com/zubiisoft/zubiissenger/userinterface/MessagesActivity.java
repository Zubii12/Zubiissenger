package com.zubiisoft.zubiissenger.userinterface;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.zubiisoft.zubiissenger.MyApplication;
import com.zubiisoft.zubiissenger.R;
import com.zubiisoft.zubiissenger.database.Database;
import com.zubiisoft.zubiissenger.entity.ChatMessage;
import com.zubiisoft.zubiissenger.entity.User;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;

public class MessagesActivity extends AppCompatActivity {

    // Tag for log chat.
    private static final String TAG = "MessagesActivity";

    // Instance of context.
    private Context mContext;

    private EditText mMessage;

    private ImageButton mSendMessage;

    private final ChatMessage mChat = new ChatMessage();

    private final LinkedList<ArrayList<String>> mMessagesList = new LinkedList<>();

    // Recycler view to show all conversations.
    private RecyclerView mRecyclerView;

    // Adapter...
    private MessageAdapter mMessageAdapter;

    // Instance of Database.
    private Database mDatabase;

    // Instance of context.
    private ActionBar mActionBar;

    private String mUid;

    private String mWith;

    private String currentMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        initInstances();
        initRecyclerView();

        clearMessagesList();
        setMessagesInRecyclerView(mUid);

        mMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged
                    (CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                currentMessage = charSequence.toString();

            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        mSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ArrayList<String> messageToSent = new ArrayList<>();
                long millis = Calendar.getInstance().getTimeInMillis();

                messageToSent.add(String.valueOf(millis));
                messageToSent.add(mUid);
                messageToSent.add(currentMessage);

                mDatabase.readIdChatAtSpecificUserAndFriendFromDatabase(new Database.IdChatCallbackDb() {
                    @Override
                    public void idChatCallbackDb(String idChat) {
                        mDatabase.writeMessageAtSpecificChatInDatabase(messageToSent, idChat);
                    }
                }, mUid, mWith);

                mMessage.setText("");
                currentMessage = "";

            }
        });
    }

    private void initInstances() {
        // Get the current context.
        mContext = getApplicationContext();

        // Get an instance of Database.
        mDatabase = MyApplication.getDatabase();

        // Get an instance of Actionbar.
        mActionBar = getSupportActionBar();

        mMessage = findViewById(R.id.message_editText);
        mSendMessage = findViewById(R.id.sendMessage_imageButton);
        mRecyclerView = findViewById(R.id.messages_recyclerView);

        // User id.
        mUid = MyApplication.getAuth().getCurrentUser().getUid();

        // Friend id.
        mWith = getIntent().getStringExtra("with");

    }

    private void initRecyclerView() {
        // Create the adapter.
        mMessageAdapter = new MessageAdapter(mContext, mMessagesList, mUid);

        // Set the adapter to the recycler view.
        mRecyclerView.setAdapter(mMessageAdapter);

        // Set the default layout for recycler view..
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        // RecyclerView resizing.
        ((LinearLayoutManager)mRecyclerView.getLayoutManager()).setStackFromEnd(true);



    }

    private void setMessagesInRecyclerView(String uid){
        mDatabase.readIdChatAtSpecificUserAndFriendFromDatabase(new Database.IdChatCallbackDb() {
            @Override
            public void idChatCallbackDb(String idChat) {
                mDatabase.readMessagesAtSpecificIdChatFromDatabase(new Database.MessagesCallbackDb() {
                    @Override
                    public void onAddMessagesCallbackDb(ArrayList<String> messages) {
                        mMessagesList.addLast(messages);
                        mRecyclerView.getAdapter().notifyDataSetChanged();
                        mRecyclerView.getLayoutManager().scrollToPosition(mMessagesList.size());
                    }

                    @Override
                    public void onRemovedMessagesCallbackDb(ArrayList<String> messages) {
                        if (mMessagesList.contains(messages)) {
                            mMessagesList.remove(messages);
                            mRecyclerView.getAdapter().notifyDataSetChanged();
                        }
                    }
                }, idChat);
            }
        }, uid, mWith);
    }


    /**
     * Clear the data from mMessagesList and notify the adapter.
     */
    private void clearMessagesList() {
        mMessagesList.clear();
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }
}