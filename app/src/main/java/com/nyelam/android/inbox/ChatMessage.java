package com.nyelam.android.inbox;

import android.support.annotation.NonNull;

import java.io.File;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by himanshusoni on 06/09/15.
 */
public class ChatMessage{

    private boolean isImage, isMine;
    private int ids;
    private String userName;
    private String content;
    private String imageFile;
    private Date dateTime;

    public ChatMessage(int ids, String userName, String message, boolean mine, boolean image, String imageFile, Date dateTime) {
        this.ids = ids;
        this.userName = userName;
        this.content = message;
        this.isMine = mine;
        this.isImage = image;
        this.imageFile = imageFile;
        this.dateTime = dateTime;
    }

    public int getId() {
        return ids;
    }

    public void setId(int ids) {
        this.ids = ids;
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

    /*Comparator for sorting the list by roll no*/
    public static Comparator<ChatMessage> StuRollno = new Comparator<ChatMessage>() {

        public int compare(ChatMessage s1, ChatMessage s2) {

            int rollno1 = s1.getId();
            int rollno2 = s2.getId();

            /*For ascending order*/
            return rollno1 - rollno2;

            /*For descending order*/
            //rollno2-rollno1;
        }
    };

}
