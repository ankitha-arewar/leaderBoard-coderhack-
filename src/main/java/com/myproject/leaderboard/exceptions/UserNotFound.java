package com.myproject.leaderboard.exceptions;

public class UserNotFound extends Exception{
    public UserNotFound(){
        super();
    }

    public UserNotFound(String msg){
        super(msg);
    }
}
