package com.vdcodeassociate.simpleregistrationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onStart() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            finish();
            startActivity(new Intent(MainActivity.this,DashboardActivity.class));
        }
        super.onStart();
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