
/*
        ArrayList<User> users = userGenerator();
        ArrayList<User.Chat> chats = chatGenerator();

        // Write in database.
        for (int i = 0; i < users.size(); i++) {
            //database.writeUserInDatabase(users.get(i));
        }

        for (int i = 0; i < chats.size(); i++) {
            //database.writeChatAtSpecificUserInDatabase(chats.get(i), users.get(i).getUid());
        }

        // Read from database.
        //for (int i = 0; i < users.size(); i++) {
            String uid = users.get(0).getUid();
            final User userr = new User();
            database.readUserFromDatabase(new UserCallbackDb() {
                @Override
                public void onUserCallbackDb(User user) {

                }
            }, uid);

        for (int i = 0; i < chats.size(); i++){
//            Log.d(TAG, "onCreate: " + database.readChatAtSpecificUserFromDatabase(chats.get(i).getIdChat(), users.get(i).getUid()));
        }



import com.zubiisoft.zubiissenger.entity.User;

public interface UserCallbackDb {
    void onUserCallbackDb(User user);
}
/*
        ArrayList<User> users = userGenerator();
        ArrayList<User.Chat> chats = chatGenerator();

        // Write in database.
        for (int i = 0; i < users.size(); i++) {
            //database.writeUserInDatabase(users.get(i));
        }

        for (int i = 0; i < chats.size(); i++) {
            //database.writeChatAtSpecificUserInDatabase(chats.get(i), users.get(i).getUid());
        }

        // Read from database.
        //for (int i = 0; i < users.size(); i++) {
            String uid = users.get(0).getUid();
            final User userr = new User();
            database.readUserFromDatabase(new UserCallbackDb() {
                @Override
                public void onUserCallbackDb(User user) {

                }
            }, uid);

        for (int i = 0; i < chats.size(); i++){
//            Log.d(TAG, "onCreate: " + database.readChatAtSpecificUserFromDatabase(chats.get(i).getIdChat(), users.get(i).getUid()));
        }


public interface UserCallbackDb {
    void onUserCallbackDb(User user);
}

@NonNull
private ArrayList<User> userGenerator() {
        ArrayList<User> users = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
final int finalI = i;
        User zubii = new User(String.valueOf(i), "firstName" + i, "lastName" + i,
        "ion@" + i, "noAvatar", "0" + i, "12/05/200" + i,
        new ArrayList<String>(){{
        add(String.valueOf(finalI));
        add(String.valueOf(finalI));
        }});
        users.add(zubii);
        }

        return users;
        }

@NonNull
private ArrayList<User.Chat> chatGenerator(){
        ArrayList<User.Chat> chats = new ArrayList<>();

        ArrayList<ArrayList<String>> messages = new ArrayList<>();
        for (int i = 0; i < 3; i ++) {
final int finalI = i;
        messages.add(new ArrayList<String>() {{
        add("11:1" + finalI);
        add("trimis" + finalI);
        add("primit" + finalI);
        }});
        }

        for(int i = 0; i < 5; i++) {
        User.Chat zubiiChat = new User.Chat("id_chat_" + i, "" + i, messages);
        chats.add(zubiiChat);
        }
        return chats;
        }

        */

/**
Bundle bundle = new Bundle();
        bundle.putString("uid", uid);
        bundle.putString("firstName", user.getFirstName());
        bundle.putString("lastName", user.getLastName());
        bundle.putString("email", user.getEmail());
        bundle.putString("avatar", user.getAvatar());
        bundle.putString("phone", user.getPhone());
        bundle.putString("createdAt", user.getCreatedAt());
        bundle.putStringArrayList("friendList", user.getFriendList());
*/