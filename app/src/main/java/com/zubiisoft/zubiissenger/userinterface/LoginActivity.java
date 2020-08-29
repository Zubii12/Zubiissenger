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
import com.zubiisoft.zubiissenger.MyApplication;
import com.zubiisoft.zubiissenger.R;

/**
 * Login Activity for login the users.
 */
public class LoginActivity extends AppCompatActivity {

    // Tag for log chat.
    private static final String TAG = "LoginActivity";

    // Instance of Email and Password for create a new account.
    private EditText mEmail;
    private EditText mPassword;

    // Instance of FirebaseAuth;
    private FirebaseAuth mAuth;

    private Context mContext;

    /**
     *  Create the content view, inflate the activity UI.
     * @param savedInstanceState Saved instance state bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = MyApplication.getAuth();

        mContext = getApplicationContext();

        // Get an instance of EditText.
        mEmail = findViewById(R.id.email_editText);
        mPassword = findViewById(R.id.password_editText);

        // For test.
        mEmail.setText("zubii0@yahoo.com");
        mPassword.setText("123456789");

    }

    /**
     * Get the string from EditText and pass them to the signIn.
     *
     * @param view The Button view.
     */
    public void loginButton(View view) {
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        signIn(email, password);
    }

    /**
     * Sign in user with specified email and password.
     * @param email
     * @param password
     */
    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(mContext, "Authentication successful.",
                                    Toast.LENGTH_LONG).show();

                            FirebaseUser user = mAuth.getCurrentUser();
                            startConversationActivity(user.getUid());

                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(mContext, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    public void startConversationActivity(String uid){
        Intent intent = new Intent(mContext, ConversationActivity.class);
        intent.putExtra("uid", uid);
        startActivity(intent);
    }
}