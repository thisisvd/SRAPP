package com.vdcodeassociate.simpleregistrationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyProfileActivity extends AppCompatActivity {

    private TextView profileName,profileEmail,profilePhone,emailVerified;
    private Button resetPassword;
    private FirebaseUser firebaseUser;
    Dialog dialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        Toolbar toolbar = findViewById(R.id.MyProfileToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Information Loading!");
        progressDialog.show();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        profileName = findViewById(R.id.ProfileName);
        profileEmail = findViewById(R.id.ProfileEmail);
        profilePhone = findViewById(R.id.ProfilePhone);
        emailVerified = findViewById(R.id.ProfileEmailVerified);
        resetPassword = findViewById(R.id.resetPassword);

        checkEmailVerification();
        loadData();

        //restPage
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"Reset Password!",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MyProfileActivity.this,ResetPassword.class);
                startActivity(intent);
            }
        });

        //dialog
        dialog = new Dialog(MyProfileActivity.this);
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

    private void loadData(){
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                User user = dataSnapshot.getValue(User.class);
                profileName.setText(user.getUsername());
                profileEmail.setText(user.getEmail());
                profilePhone.setText(user.getPhone());
//                Toast.makeText(getApplicationContext(), "Successfully loaded data", Toast.LENGTH_SHORT).show();
                progressDialog.cancel();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error in getting data!", Toast.LENGTH_SHORT).show();
                progressDialog.cancel();
            }
        };
        reference.addValueEventListener(postListener);
//        reference.child("Users").child(firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (!task.isSuccessful()) {
//                    Toast.makeText(getApplicationContext(), "Error getting data", Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    Toast.makeText(getApplicationContext(), "Successfully loaded data", Toast.LENGTH_SHORT).show();
//
//                }
//            }
//        });
    }

    public void emailCheckMethod(View view){
        if(firebaseUser != null){
            if(!firebaseUser.isEmailVerified()){
               dialog.show();
            }
        }
    }

    private void checkEmailVerification(){
        if(firebaseUser != null){
            if(firebaseUser.isEmailVerified()){
                emailVerified.setText("*Email is verified.");
                emailVerified.setTextColor(getColor(R.color.custom_green));
            }
        }
    }

}