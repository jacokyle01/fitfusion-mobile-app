package com.example.fitfusion.Chat;

import com.example.fitfusion.Messages.Message;
import com.example.fitfusion.Person.Person;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class ConversationDM {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    @ManyToOne
    private Person person1;

    @ManyToOne
    private Person person2;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<Message> messages;

    public ConversationDM()
    {
        messages = new ArrayList<>();
    }

    public ConversationDM(Person person1, Person person2) {
        this.person1 = person1;
        this.person2 = person2;
        messages = new ArrayList<Message>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Person getPerson1() {
        return person1;
    }

    public void setPerson1(Person person1) {
        this.person1 = person1;
    }

    public Person getPerson2() {
        return person2;
    }

    public void setPerson2(Person person2) {
        this.person2 = person2;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void addMessage(Message message)
    {
        messages.add(message);
    }

}
