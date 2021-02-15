package com.vdcodeassociate.simpleregistrationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.vdcodeassociate.simpleregistrationapp.R.*;

public class DashboardActivity extends AppCompatActivity {

    LinearLayout linearLayout,profileButton;
    private FirebaseAuth firebaseAuth;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_dashboard);

        firebaseAuth = FirebaseAuth.getInstance();

        linearLayout = findViewById(R.id.linearLayoutDashBoard);
        profileButton = findViewById(R.id.CardLinearLayout1);

        Toolbar toolbar = findViewById(R.id.DashboardToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Dashboard");

        checkEmailVerification();

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this,MyProfileActivity.class);
                startActivity(intent);
            }
        });

        //dialog
        dialog = new Dialog(DashboardActivity.this);
        dialog.setContentView(R.layout.email_verification_dialog);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background_drawable));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        Button sendButton = dialog.findViewById(R.id.sendEmailButton);
        TextView skipButton = dialog.findViewById(R.id.SkipTextView2);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmailVerification();
                dialog.dismiss();
            }
        });
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void checkEmailVerification(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){
            if(!firebaseUser.isEmailVerified()){
                Snackbar.make(linearLayout,"Email not verified!",Snackbar.LENGTH_LONG)
                        .setAction("Click to verify", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.show();
                            }
                        }).setActionTextColor(getResources().getColor(color.custom_red))
                        .show();
            }
        }
    }

    private void sendEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Verification mail sent!Re-login after verify...",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplicationContext()," Verification mail hasn't been sent!..!......",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case id.logout_user:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(),SignINActivity.class);
                startActivity(intent);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}