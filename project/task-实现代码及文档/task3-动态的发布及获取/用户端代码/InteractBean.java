package com.example.ll.project_main.bean;

public class InteractBean {
    private int interactId;
    private String userName;
    private String userTouxiang;
    private String interactTime;
    private String interactContent;
    private String interactPhoto;
    private String interactPraise;

    public int getInteractId() {
        return interactId;
    }

    public void setInteractId(int interactId) {
        this.interactId = interactId;
    }

    public void setInteractContent(String interactContent) {
        this.interactContent = interactContent;
    }

    public void setInteractPraise(String interactPraise) {
        this.interactPraise = interactPraise;
    }

    public void setInteractTime(String interactTime) {
        this.interactTime = interactTime;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserTouxiang(String userTouxiang) {
        this.userTouxiang = userTouxiang;
    }

    public void setInteractPhoto(String interactPhoto) {
        this.interactPhoto = interactPhoto;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserTouxiang() {
        return userTouxiang;
    }

    public String getInteractContent() {
        return interactContent;
    }

    public String getInteractPraise() {
        return interactPraise;
    }

    public String getInteractTime() {
        return interactTime;
    }

    public String getInteractPhoto() {
        return interactPhoto;
    }

    @Override
    public String toString() {
        return "InteractBean{" +
                "interactId=" + interactId +
                ", userName='" + userName + '\'' +
                ", userTouxiang='" + userTouxiang + '\'' +
                ", interactTime='" + interactTime + '\'' +
                ", interactContent='" + interactContent + '\'' +
                ", interactPhoto='" + interactPhoto + '\'' +
                ", interactPraise='" + interactPraise + '\'' +
                '}';
    }

    public InteractBean(String userName,
                             String userTouxiang,
                             String interactTime,
                             String interactContent,
                             String interactPhoto,
                             String interactPraise) {
        this.userName = userName;
        this.userTouxiang = userTouxiang;
        this.interactTime = interactTime;
        this.interactContent = interactContent;
        this.interactPhoto = interactPhoto;
        this.interactPraise = interactPraise;
    }
    public InteractBean() {
        this.userName = userName;
        this.userTouxiang = userTouxiang;
        this.interactTime = interactTime;
        this.interactContent = interactContent;
        this.interactPhoto = interactPhoto;
        this.interactPraise = interactPraise;
    }
}

