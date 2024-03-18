package com.example.gymapp23;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.Switch;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;


//BROKEN SOMEWHERE
public class PostRoutines extends AppCompatActivity {
    public static final String GETUSERURL = "http://coms-309-067.class.las.iastate.edu:8080/people/@/";
    public static final String BASE_POSTROUTINE_URL = "http://coms-309-067.class.las.iastate.edu:8080/user/"; ///user/{username}/routines
    LinearLayout layout;
    private Button workoutPG, trackRoutineBtn, addRoutine;
    private EditText routienName;
    private ArrayList<String> viewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_routines);

        layout = findViewById(R.id.containerR);
        routienName = findViewById(R.id.customName);
        workoutPG = findViewById(R.id.routineToWorkout);
        trackRoutineBtn = findViewById(R.id.trackRoutineBtn);
        addRoutine = findViewById(R.id.addRoutine);

        viewList = new ArrayList<>();

        //get request for business
        String URL = GETUSERURL + getIntent().getStringExtra("username");
        trackRoutineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                add_workouts();
                try {
                    String postROUTINES = BASE_POSTROUTINE_URL + getIntent().getStringExtra("username") + "/routines";

                    JSONObject routineJSON = new JSONObject();
                    if (!routienName.getText().toString().equals(null)) {
                        ((ViewGroup) layout).getChildCount();
//                        getLayoutInflater(layout);
                        routineJSON.put("name", routienName.getText().toString());
                    }
                    else {
                        routineJSON.put("name", getIntent().getStringExtra("username") + "'s_workout");
                    }

                    routineJSON.put("workouts", new JSONArray());

                    JsonObjectRequest postEmptyRoutineRequest = new JsonObjectRequest(Request.Method.POST, BASE_POSTROUTINE_URL + getIntent().getStringExtra("username") + "/routines", routineJSON, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Toast.makeText(PostRoutines.this, "Routine Made!", Toast.LENGTH_SHORT).show();
                                add_workouts();
                                //remove views here???
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                            Toast.makeText(PostRoutines.this, "Error Posting your Event", Toast.LENGTH_SHORT).show();
                        }
                    });

                    RequestQueue requestQueue;
                    requestQueue = Volley.newRequestQueue(PostRoutines.this);
                    requestQueue.add(postEmptyRoutineRequest);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        workoutPG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PostRoutines.this, PostWorkout.class);
                i.putExtra("username", getIntent().getStringExtra("username"));
                startActivity(i);
                finish();
            }
        });
        addRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                addCard();
            }
        });
    }
    private void add_workouts() {
        viewList.forEach((n) -> Log.d("PostRoutines", n));
        Log.d("PostRoutines","" + viewList.size());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest (Request.Method.GET, "http://coms-309-067.class.las.iastate.edu:8080/user/" + getIntent().getStringExtra("username") + "/routines", null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONObject newRoutine;
                        int id;

                        try {
                            newRoutine = findMaxID(response);
                            id = newRoutine.getInt("id");
                            Log.d("ID: ", "" + id);
                            viewList.forEach((n) -> Log.d("jsonArrayReq ", n));

//                            for(int i = 0; i < viewList.size(); i++){
//                                String eachV = viewList.get(i);
                            for (String eachV : viewList) {
//                            viewList.forEac(((eachV) -> {
                                Log.d("In for each: ", eachV);

                                JSONObject json = new JSONObject();
//                                Toast.makeText(PostRoutines.this, "== " + eachV, Toast.LENGTH_SHORT).show();
                                json.put("name", eachV);

                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                                        (Request.Method.POST, "http://coms-309-067.class.las.iastate.edu:8080/routine/" + id + "/workout", json, new Response.Listener<JSONObject>() {

                                            @Override
                                            public void onResponse(JSONObject response) {
//                                                Toast.makeText(PostRoutines.this, "Posted: " + eachV, Toast.LENGTH_SHORT).show();
                                                viewList.remove(eachV);
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
//                                                Toast.makeText(PostRoutines.this, "Error Posting Workout details", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                RequestQueue rq;
                                rq = Volley.newRequestQueue(PostRoutines.this);
                                rq.add(jsonObjectRequest);

                            }
                        } catch (Exception e) {
//                            Toast.makeText(PostRoutines.this, "Error ForEach", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(PostRoutines.this, "Error getting User details", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(PostRoutines.this);
        requestQueue.add(jsonArrayRequest);

//        layout.removeAllViewsInLayout();
//        viewList.clear();

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
    private void addCard(){
        View view = getLayoutInflater().inflate(R.layout.workout_box, null);
        TextView nameView = view.findViewById(R.id.textView);
        EditText workout = view.findViewById(R.id.workout);
        EditText weight = view.findViewById(R.id.weight);
        EditText sets = view.findViewById(R.id.sets);
        EditText reps = view.findViewById(R.id.reps);
        Button btn = view.findViewById(R.id.btn);
        Switch s = view.findViewById(R.id.switchWorkout);

        ((ViewGroup) (nameView).getParent()).removeView(nameView);
        ((ViewGroup) (weight).getParent()).removeView(weight);
        ((ViewGroup) (sets).getParent()).removeView(sets);
        ((ViewGroup) (reps).getParent()).removeView(reps);
        ((ViewGroup) (s).getParent()).removeView(s);
        workout.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("TEXT CHANGE ", "> " + workout.getText().toString());
                viewList.remove(workout.getText().toString());
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("TEXT CHANGE ", "< " + workout.getText().toString());
                viewList.add(workout.getText().toString());
            }
        });
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("REMOVE ", workout.getText().toString());
                viewList.remove(workout.getText().toString());//workout.getText().toString());
                layout.removeView(view);
            }
        });

        layout.addView(view);
    }
}