package com.example.gymapp23;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.gymapp23.databinding.ActivityBusinessProfilePageBinding;
import com.example.gymapp23.databinding.ActivityProfilePageBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


//a business profile page is its "homepage"
//access its groupchat
//have the businesses information to view
public class BusinessProfilePage extends AppCompatActivity {

    public final String GETBUSINESSURL = "http://coms-309-067.class.las.iastate.edu:8080/businesses/@/";
    private TextView username, feed, businessInfo;

    private String feedStr;
    private FloatingActionButton groupChatBtn, postBtn;
    private ActivityBusinessProfilePageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBusinessProfilePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        feed = findViewById(R.id.businessFeedTxt);
        postBtn = findViewById(R.id.postEventButton);
        businessInfo = findViewById(R.id.businessInfoTxt);
        groupChatBtn = findViewById(R.id.myGymChat_businessProfilePage);
        //feed = findViewById(R.id.eventPosts);
        String businessName = getIntent().getStringExtra("businessName");
        username = findViewById(R.id.businessNameTxt);
        username.setText(businessName);

        //get request for business
        String URL = GETBUSINESSURL + businessName;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //populate profile page
                        try {
                            feedStr = "<b><span style=color:#008080> Name: </span></b>" + response.getString("businessName") + "<br>";
                            feedStr += "<b><span style=color:#008080> Address: </span></b>" + response.getString("address") + "<br>";
                            feedStr += "<b><span style=color:#008080> City: </span></b>" + response.getString("city") + "<br>";
                            feedStr += "<b><span style=color:#008080> Zip: </span></b>" + response.getString("zipcode") + "<br>";

                            businessInfo.setText(Html.fromHtml(feedStr));
//                            feedStr = "";
//                            //just to show the scroll feature
//                            for(int i = 0; i < 100; i++) {
//                                feedStr += i + "\n";
//                            }
//
//                            feed.setText(feedStr);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        Toast.makeText(BusinessProfilePage.this, response.toString(), Toast.LENGTH_LONG).show();


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(BusinessProfilePage.this, "Error getting business details", Toast.LENGTH_LONG).show();
                    }
                });

        JsonArrayRequest postsGETRequest = new JsonArrayRequest(Request.Method.GET, URL + "/posts", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    if (response.length() == 0) {
                        feed.setText("No Posts for this business yet");
                    }
                    else {
                        feedStr = "";
                        for (int i = response.length()-1; i >= 0; i--) {

                            JSONObject object = response.getJSONObject(i);
                            String title = object.getString("title");
                            String message = object.getString("message");
                            int likes = object.getInt("likes");
                            feedStr += "<b> <span style=color:black>"+ title + ":</span> </b><br>"
                                    + message + "<br><span style=color:#505050> " +
                                    "Likes: </span><b><span style=color:#F06060>" + likes + "</span></b><br><br>";

                        }
                        feed.setText(Html.fromHtml(feedStr));
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(BusinessProfilePage.this);
        requestQueue.add(jsonObjectRequest);
        requestQueue.add(postsGETRequest);


        groupChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open MyGymChat
                Intent myGymChat = new Intent(BusinessProfilePage.this, MyGymChat.class);
                myGymChat.putExtra("username", businessName);
                myGymChat.putExtra("myGym", businessName);
                startActivity(myGymChat);
                finish();
            }
        });

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to homepage
                Intent homepage = new Intent(BusinessProfilePage.this, PostEvent.class);
                homepage.putExtra("businessName", businessName);
                startActivity(homepage);
                finish();
            }
        });

    }
}