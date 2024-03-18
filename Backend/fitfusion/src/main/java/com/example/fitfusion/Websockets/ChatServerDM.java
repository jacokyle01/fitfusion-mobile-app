package com.example.fitfusion.Websockets;

import com.example.fitfusion.Chat.ConversationDM;
import com.example.fitfusion.Chat.ConversationDMRepository;
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
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

@Controller
@ServerEndpoint(value = "/chat/{username}/DM/{recipient}", configurator = CustomSpringConfigurator.class)
public class ChatServerDM {

    // This keeps track of the relationships for users. If two users are connected
    // to a chatroom there should
    // be two instances in the map where its user, recipient and recipient, user
    private static Map<String, String> userPairs = new Hashtable<>();

    // Store all socket session and their corresponding username
    // Two maps for the ease of retrieval by key
    private static Map<Session, String> sessionUsernameMap = new Hashtable<>();
    private static Map<String, Session> usernameSessionMap = new Hashtable<>();

    // This is message history
    // private UserDMHistory dmHistory;
    private ConversationDM dmConversation;
    @Autowired
    MessageRepository messageRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    ConversationDMRepository DMConvoRepo;

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username,
            @PathParam("recipient") String recipient)
            throws IOException {

        Person curPerson = personRepository.findByUsername(username);
        Person otherPerson = personRepository.findByUsername(recipient);

        // Handle the case of a duplicate username
        if (usernameSessionMap.containsKey(username)) {
            session.getBasicRemote().sendText("Username already exists");
            session.close();
        } else if (curPerson == null) {
            session.getBasicRemote().sendText("User not registered");
            session.close();
        } else if (otherPerson == null) {
            session.getBasicRemote().sendText("Recipient is not registered");
            session.close();
        } else {
            // map current session with username
            sessionUsernameMap.put(session, username);

            // map current username with session
            usernameSessionMap.put(username, session);

            // put who you want to chat with in the userPairs map
            userPairs.put(username, recipient);

            // send to the user joining in
            // sendMessageToParticularUser(username, "Welcome to the chat server, " +
            // username);

            Session otherfella = usernameSessionMap.get(recipient);
            dmConversation = DMConvoRepo.findConversationByUsernames(username, recipient);

            // Start of conversation
            if (dmConversation == null) {
                dmConversation = new ConversationDM(curPerson, otherPerson);
                //sendMessageToParticularUser(username, "start of new conversation");

                Message message = new Message();
                if(otherfella == null)
                {
                    message.setUserName("FitFusion");
                    message.setContent("Start of new conversation");
                    sendMessageToParticularUser(username, message.getContent());
                    //messageRepository.save(message);
                    dmConversation.addMessage(message);
                }

                DMConvoRepo.save(dmConversation);

            }
            // else conversation already started get messages
            else {

                // dmConversation = DMConvoRepo.findConversationByUsernames(username,
                // recipient);
                List<Message> dmMessages = dmConversation.getMessages();

                // Will send empty if dmMessages is null
                sendMessageToParticularUser(username, getMessageHistory(dmMessages));

            }

            // ---Checking state of chatroom---
            // case 1: the other guy isn't in the chatroom
            if (otherfella == null) {
                sendMessageToParticularUser(username, "Other user not online");
            }
            // case 2: the other guy is in the chatroom with you
            else if (userPairs.get(recipient).equals(username)) {
                sendMessageToParticularUser(username, recipient + " has been awaiting your arrival");
                sendMessageToParticularUser(recipient, username + " has Joined the Chat");
            }
            // case 3: the other guy is in another chatroom with someone else
            else {
                sendMessageToParticularUser(username, "Other user is talking to someone he likes more");
            }

        }
    }

    // @Transactional
    // private List<Message> getOldMessages(UserDMHistory dmHistory) {
    // try{
    // List<Message> messages = dmHistory.getMessages();
    // return messages;
    // } catch (LazyInitializationException all)
    // {
    // return null;
    // }
    //
    // }

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
    public void onMessage(Session session, @PathParam("recipient") String recipient, String message)
            throws IOException {

        // get the username by session
        String username = sessionUsernameMap.get(session);

        Session otherfella = usernameSessionMap.get(recipient);

        // case 1: the other guy isn't connected to the server
        if (otherfella == null) {
            sendMessageToParticularUser(username, "Other user not online");
        }
        // case 2: the other guy is in the chatroom with you
        else if (userPairs.get(recipient).equals(username)) {
            sendMessageToParticularUser(username, username + ": " + message);
            sendMessageToParticularUser(recipient, username + ": " + message);
        }
        // case 3: the other guy is in another chatroom with someone else
        else {
            sendMessageToParticularUser(username, "Other user is talking to someone he likes more ");
        }

        Message sentMessage = new Message(username, message);

        dmConversation.addMessage(sentMessage);
        messageRepository.save(sentMessage);
        DMConvoRepo.save(dmConversation);

    }

    /**
     * Handles the closure of a WebSocket connection.
     *
     * @param session The WebSocket session that is being closed.
     */
    @OnClose
    public void onClose(Session session, @PathParam("recipient") String recipient) throws IOException {

        // get the username from session-username mapping
        String username = sessionUsernameMap.get(session);

        // remove user from memory mappings
        sessionUsernameMap.remove(session);
        usernameSessionMap.remove(username);

        // send the message to chat, aka both users
        // sendMessageToParticularUser(username, username + " disconnected");

        if (usernameSessionMap.get(recipient) != null) {
            sendMessageToParticularUser(recipient, username + " disconnected");
        }

        // delete the relationship in the userPairs
        userPairs.remove(username);

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
     * Broadcasts a message to all users in all chats
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
