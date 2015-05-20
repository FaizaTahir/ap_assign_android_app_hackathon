package com.example.inspiron.androidlogin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by INSPIRON on 5/15/2015.
 */
public class ComplainFormActivity extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Spinner category1;
    private Button btnsubmit;
    private EditText heading1;
    private EditText description1;
    private EditText location1;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from new_activity.xml
        setContentView(R.layout.activity_complainform);


        heading1 = (EditText) findViewById(R.id.heading);
        description1 = (EditText) findViewById(R.id.description);
        location1 = (EditText) findViewById(R.id.location);
        category1 = (Spinner) findViewById(R.id.spinner_crime);
        btnsubmit = (Button) findViewById(R.id.btnsubmit);





        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());


        // Check if user is already logged in or not
        if (!session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(ComplainFormActivity.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }

       btnsubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                String heading = heading1.getText().toString();
                String description = description1.getText().toString();
                String location = location1.getText().toString();
                String category = category1.getSelectedItem().toString();

                if (!heading.isEmpty() && !description.isEmpty()  && !category.isEmpty()&& !location.isEmpty() ) {
                    RegisterComplain(heading, description, location, category);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }


                // Start NewActivity.class
                Intent myIntent1 = new Intent(ComplainFormActivity.this,
                        MainActivity.class);
                startActivity(myIntent1);
            }
        });


    }


    private void RegisterComplain(final String heading, final String description,
                             final String category,final String location) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String uid = jObj.getString("uid");

                        JSONObject complain = jObj.getJSONObject("complain");
                        String heading2 = complain.getString("heading");
                        String description2 = complain.getString("description");
                        String location2 = complain.getString("location");
                        String category2 = complain.getString("category");
                        String created_at = complain
                                .getString("created_at");

                        // Inserting row in users table
                        db.addComplain(heading2, description2,category2,location2, uid, created_at);

                        // Launch login activity
                        Intent intent = new Intent(
                                ComplainFormActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "complain");
                params.put("heading", heading);
                params.put("description", description);
                params.put("category", category);
                params.put("location", location);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    }
