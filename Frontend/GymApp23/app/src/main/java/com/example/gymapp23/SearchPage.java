package com.example.gymapp23;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gymapp23.databinding.ActivityProfilePageBinding;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gymapp23.databinding.ActivitySearchPageBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class SearchPage extends AppCompatActivity {
    AlertDialog dialog;
    Button search;
    LinearLayout layout;
    TextView baselineText;
    EditText searchName;
    private String TAG = MainActivity.class.getSimpleName();
    String URL = "http://coms-309-067.class.las.iastate.edu:8080/people";
    String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);

        Intent intent = getIntent();
        userName = intent.getStringExtra("username");

        search = findViewById(R.id.searchBTN);
        searchName = findViewById(R.id.searchName);
        layout = findViewById(R.id.container);
        baselineText = findViewById(R.id.baselineText);
        FloatingActionButton home = findViewById(R.id.homePage);
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(SearchPage.this);

        JsonArrayRequest recomendedFriends = new JsonArrayRequest(URL+ "/@/" +userName+ "/recommend/1", new Response.Listener<JSONArray>() {
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
        requestQueue.add(recomendedFriends);



        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(SearchPage.this, HomePage.class);
                intent2.putExtra("username", userName);
                startActivity(intent2);
                finish();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                layout.removeAllViews();
                String serverUrl = URL + "/@/" + userName + "/search/" + (searchName.getText().toString()) ;
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
//                RequestQueue requestQueue;
//                requestQueue = Volley.newRequestQueue(SearchPage.this);
                requestQueue.add(jsonArrayRequest);
            }

        });
    }

    private void addCard(String name){
        View view = getLayoutInflater().inflate(R.layout.simple_box, null);
        TextView nameView = view.findViewById(R.id.textView);
        Button follow = view.findViewById(R.id.followBTN);

        nameView.setText(name);
        follow.setText("View");

        follow.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {
               try {
                   Intent tmpPfp = new Intent(SearchPage.this, FriendsProfile.class);
                   tmpPfp.putExtra("username", userName);
                   tmpPfp.putExtra("friend", name);
                   String followerURL = URL +"/@/"+ userName + "/friend/" + name;
                   startActivity(tmpPfp);
                   finish();
//                   Toast.makeText(SearchPage.this, name, Toast.LENGTH_LONG).show();

               } catch (Exception e){
                   e.printStackTrace();
               }

           }
        });

        layout.addView(view);
    }
}