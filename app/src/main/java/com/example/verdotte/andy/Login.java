package com.example.verdotte.andy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    TextInputEditText email, password;
    Button Login, SignUp;
    TextView reset;
    ProgressDialog progressDialog;
    String strPassword;
    String strEmail;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        component();

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               strEmail=email.getText().toString().trim();
               strPassword=password.getText().toString().trim();


                if(strEmail.isEmpty()) {
                    email.setError("Fill this form!");
                    email.requestFocus();
                    return;
                }


                if(strPassword.isEmpty()){
                    password.setError("Fill this form!");
                    password.requestFocus();
                    return;
                }

                login(strEmail,strPassword);

            }
        });

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ResetPass.class));
            }
        });


    }

    public void component (){

        email=(TextInputEditText)findViewById(R.id.input_email);
        password=(TextInputEditText)findViewById(R.id.input_password);
        reset=(TextView)findViewById(R.id.reset_password);
        Login=(Button)findViewById(R.id.btn_signin);
        SignUp=(Button)findViewById(R.id.btn_signup);



        progressDialog = new ProgressDialog(this);
        firebaseAuth=FirebaseAuth.getInstance();
    }

    public void login (String User_email, String User_password){

        progressDialog.setMessage("Sign up...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(User_email, User_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    startActivity(new Intent(getApplicationContext(),Voting.class));

                }else{
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Error occurs", Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}
