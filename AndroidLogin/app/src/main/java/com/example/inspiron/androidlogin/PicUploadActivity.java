package com.example.inspiron.androidlogin;

/**
 * Created by INSPIRON on 5/14/2015.
 */


import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;

public class PicUploadActivity extends Activity {
    private Button btnBack;
    private Button btnNext;
    private Button btnpicupload;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from new_activity.xml
        setContentView(R.layout.activity_picupload);
        btnBack = (Button) findViewById(R.id.picupload_back_button);
        btnNext = (Button) findViewById(R.id.picupload_button_next);
        btnpicupload = (Button) findViewById(R.id.picupload_button);

        btnBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                // Start NewActivity.class
                Intent myIntent1 = new Intent( PicUploadActivity.this,
                        MainActivity.class);
                startActivity(myIntent1);
            }
        });


        btnNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                // Start NewActivity.class
                Intent myIntent2 = new Intent( PicUploadActivity.this,
                        ComplainFormActivity.class);
                startActivity(myIntent2);
            }
        });



        btnpicupload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                // Start NewActivity.class
                Intent myIntent2 = new Intent( PicUploadActivity.this,
                        UploadAndViewPicture.class);
                startActivity(myIntent2);
            }
        });




    }
}