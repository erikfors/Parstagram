package com.fors.erik.parstagram.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fors.erik.parstagram.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    //TAG for this activity
    public static final String TAG = "LoginActivity";

    //widget declaration
    EditText etUserName;
    EditText etPassword;
    Button btnLogin;
    TextView tbtnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //checking if someone is already log in
        if(ParseUser.getCurrentUser() != null){
            //go to main activity if user is already sign in
            goToMainActivity();
        }

        //finding views by id
        etUserName = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tbtnSignUp = findViewById(R.id.tbtnSignUp);

        //click listeners

        //login button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getting username and password
                String userName = etUserName.getText().toString();
                String password = etPassword.getText().toString();
                //trying to log in user
                loginUser(userName,password);
            }
        });

        //sign up listener
        tbtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //goes to sign up screen
                goToSignUp();
            }
        });
    }

    //goes to sign up page
    private void goToSignUp() {
        Intent i = new Intent(this, SignUpActivity.class);
        startActivity(i);
    }

    //this function will try to log in the user
    private void loginUser(String userName, String password) {
        Log.i(TAG,"Trying to log in user");

        //navigate to main activity if user sign in properly
        ParseUser.logInInBackground(userName, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {

                //encounter a problem
                if(e != null){
                    //TODO: better error handlin
                    Log.e(TAG,"Issue logging in!",e);
                    Toast.makeText(LoginActivity.this,"Issue with login!",Toast.LENGTH_SHORT).show();
                    return;
                }

                //if succeed
                goToMainActivity();
                //tell user log in was successfull
                Toast.makeText(LoginActivity.this,"Success!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    //send user to main activity
    private void goToMainActivity() {
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
        //finishing login activity
        finish();
    }

}