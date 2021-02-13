package com.vdcodeassociate.simpleregistrationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToSignIN(View view){
        Intent intent = new Intent(MainActivity.this,SignINActivity.class);
        startActivity(intent);
    }

    public void goToSignUP(View view){
        Intent intent = new Intent(MainActivity.this,SignUPActivity.class);
        startActivity(intent);
    }
}