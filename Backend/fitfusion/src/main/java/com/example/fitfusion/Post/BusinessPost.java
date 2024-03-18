package com.example.fitfusion.Post;

import com.example.fitfusion.Business.Business;
import com.example.fitfusion.Image.Image;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
public class BusinessPost extends Post{

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    @ManyToOne
    @JsonIgnore
    private Business business;

    @Column
    private String title;
    @Column
    private String message;
    @Column //(nullable = true)
    private int likes;

    @JsonIgnore
    @OneToOne
    private Image image;

    @Column
    private Long imageID;

    public BusinessPost(String title, String message, int likes) {
        //super(title, message, likes);
        this.title = title;
        this.message = message;
        this.likes = likes;
    }

    public BusinessPost() {
        //super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getImageID() {
        return imageID;
    }

    public void setImageID(Long imageID) {
        this.imageID = imageID;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    //@Override
    public String getTitle() {
        //return super.getTitle();
        return title;
    }

    //@Override
    public void setTitle(String title) {
        //super.setTitle(title);
        this.title = title;
    }

    //@Override
    public String getMessage() {
        //return super.getMessage();
        return message;
    }

    //@Override
    public void setMessage(String message) {
        //super.setMessage(message);
        this.message = message;
    }

    //@Override
    public int getLikes() {
        //return super.getLikes();
        return likes;
    }

    //@Override
    public void setLikes(int likes) {
        //super.setLikes(likes);
        this.likes = likes;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public Business getBusiness() {
        return this.business;
    }

}
