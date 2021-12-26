package com.example.ambulanceemergencyresponsesystem.entities;

public class User {

    public String email;
    public String username;
    public String phoneNumber;
    public String address;

    public User() {
    }

    public User(String username, String email, String phoneNumber, String address) {
        this.email = email;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }
}
