package com.zubiisoft.zubiissenger.database;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.zubiisoft.zubiissenger.MyApplication;
import com.zubiisoft.zubiissenger.entity.ChatMessage;
import com.zubiisoft.zubiissenger.entity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {

    // Tag for log chat.
    private static final String TAG = "Database";

    // Instance of database.
    private DatabaseReference mDatabaseReference;

    private static Database sInstance;

    private FirebaseAuth mAuth;

    private String mCurrentUid;

    public static Database getInstance() {
        if (sInstance == null) {
            synchronized (Database.class) {
                if (sInstance == null) {
                    sInstance = new Database();
                }
            }
        }
        return sInstance;
    }

    public Database() {
        mDatabaseReference = MyApplication.getDatabaseReference();
        mAuth = MyApplication.getAuth();
    }

    public interface UserCallbackDb {
        void onUserCallbackDb(User user);
        void onUserCallbackDb(ArrayList<User> users);
    }

    public interface ChatsCallbackDb {
        void onChatsCallbackDb(ArrayList<ChatMessage> chats, ArrayList<User> conversationsWithUser);
    }

    public interface ChatCallbackDb {
        void onChatCallbackDb(ChatMessage chatMessage, User friend);
    }

    public interface FriendCallbackDb {
        void onFriendCallbackDb(ArrayList<String> friends);
    }

    public interface UserAndFriendCallback {
        void onUserAndFriendCallback(ArrayList<User> users, ArrayList<String> friends);
    }

    public interface IdChatCallbackDb {
        void idChatCallbackDb(String idChat);
    }

    public interface MessagesCallbackDb{
        void onAddMessagesCallbackDb(ArrayList<String> messages);
        void onRemovedMessagesCallbackDb(ArrayList<String> messages);
    }

    public interface MessageCallbackDb{
        void onMessageCallbackDb(String message);
    }

    public interface ChatMessageCallbackDb{
        void onChatMessageCallbackDb(ChatMessage chatMessage);
    }
    public interface ExistMessagesCallbackDb{
        void onExistMessagesCallbackDb(boolean existMessages);
    }
    /**
     * Write an user in database.
     * @param user User class.
     */
    public void writeUserInDatabase(@NonNull User user) {
        mDatabaseReference.child("users").child(user.getUid()).setValue(user);
    }

    public void writeFriendAnSpecificUserInDatabase(final String uidUser,
                                                    final String uidFriend) {
        mDatabaseReference.child("users").child(uidUser).child("friendList").push().
                setValue(uidFriend);
        mDatabaseReference.child("users").child(uidFriend).child("friendList").push().
                setValue(uidUser);
    }

    /**
     * Write a chat in database at specific user.
     * @param chat User.Chat class
     */
    public void writeIdChatAtSpecificUserInDatabase(@NonNull ChatMessage chat) {
        mDatabaseReference.child("users").child(chat.getSender()).child("chats").
                child(chat.getIdChat()).setValue(chat.getReceiver());
        mDatabaseReference.child("users").child(chat.getReceiver()).child("chats").
                child(chat.getIdChat()).setValue(chat.getSender());
    }

    public void writeChatMessageInDatabase(@NonNull ChatMessage chatMessage) {
        mDatabaseReference.child("chats").child(chatMessage.getIdChat()).setValue(chatMessage);
    }

    public void writeMessageAtSpecificChatInDatabase(ArrayList<String> message,
                                                     String idChat) {
        mDatabaseReference.child("chats").child(idChat).child("messages").push().setValue(message);

    }

    public void readMessagesAtSpecificIdChatFromDatabase(final MessagesCallbackDb messagesCallbackDb, String idChat) {
        mDatabaseReference.child("chats").child(idChat).child("messages").orderByChild("0").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                messagesCallbackDb.onAddMessagesCallbackDb((ArrayList<String>) snapshot.getValue());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                messagesCallbackDb.onRemovedMessagesCallbackDb((ArrayList<String>) snapshot.getValue());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void existMessagesAtSpecificIdChatInDatabase(final ExistMessagesCallbackDb existMessagesCallbackDb,String idChat) {
        mDatabaseReference.child("chats").child(idChat).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("messages").getValue() == null) {
                    existMessagesCallbackDb.onExistMessagesCallbackDb(false);
                } else {
                    existMessagesCallbackDb.onExistMessagesCallbackDb(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    public void readLastMessageAtSpecificIdChatFromDatabase(final MessageCallbackDb messageCallbackDb, final String idChat) {
        existMessagesAtSpecificIdChatInDatabase(new ExistMessagesCallbackDb() {
            @Override
            public void onExistMessagesCallbackDb(boolean existMessages) {
                if (existMessages) {
                    mDatabaseReference.child("chats").child(idChat).child("messages").orderByChild("0").limitToLast(1).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            ArrayList<String> cf = (ArrayList<String>) snapshot.getValue();
                            Log.d(TAG, "onChildAdded: " + snapshot);
                            //Log.d(TAG, "onChildAdded: " + cf);
                            messageCallbackDb.onMessageCallbackDb(cf.get(2));
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            Log.d(TAG, "onChildChanged: ");
                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                            Log.d(TAG, "onChildRemoved: ");
                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            Log.d(TAG, "onChildMoved: ");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.d(TAG, "onCancelled: ");
                        }
                    });
                } else {
                    messageCallbackDb.onMessageCallbackDb("");
                }
            }
        }, idChat);

        /*
        mDatabaseReference.child("chats").child(idChat).child("messages").orderByChild("0").limitToLast(1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ArrayList<String> cf = (ArrayList<String>) snapshot.getValue();
                Log.d(TAG, "onChildAdded: " + snapshot);
                Log.d(TAG, "onChildAdded: " + cf);
                messageCallbackDb.onMessageCallbackDb(cf.get(2));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d(TAG, "onChildChanged: ");
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "onChildRemoved: ");
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d(TAG, "onChildMoved: ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: ");
            }
        });
         */
    }

    public void readUserFromDatabase(final UserCallbackDb userCallbackDb, final String uid) {

        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, String> chatsMap;
                ArrayList<String> chats = new ArrayList<>();
                Map<String, String> friendsMap;
                ArrayList<String> friends = new ArrayList<>();

                if (snapshot.child("users").child(uid).child("chats").getValue() != null) {
                    chatsMap = (Map) snapshot.child("users").child(uid).child("chats").getValue();
                    chats = new ArrayList<>(chatsMap.values());
                }

                if (snapshot.child("users").child(uid).child("friendList").getValue() != null) {
                    friendsMap = (Map) snapshot.child("users").child(uid).child("friendList").getValue();
                    friends = new ArrayList<>(friendsMap.values());
                }

                User user = new User(
                        snapshot.child("users").child(uid).child("uid").getValue(String.class),
                        snapshot.child("users").child(uid).child("firstName").getValue(String.class),
                        snapshot.child("users").child(uid).child("lastName").getValue(String.class),
                        snapshot.child("users").child(uid).child("email").getValue(String.class),
                        snapshot.child("users").child(uid).child("avatar").getValue(String.class),
                        snapshot.child("users").child(uid).child("phone").getValue(String.class),
                        snapshot.child("users").child(uid).child("createdAt").getValue(String.class),
                        chats,
                        friends);

                userCallbackDb.onUserCallbackDb(user);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

    }

    public void readChatsAtSpecificUserFromDatabase(final ChatsCallbackDb chatsCallbackDb, @NonNull final String uid) {
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> chats = new ArrayList<>();
                ArrayList<User> conversationWithFriends = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.child("users").child(uid).child("chats").getChildren()) {
                    String uid = dataSnapshot.getValue(String.class);
                    String idChat = dataSnapshot.getKey();
                    chats.add(idChat);

                    Map<String, String> chatsMap;
                    ArrayList<String> _chats = new ArrayList<>();
                    Map<String, String> friendsMap;
                    ArrayList<String> friends = new ArrayList<>();

                    if (snapshot.child("users").child(uid).child("chats").getValue() != null) {
                        chatsMap = (Map) snapshot.child("users").child(uid).child("chats").getValue();
                        _chats = new ArrayList<>(chatsMap.values());
                    }

                    if (snapshot.child("users").child(uid).child("friendList").getValue() != null) {
                        friendsMap = (Map) snapshot.child("users").child(uid).child("friendList").getValue();
                        friends = new ArrayList<>(friendsMap.values());
                    }

                    User user = new User(
                            snapshot.child("users").child(uid).child("uid").getValue(String.class),
                            snapshot.child("users").child(uid).child("firstName").getValue(String.class),
                            snapshot.child("users").child(uid).child("lastName").getValue(String.class),
                            snapshot.child("users").child(uid).child("email").getValue(String.class),
                            snapshot.child("users").child(uid).child("avatar").getValue(String.class),
                            snapshot.child("users").child(uid).child("phone").getValue(String.class),
                            snapshot.child("users").child(uid).child("createdAt").getValue(String.class),
                            _chats,
                            friends);
                    conversationWithFriends.add(user);

                }

                ArrayList<ChatMessage> chatMessages = new ArrayList<>();
                for (String idChat : chats) {

                    Map<String, ArrayList<String>> messagesMap;
                    ArrayList<ArrayList<String>> messages = new ArrayList<>();
                    if (snapshot.child("chats").child(idChat).child("messages").getValue() != null) {
                        messagesMap = (Map) snapshot.child("chats").child(idChat).child("messages").getValue();
                        messages = new ArrayList<>(messagesMap.values());
                    }

                    ChatMessage chatMessage = new ChatMessage(
                            snapshot.child("chats").child(idChat).child("idChat").getValue(String.class),
                            snapshot.child("chats").child(idChat).child("sender").getValue(String.class),
                            snapshot.child("chats").child(idChat).child("receiver").getValue(String.class),
                            messages);

                    chatMessages.add(chatMessage);
                }
                chatsCallbackDb.onChatsCallbackDb(chatMessages, conversationWithFriends);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void readChatMessageAtSpecificUserFromDatabase
            (final ChatCallbackDb chatCallbackDb, final String uid, final String friendUid) {
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, String> chatsMap;
                ArrayList<String> chats = new ArrayList<>();
                Map<String, String> friendsMap;
                ArrayList<String> friends = new ArrayList<>();

                if (snapshot.child("users").child(uid).child("chats").getValue() != null) {
                    chatsMap = (Map) snapshot.child("users").child(uid).child("chats").getValue();
                    chats = new ArrayList<>(chatsMap.values());
                }

                if (snapshot.child("users").child(uid).child("friendList").getValue() != null) {
                    friendsMap = (Map) snapshot.child("users").child(uid).child("friendList").getValue();
                    friends = new ArrayList<>(friendsMap.values());
                }

                User friend = new User(
                        snapshot.child("users").child(uid).child("uid").getValue(String.class),
                        snapshot.child("users").child(uid).child("firstName").getValue(String.class),
                        snapshot.child("users").child(uid).child("lastName").getValue(String.class),
                        snapshot.child("users").child(uid).child("email").getValue(String.class),
                        snapshot.child("users").child(uid).child("avatar").getValue(String.class),
                        snapshot.child("users").child(uid).child("phone").getValue(String.class),
                        snapshot.child("users").child(uid).child("createdAt").getValue(String.class),
                        chats,
                        friends);

                ChatMessage chatMessage = new ChatMessage();


                for (DataSnapshot dataSnapshot : snapshot.child("users").child(friendUid).child("chats").getChildren()) {
                    if (dataSnapshot.getValue(String.class).equals(uid)) {
                        String idChat = dataSnapshot.getKey();
                        Map<String, ArrayList<String>> messagesMap;
                        ArrayList<ArrayList<String>> messages = new ArrayList<>();


                        if (snapshot.child("chats").child(idChat).child("messages").getValue() != null) {
                            messagesMap = (Map) snapshot.child("chats").child(idChat).child("messages").getValue();
                            messages = new ArrayList<>(messagesMap.values());
                        }

                        chatMessage = new ChatMessage(
                                snapshot.child("chats").child(idChat).child("idChat").getValue(String.class),
                                snapshot.child("chats").child(idChat).child("sender").getValue(String.class),
                                snapshot.child("chats").child(idChat).child("receiver").getValue(String.class),
                                messages);

                    }
                }

                chatCallbackDb.onChatCallbackDb(chatMessage, friend);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    public void readAllFriendsAtSpecificUserFromDatabase
            (final FriendCallbackDb friendCallbackDb, final String uid) {
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> friends = new ArrayList<>();
                for (DataSnapshot dataSnapshot :
                        snapshot.child("users").child(uid).child("friendList").getChildren()) {
                    friends.add(dataSnapshot.getValue(String.class));
                }
                friendCallbackDb.onFriendCallbackDb(friends);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    public void readAllUsersAndFriendsAtSpecificUserFromDatabase
            (final UserAndFriendCallback userAndFriendCallback, final String uid) {
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long _long = snapshot.child("users").getChildrenCount();
                int length = (int) _long;

                ArrayList<User> users = new ArrayList<>(length);
                mCurrentUid = mAuth.getCurrentUser().getUid();


                for (DataSnapshot dataSnapshot : snapshot.child("users").getChildren()) {

                    if (!dataSnapshot.child("uid").getValue().equals(mCurrentUid)) {
                        Map<String, String> chatsMap;
                        ArrayList<String> chats = new ArrayList<>();
                        Map<String, String> friendsMap;
                        ArrayList<String> friends = new ArrayList<>();

                        if (dataSnapshot.child("chats").getValue() != null) {
                            chatsMap = (Map) dataSnapshot.child("chats").getValue();
                            chats = new ArrayList<>(chatsMap.values());
                        }

                        if (dataSnapshot.child("friendList").getValue() != null) {
                            friendsMap = (Map) dataSnapshot.child("friendList").getValue();
                            friends = new ArrayList<>(friendsMap.values());
                        }

                        users.add(new User(
                                dataSnapshot.child("uid").getValue(String.class),
                                dataSnapshot.child("firstName").getValue(String.class),
                                dataSnapshot.child("lastName").getValue(String.class),
                                dataSnapshot.child("email").getValue(String.class),
                                dataSnapshot.child("avatar").getValue(String.class),
                                dataSnapshot.child("phone").getValue(String.class),
                                dataSnapshot.child("createdAt").getValue(String.class),
                                chats,
                                friends));
                    }

                }

                ArrayList<String> friends = new ArrayList<>();
                for (DataSnapshot dataSnapshot :
                        snapshot.child("users").child(uid).child("friendList").getChildren()) {
                    friends.add(dataSnapshot.getValue(String.class));
                }

                userAndFriendCallback.onUserAndFriendCallback(users, friends);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void readIdChatAtSpecificUserAndFriendFromDatabase(final IdChatCallbackDb idChatCallbackDb, final String uid, final String friendUid) {
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String idChat = null;
                for (DataSnapshot dataSnapshot : snapshot.child("users").child(uid).child("chats").getChildren()) {
                    if (dataSnapshot.getValue(String.class).equals(friendUid)) {
                        idChat = dataSnapshot.getKey();
                    }
                }

                idChatCallbackDb.idChatCallbackDb(idChat);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void readAllUsersFromDatabase(final UserCallbackDb userCallbackDb) {
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long _long = snapshot.child("users").getChildrenCount();
                int length = (int) _long;

                ArrayList<User> users = new ArrayList<>(length);
                mCurrentUid = mAuth.getCurrentUser().getUid();

                for (DataSnapshot dataSnapshot : snapshot.child("users").getChildren()) {
                    ArrayList<String> friendList = new ArrayList<>();
                    ArrayList<String> chats = new ArrayList<>();

                    if (!dataSnapshot.child("uid").getValue().equals(mCurrentUid)) {

                        if (dataSnapshot.child("friendList").getValue() != null) {
                            friendList = (ArrayList<String>) dataSnapshot.child("friendList").
                                    getValue();
                        }

                        if (dataSnapshot.child("chats").getValue() != null) {
                            chats = (ArrayList<String>) dataSnapshot.child("chats").getValue();
                        }
                        users.add(new User(
                                dataSnapshot.child("uid").getValue(String.class),
                                dataSnapshot.child("firstName").getValue(String.class),
                                dataSnapshot.child("lastName").getValue(String.class),
                                dataSnapshot.child("email").getValue(String.class),
                                dataSnapshot.child("avatar").getValue(String.class),
                                dataSnapshot.child("phone").getValue(String.class),
                                dataSnapshot.child("createdAt").getValue(String.class),
                                chats,
                                friendList));

                        Log.d(TAG, "onDataChange: OK " );

                    }
                }
                userCallbackDb.onUserCallbackDb(users);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void listenerOnChatMessages(final ChatMessageCallbackDb chatMessageCallbackDb) {
        mDatabaseReference.child("chats").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setIdChat(snapshot.child("idChat").getValue(String.class));
                chatMessage.setSender(snapshot.child("sender").getValue(String.class));
                chatMessage.setReceiver(snapshot.child("receiver").getValue(String.class));

                chatMessageCallbackDb.onChatMessageCallbackDb(chatMessage);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setIdChat(snapshot.child("idChat").getValue(String.class));
                chatMessage.setSender(snapshot.child("sender").getValue(String.class));
                chatMessage.setReceiver(snapshot.child("receiver").getValue(String.class));
                chatMessageCallbackDb.onChatMessageCallbackDb(chatMessage);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
