package com.house.search_near.model;

public class SettingModel {
    String title;
    String images;
    String rent_type;
    String bedroom;
    String bathroom;
    String rent_price;
    String post_id;

    public SettingModel() {
    }

    public SettingModel(String title, String images, String rent_type, String bedroom, String bathroom, String rent_price, String post_id) {
        this.title = title;
        this.images = images;
        this.rent_type = rent_type;
        this.bedroom = bedroom;
        this.bathroom = bathroom;
        this.rent_price = rent_price;
        this.post_id = post_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getRent_type() {
        return rent_type;
    }

    public void setRent_type(String rent_type) {
        this.rent_type = rent_type;
    }

    public String getBedroom() {
        return bedroom;
    }

    public void setBedroom(String bedroom) {
        this.bedroom = bedroom;
    }

    public String getBathroom() {
        return bathroom;
    }

    public void setBathroom(String bathroom) {
        this.bathroom = bathroom;
    }

    public String getRent_price() {
        return rent_price;
    }

    public void setRent_price(String rent_price) {
        this.rent_price = rent_price;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }
}
