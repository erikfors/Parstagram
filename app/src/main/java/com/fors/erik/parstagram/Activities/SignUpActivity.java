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
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    //TAGS
    public static final String TAG = "SignUpActivity";

    //widget declaration
    EditText etUserName;
    EditText etPassword;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //finding views by id
        etUserName = findViewById(R.id.etUsernameSUP);
        etPassword = findViewById(R.id.etPasswordSUP);
        btnSignUp = findViewById(R.id.btnSignUp);

        //listeners

        //btnSign up
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUserName.getText().toString();
                String password = etPassword.getText().toString();
                signUPUser(username,password);
            }
        });
    }

    //sign up the user
    private void signUPUser(String username, String password) {
        // Create the ParseUser
        ParseUser user = new ParseUser();
        // Set core properties
        user.setUsername(username);
        user.setPassword(password);
        // Invoke signUpInBackground
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {

                // Sign up didn't succeed. Look at the ParseException
                // to figure out what went wrong
                if (e != null) {
                    Log.e(TAG,"Issue signing in!",e);
                    Toast.makeText(SignUpActivity.this,"Issue with sign in!",Toast.LENGTH_SHORT).show();
                    return;
                }

                //sign in is a success
                //if succeed
                goToMainActivity();
                //tell user log in was successfull
                Toast.makeText(SignUpActivity.this,"Success!",Toast.LENGTH_SHORT).show();
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