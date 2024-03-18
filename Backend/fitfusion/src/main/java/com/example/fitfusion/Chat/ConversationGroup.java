package com.example.fitfusion.Chat;

import com.example.fitfusion.Messages.Message;
import com.example.fitfusion.Person.Person;
import jakarta.persistence.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ConversationGroup {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    //A groupchat should have only one group history
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "groupchat_id")
    private Groupchat groupchat;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<Message> messages;

    public ConversationGroup(Groupchat groupchat) {
        //this.groupchat = groupchat;
        messages = new ArrayList<Message>();
    }

    public ConversationGroup()
    {
        messages = new ArrayList<Message>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Groupchat getGroupchat() {
        return groupchat;
    }

    public void setGroupchat(Groupchat groupchat) {
        this.groupchat = groupchat;
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
