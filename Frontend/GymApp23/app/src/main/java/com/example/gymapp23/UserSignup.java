package com.example.gymapp23;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class UserSignup extends AppCompatActivity {

    private boolean flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_signup);

        EditText usernameETxt = (EditText) findViewById(R.id.usernameETxt);
        EditText passwordETxt = (EditText) findViewById(R.id.passwordETxt);
        EditText homeGymEtx = findViewById(R.id.homeGymETxt);

        Button loginPageBtn = (Button) findViewById(R.id.signupToLoginPageBtn);
        Button createUserBtn = (Button) findViewById(R.id.createAccountBtn);
        //final String URL = "https://dbfca566-da98-43bd-8c06-e71b77d0240d.mock.pstmn.io/people/create";
        //final String URL = "https://dbfca566-da98-43bd-8c06-e71b77d0240d.mock.pstmn.io/people/create?username&password";
        final String URL = "http://coms-309-067.class.las.iastate.edu:8080/people"; //kyle endpoint
        //final String URL = "http://coms-309-067.class.las.iastate.edu:8080/persons"; //joe endpoint

        createUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestQueue requestQueue;
                requestQueue = Volley.newRequestQueue(UserSignup.this);
                flag = true;
                    String getURL = URL + "/@/" + usernameETxt.getText().toString();

                    JsonObjectRequest getUserRequest = new JsonObjectRequest(Request.Method.GET, getURL, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(UserSignup.this,"Account already exists", Toast.LENGTH_LONG).show();
                            flag = false;
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            try {
                                //if there was an error, user is not in system and we post the user
                                flag = true;

                                //post request
                                JSONObject json = new JSONObject();
                                json.put("username", usernameETxt.getText().toString());
                                json.put("password", passwordETxt.getText().toString());
                                //json.put("myGym", homeGymEtx.getText().toString());
                                //json.put("name", usernameETxt.getText().toString());


                                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, json, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            if (flag) {

                                                String postMyWorkout = "http://coms-309-067.class.las.iastate.edu:8080/people/@/" + getIntent().getStringExtra("username") + "/setHome/stateGym";
                                                StringRequest postMyWorkoutRequest = new StringRequest(Request.Method.POST, postMyWorkout, new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        Toast.makeText(UserSignup.this, "myGym posted", Toast.LENGTH_LONG).show();
                                                    }
                                                    }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Toast.makeText(UserSignup.this, "error posting myGym", Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                                RequestQueue requestQueue;
                                                requestQueue = Volley.newRequestQueue(UserSignup.this);
                                                requestQueue.add(postMyWorkoutRequest);

                                                Toast.makeText(UserSignup.this, response.getString("Account Created"),
                                                        Toast.LENGTH_LONG).show();
                                                passwordETxt.setText(response.toString());
                                            }
                                        }
                                        catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(UserSignup.this, error.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });


                                requestQueue.add(request);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    });


                    requestQueue.add(getUserRequest);




            }
        });

        loginPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSignup.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //hello


    }
}