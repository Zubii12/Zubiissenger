package com.zubiisoft.zubiissenger;

import android.app.Application;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zubiisoft.zubiissenger.database.Database;

/**
 * Android Application class. Used for accessing singletons.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @NonNull
    public static FirebaseAuth getAuth() {
        return FirebaseAuth.getInstance();
    }

    @NonNull
    public static DatabaseReference getDatabaseReference() {
        return FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    public static Database getDatabase() {
        return Database.getInstance();
    }
}
