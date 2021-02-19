package com.vdcodeassociate.simpleregistrationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.LocusId;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class SignUPActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;
    private TextInputEditText username,email,password,rePassword,phone;
    private Button createAccountButton;
    ProgressDialog progressDialog;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_u_p);

        firebaseAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.signUPToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressDialog = new ProgressDialog(this);
        username = findViewById(R.id.signUPUserName);
        email = findViewById(R.id.signUPEmail);
        password = findViewById(R.id.signUPPassword);
        rePassword = findViewById(R.id.signUPRetypePassword);
        phone = findViewById(R.id.signUPPhone);
        createAccountButton = findViewById(R.id.createAccountButton);

    }

    private void registerUser(String username,String email,String password,String phone){
        progressDialog.setMessage("Please wait!");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    String userID = firebaseUser.getUid();

                    firebaseDatabase = FirebaseDatabase.getInstance();
                    reference = FirebaseDatabase.getInstance().getReference("Users");

                    User user = new User(username,email,phone);

                    reference.child(userID).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
//                                Toast.makeText(getApplicationContext(),"Registration successful!...",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUPActivity.this,DashboardActivity.class);
                                intent.putExtra("phone",SignUPActivity.this.phone.getText().toString().trim());
                                startActivity(intent);
                            }
                        }
                    });
                }else {
                    Toast.makeText(getApplicationContext(),"Email already in use! Try again with different email...",Toast.LENGTH_SHORT).show();
                    progressDialog.cancel();
                }
            }
        });
    }

    public void clickCreateAccount(View view) {

        if(!isInternetConnected(this)){
            showDialog();
        }else {

            if (!textIsEmpty()) {
                registerUser(username.getText().toString(), email.getText().toString(), password.getText().toString(), phone.getText().toString());
            } else {
                // Something went wrong...
                Log.i("ERROR :- ", "SOMETHING WENT WRONG SOMEWHERE!....");
            }
        }
    }

    //check internet connection ----------
    private boolean isInternetConnected(SignUPActivity signINActivity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) signINActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if(wifi != null && wifi.isConnected() || mobile != null && mobile.isConnected()) {
            return true;
        }else {
            return false;
        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUPActivity.this);
        builder.setMessage("Please connect to the internet to proceed further!")
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.show();
    }

    private boolean textIsEmpty(){
        boolean result = false;
        String userName = username.getText().toString().trim();
        String userEmail = email.getText().toString().trim();
        String userPassword = password.getText().toString().trim();
        String userRePassword = rePassword.getText().toString().trim();
        String userPhone = phone.getText().toString().trim();

        if(userName.isEmpty() || userEmail.isEmpty() || userPassword.isEmpty() || userRePassword.isEmpty() || userPhone.isEmpty()){
            Toast.makeText(getApplicationContext(),"All fields are required!",Toast.LENGTH_SHORT).show();
            result = true;
        }
        if(!userPassword.equals(userRePassword)) {
            Toast.makeText(getApplicationContext(),"Password doesn't match!",Toast.LENGTH_SHORT).show();
            result = true;
        }
        if(password.length() < 6){
            Toast.makeText(getApplicationContext(),"Password length shouldn't smaller than 6!",Toast.LENGTH_SHORT).show();
            result = true;
        }
        return result;
    }

    public void goToSignIn(View view){
        Intent intent = new Intent(SignUPActivity.this,SignINActivity.class);
        startActivity(intent);
    }
}