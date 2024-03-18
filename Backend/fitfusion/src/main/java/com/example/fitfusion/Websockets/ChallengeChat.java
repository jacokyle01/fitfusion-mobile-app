package com.example.fitfusion.Websockets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.server.standard.SpringConfigurator;

import com.example.fitfusion.Challenge.Challenge;
import com.example.fitfusion.Challenge.ChallengeRepository;
import com.example.fitfusion.Challenge.Status;
import com.example.fitfusion.Person.Person;
import com.example.fitfusion.Person.PersonRepository;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

@Controller
@ServerEndpoint(value = "/challengeChat/{username}", configurator = CustomSpringConfigurator.class)
public class ChallengeChat {

    // Store all socket session and their corresponding username.
    private static Map<Session, String> sessionUsernameMap = new Hashtable<>();
    private static Map<String, Session> usernameSessionMap = new Hashtable<>();

    String[] encouragements = {
            "You're doing great! Keep it up!",
            "You're almost there! Keep your head up!",
            "Stay positive! You're almost finished with this challenge.",
            "Hard work is only going to make you stronger!",
            "I'm so proud of you! Keep working hard!",
            "You got this! Don't give up now!"
    };

    String[] trashtalkings = {
            "I knew I should have challenged someone stronger.",
            "Time's ticking.. What's taking so long?",
            "Hurry up and complete this challenge before I fall asleep.",
            "You need to step up your game buddy!",
            "You're lifting what, marshmellows?"
    };

    @Autowired
    ChallengeRepository challengeRepository;

    @Autowired
    PersonRepository personRepository;

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException {
        // Handle the case of a duplicate username
        if (usernameSessionMap.containsKey(username)) {
            session.getBasicRemote().sendText("Username already exists");
            session.close();
        }

        else {
            // map current session with username
            sessionUsernameMap.put(session, username);

            // map current username with session
            usernameSessionMap.put(username, session);
        }
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        String username = sessionUsernameMap.get(session);
        // get all challengees for this user and send them a message
        List<String> challengeeNames = new ArrayList<String>();
        List<Challenge> sentChallenges = challengeRepository.getChallengesByChallenger(username);
        for (Challenge challenge : sentChallenges) {
            if (challenge.getStatus() == Status.IN_PROGRESS) {
                String challengeeName = challenge.getChallengee().getUsername();
                challengeeNames.add(challengeeName);
            }
        }
        // send them a message!

        String messageToSend;
        if (message.equals("!trashtalk")) {
            int numPhrases = trashtalkings.length;
            Random rand = new Random();
            int index = rand.nextInt(numPhrases);
            messageToSend = trashtalkings[index];
        } else if (message.equals("!encourage")) {
            int numPhrases = encouragements.length;
            Random rand = new Random();
            int index = rand.nextInt(numPhrases);
            messageToSend = encouragements[index];
        } else {
            messageToSend = message;
        }

        challengeeNames.forEach((challengee) -> {
            sendMessageToParticularUser(challengee, "<Challenger> " + username + ": " + messageToSend);
        });
    }

    @OnClose
    public void onClose(Session session) throws IOException {

        // get the username from session-username mapping
        String username = sessionUsernameMap.get(session);

        // remove user from memory mappings
        sessionUsernameMap.remove(session);
        usernameSessionMap.remove(username);
    }

    private void sendMessageToParticularUser(String username, String message) {
        // don't try sending a message to a user who isn't connected
        if (!usernameSessionMap.containsKey(username)) {
            return;
        }
        try {
            usernameSessionMap.get(username).getBasicRemote().sendText(message);
        } catch (IOException e) {
            // logger.info("[DM Exception] " + e.getMessage());
        }
    }
}
