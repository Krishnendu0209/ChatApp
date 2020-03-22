package com.example.chatap.Model;

public class User
{
    public String userPhoneNumber;
    public String userName;
    public String status;
    public String lastMessage;

    public User()
    {

    }
    public User(String userName, String userPhoneNumber,String status, String lastMessage)
    {
        this.userName = userName;
        this.userPhoneNumber = userPhoneNumber;
        this.status = status;
        this.lastMessage = lastMessage;
    }

    public String getUserPhoneNumber()
    {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber)
    {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getLastMessage()
    {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage)
    {
        this.lastMessage = lastMessage;
    }
}
