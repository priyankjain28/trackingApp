package com.hora.priyank.trackingapp.data.dao;


public interface Message {

    public String getTitle();

    public void setTitle(String title);

    public String getUserName();

    public void setUserName(String name);

    public String getType();

    public void setType(String type);

    public Double getLat();

    public void setLat(Double lat);

    public Double getLon();

    public void setLon(Double lon);

    public String getCreateDate();

    public void setCreateDate(String createDate);

}
