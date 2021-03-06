package com.alex.bagofwords;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;


public class RegistrationActivity extends AppCompatActivity {

    UserSessionHandler userSessionHandler;
    EditText name, username, email, password, confirmPassword;
    Button register;
    TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Display alert dialog if no network connection
        if(!isNetworkAvailable(getApplicationContext())) {
            deviceWifiSettings();
        }

        name = (EditText) findViewById(R.id.input_name);
        username = (EditText) findViewById(R.id.input_username);
        email = (EditText) findViewById(R.id.input_email);
        password = (EditText) findViewById(R.id.input_password);
        confirmPassword = (EditText) findViewById(R.id.compare_input_password);
        register = (Button) findViewById(R.id.button_register);
        login = (TextView) findViewById(R.id.link_login);
        userSessionHandler = new UserSessionHandler(getApplicationContext());

        // --- Register button pressed -----
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(LoginActivity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                // Check validation before attempting to post to the database
                if (!Validation.validName(name.getText().toString())) {    // Validation for name field
                    name.setError("Invalid name");
                    name.requestFocus();
                } else if (!Validation.validUsername(username.getText().toString())) {  // Validation for user field
                    username.setError("Invalid username.\n" + "Must be between 2 & 15 characters");
                    username.requestFocus();
                } else if (!Validation.validEmail(email.getText().toString())) {    // Validation for email
                    email.setError("Invalid Email");
                    email.requestFocus();
                } else if (!Validation.validPassword(password.getText().toString())) {  // Validation for password
                    password.setError("Invalid Password");
                    password.requestFocus();
                } else if (!Validation.matchingPassword(password.getText().toString(), confirmPassword.getText().toString())) { // Validation for confirm Password
                    confirmPassword.setError("Passwords don't match");
                    confirmPassword.requestFocus();
                } else {
                        // Entered credentials hold true for all native validation
                        // Send valid data to server
                        registerUser(name.getText().toString(), username.getText().toString(), email.getText().toString(), confirmPassword.getText().toString());
                }
            }
        });

        // --- Jump to login activity ---
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    // --- Post entered credentials to user table ---
    private void registerUser(final String name, final String username, final String email, final String password) {
        final String REGISTER_URL = "http://www.bagofwords-ca400.com/webservice/RegisterUserV6.php";    // URL script for registration script
        final EditText usernameEditText= (EditText) findViewById(R.id.input_username);
        final EditText emailEditText= (EditText) findViewById(R.id.input_email);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request;
        request = new StringRequest(Request.Method.POST, REGISTER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {       // Returned response from database
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("response");
                    switch (success) {
                        case "successful":  // Successful response
                            String id = jsonObject.getString("id");
                            String score = jsonObject.getString("score");
                            userSessionHandler.establishUserSession(id, name, username, email, score, password);
                            Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                            break;
                        case "usernameAndEmailExists":  // Unsuccessful response, username and email already exist in database
                            usernameEditText.setError("Username already exists");
                            emailEditText.setError("Email already exists");
                            usernameEditText.requestFocus();
                            emailEditText.requestFocus();
                            break;

                        case "usernameExists":      // Unsuccessful response, username already exist in database
                            usernameEditText.setError("Username already exists");
                            usernameEditText.requestFocus();
                            break;

                        case "emailExists": // Unsuccessful response, email already exist in database
                            emailEditText.setError("Email already exists");
                            emailEditText.requestFocus();
                            break;

                        default:    // Error response from database
                            Toast.makeText(getApplicationContext(), "Unsuccessful Registration", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {   // Error response from database
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error connecting to database, check network connection", Toast.LENGTH_LONG).show(); // Display error response
            }
        }){
            // User entered credential to be posted
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("name", name);
                hashMap.put("username", username);
                hashMap.put("email", email);
                hashMap.put("password", password);
                return hashMap;
            }
        };
        requestQueue.add(request);      // Post data to server

    }

    // Valid if the device has network connection
    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    // Alert dialog requesting to jump to device network settings
    public void deviceWifiSettings() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("No Internet Connection");
        alertDialog.setMessage("Check network settings");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_LONG).show();
            }
        });
        alertDialog.create().show();

    }


}
