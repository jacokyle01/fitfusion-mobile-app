package com.example.gymapp23;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gymapp23.databinding.ActivityDirectMessageBinding;
import com.example.gymapp23.databinding.ActivityProfilePageBinding;

import org.java_websocket.handshake.ServerHandshake;
import org.w3c.dom.Text;


public class DirectMessage extends AppCompatActivity implements WebSocketListener{

    //private String BASE_URL = "ws://10.0.2.2:8080/chat/";
    ///chat/{username}/DM/{recipient}
    private String BASE_URL = "ws://coms-309-067.class.las.iastate.edu:8080/chat/";
    //private String BASE_URL = "ws://localhost:8080/chat/";
    private Button sendBtn;
    private EditText usernameEtx, msgEtx;
    private TextView msgTv;

    private Button homeBtn;

    private ActivityDirectMessageBinding binding;
    /**
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDirectMessageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        //connect to the other user
        //String serverUrl = BASE_URL + usernameEtx.getText().toString();
        String serverUrl = BASE_URL + intent.getStringExtra("username") + "/DM/" + intent.getStringExtra("other_username");

        // Establish WebSocket connection and set listener
        WebSocketManager.getInstance().connectWebSocket(serverUrl);
        WebSocketManager.getInstance().setWebSocketListener(DirectMessage.this);

//        Button sendDMBtn = findViewById(R.id.sendDMBtn);
//        EditText userMsgETxt = findViewById(R.id.userMsgEtx);
//        TextView messageBoard = findViewById(R.id.messagesTxt_DM);
        TextView chattingWithTxt = findViewById(R.id.chattingWithTxt);

        chattingWithTxt.setText("chatting with: " + intent.getStringExtra("other_username"));

        /* initialize UI elements */
        sendBtn = (Button) findViewById(R.id.sendDMBtn);
        //usernameEtx = (EditText) findViewById(R.id.messagesTxt_DM);
        msgEtx = (EditText) findViewById(R.id.userMsgEtx);
        msgTv = (TextView) findViewById(R.id.messagesTxt_DM);


        /* send button listener */
        sendBtn.setOnClickListener(v -> {
            try {

                // send message
                WebSocketManager.getInstance().sendMessage(msgEtx.getText().toString());
            } catch (Exception e) {
                Log.d("ExceptionSendMessage:", e.getMessage().toString());
            }
        });
        homeBtn = findViewById(R.id.dmToHomeBtn);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            /**
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                WebSocketManager.getInstance().disconnectWebSocket();
                Intent i = new Intent(DirectMessage.this, HomePage.class);
                i.putExtra("username", intent.getStringExtra("username").toString());
                startActivity(i);
                finish();
            }
        });
    }

    /**
     *
     * @param message The received WebSocket message.
     */
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

    /**
     *
     * @param code   The status code indicating the reason for closure.
     * @param reason A human-readable explanation for the closure.
     * @param remote Indicates whether the closure was initiated by the remote endpoint.
     */

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        String closedBy = remote ? "server" : "local";
        runOnUiThread(() -> {
            String s = msgTv.getText().toString();
            msgTv.setText(s + "---\nconnection closed by " + closedBy + "\nreason: " + reason);
        });
    }

    /**
     *
     * @param handshakedata Information about the server handshake.
     */
    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {}

    /**
     *
     * @param ex The exception that describes the error.
     */
    @Override
    public void onWebSocketError(Exception ex) {}

}