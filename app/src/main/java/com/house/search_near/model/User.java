package com.house.search_near.model;

public class User {
    private String name;
    private String email;
    private String pass;
    private String location;
    private String mobile;
    private String job;
    private String image;

    public User(String name, String email, String pass, String location, String mobile, String job, String image) {
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.location = location;
        this.mobile = mobile;
        this.job = job;
        this.image = image;
    }

    public User() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}
