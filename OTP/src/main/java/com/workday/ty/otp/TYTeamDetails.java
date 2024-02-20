package com.workday.ty.otp;

import org.springframework.stereotype.Service;

@Service
public class TYTeamDetails {
    private String username;
    private String password;
    private String email;
    private String code;

    public TYTeamDetails() {
        new TYProjectFileUtils();
    }
    public TYTeamDetails(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
    public TYTeamDetails(String username, String password, String email, String code) {
        this.code = code;
        this.username = username;
        this.password = password;
        this.email = email;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public String toString() {
        return getUsername() + ", " + getPassword() + ", " + getEmail()  + ", " + getCode();
    }
}
