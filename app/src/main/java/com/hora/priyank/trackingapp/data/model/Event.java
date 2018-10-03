package com.hora.priyank.trackingapp.data.model;

import com.hora.priyank.trackingapp.data.dao.Message;

/**
 * Created by Priyank Jain on 27-09-2018.
 */
public class Event implements Message {
    private String userName;
    private String title;
    private String type;
    private Double lat;
    private Double lon;
    private String createDate;

    public Event() {
    }

    public Event(String username, String title, String type, Double lat, Double lon, String createDate) {
        this.userName = username;
        this.title = title;
        this.type = type;
        this.lat = lat;
        this.lon = lon;
        this.createDate = createDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "Event{" +
                "username='" + userName + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", createDate='" + createDate + '\'' +
                '}';
    }
}
