package com.hoangnguyen.news.model;

public class Weather {
    private String day;
    private String status;
    private String image;
    private String avrTemp;

    public Weather(String day, String status, String image, String avrTemp) {
        this.day = day;
        this.status = status;
        this.image = image;
        this.avrTemp = avrTemp;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAvrTemp() {
        return avrTemp;
    }

    public void setAvrTemp(String avrTemp) {
        this.avrTemp = avrTemp;
    }
}
