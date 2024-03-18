package com.example.fitfusion.Post;

import com.example.fitfusion.Business.Business;
import com.example.fitfusion.Image.Image;
import com.example.fitfusion.Person.Person;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class PersonPost extends Post{

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    @ManyToOne
    @JsonIgnore
    private Person person;

    @Column
    private String title;
    @Column
    private String message;
    @Column //(nullable = true)
    private int likes;

    private Set<String> likedBy = new HashSet<>();;

    @OneToOne
    @JsonIgnore
    private Image personImage;

    @Column
    private Long personImageID;

    public PersonPost(String title, String message, int likes) {
        super(title, message, likes);
    }

    public PersonPost() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Image getPersonImage() {
        return personImage;
    }

    public void setPersonImage(Image image) {
        this.personImage = image;
    }

    public Long getPersonImageID() {
        return personImageID;
    }

    public void setPersonImageID(Long imageID) {
        this.personImageID = imageID;
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

    public Set<String> getLikedBy() {
        return likedBy;
    }

    public void setLikedBy(Set<String> likedBy) {
        this.likedBy = likedBy;
    }

    public void addLiker(String name)
    {
        likedBy.add(name);
    }

    public void removeLiker(String name)
    {
        likedBy.remove(name);
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Person getPerson() {
        return this.person;
    }

}
