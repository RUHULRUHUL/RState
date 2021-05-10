package com.house.search_near.model;

import com.google.firebase.firestore.GeoPoint;

public class PostModel {
    ///for post
    private GeoPoint geoPoint;
    public int id;
    public String title;
    public String img_one;
    public String img_two;
    public String img_three;
    public String renter_type;
    public String description;
    public String rent_per_month;
    public String drawing;
    public String floor_no;
    public String bedroom;
    public String bathroom;
    public String generator;
    public String cctv;
    public String gas_connection;
    public String floor_type;
    public String lift;
    public String geo_point;
    public String key;
    public String email;
    public String image;
    public String location;
    public String mobile;
    public String name;
    public String car_parking;
    public String belconi;
    public boolean favourite;


    public PostModel(GeoPoint geoPoint, String title , String img_one, String img_two, String img_three,
                     String renter_type, String description, String rent_per_month, String drawing,
                     String floor_no, String bedroom, String bathroom, String generator, String cctv,
                     String gas_connection, String floor_type, String lift, String geo_point, String key ,
                     String email , String image , String location , String mobile , String name, String car_parking , String belconi ,
                     boolean favourite) {
        this.geoPoint = geoPoint;
        this.title = title;
        this.img_one = img_one;
        this.img_two = img_two;
        this.img_three = img_three;
        this.renter_type = renter_type;
        this.description = description;
        this.rent_per_month = rent_per_month;
        this.drawing = drawing;
        this.floor_no = floor_no;
        this.bedroom = bedroom;
        this.bathroom = bathroom;
        this.car_parking = car_parking;
        this.belconi = belconi;
        this.generator = generator;
        this.cctv = cctv;
        this.gas_connection = gas_connection;
        this.floor_type = floor_type;
        this.lift = lift;
        this.geo_point = geo_point;
        this.key = key;
        this.email = email;
        this.image = image;
        this.location = location;
        this.mobile = mobile;
        this.name = name;
        this.favourite = favourite;
    }

    public PostModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCar_parking() {
        return car_parking;
    }

    public void setCar_parking(String car_parking) {
        this.car_parking = car_parking;
    }

    public String getBelconi() {
        return belconi;
    }

    public void setBelconi(String belconi) {
        this.belconi = belconi;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }

    public String getImg_one() {
        return img_one;
    }

    public void setImg_one(String img_one) {
        this.img_one = img_one;
    }

    public String getImg_two() {
        return img_two;
    }

    public void setImg_two(String img_two) {
        this.img_two = img_two;
    }

    public String getImg_three() {
        return img_three;
    }

    public void setImg_three(String img_three) {
        this.img_three = img_three;
    }

    public String getRenter_type() {
        return renter_type;
    }

    public void setRenter_type(String renter_type) {
        this.renter_type = renter_type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRent_per_month() {
        return rent_per_month;
    }

    public void setRent_per_month(String rent_per_month) {
        this.rent_per_month = rent_per_month;
    }

    public String getDrawing() {
        return drawing;
    }

    public void setDrawing(String drawing) {
        this.drawing = drawing;
    }

    public String getFloor_no() {
        return floor_no;
    }

    public void setFloor_no(String floor_no) {
        this.floor_no = floor_no;
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

    public String getGenerator() {
        return generator;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }

    public String getCctv() {
        return cctv;
    }

    public void setCctv(String cctv) {
        this.cctv = cctv;
    }

    public String getGas_connection() {
        return gas_connection;
    }

    public void setGas_connection(String gas_connection) {
        this.gas_connection = gas_connection;
    }

    public String getFloor_type() {
        return floor_type;
    }

    public void setFloor_type(String floor_type) {
        this.floor_type = floor_type;
    }

    public String getLift() {
        return lift;
    }

    public void setLift(String lift) {
        this.lift = lift;
    }

    public String getGeo_point() {
        return geo_point;
    }

    public void setGeo_point(String geo_point) {
        this.geo_point = geo_point;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "PostModel{" +
                "geoPoint=" + geoPoint +
                ", img_one='" + img_one + '\'' +
                ", img_two='" + img_two + '\'' +
                ", img_three='" + img_three + '\'' +
                ", renter_type='" + renter_type + '\'' +
                ", description='" + description + '\'' +
                ", rent_per_month='" + rent_per_month + '\'' +
                ", drawing='" + drawing + '\'' +
                ", floor_no='" + floor_no + '\'' +
                ", bedroom='" + bedroom + '\'' +
                ", bathroom='" + bathroom + '\'' +
                ", generator='" + generator + '\'' +
                ", cctv='" + cctv + '\'' +
                ", gas_connection='" + gas_connection + '\'' +
                ", floor_type='" + floor_type + '\'' +
                ", lift='" + lift + '\'' +
                ", geo_point='" + geo_point + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
