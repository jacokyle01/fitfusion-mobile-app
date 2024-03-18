package com.example.gymapp23;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gymapp23.databinding.ActivityMyGymChatBinding;
import com.example.gymapp23.databinding.ActivityProfilePageBinding;

import org.java_websocket.handshake.ServerHandshake;

public class MyGymChat extends AppCompatActivity implements  WebSocketListener{
    private Button sendBtn, homeBtn;
    private EditText msgEtx;
    private TextView msgTv;
    LinearLayout layout;

    ///chat/{username}/groupchat/{groupname}
    private String BASE_URL = "ws://coms-309-067.class.las.iastate.edu:8080/chat/";

    private ActivityMyGymChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyGymChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();

        //connect to the other user
        //String serverUrl = BASE_URL + usernameEtx.getText().toString();
        String serverUrl = BASE_URL + intent.getStringExtra("username") + "/groupchat/" + intent.getStringExtra("myGym");

        // Establish WebSocket connection and set listener
        WebSocketManager.getInstance().connectWebSocket(serverUrl);
        WebSocketManager.getInstance().setWebSocketListener(MyGymChat.this);

//        Button sendDMBtn = findViewById(R.id.sendDMBtn);
//        EditText userMsgETxt = findViewById(R.id.userMsgEtx);
//        TextView messageBoard = findViewById(R.id.messagesTxt_DM);
        TextView chattingWithTxt = findViewById(R.id.chattingWithGroupTxt);

        chattingWithTxt.setText("chatting with: " + intent.getStringExtra("myGym").toString());

        /* initialize UI elements */
        sendBtn = (Button) findViewById(R.id.sendGCBtn);
        //usernameEtx = (EditText) findViewById(R.id.messagesTxt_DM);
        msgEtx = (EditText) findViewById(R.id.userMsgEtx_GC);
        msgTv = (TextView) findViewById(R.id.messagesTxt_GC);
        homeBtn = findViewById(R.id.gcToHomeBtn);

        msgTv.setText(serverUrl);

        /* send button listener */
        sendBtn.setOnClickListener(v -> {
            try {
                // send message
                WebSocketManager.getInstance().sendMessage(msgEtx.getText().toString());
            } catch (Exception e) {
                Log.d("ExceptionSendMessage:", e.getMessage().toString());
            }
        });
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebSocketManager.getInstance().disconnectWebSocket();
                if(intent.getStringExtra("username").equals(intent.getStringExtra("myGym"))) {
                    //business is logged in -> send to business profile
                    Intent i = new Intent(MyGymChat.this, BusinessProfilePage.class);
                    i.putExtra("username", intent.getStringExtra("username").toString());
                    i.putExtra("businessName", intent.getStringExtra("myGym"));
                    startActivity(i);
                    finish();
                }
                else {
                    //user is logged in -> send to home page
                    Intent i = new Intent(MyGymChat.this, HomePage.class);
                    i.putExtra("username", intent.getStringExtra("username").toString());
                    startActivity(i);
                    finish();
                }
            }
        });
    }
    @Override
    public void onWebSocketMessage(String message) {
        /**
         * In Android, all UI-related operations must be performed on the main UI thread
         * to ensure smooth and responsive user interfaces. The 'runOnUiThread' method
         * is used to post a runnable to the UI thread's message queue, allowing UI updates
         * to occur safely from a background or non-UI thread.
         */
        runOnUiThread(() -> {
            String s = msgTv.getText().toString();
            msgTv.setText(s + "\n"+message);
        });
    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        String closedBy = remote ? "server" : "local";
        runOnUiThread(() -> {
            String s = msgTv.getText().toString();
            msgTv.setText(s + "---\nconnection closed by " + closedBy + "\nreason: " + reason);
        });
    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {}

    @Override
    public void onWebSocketError(Exception ex) {}

    private String concatToURL(String username) {
        return this.BASE_URL += username;
    }
}