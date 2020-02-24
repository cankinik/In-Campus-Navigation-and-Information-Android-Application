package com.example.can.bilmapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.FirebaseApp;

public class EventActivity extends AppCompatActivity {

    //Properties
    private Button eventButton;
    private Button ge250Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        eventButton = (Button) findViewById(R.id.eventButton);
        eventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPasswordActivity();
            }
        });

        ge250Button = (Button) findViewById(R.id.ge250Button);
        ge250Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGE250Activity();
            }
        });
    }

    /* Go to the course page... You will see added courses
     */

    /* Go to the events page... You will see and add events
     */
    public void openPasswordActivity(){
        Intent intent = new Intent(this, EventPassword.class);
        startActivity(intent);
        finish();
    }

    /* Go to the current Ge250 events page...
     */
    public void openGE250Activity(){
        Intent intent = new Intent(this, CurrentEvents.class);
        startActivity(intent);
        finish();
    }
}
