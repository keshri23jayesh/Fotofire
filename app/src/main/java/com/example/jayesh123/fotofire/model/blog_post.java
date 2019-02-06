package com.example.jayesh123.fotofire.model;

public class blog_post {
    public String title, detail, image;

    public blog_post(String title, String detail, String image) {
        this.title = title;
        this.detail = detail;
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    public String getImage() {
        return image;
    }
}
