package com.example.fitfusion.Post;

import jakarta.persistence.Column;

public abstract class Post {

    @Column
    private String title;
    @Column
    private String message;
    @Column//(nullable = true)
    private int likes;

    public Post(String title, String message, int likes) {
        this.title = title;
        this.message = message;
        this.likes = likes;
    }

    public Post()
    {
        //likes = 0;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }


}
