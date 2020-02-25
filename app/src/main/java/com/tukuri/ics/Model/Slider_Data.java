package com.tukuri.ics.Model;

public class Slider_Data {

    private String id;

    private String image;

    private String status;

    public Slider_Data(String id, String image, String status) {
        this.id = id;
        this.image = image;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
