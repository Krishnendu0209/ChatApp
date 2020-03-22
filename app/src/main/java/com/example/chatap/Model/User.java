package com.example.chatap.Model;

public class User
{
    public String userPhoneNumber;
    public String userName;
    public User(String userName, String userPhoneNumber)
    {
        this.userName = userName;
        this.userPhoneNumber = userPhoneNumber;
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
}
