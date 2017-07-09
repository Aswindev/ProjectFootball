package com.projectfootball.projectfootball.database.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by aswin on 09/07/2017.
 */

@IgnoreExtraProperties
public class User {

    public String name;
    public String email;
//    public String mobno;
    public String useruid;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public User() {
    }

    public User(String name, String email, String useruid) {
        this.name = name;
        this.email = email;
        this.useruid=useruid;
    }
}