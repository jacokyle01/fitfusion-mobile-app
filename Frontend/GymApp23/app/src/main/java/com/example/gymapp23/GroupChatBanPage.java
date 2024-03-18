package com.example.gymapp23;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gymapp23.databinding.ActivityGroupChatBanPageBinding;
import com.example.gymapp23.databinding.ActivityProfilePageBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class GroupChatBanPage extends AppCompatActivity {
    AlertDialog dialog;
    Button search, NAVRight;
    LinearLayout layout, blackListCards;
    ScrollView blackListsSV;
    TextView baselineText, blacklistTextview;
    EditText searchName, groupname;
    private String TAG = MainActivity.class.getSimpleName();
    String URL = "http://coms-309-067.class.las.iastate.edu:8080/people";
    String userName;
    private Button NAVLeft, viewBlacklist;



    private ActivityGroupChatBanPageBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupChatBanPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        userName = intent.getStringExtra("username");

        search = findViewById(R.id.searchBTN);
        searchName = findViewById(R.id.searchName);
        layout = findViewById(R.id.container);
        baselineText = findViewById(R.id.baselineText);
        groupname = findViewById(R.id.groupNameETxt);
        blackListsSV = findViewById(R.id.blacklistScrollView);
        blacklistTextview = findViewById(R.id.blacklistTextView);
        blackListCards = findViewById(R.id.blackListCardsLL);

        NAVLeft = findViewById(R.id.BusinessDeleteNAVBtn);
        viewBlacklist = findViewById(R.id.viewBlacklistBtn);

        NAVLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GroupChatBanPage.this, BusinessDeletePage.class);
                startActivity(i);
                finish();
            }
        });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if (groupname.getText().toString().equals("")) {
                    Toast.makeText(GroupChatBanPage.this,"Please Enter A Group Name", Toast.LENGTH_LONG).show();
                }
                else {
                    layout.removeAllViews();
                   String serverUrl = URL + "/@/" + userName + "/search/" + (searchName.getText().toString()) ;
                    //String serverUrl = "http://coms-309-067.class.las.iastate.edu:8080/groupchats"
                    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(serverUrl, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                if (response.length() == 0) {
                                    addCard("No People");
                                } else {
                                    for (int i = response.length() - 1; i >= 0; i--) {
                                        JSONObject object = response.getJSONObject(i);
                                        String name = object.getString("username");
                                        addCard(name);
                                    }
                                }
                            } catch (JSONException e) {
                                VolleyLog.d(TAG, "Error:" + e.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.d(TAG, "Error:" + error.getMessage());
                        }
                    });
                    RequestQueue requestQueue;
                    requestQueue = Volley.newRequestQueue(GroupChatBanPage.this);
                    requestQueue.add(jsonArrayRequest);
                }
            }
        });

        viewBlacklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                //layout is a linear view, make a second one

                blackListCards.removeAllViews();
                String serverUrl = "http://coms-309-067.class.las.iastate.edu:8080/blacklists/" + groupname.getText().toString() ;
                //String serverUrl = "http://coms-309-067.class.las.iastate.edu:8080/blacklists/demo4";

                StringRequest rq = new StringRequest(Request.Method.GET, serverUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        blacklistTextview.setText("Response: " + response);
                        try {
                            JSONArray arr = new JSONArray(response);
                            //blacklistTextview.setText(arr.get(0).toString());
                            if (arr.length() == 0) {
                                addTextCard("No People");
                            } else {
                                for (int i = arr.length() - 1; i >= 0; i--) {
                                    String name = arr.get(i).toString();
                                    addTextCard(name);
                                }
                            }
                        }
                        catch (JSONException e) {
                            blacklistTextview.setText("Error: " + e.getMessage());
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        blacklistTextview.setText("Error: " + error.getMessage() + "\n" + serverUrl);
                    }
                });
                RequestQueue requestQueue;
                requestQueue = Volley.newRequestQueue(GroupChatBanPage.this);
                requestQueue.add(rq);
            }

        });

    }
    private void addCard(String name){
        View view = getLayoutInflater().inflate(R.layout.simple_box_2btn, null);
        TextView nameView = view.findViewById(R.id.textView);
        Button banBtn = view.findViewById(R.id.btn1);
        banBtn.setBackgroundColor(Color.RED);
        banBtn.setText("Blacklist");

        Button unbanBtn = view.findViewById(R.id.btn2);
        unbanBtn.setBackgroundColor(Color.GRAY);
        unbanBtn.setText("UNBAN");

        nameView.setText(name);

        banBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                    if (groupname.getText().toString().equals("")) {
                        Toast.makeText(GroupChatBanPage.this,"Error Blacklisting <No Group>",Toast.LENGTH_LONG).show();
                    }
                    else {
                        //String deleteUserURL = URL +"/@/"+ name + "/ban";
                        String blacklistUserURL = "http://coms-309-067.class.las.iastate.edu:8080/blacklists/" + groupname.getText().toString() + "/" + name;
                        //Toast.makeText(GroupChatBanPage.this, blacklistUserURL, Toast.LENGTH_LONG).show();
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, blacklistUserURL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(GroupChatBanPage.this, response.toString(), Toast.LENGTH_LONG).show();
//                           nameView.setText(response.toString());
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(GroupChatBanPage.this, "Error: "+ error.toString(), Toast.LENGTH_LONG).show();
//                           nameView.setText(error.toString());
                            }
                        }
                        );

                        RequestQueue requestQueue;
                        requestQueue = Volley.newRequestQueue(GroupChatBanPage.this);
                        requestQueue.add(stringRequest);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        unbanBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                    if (groupname.getText().toString().equals("")) {
                        Toast.makeText(GroupChatBanPage.this,"Error Blacklisting <No Group>",Toast.LENGTH_LONG).show();
                    }
                    else {
                        //String deleteUserURL = URL +"/@/"+ name + "/ban";
                        String blacklistUserURL = "http://coms-309-067.class.las.iastate.edu:8080/blacklists/" + groupname.getText().toString() + "/" + name;
                        //Toast.makeText(GroupChatBanPage.this, blacklistUserURL, Toast.LENGTH_LONG).show();
                        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, blacklistUserURL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(GroupChatBanPage.this, response.toString(), Toast.LENGTH_LONG).show();
//                           nameView.setText(response.toString());
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(GroupChatBanPage.this, "Error: "+ error.toString(), Toast.LENGTH_LONG).show();
//                           nameView.setText(error.toString());
                            }
                        }
                        );

                        RequestQueue requestQueue;
                        requestQueue = Volley.newRequestQueue(GroupChatBanPage.this);
                        requestQueue.add(stringRequest);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        layout.addView(view);
    }
    private void addTextCard(String name){
        View view = getLayoutInflater().inflate(R.layout.simple_text_box, null);
        TextView nameView = view.findViewById(R.id.textView);

        nameView.setText(name);

        blackListCards.addView(view);
    }
}