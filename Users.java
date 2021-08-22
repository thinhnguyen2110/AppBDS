package com.example.DoFireBase02.models;

public class Users {
    String phone;
    String name;
    String pass;

    public Users() {
    }

    public Users(String phone, String name, String password) {
        this.phone = phone;
        this.name = name;
        this.pass = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String password) {
        this.pass = password;
    }
}
