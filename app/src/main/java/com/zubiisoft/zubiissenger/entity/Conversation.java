package com.zubiisoft.zubiissenger.entity;

public class Conversation {
    private String avatarImage;
    private String name;
    private String lastMessage;
    private String with;

    public Conversation() {

    }
    public Conversation(String avatarImage, String name, String lastMessage, String with) {
        this.avatarImage = avatarImage;
        this.name = name;
        this.lastMessage = lastMessage;
        this.with = with;
    }

    public void setAvatarImage(String avatarImage) {
        this.avatarImage = avatarImage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getName() {
        return name;
    }

    public String getAvatarImage() {
        return avatarImage;
    }

    public String getWith() {
        return with;
    }

    public void setWith(String with) {
        this.with = with;
    }

    @Override
    public String toString() {
        return "Conversation{" +
                "avatarImage='" + avatarImage + '\'' +
                ", name='" + name + '\'' +
                ", lastMessage='" + lastMessage + '\'' +
                ", with='" + with + '\''+
                '}';
    }

}
