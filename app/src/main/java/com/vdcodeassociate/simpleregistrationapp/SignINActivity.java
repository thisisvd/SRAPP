package com.vdcodeassociate.simpleregistrationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignINActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private TextInputLayout emailLayout,signInPasswordLayout;
    private TextInputEditText email,password;
    private Button signINButton,forgotPasswordButton;
    private TextView signUPTextView;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_i_n);

        firebaseAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.signInToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressDialog = new ProgressDialog(this);
        emailLayout = findViewById(R.id.EmailLayout);
        signInPasswordLayout = findViewById(R.id.signInPasswordLayout);
        email = findViewById(R.id.signInEmail);
        password = findViewById(R.id.password_SignIn_Page);
        signINButton = findViewById(R.id.signInButton);
        forgotPasswordButton = findViewById(R.id.forgotPasswordSignIn);
        signUPTextView = findViewById(R.id.signUnPageButton);

    }

    private void validate(String name,String password){
        progressDialog.setMessage("Loading......");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(name, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();  // Shutting down progress bar -----
                    // Sign in success, update UI with the signed-in user's information
                    finish();
                    startActivity(new Intent(SignINActivity.this,DashboardActivity.class));
                    Toast.makeText(SignINActivity.this, "Login Successful!...", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignINActivity.this, "Login Failed!...", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void clickLoginButton(View view) {
        if (textIsEmpty()) {
            validate(email.getText().toString(), password.getText().toString());
        }else {
            // Something went wrong...
            Log.i("ERROR :- ","SOMETHING WENT WRONG SOMEWHERE!....");
        }
    }

    private boolean textIsEmpty(){
        boolean result = false;
        String userEmail = email.getText().toString().trim();
        String userPassword = password.getText().toString().trim();

        if(userEmail.isEmpty() && userPassword.isEmpty()){
            emailLayout.setError("Email can't be empty!");
            signInPasswordLayout.setError("Password can't be empty!");
        }else if(userEmail.isEmpty() ){
            emailLayout.setError("Email can't be empty!");
        }else if(userPassword.isEmpty()){
            signInPasswordLayout.setError("Password can't be empty!");
        }else {
            result = true;
        }
        return result;
    }

    public void goToSignUP(View view){
        Intent intent = new Intent(SignINActivity.this,SignUPActivity.class);
        startActivity(intent);
    }
}