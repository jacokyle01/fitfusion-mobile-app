package com.example.gymapp23;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gymapp23.databinding.ActivityHomePageBinding;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

//import androidx.navigation.NavController;
//import androidx.navigation.Navigation;
//import androidx.navigation.ui.AppBarConfiguration;
//import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HomePage extends AppCompatActivity {
    private String GETUSERURL = "http://coms-309-067.class.las.iastate.edu:8080/people/@/";
    private String userGym;
    private String TAG = MainActivity.class.getSimpleName();
    private ActivityHomePageBinding binding;
    private LinearLayout feedContainer;
    private TextView helloUserTxt;

    private JSONArray users, usersPosts;

    private ImageView profilePicOnPost;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        feedContainer = findViewById(R.id.feedContainer);

        Intent intent = getIntent();
        helloUserTxt = findViewById(R.id.helloUserTxt);
        helloUserTxt.setText("Hello " + intent.getStringExtra(LoginPage.USERNAME));


//        BottomNavigationView navView = findViewById(R.id.nav_view);
//         // Passing each menu ID as a set of Ids because each
//         // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(binding.navView, navController);

//        Toolbar toolbar = binding.toolbar;
//        setSupportActionBar(toolbar);
//        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
//        toolBarLayout.setTitle(getTitle());


        Button dmBtn = findViewById(R.id.messagesFAB);
        Button profile = findViewById(R.id.UserProfileFAB);
        Button workout = findViewById(R.id.workoutsFAB);
        Button search = findViewById(R.id.searchFAB);
        TextView feedTxt = findViewById(R.id.feedTxt);

        //getAllUsers();
        //Log.d("USERS RETREIVED", "USERS RETREIVED");

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(HomePage.this, SearchPage.class);
                intent2.putExtra("username", intent.getStringExtra(LoginPage.USERNAME));
                startActivity(intent2);
                finish();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            /**
             *
             * @param view The view that was clicked.
             */
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(HomePage.this, ProfilePage.class);
                intent2.putExtra("username", intent.getStringExtra(LoginPage.USERNAME));


                startActivity(intent2);
                finish();
            }
        });

        workout.setOnClickListener(new View.OnClickListener() {
            /**
             *
             * @param view The view that was clicked.
             */
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(HomePage.this, PostWorkout.class);
                intent2.putExtra("username", intent.getStringExtra(LoginPage.USERNAME));
                startActivity(intent2);
                finish();
            }
        });

        dmBtn.setOnClickListener(new View.OnClickListener() {
            /**
             *
             * @param view The view that was clicked.
             */
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(HomePage.this, openUserDM.class);
                intent2.putExtra("username", intent.getStringExtra(LoginPage.USERNAME));
                startActivity(intent2);
                finish();
            }
        });

        Button myGymBtn = findViewById(R.id.myGymFAB);
        myGymBtn.setOnClickListener(new View.OnClickListener() {

            /**
             *
             *
             * @param v The view that was clicked.
             */

            @Override
            public void onClick(View v) {
                //open a group chat of the users gym
                //need a call to get a users gym
                String tempGym = getUserGym();
                Intent i = new Intent(HomePage.this, MyGymChat.class);
                i.putExtra("username", getIntent().getStringExtra("username").toString());
                Log.d("USER GYM", tempGym);
                i.putExtra("myGym", tempGym);
                startActivity(i);
                finish();
            }
        });

        Button challengeBtn = findViewById(R.id.challenge);
        challengeBtn.setOnClickListener(new View.OnClickListener() {
            /**
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                //open a group chat of the users gym
                //need a call to get a users gym

                Intent i = new Intent(HomePage.this, challengesPage.class);
                i.putExtra("username", getIntent().getStringExtra("username").toString());
                startActivity(i);
                finish();
            }
        });

        //        WebSocketManager.getInstance().connectWebSocket(serverUrl);
//        WebSocketManager.getInstance().setWebSocketListener(challengesPage.this);
//
//


//        createChallenge.setOnClickListener(v -> {
//            try {
//                // send challenge
//                WebSocketManager.getInstance().sendMessage(newChallenge.getText().toString());
//            } catch (Exception e) {
//                Log.d("ExceptionSendMessage:", e.getMessage().toString());
//            }
//        });
        loadFeed();
    }
//    You have been challanged by " + *username* + " to " *challenge*);
//    @Override
//    public void onWebSocketMessage(String message){
//        runOnUiThread(() -> {
//            String s = showChallenges.getText().toString();
//            showChallenges.setText(s + "\n" + message);
//                });
//    }
//
//    @Override
//    public void onWebSocketClose(int code, String reason, boolean remote) {
//        String closedBy = remote ? "server" : "local";
//        runOnUiThread(() -> {
//            String s = showChallenges.getText().toString();
//            showChallenges.setText(s + "---\nconnection closed by " + closedBy + "\nreason: " + reason);
//        });
//    }

    //    @Override
//    public void onWebSocketOpen(ServerHandshake handshakedata) {}
//
//    @Override
//    public void onWebSocketError(Exception ex) {}
//    private String getUserGym() {
//
//        String URL = GETUSERURL + getIntent().getStringExtra("username");
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
//                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            //Toast.makeText(ProfilePage.this, response.toString(), Toast.LENGTH_LONG).show();
//                            userGym = response.getJSONObject("myGym").getString("businessName");
//                        } catch (JSONException e) {
//                            userGym = "";
//                        }
//                    }
//                }, new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(HomePage.this, "Error getting user details", Toast.LENGTH_LONG).show();
//                    }
//                });
//        RequestQueue rq = Volley.newRequestQueue(HomePage.this);
//        rq.add(jsonObjectRequest);
//
//        return userGym;
//    }

    private String getUserGym() {
        String URL = GETUSERURL + getIntent().getStringExtra("username");

        userGym = "StateGym"; //default value
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Toast.makeText(ProfilePage.this, response.toString(), Toast.LENGTH_LONG).show();
                            userGym = response.getJSONObject("myGym").getString("businessName");
                        } catch (JSONException e) {
                            userGym = "StateGym";
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(HomePage.this, "Error getting user details", Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue rq = Volley.newRequestQueue(HomePage.this);
        rq.add(jsonObjectRequest);

        return userGym;
    }
    private void loadFeed() {

        String URL = "http://coms-309-067.class.las.iastate.edu:8080/posts/" + getIntent().getStringExtra("username") + "/feed";

        JsonArrayRequest arrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //Toast.makeText(HomePage.this,"Posting feed", Toast.LENGTH_LONG).show();

                TextView debug = findViewById(R.id.feedTxt);
                debug.setText("");

                //feedContainer.addView(new View(findViewById(R.layout.post_container)));


                try {
                    if (response.length() == 0) {
                        Toast.makeText(HomePage.this, "Response Length 0", Toast.LENGTH_SHORT).show();
                        addPostCard("No Feed - Find friends to see their posts", null);
                    } else {
                        //debug.setText(debug.getText().toString() + "\n" + response.toString());
                        //helloUserTxt.setText("Response length: " + response.length());
                        //Toast.makeText(HomePage.this, "Processing Response", Toast.LENGTH_SHORT).show();
                        //helloUserTxt.setText(response.toString());
                        for (int i = 0; i < response.length(); i++) {
                            //debug.setText(debug.getText().toString() + "\n" + "in for loop: " + i);
//                            Toast.makeText(HomePage.this, String.valueOf(i), Toast.LENGTH_SHORT).show();
                            //helloUserTxt.setText(String.valueOf(i+1));
                            JSONObject object = response.getJSONObject(i);
                            String name = getIntent().getStringExtra("username");
                            //debug.setText(debug.getText().toString() + "\n" + "before addpost");
                            addPostCard(name, object);
                            //debug.setText(debug.getText().toString() + "\n" + "Passed addpostCard");
                        }
                        //debug.setText(debug.getText().toString() + "\n" + "after for loop");

                    }
                } catch (JSONException e) {
                    Toast.makeText(HomePage.this, "Error Loading feed", Toast.LENGTH_SHORT).show();
                    VolleyLog.d(TAG, "Error:" + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error:" + error.getMessage());
            }
        });

        RequestQueue rq = Volley.newRequestQueue(HomePage.this);
        rq.add(arrayRequest);
    }

    public void addPostCard(String name, JSONObject post) {
        TextView debug = findViewById(R.id.feedTxt);
        //debug.setText("in add post");
        View view = getLayoutInflater().inflate(R.layout.post_container, null);
        TextView usernameTxt = view.findViewById(R.id.usernameTextView);
        TextView titleTxt = view.findViewById(R.id.titleTextView);
        TextView msgTxt = view.findViewById(R.id.messageTextView);
        TextView likesTxt = view.findViewById(R.id.likesTextView);
        Button likeBtn = view.findViewById(R.id.likeBTN);
        //profilePicOnPost = view.findViewById(R.id.postProfilePic);

        int postID;

        //debug.setText(debug.getText().toString() + "\n" + post.toString());

//        Toast.makeText(HomePage.this, "adding card" + post.toString(), Toast.LENGTH_SHORT).show();

        if (name.equals("No Feed - Find friends to see their posts") || post == null) {
            Log.d("ADD CARD", "NO BODY");
            View Errorview = getLayoutInflater().inflate(R.layout.simple_text_box, null);
            TextView text = Errorview.findViewById(R.id.textView);
            text.setText("No Feed - Find friends to see their post");
            feedContainer.addView(Errorview);
        } else {
            Log.d("ADD CARD", "BODY" + name);
            try {
                usernameTxt.setText(""); //leave blank until owner of post is passed
                titleTxt.setText(post.getString("title"));
                msgTxt.setText(post.getString("message"));
                postID = post.getInt("id");
                int numLikes = post.getInt("likes");

                Log.d("NUM LIKES", "Num Likes: " + numLikes);
                if (numLikes < 0) {
                    numLikes = 0;
                }
                likesTxt.setText(String.valueOf(numLikes));

                //display image
                //displayProfilePicToCard(view, postID);

                likeBtn.setOnClickListener(new View.OnClickListener() {
                    boolean hasLiked;

                    @Override
                    public void onClick(View v) {
                        //check if user has already liked it
                        try {
                            ///posts/person/{id}/like/{person}
                            String checkLikedURL = "http://coms-309-067.class.las.iastate.edu:8080/posts/person/" + postID + "/like/" + getIntent().getStringExtra("username");
//                   Toast.makeText(GroupChatBanPage.this, name, Toast.LENGTH_LONG).show();
                            StringRequest stringRequest = new StringRequest(Request.Method.GET, checkLikedURL, new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {
                                    Log.d("LIKE BUTTON", "HasLiked response:" + response.toString());

                                    if (response.equals("liked")) {
                                        hasLiked = true;
                                        String unlikeURL = "http://coms-309-067.class.las.iastate.edu:8080/posts/person/" + postID + "/unlike/"+ getIntent().getStringExtra("username");
                                        StringRequest unlikeRequest = new StringRequest(Request.Method.PUT, unlikeURL, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                Toast.makeText(HomePage.this, "unliked", Toast.LENGTH_SHORT).show();
                                                //update like counter in real time to prevent reloading feed.

                                                int decrementLikes = Integer.parseInt(likesTxt.getText().toString()) - 1;
                                                String intToString = "" + decrementLikes;
                                                likesTxt.setText(intToString);
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Toast.makeText(HomePage.this, "Error unliking", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        RequestQueue rq;
                                        rq = Volley.newRequestQueue(HomePage.this);
                                        rq.add(unlikeRequest);
                                    }
                                    else {
                                        hasLiked = false;
                                        String likeURL = "http://coms-309-067.class.las.iastate.edu:8080/posts/person/" + postID + "/like/"+ getIntent().getStringExtra("username");
                                        StringRequest likeRequest = new StringRequest(Request.Method.PUT, likeURL, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                Toast.makeText(HomePage.this, "Liked", Toast.LENGTH_SHORT).show();
                                                //update like counter in real time to prevent reloading feed.
                                                int incrementLikes = Integer.parseInt(likesTxt.getText().toString()) + 1;
                                                String intToString = "" + incrementLikes;
                                                likesTxt.setText(intToString);
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Toast.makeText(HomePage.this, "Error Liked", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        RequestQueue rq;
                                        rq = Volley.newRequestQueue(HomePage.this);
                                        rq.add(likeRequest);
                                    }

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("hasliked error", error.toString());
                                }
                            }
                            );


                            Log.d("LIKE BUTTON", "Hasliked: " + hasLiked);
                            Log.d("LIKE BUTTON", "Post id: " + postID);


                            RequestQueue requestQueue;
                            requestQueue = Volley.newRequestQueue(HomePage.this);
                            requestQueue.add(stringRequest);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
                feedContainer.addView(view);

            } catch (JSONException e) {
                VolleyLog.d(TAG, "addPostCard Error");
            }
        }

    }

//    private void displayProfilePicToCard(View view, int postID) {
//        int j = 0;
//        boolean idFlag = false;
//        String otherUsername = "";
//        for (int i = 0; i < users.length(); i++) {
//            //iterate through users
//            try {
//                getUserPosts(users.getJSONObject(i).getString("username"));
//
//                for (j = 0; j < usersPosts.length(); j++) {
//                    //iterate through posts until postID found
//                    if (usersPosts.getJSONObject(j).getInt("id") == postID) {
//                        idFlag = true;
//                        break;
//                    }
//                }
//
//                if (idFlag) {
//                    otherUsername = users.getJSONObject(i).getString("username");
//                    break;
//                }
//            } catch (JSONException e) {
//
//            }
//        }
//            //store that users username
//        //load that users username profile picture onto the one
//        loadProfilePic(otherUsername);
//
//
//    }
//
//    private void getAllUsers() {
//        do {
//            users = new JSONArray();
//            Log.d("LIST OF USERS", "Start of getAllUsers()");
//            String URL = "http://coms-309-067.class.las.iastate.edu:8080/people";
//            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
//                    (Request.Method.GET, URL, null,  new Response.Listener<JSONArray>() {
//                        @Override
//                        public void onResponse(JSONArray response) {
//                            Log.d("LIST OF USERS", "NUM USERS: " + response.length());
//                            for (int i = 0; i < response.length(); i++) {
//                                try {
//                                    users.put(response.get(i));
//                                } catch (JSONException e) {
//                                    throw new RuntimeException(e);
//                                }
//                            }
//
//                        }
//                    }, new Response.ErrorListener() {
//
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            Log.d("LIST OF USERS", "ERROR");
//                        }
//                    });
//
//            RequestQueue rq;
//            rq = Volley.newRequestQueue(HomePage.this);
//            rq.add(jsonArrayRequest);
//        } while (users.length() == 0);
//    }
//    private void getUserPosts(String name) {
//        String URL = "http://coms-309-067.class.las.iastate.edu:8080/posts/" + name;
//
//
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
//                (Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        usersPosts = response;
//
//                    }
//                }, new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        usersPosts = null;
//                    }
//                });
//        RequestQueue rq = Volley.newRequestQueue(HomePage.this);
//        rq.add(jsonArrayRequest);
//    }
//
//    private void loadProfilePic(String name) {
//        String PROFILEPICURL = GETUSERURL + name + "/profilePicture";
//
//        StringRequest request = new StringRequest
//                (Request.Method.GET, PROFILEPICURL, new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        if (!response.isEmpty()) {
//
//                            String imageUrl = PROFILEPICURL;
//
//                            // Execute an AsyncTask to load the image from the URL
//                            new HomePage.LoadImageTask().execute(imageUrl);
//                        } else {
//                            // generate custom profile pic from URL
//                            String imageUrl = "https://robohash.org/" + getIntent().getStringExtra("username");
//
//                            // Execute an AsyncTask to load the image from the URL
//                            new HomePage.LoadImageTask().execute(imageUrl);
//                        }
//                    }
//                }, new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.d(TAG, error.toString());
//                        Toast.makeText(HomePage.this, error.toString(), Toast.LENGTH_LONG).show();
//                    }
//                });
//        RequestQueue rq = Volley.newRequestQueue(HomePage.this);
//        rq.add(request);
//    }
//    private class LoadImageTask extends AsyncTask<String, Void, byte[]> {
//
//        @Override
//        protected byte[] doInBackground(String... params) {
//            String imageUrl = params[0];
//
//            try {
//                URL url = new URL(imageUrl);
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setDoInput(true);
//                connection.connect();
//
//                InputStream input = connection.getInputStream();
//                ByteArrayOutputStream output = new ByteArrayOutputStream();
//
//                byte[] buffer = new byte[4096];
//                int bytesRead;
//                while ((bytesRead = input.read(buffer)) != -1) {
//                    output.write(buffer, 0, bytesRead);
//                }
//
//                input.close();
//                return output.toByteArray();
//            } catch (Exception e) {
//                Log.e("ImageLoading", "Error loading image", e);
//                return null;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(byte[] imageBytes) {
//            if (imageBytes != null && imageBytes.length > 0) {
//                // Convert byte array to Bitmap
//                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
//
//                // Set Bitmap to ImageView
//                profilePicOnPost.setImageBitmap(bitmap);
//            } else {
//                Log.e("ImageLoading", "Empty or null byte array");
//            }
//        }
//    }
}

