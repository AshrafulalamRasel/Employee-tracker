package com.chumbokit.doctor.project_final_years;

/**
 * Created by Rasel on 6/11/2019.
 */

public class User {
    String Displayname;


    String Email;
    long createdAt;

    public User (){};
    public User(String displayname,String email,long createdAt){
        this.Displayname=displayname;
        this.Email=email;
        this.createdAt=createdAt;
    }


    public String getDisplayname() {
        return Displayname;
    }

    public String getEmail() {
        return Email;
    }

    public long getCreatedAt() {
        return createdAt;
    }

}