package com.zubiisoft.zubiissenger.userinterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.zubiisoft.zubiissenger.MyApplication;
import com.zubiisoft.zubiissenger.R;
import com.zubiisoft.zubiissenger.database.Database;
import com.zubiisoft.zubiissenger.entity.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Register Activity for create new accounts.
 */
public class RegisterActivity extends AppCompatActivity {

    // Tag for log chat.
    private static final String TAG = "RegisterActivity";

    // Instance of Email and Password for create a new account.
    private EditText mEmail;
    private EditText mPassword;

    // Instance of FirebaseAuth.
    private FirebaseAuth mAuth;

    // Instance of Database.
    private Database mDatabase;

    // Instance of Context.
    private Context mContext;
    /**
     *  Create the content view, inflate the activity UI.
     * @param savedInstanceState Saved instance state bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Get an instance of Auth.
        mAuth = MyApplication.getAuth();

        // Get an instance of Database.
        mDatabase = MyApplication.getDatabase();

        // Get an instance of Context.
        mContext = getApplicationContext();

        // Get an instance of EditText.
        mEmail = findViewById(R.id.email_editText);
        mPassword = findViewById(R.id.password_editText);

    }

    /**
     * Get the strings from EditText and pass them to the createAccount.
     *
     * @param view The Button view.
     */
    public void registerButton(View view) {
        // Get the text from mEmail and mPassword.
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        // Create an account.
        createAccount(email, password);
    }

    /**
     * Create an account with specified email and password.
     * @param email
     * @param password
     */
    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(mContext, "Authentication successful.",
                                    Toast.LENGTH_LONG).show();

                            // Get the user I just created.
                            FirebaseUser user = mAuth.getCurrentUser();

                            // Get the current time.
                            Date currentTime = Calendar.getInstance().getTime();

                            // Write user in database.
                            mDatabase.writeUserInDatabase(new User(
                                    user.getUid(), // uid.
                                    user.getEmail().substring
                                            (0, user.getEmail().length() - 10), // firstName.
                                    "", // lastName.
                                    user.getEmail(), // email.
                                    "none", // avatar.
                                    "07etc", // phone
                                    currentTime.toString(), // current time
                                    null, // chats
                                    null) // friend list.
                            );

                            // Start the conversation activity.
                            startConversationActivity(user.getUid());

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(mContext, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void startConversationActivity(String uid) {
        Intent intent = new Intent(mContext, ConversationActivity.class);
        intent.putExtra("uid", uid);
        startActivity(intent);
    }
}