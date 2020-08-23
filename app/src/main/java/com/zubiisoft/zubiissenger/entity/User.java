package com.zubiisoft.zubiissenger.entity;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Objects;

public class User {

    private String uid,
            firstName,
            lastName,
            email,
            avatar,
            phone,
            createdAt;

    private ArrayList<String> friendList, chats;

    public User() { }

    public User(String uid, String firstName, String lastName, String email,
                String avatar, String phone, String createdAt, ArrayList<String> chats,
                ArrayList<String> friendList) {
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.avatar = avatar;
        this.phone = phone;
        this.createdAt = createdAt;
        this.chats = chats;
        this.friendList = friendList;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", avatar='" + avatar + '\'' +
                ", phone='" + phone + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", chats='" + chats + '\'' +
                ", friendList=" + friendList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return uid.equals(user.uid) &&
                firstName.equals(user.firstName) &&
                lastName.equals(user.lastName) &&
                email.equals(user.email) &&
                avatar.equals(user.avatar) &&
                phone.equals(user.phone) &&
                createdAt.equals(user.createdAt) &&
                friendList.equals(user.friendList) &&
                chats.equals(user.chats);
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setChats(ArrayList<String> chats) {
        this.chats = chats;
    }

    public ArrayList<String> getChats() {
        return  this.chats;
    }

    public ArrayList<String> getFriendList() {
        return friendList;
    }

    public void setFriendList(ArrayList<String> friendList) {
        this.friendList = friendList;
    }

}
