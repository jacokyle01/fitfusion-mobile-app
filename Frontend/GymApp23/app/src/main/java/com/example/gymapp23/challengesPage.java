package com.example.gymapp23;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gymapp23.databinding.ActivityChallengesPageBinding;
import com.example.gymapp23.databinding.ActivityProfilePageBinding;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class challengesPage extends AppCompatActivity implements WebSocketListener{
    private EditText newChallengeUser;
    private Button createChallenge, homebtn, encourageChallenge, trashtalkChallenge;
    private RequestQueue requestQueue ;
    private TextView showChallenges;
    LinearLayout layout;
//    String currentChallenges = "";
    String BASE_URL = "http://coms-309-067.class.las.iastate.edu:8080/";
    private String challengeURL = "ws://coms-309-067.class.las.iastate.edu:8080/challengeChat/";

    private ActivityChallengesPageBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChallengesPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        createChallenge    = (Button)   findViewById(R.id.createChallenge);
        homebtn            = (Button)   findViewById(R.id.homebtn);
        encourageChallenge = (Button)   findViewById(R.id.encourageChallenge);
        trashtalkChallenge = (Button)   findViewById(R.id.trashtalkChallenge);
        newChallengeUser   = (EditText) findViewById(R.id.newChallengeUser);
//        showChallenges     = (TextView) findViewById(R.id.showChallenges);
        layout             =            findViewById(R.id.container);
        Intent intent = getIntent();
        String userName = intent.getStringExtra("username");
        requestQueue = Volley.newRequestQueue(challengesPage.this);

        WebSocketManager.getInstance().connectWebSocket(challengeURL + userName);
        WebSocketManager.getInstance().setWebSocketListener(challengesPage.this);

        JsonArrayRequest getToChallenges = new JsonArrayRequest(Request.Method.GET, BASE_URL + "challenges/to/" + (intent.getStringExtra("username")), null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    if (response.length() == 0) {
                        Toast.makeText(challengesPage.this, "no sent challenges ", Toast.LENGTH_LONG).show();
//                        currentChallenges += ("No TO Challenges");
                    }
                    else {
                        for (int i = response.length() - 1; i >= 0; i--) {
                            JSONObject object = response.getJSONObject(i);
                            String status = object.getString("status");
                            int id = object.getInt("id");
                            String name = (object.getJSONObject("challenger")).getString("username");
                            if(status.equals("PENDING")) {
                                addCardPending("from " + name, id);
                            }
                            else if (status.equals("IN_PROGRESS")) {
                                addCardInProgress("from " + name, id);
                            }
//                            currentChallenges += "\n\nSTATUS: " + status + " to " + username;
                        }
                    }
//                    showChallenges.setText(currentChallenges);
                } catch (Exception e) {
                    //Toast.makeText(challengesPage.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(challengesPage.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        JsonArrayRequest getFromChallenges = new JsonArrayRequest(Request.Method.GET, BASE_URL + "challenges/from/" + (intent.getStringExtra("username")), null, new Response.Listener<JSONArray>() {
            @Override

            public void onResponse(JSONArray response) {
                try {
                    if (response.length() == 0) {
                        Toast.makeText(challengesPage.this, "no FROM challenges", Toast.LENGTH_LONG).show();
                    } else {
                        for (int i = response.length() - 1; i >= 0; i--) {
                            JSONObject object = response.getJSONObject(i);
                            String status = object.getString("status");
                            int id = object.getInt("id");
                            String name = (object.getJSONObject("challengee")).getString("username");
//                            addCardPending(status + " from" + username);
                            if(status.equals("PENDING")) {
                                addCardWaiting("to " + name, id);
                            }
                            else if (status.equals("IN_PROGRESS")) {
                                addCardInProgress("to " + name, id);
                            }
                        }
                    }
//                    showChallenges.setText(currentChallenges);
                } catch (JSONException e) {
//                    showChallenges.setText("Error IN: " + e.toString());
                    //Toast.makeText(challengesPage.this, "Error: " + e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(challengesPage.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
//                showChallenges.setText("Error: " + error.getMessage());
            }
        });

        requestQueue.add(getToChallenges);
        requestQueue.add(getFromChallenges);

        encourageChallenge.setOnClickListener(v -> {
            try {
                WebSocketManager.getInstance().sendMessage("!encourage");
            } catch (Exception e) {
                Log.d("ExceptionSendMessage:", e.getMessage().toString());
            }
        });
        trashtalkChallenge.setOnClickListener(v -> {
            try {
                WebSocketManager.getInstance().sendMessage("!trashtalk");
            } catch (Exception e) {
                Log.d("ExceptionSendMessage:", e.getMessage().toString());
            }
        });
        createChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///people/@/{username}/challenge/{username}
                if (newChallengeUser.getText().toString() != "") {
                    try {
                        JSONObject jsonPost = new JSONObject();
                        jsonPost.put("status", "PENDING");
                        Toast.makeText(challengesPage.this, "USER: " + intent.getStringExtra("username") + "/challenge/" + (newChallengeUser.getText().toString()), Toast.LENGTH_LONG).show();

                        JsonObjectRequest postChallenge = new JsonObjectRequest(Request.Method.POST,
                                (BASE_URL + "people/@/" + intent.getStringExtra("username") + "/challenge/" + (newChallengeUser.getText().toString()))
                                , jsonPost, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(challengesPage.this, "Challenge sent!", Toast.LENGTH_LONG).show();
//                                addCardPending("STATUS: PENDING to " + newChallengeUser.getText().toString(), );
//                                currentChallenges += "\n\nSTATUS: PENDING to " + newChallengeUser.getText().toString();
//                                showChallenges.setText(currentChallenges);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //Toast.makeText(challengesPage.this, "Error: " + error.toString(), Toast.LENGTH_LONG).show();
                            }
                        });
                        requestQueue.add(postChallenge);
                        //                    requestQueue.add(postsGETRequest);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(challengesPage.this, "NO GIVEN USER!", Toast.LENGTH_LONG).show();
                }
            }
        });
        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebSocketManager.getInstance().disconnectWebSocket();
                Intent i = new Intent(challengesPage.this, HomePage.class);
                i.putExtra("username", intent.getStringExtra("username").toString());
                startActivity(i);
                finish();
            }
        });
    }
    // homebtn.setText("here");
    @Override
    public void onWebSocketMessage(String message) {
        runOnUiThread(() -> {
//            String s = msgTv.getText().toString();
            Toast.makeText(challengesPage.this, "WS: " + message , Toast.LENGTH_LONG).show();
        });
    }
    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {
        Toast.makeText(challengesPage.this, "WS: " + "---connection opened",  Toast.LENGTH_LONG).show();
    }
    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        String closedBy = remote ? "server" : "local";
        runOnUiThread(() -> {
//            String s = msgTv.getText().toString();
//            msgTv.setText(s + "---\nconnection closed by " + closedBy + "\nreason: " + reason);
            //Toast.makeText(challengesPage.this, "WS: " + "---\nconnection closed by " + closedBy + "\nreason: " + reason , Toast.LENGTH_LONG).show();

        });
    }

    @Override
    public void onWebSocketError(Exception ex) {}

    private void addCardPending(String name, int id) {
        View view = getLayoutInflater().inflate(R.layout.simple_box_2btn, null);
        TextView nameView = view.findViewById(R.id.textView);
        Button btn1 = view.findViewById(R.id.btn1);
        Button btn2 = view.findViewById(R.id.btn2);

        nameView.setText(name);
        btn1.setText("O");
        btn1.setBackgroundColor(getResources().getColor(R.color.bright_green));
        btn2.setText("RED");
        btn2.setBackgroundColor(getResources().getColor(R.color.purple_200));

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JsonArrayRequest sentAccept = new JsonArrayRequest(Request.Method.PUT, BASE_URL + "challenges/" + id + "/accept", null, new Response.Listener<JSONArray>() {
                    @Override

                    public void onResponse(JSONArray response) {
                        Toast.makeText(challengesPage.this, "Challenge Accepted",  Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(challengesPage.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
//                showChallenges.setText("Error: " + error.getMessage());
                    }
                });
                requestQueue.add(sentAccept);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JsonArrayRequest sentDecline = new JsonArrayRequest(Request.Method.DELETE, BASE_URL + "challenges/" + id + "/decline", null, new Response.Listener<JSONArray>() {
                    @Override

                    public void onResponse(JSONArray response) {
//                        Toast.makeText(challengesPage.this, "Challenge Declined",  Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(challengesPage.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
                requestQueue.add(sentDecline);
            }
        });

        layout.addView(view);
    }
    private void addCardInProgress(String name, int id) {
        View view = getLayoutInflater().inflate(R.layout.simple_box, null);
        TextView nameView = view.findViewById(R.id.textView);
        Button Complete = view.findViewById(R.id.followBTN);
        //  /challenges/{id}/accept, decline, complete
        nameView.setText(name);
        Complete.setText("Complete");
        Complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JsonArrayRequest sentComplete = new JsonArrayRequest(Request.Method.DELETE, BASE_URL + "challenges/" + id + "/complete", null, new Response.Listener<JSONArray>() {
                    @Override

                    public void onResponse(JSONArray response) {
//                        Toast.makeText(challengesPage.this, "Challenge Completed",  Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(challengesPage.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
                requestQueue.add(sentComplete);
            }
        });
        layout.addView(view);
    }
    private void addCardWaiting(String name, int id) {
        View view = getLayoutInflater().inflate(R.layout.simple_text_box, null);
        TextView nameView = view.findViewById(R.id.textView);

        nameView.setText(name);
        layout.addView(view);
    }

}