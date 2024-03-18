package com.example.fitfusion.Chat;

import com.example.fitfusion.Messages.Message;
import com.example.fitfusion.Person.Person;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Groupchat {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    @OneToOne
    @JsonIgnore
    private ConversationGroup conversationGroup;

    @Column(unique = true)
    private String groupname;

    private Set<String> blacklist = new HashSet<>();

    private Set<String> groupMembers = new HashSet<>();

    public Groupchat(String groupname) {
        this.groupname = groupname;
    }

    public Groupchat() {
    }

    public ConversationGroup getConversationGroup() {
        return conversationGroup;
    }

    public void setConversationGroup(ConversationGroup conversationGroup) {
        this.conversationGroup = conversationGroup;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

     public Set<String> getGroupMembers() {
        return groupMembers;
     }

     public void setGroupMembers(Set<String> groupMembers) {
        this.groupMembers = groupMembers;
     }

     public void addGroupMember(String groupMember)
     {
     groupMembers.add(groupMember);
     }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public Set<String> getBlacklist() {
        return blacklist;
    }

    public void addToBlacklist(String name) {
        blacklist.add(name);
    }
}
