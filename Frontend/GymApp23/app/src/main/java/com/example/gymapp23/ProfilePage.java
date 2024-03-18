//package com.example.gymapp23;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Bundle;
//
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.VolleyLog;
//import com.android.volley.toolbox.JsonArrayRequest;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;
//import com.google.android.material.appbar.CollapsingToolbarLayout;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.android.material.snackbar.Snackbar;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//
//import android.text.Html;
//import android.util.Base64;
//import android.util.Log;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ScrollView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//import android.os.AsyncTask;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.io.BufferedInputStream;
//
//
//
//public class ProfilePage extends AppCompatActivity {
//    public String GETUSERURL = "http://coms-309-067.class.las.iastate.edu:8080/people/@/";
//    public String GETPOSTSURL = "http://coms-309-067.class.las.iastate.edu:8080/posts/";
//    private String TAG = MainActivity.class.getSimpleName(), feedStr;
//    private TextView workoutPosts, userName, followsTxt, userFeed, pointsTxt, userRoutines;
//
//    private FloatingActionButton myGymChatFAB, followList, addPostBtn;
//
//    private Button followBtn, loadRoutinesBtn, postMyGym;
//
//    private Button showRoutines, showPosts;
//
//    private EditText myGymEtxt;
//    private String userGym;
//    private LinearLayout profileInfo;
//    private ImageView ProfilePicture;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_profile_page);
//
//        Intent intent = getIntent();
//        FloatingActionButton home = findViewById(R.id.homePage);
//
//        userName = findViewById(R.id.userName);
//        myGymChatFAB = findViewById(R.id.myGymChat_userProfilePage);
//        followBtn = findViewById(R.id.userFollowBtn);
//        pointsTxt = findViewById(R.id.userProfilePointsTxt);
//        showRoutines = findViewById(R.id.showRoutines);
//        showPosts = findViewById(R.id.showPosts);
//        postMyGym = findViewById(R.id.postMyGymBtn);
//        myGymEtxt = findViewById(R.id.postMyGymEtx);
//
//        addPostBtn = findViewById(R.id.userPostBtn);
//
//        profileInfo = findViewById(R.id.profileInfo);
//        ProfilePicture = findViewById(R.id.PFP);
//
//
//
//        loadProfilePic();
//
//        loadUserInfo();
//        loadFeed();
//
//        userGym = getUserGym(); //call it initially otherwise null on first click
//        postMyGym.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //call post request
//                    //post request //sets the mygym attribute in the URL to the users gym
//                    String postMyWorkout = GETUSERURL + getIntent().getStringExtra("username") + "/setHome/" + myGymEtxt.getText().toString();
//                StringRequest postMyWorkoutRequest = new StringRequest(Request.Method.POST, postMyWorkout, new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        userGym = getUserGym();
//                        Toast.makeText(ProfilePage.this, "myGym posted", Toast.LENGTH_LONG).show();
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(ProfilePage.this, "error posting myGym", Toast.LENGTH_LONG).show();
//                    }
//                });
//                RequestQueue requestQueue;
//                requestQueue = Volley.newRequestQueue(ProfilePage.this);
//                requestQueue.add(postMyWorkoutRequest);
//            }
//        });
//        home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent2 = new Intent(ProfilePage.this, HomePage.class);
//                intent2.putExtra("username", intent.getStringExtra(LoginPage.USERNAME));
//                startActivity(intent2);
//                finish();
//            }
//        });
//        followBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent2 = new Intent(ProfilePage.this, FollowingList.class);
//                intent2.putExtra("username", intent.getStringExtra(LoginPage.USERNAME));
//                startActivity(intent2);
//                finish();
//            }
//        });
//        showRoutines.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                profileInfo.removeAllViews();
//                loadAllRoutines();
//            }
//        });
//        showPosts.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                profileInfo.removeAllViews();
//                loadFeed();
//            }
//        });
//        myGymChatFAB.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String usersGym = getUserGym();
//   //             Toast.makeText(ProfilePage.this, usersGym, Toast.LENGTH_LONG).show();
//                Intent gcIntent = new Intent(ProfilePage.this, MyGymChat.class);
//                gcIntent.putExtra("username", getIntent().getStringExtra("username"));
//                gcIntent.putExtra("myGym", usersGym);
//                startActivity(gcIntent);
//                finish();
//            }
//        });
//
//
//        addPostBtn.setOnClickListener(new View.OnClickListener() {
//                                          @Override
//                                          public void onClick(View v) {
//                                              //open up a post screen
//                                              Intent i = new Intent(ProfilePage.this, UserPosts.class);
//                                              startActivity(i);
//                                              finish();
//                                          }
//                                      });
//
//        ProfilePicture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(ProfilePage.this, ProfilePictureSelect.class);
//                i.putExtra("username", getIntent().getStringExtra("username"));
//                startActivity(i);
//                finish();
//            }
//        });
//    }
//
//    private void loadUserInfo() {
//        String feedStr;
//        String URL = GETUSERURL + getIntent().getStringExtra("username");
//
//        Toast.makeText(ProfilePage.this, URL, Toast.LENGTH_LONG);
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
//                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        //populate profile page
//                        try {
//                            userName.setText(response.getString("username"));
//                            pointsTxt.setText("Points: " +response.getInt("points"));
//                        } catch (JSONException e) {
//                            throw new RuntimeException(e);
//                        }
//
//
//                    }
//                }, new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(ProfilePage.this, "Error getting user details", Toast.LENGTH_LONG).show();
//                    }
//                });
//        RequestQueue rq = Volley.newRequestQueue(ProfilePage.this);
//        rq.add(jsonObjectRequest);
//    }
//    private void loadFeed() {
//
//        JsonArrayRequest postsGETRequest = new JsonArrayRequest(Request.Method.GET, GETPOSTSURL + getIntent().getStringExtra("username"), null, new Response.Listener<JSONArray>() {
//
//            @Override
//            public void onResponse(JSONArray response) {
//                try {
//                    if (response.length() == 0) {
//                        addTextCard("No posts here yet");
//                    }
//                    else {
//                        feedStr = "";
//                        for (int i = response.length()-1; i >= 0; i--) {
//                            JSONObject object = response.getJSONObject(i);
//                            String title = object.getString("title");
//                            String message = object.getString("message");
//                            int likes = object.getInt("likes");
//                            feedStr = "<h2><b> <span style=color:black>"+ title + ":</span> </b></h2>"
//                                    + "<p>" + message + "<p><span style=color:#505050> " +
//                                    "Likes: </span><b><span style=color:#F06060>" + likes + "</span>";
//                            addCard(feedStr);
//                        }
//                    }
//                }
//                catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                addTextCard("Error loading feed");
//                error.printStackTrace();
//
//            }
//        });
//
//
//        RequestQueue requestQueue;
//        requestQueue = Volley.newRequestQueue(ProfilePage.this);
//        requestQueue.add(postsGETRequest);
//    }
//    private void loadAllRoutines() {
//        String getAllRoutinesURL = "http://coms-309-067.class.las.iastate.edu:8080/user/" + getIntent().getStringExtra("username") + "/routines";
//
//        //get request
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
//                (Request.Method.GET, getAllRoutinesURL, null, new Response.Listener<JSONArray>() {
//
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        //find highest id
//                        JSONObject cur;
//                        int id;
//                        String msg;
//
////                        msg = "<h2><b><span style=color:black>Latest Routine: </b></h2>";
//
//
//                        try {
//                            //print out all routines
//                            for (int j = 0; j < response.length(); j++) {
//                                cur = response.getJSONObject(j);
//                                JSONArray workouts = cur.getJSONArray("workouts");
//                                msg = "<h2><span style=color:#64000A>" + cur.getString("name") + "</span></h2>";
//
//                                //iterate through all the workouts
//                                for (int i = 0; i < workouts.length(); i++) {
//                                    JSONObject exercise = workouts.getJSONObject(i);
//                                    msg+= "<p><b><span style=color:black>Exercise " + (i+1) + ": </b></style><span style=color:#64000A>" + exercise.getString("name") +
//                                            "</span>\t<b><span style=color:black>\tPR: </style></b><span style=color:#64000A>" + exercise.getString("record") + " </span><b><span style=color:black> reps: </style></b><span style=color:#64000A>" + exercise.getString("reps") + "</span></p>";
//                                }
//
//                                msg += "<p><span style=color:black>-------------------------------------------------</span></p>";
//                                addTextCard(msg);
//                            }
//                        } catch (JSONException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
//                }, new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(ProfilePage.this, "Error getting User details", Toast.LENGTH_LONG).show();
//                    }
//                });
//
//
//        RequestQueue requestQueue;
//        requestQueue = Volley.newRequestQueue(ProfilePage.this);
//        requestQueue.add(jsonArrayRequest);
//
//    }
//    private String getUserGym() {
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
//
//                            userGym = "stateGym";
//
//                        }
//                    }
//                }, new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(ProfilePage.this, "Error getting user details", Toast.LENGTH_LONG).show();
//                    }
//                });
//        RequestQueue rq = Volley.newRequestQueue(ProfilePage.this);
//        rq.add(jsonObjectRequest);
//
//        return userGym;
//    }
//    private void addTextCard(String text){
//        View view = getLayoutInflater().inflate(R.layout.simple_text_box, null);
//        TextView nameView = view.findViewById(R.id.textView);
//
//        nameView.setText(Html.fromHtml(text));
//
//        profileInfo.addView(view);
//    }
//    private void addCard(String text){
//        View view = getLayoutInflater().inflate(R.layout.simple_box, null);
//        TextView nameView = view.findViewById(R.id.textView);
//        Button btn = view.findViewById(R.id.followBTN);
//
//        nameView.setText(Html.fromHtml(text));
//        btn.setText("Like"); //ADD LIKE FEATURE
//
//        profileInfo.addView(view);
//    }
//    private void loadProfilePic() {
//        String PROFILEPICURL = GETUSERURL + getIntent().getStringExtra("username") + "/profilePicture";
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
//                            new LoadImageTask().execute(imageUrl);
//                        } else {
//                            // generate custom profile pic from URL
//                            String imageUrl = "https://robohash.org/" + getIntent().getStringExtra("username");
//
//                            // Execute an AsyncTask to load the image from the URL
//                            new LoadImageTask().execute(imageUrl);
//                        }
//                    }
//                }, new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.d(TAG, error.toString());
//                        Toast.makeText(ProfilePage.this, error.toString(), Toast.LENGTH_LONG).show();
//                    }
//                });
//        RequestQueue rq = Volley.newRequestQueue(ProfilePage.this);
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
//                ProfilePicture.setImageBitmap(bitmap);
//            } else {
//                Log.e("ImageLoading", "Empty or null byte array");
//            }
//        }
//    }
//}
package com.example.gymapp23;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.example.gymapp23.databinding.ActivityProfilePageBinding;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedInputStream;



public class ProfilePage extends AppCompatActivity {
    public String GETUSERURL = "http://coms-309-067.class.las.iastate.edu:8080/people/@/";
    public String GETPOSTSURL = "http://coms-309-067.class.las.iastate.edu:8080/posts";
    private String TAG = MainActivity.class.getSimpleName(), feedStr;
    private TextView userName, pointsTxt;
    private FloatingActionButton myGymChatFAB, addPostBtn;
    private Button followBtn, showRoutines, postMyGym, showPosts;
    private EditText myGymEtxt;
    private String userGym;
    private LinearLayout profileInfo;
    private ImageView ProfilePicture;
    private ActivityProfilePageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfilePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        FloatingActionButton home = findViewById(R.id.homePage);

        userName = findViewById(R.id.userName);
        myGymChatFAB = findViewById(R.id.myGymChat_userProfilePage);
        followBtn = findViewById(R.id.userFollowBtn);
        pointsTxt = findViewById(R.id.userProfilePointsTxt);
        showRoutines = findViewById(R.id.showRoutines);
        showPosts = findViewById(R.id.showPosts);
        postMyGym = findViewById(R.id.postMyGymBtn);
        myGymEtxt = findViewById(R.id.postMyGymEtx);
        profileInfo = findViewById(R.id.profileInfo);
        ProfilePicture = findViewById(R.id.PFP);
        addPostBtn = findViewById(R.id.userPostBtn);



        loadProfilePic();

        loadUserInfo();
        loadFeed();

        userGym = getUserGym(); //call it initially otherwise null on first click
        postMyGym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call post request
                //post request //sets the mygym attribute in the URL to the users gym
                String postMyWorkout = GETUSERURL + getIntent().getStringExtra("username") + "/setHome/" + myGymEtxt.getText().toString();
                StringRequest postMyWorkoutRequest = new StringRequest(Request.Method.POST, postMyWorkout, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        userGym = getUserGym();
                        Toast.makeText(ProfilePage.this, "myGym posted", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProfilePage.this, "error posting myGym", Toast.LENGTH_LONG).show();
                    }
                });
                RequestQueue requestQueue;
                requestQueue = Volley.newRequestQueue(ProfilePage.this);
                requestQueue.add(postMyWorkoutRequest);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(ProfilePage.this, HomePage.class);
                intent2.putExtra("username", intent.getStringExtra(LoginPage.USERNAME));
                startActivity(intent2);
                finish();
            }
        });
        followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(ProfilePage.this, FollowingList.class);
                intent2.putExtra("username", intent.getStringExtra(LoginPage.USERNAME));
                startActivity(intent2);
                finish();
            }
        });
        showRoutines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileInfo.removeAllViews();
                loadAllRoutines();
            }
        });
        showPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileInfo.removeAllViews();
                loadFeed();
            }
        });
        myGymChatFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usersGym = getUserGym();
                //             Toast.makeText(ProfilePage.this, usersGym, Toast.LENGTH_LONG).show();
                Intent gcIntent = new Intent(ProfilePage.this, MyGymChat.class);
                gcIntent.putExtra("username", getIntent().getStringExtra("username"));
                gcIntent.putExtra("myGym", usersGym);
                startActivity(gcIntent);
                finish();
            }
        });
        ProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfilePage.this, ProfilePictureSelect.class);
                i.putExtra("username", getIntent().getStringExtra("username"));
                startActivity(i);
                finish();
            }
        });
        addPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open up a post screen
                Intent i = new Intent(ProfilePage.this, UserPosts.class);
                i.putExtra("username", getIntent().getStringExtra("username"));
                startActivity(i);
                finish();
            }
        });
    }

    private void loadUserInfo() {
        String feedStr;
        String URL = GETUSERURL + getIntent().getStringExtra("username");

        Toast.makeText(ProfilePage.this, URL, Toast.LENGTH_LONG);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //populate profile page
                        try {
                            userName.setText(response.getString("username"));
                            pointsTxt.setText("Points: " +response.getInt("points"));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProfilePage.this, "Error getting user details", Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue rq = Volley.newRequestQueue(ProfilePage.this);
        rq.add(jsonObjectRequest);
    }
    private void loadFeed() {
        JsonArrayRequest postsGETRequest = new JsonArrayRequest(Request.Method.GET, GETPOSTSURL+ "/" + getIntent().getStringExtra("username"), null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    if (response.length() == 0) {
                        addTextCard("No posts here yet");
                    }
                    else {
                        feedStr = "";
                        for (int i = response.length()-1; i >= 0; i--) {
                            JSONObject object = response.getJSONObject(i);
                            String title = object.getString("title");
                            String message = object.getString("message");
                            int likes = object.getInt("likes");
                            feedStr = "<h2><b> <span style=color:black>"+ title + ":</span> </b></h2>"
                                    + "<p>" + message + "<p><span style=color:#505050> " +
                                    "Likes: </span><b><span style=color:#F06060>" + likes + "</span>";

                            //addCard(feedStr);
                            addPostCard(getIntent().getStringExtra("username"), response.getJSONObject(i));
                        }
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                addTextCard("Error loading feed");
                error.printStackTrace();

            }
        });


        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(ProfilePage.this);
        requestQueue.add(postsGETRequest);
    }
    private void loadAllRoutines() {
        String getAllRoutinesURL = "http://coms-309-067.class.las.iastate.edu:8080/user/" + getIntent().getStringExtra("username") + "/routines";

        //get request
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, getAllRoutinesURL, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        //find highest id
                        JSONObject cur;
                        int id;
                        String msg;

//                        msg = "<h2><b><span style=color:black>Latest Routine: </b></h2>";


                        try {
                            //print out all routines
                            for (int j = 0; j < response.length(); j++) {
                                cur = response.getJSONObject(j);
                                JSONArray workouts = cur.getJSONArray("workouts");
                                msg = "<h2><span style=color:#64000A>" + cur.getString("name") + "</span></h2>";

                                //iterate through all the workouts
                                for (int i = 0; i < workouts.length(); i++) {
                                    JSONObject exercise = workouts.getJSONObject(i);
                                    msg+= "<p><b><span style=color:black>Exercise " + (i+1) + ": </b></style><span style=color:#64000A>" + exercise.getString("name");
//                                            + "</span>\t<b><span style=color:black>\tPR: </style></b><span style=color:#64000A>" + exercise.getString("record") + " </span><b><span style=color:black> reps: </style></b><span style=color:#64000A>" + exercise.getString("reps") + "</span></p>";
                                }

                                msg += "<p><span style=color:black>-------------------------------------------------</span></p>";
                                addTextCard(msg);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProfilePage.this, "Error getting User details", Toast.LENGTH_LONG).show();
                    }
                });


        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(ProfilePage.this);
        requestQueue.add(jsonArrayRequest);

    }
    private String getUserGym() {
        String URL = GETUSERURL + getIntent().getStringExtra("username");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Toast.makeText(ProfilePage.this, response.toString(), Toast.LENGTH_LONG).show();
                            userGym = response.getJSONObject("myGym").getString("businessName");
                        } catch (JSONException e) {
                            userGym = "stateGym";
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProfilePage.this, "Error getting user details", Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue rq = Volley.newRequestQueue(ProfilePage.this);
        rq.add(jsonObjectRequest);

        return userGym;
    }
    private void addTextCard(String text){
        View view = getLayoutInflater().inflate(R.layout.simple_text_box, null);
        TextView nameView = view.findViewById(R.id.textView);

        nameView.setText(Html.fromHtml(text));

        profileInfo.addView(view);
    }
//    private void addCard(String text){
//        View view = getLayoutInflater().inflate(R.layout.simple_box, null);
//        TextView nameView = view.findViewById(R.id.textView);
//        Button btn = view.findViewById(R.id.followBTN);
//
//        nameView.setText(Html.fromHtml(text));
//        btn.setText("Like"); //ADD LIKE FEATURE
//
//
//        profileInfo.addView(view);
//    }
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

        if (name.equals("No Feed - Find friends to see their posts")) {
            View Errorview = getLayoutInflater().inflate(R.layout.simple_text_box, null);
            TextView text = findViewById(R.id.textView);
            text.setText("No Feed - Find friends to see their post");
            profileInfo.addView(Errorview);
        } else {
            try {
                usernameTxt.setText(""); //leave blank until owner of post is passed
                titleTxt.setText(post.getString("message"));
                msgTxt.setText(post.getString("title"));
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
                                                Toast.makeText(ProfilePage.this, "unliked", Toast.LENGTH_SHORT).show();
                                                //update like counter in real time to prevent reloading feed.

                                                int decrementLikes = Integer.parseInt(likesTxt.getText().toString()) - 1;
                                                String intToString = "" + decrementLikes;
                                                likesTxt.setText(intToString);
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Toast.makeText(ProfilePage.this, "Error unliking", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        RequestQueue rq;
                                        rq = Volley.newRequestQueue(ProfilePage.this);
                                        rq.add(unlikeRequest);
                                    }
                                    else {
                                        hasLiked = false;
                                        String likeURL = "http://coms-309-067.class.las.iastate.edu:8080/posts/person/" + postID + "/like/"+ getIntent().getStringExtra("username");
                                        StringRequest likeRequest = new StringRequest(Request.Method.PUT, likeURL, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                Toast.makeText(ProfilePage.this, "Liked", Toast.LENGTH_SHORT).show();
                                                //update like counter in real time to prevent reloading feed.
                                                int incrementLikes = Integer.parseInt(likesTxt.getText().toString()) + 1;
                                                String intToString = "" + incrementLikes;
                                                likesTxt.setText(intToString);
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Toast.makeText(ProfilePage.this, "Error Liked", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        RequestQueue rq;
                                        rq = Volley.newRequestQueue(ProfilePage.this);
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
                            requestQueue = Volley.newRequestQueue(ProfilePage.this);
                            requestQueue.add(stringRequest);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
                profileInfo.addView(view);

            } catch (JSONException e) {
                VolleyLog.d(TAG, "addPostCard Error");
            }
        }

    }
    private void loadProfilePic() {
        String PROFILEPICURL = GETUSERURL + getIntent().getStringExtra("username") + "/profilePicture";

        StringRequest request = new StringRequest
                (Request.Method.GET, PROFILEPICURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!response.isEmpty()) {

                            String imageUrl = PROFILEPICURL;

                            // Execute an AsyncTask to load the image from the URL
                            new LoadImageTask().execute(imageUrl);
                        } else {
                            // generate custom profile pic from URL
                            String imageUrl = "https://robohash.org/" + getIntent().getStringExtra("username");

                            // Execute an AsyncTask to load the image from the URL
                            new LoadImageTask().execute(imageUrl);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.toString());
                        Toast.makeText(ProfilePage.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue rq = Volley.newRequestQueue(ProfilePage.this);
        rq.add(request);
    }
    private class LoadImageTask extends AsyncTask<String, Void, byte[]> {

        @Override
        protected byte[] doInBackground(String... params) {
            String imageUrl = params[0];

            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();

                InputStream input = connection.getInputStream();
                ByteArrayOutputStream output = new ByteArrayOutputStream();

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = input.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }

                input.close();
                return output.toByteArray();
            } catch (Exception e) {
                Log.e("ImageLoading", "Error loading image", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(byte[] imageBytes) {
            if (imageBytes != null && imageBytes.length > 0) {
                // Convert byte array to Bitmap
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                // Set Bitmap to ImageView
                ProfilePicture.setImageBitmap(bitmap);
            } else {
                Log.e("ImageLoading", "Empty or null byte array");
            }
        }
    }
}

