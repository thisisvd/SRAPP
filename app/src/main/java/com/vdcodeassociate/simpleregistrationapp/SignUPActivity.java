package com.vdcodeassociate.simpleregistrationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.LocusId;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUPActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference reference;

    private TextInputLayout signUPUserNameLayout,signUPEmailLayout,signUPPasswordLayout,signUPPhoneLayout,signUPRetypePasswordLayout;
    private TextInputEditText username,email,password,rePassword,phone;
    private Button createAccountButton;
    ProgressDialog progressDialog;

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
        signUPUserNameLayout = findViewById(R.id.signUPUserNameLayout);
        signUPEmailLayout = findViewById(R.id.signUPEmailLayout);
        signUPPasswordLayout = findViewById(R.id.signUPPasswordLayout);
        signUPRetypePasswordLayout = findViewById(R.id.signUPRetypePasswordLayout);
        signUPPhoneLayout = findViewById(R.id.signUPPhoneLayout);
        username = findViewById(R.id.signUPUserName);
        email = findViewById(R.id.signUPEmail);
        password = findViewById(R.id.signUPPassword);
        rePassword = findViewById(R.id.signUPRetypePassword);
        phone = findViewById(R.id.signUPPhone);
        createAccountButton = findViewById(R.id.createAccountButton);

    }

    private void registerUser(String username,String email,String password,String phone){

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            assert user != null;
                            String userid = user.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String,String> hashMap = new HashMap<>();
                            hashMap.put("id",userid);
                            hashMap.put("username",username);

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Intent intent = new Intent(SignUPActivity.this,DashboardActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(),"Registration failed! Try Again!...",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void clickCreateAccount(View view) {
        Log.i("username",username.getText().toString().trim());
        Log.i("userEmail",email.getText().toString().trim());
        Log.i("userPassword", password.getText().toString().trim());
        Log.i("userRePassword",rePassword.getText().toString().trim());
        Log.i("userPhone",phone.getText().toString().trim());

        if (textIsEmpty()) {
            registerUser(username.getText().toString(),email.getText().toString(), password.getText().toString(), phone.getText().toString());
        }else {
            // Something went wrong...
            Log.i("ERROR :- ","SOMETHING WENT WRONG SOMEWHERE!....");
        }
    }

    private boolean textIsEmpty(){
        boolean result = true;
        String userName = username.getText().toString().trim();
        String userEmail = email.getText().toString().trim();
        String userPassword = password.getText().toString().trim();
        String userRePassword = rePassword.getText().toString().trim();
        String userPhone = phone.getText().toString().trim();

        if(userPassword.length()<6){
            signUPPasswordLayout.setError("*Password length > 6");
            result =  false;
        }
        if(userName.isEmpty()){
            signUPUserNameLayout.setError("*Required");
            result =  false;
        }
        if(userEmail.isEmpty()){
            signUPEmailLayout.setError("*Required");
            result =  false;
        }
        if(userPassword.isEmpty()){
            signUPPasswordLayout.setError("*Required");
            result =  false;
        }
        if(userRePassword.isEmpty()){
            signUPRetypePasswordLayout.setError("*Required");
            result =  false;
        }
        if(userPhone.isEmpty()){
            signUPPhoneLayout.setError("*Required");
            result =  false;
        }
        if(!userPassword.equals(userRePassword)) {
            signUPRetypePasswordLayout.setError("Password doesn't match");
            return false;
        }
        return result;
    }

    public void goToSignIn(View view){
        Intent intent = new Intent(SignUPActivity.this,SignINActivity.class);
        startActivity(intent);
    }
}