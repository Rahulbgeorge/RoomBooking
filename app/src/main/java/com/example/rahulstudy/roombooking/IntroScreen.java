package com.example.rahulstudy.roombooking;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class IntroScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_screen);

    }

    public void login(View view)
    {
        Intent loginScreen=new Intent(this,MainActivity.class);
        startActivity(loginScreen);
    }

    public void noLogin(View view)
    {
        Intent homeScreen=new Intent(this,MainActivity.class);
        startActivity(homeScreen);
    }
}
