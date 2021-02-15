package com.vdcodeassociate.simpleregistrationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private TextInputLayout resetPasswordEmailLayout;
    private TextInputEditText resetPasswordEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Toolbar toolbar = findViewById(R.id.ResetPasswordToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        resetPasswordEmail = findViewById(R.id.ResetPasswordEmail);
        resetPasswordEmailLayout = findViewById(R.id.ResetPasswordEmailLayout);

    }

    public void sendRestPassword(View view){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = resetPasswordEmail.getText().toString();

        if(!emailAddress.trim().isEmpty()) {
            auth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Password Reset Email Sent!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Enter a registered email!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            Toast.makeText(getApplicationContext(), "Please fill registered email address!", Toast.LENGTH_SHORT).show();
            resetPasswordEmailLayout.setError("*Required!");
        }
    }

}