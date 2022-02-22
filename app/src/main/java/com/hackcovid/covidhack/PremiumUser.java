package com.hackcovid.covidhack;

public class PremiumUser {

    private String name,email;

    public PremiumUser() {

    }

    public PremiumUser(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
