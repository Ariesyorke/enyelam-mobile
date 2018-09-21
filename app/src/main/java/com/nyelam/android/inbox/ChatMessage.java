package com.nyelam.android.inbox;

import java.io.File;
import java.util.Date;

/**
 * Created by himanshusoni on 06/09/15.
 */
public class ChatMessage {

    private boolean isImage, isMine;
    private String userName;
    private String content;
    private String imageFile;
    private Date dateTime;

    public ChatMessage(String userName, String message, boolean mine, boolean image, String imageFile, Date dateTime) {
        this.userName = userName;
        this.content = message;
        this.isMine = mine;
        this.isImage = image;
        this.imageFile = imageFile;
        this.dateTime = dateTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setIsMine(boolean isMine) {
        this.isMine = isMine;
    }

    public boolean isImage() {
        return isImage;
    }

    public void setIsImage(boolean isImage) {
        this.isImage = isImage;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public Date getDate() {
        return dateTime;
    }

    public void setDate(Date dateTime) {
        this.dateTime = dateTime;
    }
}
