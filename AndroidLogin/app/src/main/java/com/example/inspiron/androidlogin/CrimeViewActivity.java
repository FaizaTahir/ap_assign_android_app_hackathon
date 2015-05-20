package com.example.inspiron.androidlogin;

/**
 * Created by INSPIRON on 5/14/2015.
 */

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class CrimeViewActivity extends Activity {
    private Button btnBack;
    private String jsonResult;
    private String url = AppConfig.URL_ViewCrime;
    private ListView listView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from new_activity.xml
        setContentView(R.layout.activity_crimeview);
        listView = (ListView) findViewById(R.id.ViewCrime);
        accessWebService();

        btnBack = (Button) findViewById(R.id.crime_back_button);

        btnBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                // Start NewActivity.class
                Intent myIntent1 = new Intent(CrimeViewActivity.this,MainActivity.class);
                startActivity(myIntent1);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;  }    // Async Task to access the web
    private class JsonReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);
            try {
                HttpResponse response = httpclient.execute(httppost);
                jsonResult = inputStreamToString(       response.getEntity().getContent()).toString();
            }      catch (ClientProtocolException e) {
                e.printStackTrace();
            }
            catch (IOException e) {     e.printStackTrace();    }
            return null;
        }
        private StringBuilder inputStreamToString(InputStream is) {
            String rLine = "";
            StringBuilder answer = new StringBuilder();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            try {
                while ((rLine = rd.readLine()) != null) {
                    answer.append(rLine);
                }
            }
            catch (IOException e) {     // e.printStackTrace();

                Toast.makeText(getApplicationContext(),
                        "Error..." + e.toString(), Toast.LENGTH_LONG).show();
            }
            return answer;   }
        @Override
        protected void onPostExecute(String result) {    ListDrwaer();   }  }// end async task
    public void accessWebService() {   JsonReadTask task = new JsonReadTask();   // passes values for the urls string array
        task.execute(new String[] { url });  }    // build hash set for list view
    public void ListDrwaer() {
        List<Map<String, String>> complainList = new ArrayList<Map<String, String>>();
        try{    JSONObject jsonResponse = new JSONObject(jsonResult);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("COMPLAIN");
            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String heading = jsonChildNode.optString("heading");
                String description = jsonChildNode.optString("description");
                String category = jsonChildNode.optString("category");
                String location = jsonChildNode.optString("location");


              //  String number = jsonChildNode.optString("employee_no");
                String outPut = heading + "\n" + description + "\n" + category + " - " + location;
                complainList.add(createComplainForm("Complain", outPut));    }   }
        catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error" + e.toString(),
                    Toast.LENGTH_SHORT).show();   }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, complainList,     android.R.layout.simple_list_item_1,     new String[] { "Complain" }, new int[] { android.R.id.text1 });
        listView.setAdapter(simpleAdapter);  }

    private HashMap<String, String> createComplainForm(String name, String number) {
        HashMap<String, String> complainNameNo = new HashMap<String, String>();
        complainNameNo.put(name, number);
        return complainNameNo;
    }

}