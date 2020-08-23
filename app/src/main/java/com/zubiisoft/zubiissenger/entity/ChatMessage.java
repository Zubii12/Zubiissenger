package com.zubiisoft.zubiissenger.entity;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class ChatMessage {
    private String idChat;
    private String sender;
    private String receiver;
    private ArrayList<ArrayList<String>> messages;

    public ChatMessage(){ };

    public ChatMessage(String idChat, String sender, String receiver,
                       ArrayList<ArrayList<String>> messages) {
        this.idChat = idChat;
        this.sender = sender;
        this.receiver = receiver;
        this.messages = messages;
    }

    @NonNull
    @Override
    public String toString() {
        return "Chat{" +
                "idChat='" + idChat + '\'' +
                ", with='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", messages=" + messages +
                '}';
    }

    public String getIdChat() {
        return idChat;
    }

    public void setIdChat(String idChat) {
        this.idChat = idChat;
    }

    public String getSender() {
        return this.sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return this.receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public ArrayList<ArrayList<String>> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<ArrayList<String>> messages) {
        this.messages = messages;
    }
}