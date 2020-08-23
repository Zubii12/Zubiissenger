package com.zubiisoft.zubiissenger.userinterface;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.zubiisoft.zubiissenger.MyApplication;
import com.zubiisoft.zubiissenger.R;
import com.zubiisoft.zubiissenger.database.Database;
import com.zubiisoft.zubiissenger.entity.Conversation;
import com.zubiisoft.zubiissenger.entity.User;

/**
 * Main Activity to choose Login or Register if the user is not already login.
 */
public class MainActivity extends AppCompatActivity {

    // Tag for log chat.
    private static final String TAG = "MainActivity";

    // Instance of FirebaseAuth;
    private FirebaseAuth mAuth;

    // Instance of Database.
    private Database mDatabase;

    private Context mContext;

    public static final int TEXT_REQUEST = 1;

    /**
     *  Create the content view, inflate the activity UI.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get an instance of Auth.
        mAuth = MyApplication.getAuth();

        // Get an instance of Database.
        mDatabase = MyApplication.getDatabase();

        mContext = getApplicationContext();
    }

    /**
     * Check if user is already signed in.
     */
    @Override
    protected void onStart() {
        super.onStart();

        // Get the current user.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Check if user is signed in (non-null) and update UI accordingly.
        if (currentUser != null) {
            startConversationActivity(currentUser.getUid());
        }
    }

    public void startConversationActivity(String uid){
        Intent intent = new Intent(mContext, ConversationActivity.class);
        intent.putExtra("uid", uid);
        startActivity(intent);
    }

    /**
     * Launch the LoginActivity.
     * @param view The Button view.
     */
    public void startLoginButton(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * Launch the RegisterActivity.
     * @param view The Button view.
     */
    public void startRegisterButton(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}