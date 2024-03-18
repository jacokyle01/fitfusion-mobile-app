package com.example.gymapp23;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import com.android.volley.toolbox.StringRequest;
//import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Locale;

public class PostWorkout extends AppCompatActivity {
    LinearLayout layout;
    private Button homeBtn, trackWorkoutBtn, routine;
    private Dictionary dict= new Hashtable<String, RSW>();
    private ArrayList<String> workoutViewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_workout);

        homeBtn = findViewById(R.id.workoutsToHomeBtn);
        trackWorkoutBtn = findViewById(R.id.trackWorkoutBtn);
        layout = findViewById(R.id.container);
        routine = findViewById(R.id.routine);
        workoutViewList = new ArrayList<>();
        //load user data

        int year = Integer.parseInt( new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date()) );
        int month = Integer.parseInt( new SimpleDateFormat("MM", Locale.getDefault()).format(new Date()) );
        int day = Integer.parseInt( new SimpleDateFormat("dd", Locale.getDefault()).format(new Date()) );


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, "http://coms-309-067.class.las.iastate.edu:8080/people/@/" + getIntent().getStringExtra("username") + "/workouts", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try{
                    for(int i = 0; i < response.length(); i++){
                        try{
                            if(response.getString(i) != ""){
                                addCard(response.getString(i));
                            }
                        }catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }catch (Error e){
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(PostWorkout.this, "Error getting User details", Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(PostWorkout.this);
        requestQueue.add(jsonArrayRequest);

        trackWorkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://coms-309-067.class.las.iastate.edu:8080/wsessions/" + getIntent().getStringExtra("username")+"/"+ year +"/"+ month +"/"+ day +"/new", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        for(String name : workoutViewList){
                            try {
                                JSONObject json = new JSONObject();
                                json.put("name",    name);
                                json.put("sets",    ((RSW)(dict.get(name))).set);
                                json.put("reps",    ((RSW)(dict.get(name))).rep);
                                json.put("weight",  ((RSW)(dict.get(name))).weight);
                                Log.d("SRW",
                                           "sets" + ((RSW)(dict.get(name))).set +
                                                " reps" + ((RSW)(dict.get(name))).rep +
                                                "weight" + ((RSW)(dict.get(name))).weight
                                        );


                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                                        (Request.Method.POST, "http://coms-309-067.class.las.iastate.edu:8080/wsessions/" +  getIntent().getStringExtra("username")+"/"+ year +"/"+ month +"/"+ day, json, new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                Log.d("Posting workout", ""+name);
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
//                                                Toast.makeText(PostWorkout.this, "Error Posting Workout details", Toast.LENGTH_LONG).show();
                                            }
                                        });

                                RequestQueue rq;
                                rq = Volley.newRequestQueue(PostWorkout.this);
                                rq.add(jsonObjectRequest);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                RequestQueue requestQueue;
                requestQueue = Volley.newRequestQueue(PostWorkout.this);
                requestQueue.add(stringRequest);
            }
        });
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PostWorkout.this, HomePage.class);
                i.putExtra("username", getIntent().getStringExtra("username"));
                startActivity(i);
                finish();
            }
        });
        routine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PostWorkout.this, PostRoutines.class);
                i.putExtra("username", getIntent().getStringExtra("username"));
                startActivity(i);
                finish();
            }
        });
    }

    private static JSONObject findMaxID(JSONArray arr) {
        int maxID, curID, index = 0;
        try {
            JSONObject max = arr.getJSONObject(0);
            maxID = max.getInt("id");
            for (int i = 1; i < arr.length(); i++) {
                curID = arr.getJSONObject(i).getInt("id");
                if (curID > maxID) {
                    maxID = curID;
                    index = i;
                }
            }

            return arr.getJSONObject(index);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }
    private void addCard(String name){
        View view = getLayoutInflater().inflate(R.layout.workout_box, null);
        TextView nameView = view.findViewById(R.id.textView);
        nameView.setText(name);
        EditText workout = view.findViewById(R.id.workout);
        EditText weight = view.findViewById(R.id.weight);
        EditText sets = view.findViewById(R.id.sets);
        EditText reps = view.findViewById(R.id.reps);
        Button btn = view.findViewById(R.id.btn);
        Switch s = view.findViewById(R.id.switchWorkout);
        //workoutViewList

        dict.put(name, new RSW());

        ((ViewGroup) (workout).getParent()).removeView(workout);
        ((ViewGroup) (btn).getParent()).removeView(btn);
        reps.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(reps.getText().toString() != null && !(reps.getText().toString()).isEmpty()) {
                    Log.d("REPS", ">" + ((RSW) (dict.get(name))).rep);
                    ((RSW) (dict.get(name))).setRep(0);
                }else { Log.d("REPS", "> null"); }
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                if(reps.getText().toString() != null && !(reps.getText().toString()).isEmpty()){
                    Log.d("REPS", ">" + ((RSW) (dict.get(name))).rep);
                    ((RSW) (dict.get(name))).setRep( Integer.parseInt(reps.getText().toString()));
                }else { Log.d("REPS", "> null"); }
            }
        });
        sets.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(sets.getText().toString() != null && !(sets.getText().toString()).isEmpty()) {
                    Log.d("SETS", ">" + ((RSW) (dict.get(name))).set);
                    ((RSW) (dict.get(name))).setSet(0);
                }else { Log.d("SETS", "> null"); }
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                if(sets.getText().toString() != null && !(sets.getText().toString()).isEmpty()){
                    Log.d("SETS", ">" + ((RSW) (dict.get(name))).set);
                    ((RSW) (dict.get(name))).setSet( Integer.parseInt(sets.getText().toString()));
                }else { Log.d("SETS", "> null"); }
            }
        });
        weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(weight.getText().toString() != null && !(weight.getText().toString()).isEmpty()) {
                    Log.d("WEIGHT", ">" + ((RSW) (dict.get(name))).weight);
                    ((RSW) (dict.get(name))).setWeight(0);
                }else { Log.d("WEIGHT", "> null"); }
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                if(weight.getText().toString() != null && !(weight.getText().toString()).isEmpty()){
                    Log.d("WEIGHT", ">" + ((RSW) (dict.get(name))).weight);
                    ((RSW) (dict.get(name))).setWeight( Integer.parseInt(weight.getText().toString()));
                }else { Log.d("WEIGHT", "> null"); }
            }
        });
//        reps.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean b) {
//                if(b){
//                    if(reps.getText().toString() != null && !(reps.getText().toString()).isEmpty()){
//                        ((RSW) (dict.get(name))).setRep(0);
////                        Toast.makeText(PostWorkout.this, "> ", Toast.LENGTH_LONG).show();
//                    }
//                }
//                if(!b){
//                    if(reps.getText().toString() != null && !(reps.getText().toString()).isEmpty()){
//                        ((RSW) (dict.get(name))).setRep( Integer.parseInt(reps.getText().toString()));
////                        Toast.makeText(PostWorkout.this, "< " + reps.getText().toString(), Toast.LENGTH_LONG).show();
//                    }
//                }
//            }
//        });
//        sets.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean b) {
//                if(b){
//                    if(sets.getText().toString() != null && !(sets.getText().toString()).isEmpty()){
//                        ((RSW) (dict.get(name))).setSet(0);
////                        Toast.makeText(PostWorkout.this, "> " + sets.getText().toString(), Toast.LENGTH_LONG).show();
//                    }
//                }
//                if(!b){
//                    if(sets.getText().toString() != null && !(sets.getText().toString()).isEmpty()) {
//                        ((RSW) (dict.get(name))).setSet(Integer.parseInt(sets.getText().toString()));
////                        Toast.makeText(PostWorkout.this, "< " + sets.getText().toString(), Toast.LENGTH_LONG).show();
//                    }
//                }
//            }
//        });
//        weight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean b) {
//                if(b){
//                    if(workout.getText().toString() != null && !(workout.getText().toString()).isEmpty()){
//                        ((RSW) (dict.get(name))).setWeight(0);
////                        Toast.makeText(PostWorkout.this, "> " + weight.getText().toString(), Toast.LENGTH_LONG).show();
//                    }
//                }
//                if(!b){
//                    if(workout.getText().toString() != null && !(workout.getText().toString()).isEmpty()){
//                        ((RSW) (dict.get(name))).setWeight( Integer.parseInt(reps.getText().toString()));
////                        Toast.makeText(PostWorkout.this, "< " + weight.getText().toString(), Toast.LENGTH_LONG).show();
//                    }
//                }
//            }
//        });
        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(s.isChecked()){
                    if(! workoutViewList.contains( nameView.getText().toString() ) ) {
                        workoutViewList.add(nameView.getText().toString());
                    }
                }
                else{
                    if( workoutViewList.contains( nameView.getText().toString() ) ) {
                        workoutViewList.remove(nameView.getText().toString());
                    }
                }
            }
        });

        layout.addView(view);
    }
    class RSW {
        public int rep;
        public int set;
        public int weight;
        public RSW(){
            this.rep = 0;
            this.set = 0;
            this.weight = 0;
        }
        public void setRep(int i){
            rep = i;
        }
        public void setSet(int i){
            set = i;
        }
        public void setWeight(int i){
            weight = i;
        }
    }
}