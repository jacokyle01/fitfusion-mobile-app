package com.example.fitfusion.Websockets;

import com.example.fitfusion.Chat.ConversationGroup;
import com.example.fitfusion.Chat.ConversationGroupRepository;
import com.example.fitfusion.Chat.Groupchat;
import com.example.fitfusion.Chat.GroupchatRepository;
import com.example.fitfusion.Messages.Message;
import com.example.fitfusion.Messages.MessageRepository;
import com.example.fitfusion.Person.Person;
import com.example.fitfusion.Person.PersonRepository;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@ServerEndpoint(value = "/chat/{username}/groupchat/{groupname}", configurator = CustomSpringConfigurator.class)
public class ChatServerGroup {

    // Store all socket session and their corresponding username.
    private static Map<Session, String> sessionUsernameMap = new Hashtable<>();
    private static Map<String, Session> usernameSessionMap = new Hashtable<>();

    // This is where the different groupchats are stored. Given a group name it will
    // give you a list of all the usernames(keys)
    // of users
    private static Map<String, List<String>> onlineGroupchats = new Hashtable<>();

    @Autowired
    MessageRepository messageRepository;
    @Autowired
    PersonRepository personRepository;
    @Autowired
    ConversationGroupRepository conversationGroupRepo;
    @Autowired
    GroupchatRepository groupchatRepository;

    private ConversationGroup conversationGroup;

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username,
            @PathParam("groupname") String groupname)
            throws IOException {

        // Handle the case of a duplicate username
        if (usernameSessionMap.containsKey(username)) {
            session.getBasicRemote().sendText("Username already exists");
            session.close();
        } else if (personRepository.findByUsername(username) == null) {
            session.getBasicRemote().sendText("User not registered");
            session.close();
        } else {

            // check if group exists already
            if (groupchatRepository.findByGroupname(groupname) != null) {

                Groupchat groupchat = groupchatRepository.findByGroupname(groupname);
                Set<String> blacklist = groupchat.getBlacklist();

                // ...if yes, check if user is blacklisted
                if (blacklist.contains(username)) {
                    session.getBasicRemote().sendText("This user is blacklisted");
                    session.close();
                    return;
                }
            }

            // map current session with username
            sessionUsernameMap.put(session, username);
            // map current username with session
            usernameSessionMap.put(username, session);

            List<String> onlineGroupmembers = onlineGroupchats.get(groupname);

            // checking to see if list of group needs to be initalized
            if (onlineGroupmembers == null) {
                onlineGroupchats.put(groupname, new ArrayList<String>());
                onlineGroupmembers = onlineGroupchats.get(groupname);
            }

            // Put user in respective group
            onlineGroupmembers.add(username);

            // ----Below is for getting message history----
            conversationGroup = conversationGroupRepo.findConversationByGroupname(groupname);
            Person user = personRepository.findByUsername(username);

            // If there is not a conversation already create one
            if (conversationGroup == null) {

                Groupchat tempGroupchat = new Groupchat(groupname);
                tempGroupchat.addGroupMember(username);
                conversationGroup = new ConversationGroup();
                tempGroupchat.setConversationGroup(conversationGroup);
                conversationGroup.setGroupchat(tempGroupchat);

                Message message = new Message("FitFusion", "Start of new conversation created by, " + username);
                sendMessageToGroup(groupname, message.getContent());
                //messageRepository.save(message);
                conversationGroup.addMessage(message);

                conversationGroupRepo.save(conversationGroup);
                // groupchatRepository.save(tempGroupchat);

            }
            // else chat already started
            else {
                List<Message> groupMessages = conversationGroup.getMessages();
                sendMessageToParticularUser(username, getMessageHistory(groupMessages));

                Groupchat currentGroupchat = conversationGroup.getGroupchat();
                Set<String> groupMembers = currentGroupchat.getGroupMembers();

                 if(!groupMembers.contains(username))
                 {
                    Message message = new Message("FitFusion", username + " has joined the group, say hi!");
                    currentGroupchat.addGroupMember(username);
                    sendMessageToGroup(groupname, message.getContent());
                    messageRepository.save(message);
                    conversationGroup.addMessage(message);
                    conversationGroupRepo.save(conversationGroup);
                 }

            }

            // send to the user joining in
            //sendMessageToParticularUser(username, "Welcome to the chat server, " + username);

//            Message message = new Message("FitFusion", "User: " + username + " has Joined the Chat");
//            // send to everyone in the chat
//            sendMessageToGroup(groupname, message.getContent());
//            messageRepository.save(message);
//            conversationGroup.addMessage(message);
//            conversationGroupRepo.save(conversationGroup);

        }
    }

    private String getMessageHistory(List<Message> dmMessages) {
        StringBuilder sb = new StringBuilder();
        if (dmMessages.size() == 0) {
            sb.append("Start of new conversation");
        } else {
            for (Message message : dmMessages) {
                sb.append(message.getUserName() + ": " + message.getContent() + "\n");
            }
        }
        return sb.toString();
    }

    @OnMessage
    public void onMessage(Session session, @PathParam("groupname") String groupname, String message)
            throws IOException {

        // get the username by session
        String username = sessionUsernameMap.get(session);

        // handle sending commands
        Groupchat groupchat = groupchatRepository.findByGroupname(groupname);
        Set<String> blacklist = groupchat.getBlacklist(); 

        if (message.equals("!blacklist")) {
            String newMessage = "The following people are blacklisted- \n";
            for (String name : blacklist) {
                newMessage += name + "\n";
            }
            message = newMessage;

        }

        sendMessageToGroup(groupname, username + ": " + message);

        Message sentMessage = new Message(username, message);

        conversationGroup.addMessage(sentMessage);
        messageRepository.save(sentMessage);
        conversationGroupRepo.save(conversationGroup);

    }

    /**
     * Handles the closure of a WebSocket connection.
     *
     * @param session The WebSocket session that is being closed.
     */
    @OnClose
    public void onClose(Session session, @PathParam("groupname") String groupname) throws IOException {

        // get the username from session-username mapping
        String username = sessionUsernameMap.get(session);

        // remove user from memory mappings
        sessionUsernameMap.remove(session);
        usernameSessionMap.remove(username);

        List<String> group = onlineGroupchats.get(groupname);

        if (group == null) {
            // name wrong foo
        }

        group.remove(username);

        // send the message to chat
        sendMessageToGroup(groupname, username + " disconnected");

    }

    /**
     * Handles WebSocket errors that occur during the connection.
     *
     * @param session   The WebSocket session where the error occurred.
     * @param throwable The Throwable representing the error condition.
     */
    @OnError
    public void onError(Session session, Throwable throwable) {

        // get the username from session-username mapping
        String username = sessionUsernameMap.get(session);

        // do error handling here
        // logger.info("[onError]" + username + ": " + throwable.getMessage());
    }

    private void sendMessageToGroup(String groupname, String message) {
        List<String> group = onlineGroupchats.get(groupname);

        if (group == null)
            return;

        // SendMessageTOParticularUser should hopefully handle if someone isnt online
        group.forEach((username) -> {
            sendMessageToParticularUser(username, message);
        });

    }

    /**
     * Sends a message to a specific user in the chat (DM).
     *
     * @param username The username of the recipient.
     * @param message  The message to be sent.
     */
    private void sendMessageToParticularUser(String username, String message) {
        try {
            usernameSessionMap.get(username).getBasicRemote().sendText(message);
        } catch (IOException e) {
            // logger.info("[DM Exception] " + e.getMessage());
        }
    }

    /**
     * Broadcasts a message to all users in the chat.
     *
     * @param message The message to be broadcasted to all users.
     */
    private void broadcast(String message) {
        sessionUsernameMap.forEach((session, username) -> {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                // logger.info("[Broadcast Exception] " + e.getMessage());
            }
        });
    }

}
