package com.alex.bagofwords;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LeaderBoardActivity extends AppCompatActivity {

    Button mainMenu, settings;
    public static final String Fetch_LeaderBoard_URL = "http://www.bagofwords-ca400.com/webservice/LeaderBoardFetch.php";   // URL script for leader board script
    LeaderBoardAdapter leaderBoardAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        listView = (ListView) findViewById(R.id.listview);
        leaderBoardAdapter = new LeaderBoardAdapter(this, R.layout.row_layout);
        listView.setAdapter(leaderBoardAdapter);


        mainMenu = (Button) findViewById(R.id.mainMenu);
        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainMenuIntent = new Intent(getApplicationContext(), MainMenu.class);
                startActivity(mainMenuIntent);
                finish();
            }
        });

        settings = (Button) findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainMenuIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(mainMenuIntent);
                finish();
            }
        });


        // --- Fetching leader board data ---
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Fetch_LeaderBoard_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("leaderBoard");
                            for(int i = 0; i < jsonArray.length(); i++) {
                                JSONObject leaderBoard = jsonArray.getJSONObject(i);
                                String usernameFetch = leaderBoard.getString("username");
                                String scoreFetch = leaderBoard.getString("score");
                                int number = i + 1;
                                String numberToString = Integer.toString(number);
                                LeaderBoardUser leaderBoardUser = new LeaderBoardUser(numberToString, usernameFetch, scoreFetch);
                                leaderBoardAdapter.add(leaderBoardUser);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {      // Error response from database
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error fetch data from database, check network connection", Toast.LENGTH_LONG).show();
                    }
                });
        requestQueue.add(jsonObjectRequest);    // post the get volley method
    }

}
