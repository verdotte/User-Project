package com.example.verdotte.andy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    TextInputEditText email, password, registration_number;
    Button SignUp;
    ProgressDialog progressDialog;
    String object;

    FirebaseAuth firebaseAuth;
    DatabaseReference Ref, databaseReference;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        component ();

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               final String strEmail=email.getText().toString().trim();
               final String strPassword=password.getText().toString().trim();
               final String strRegistration=registration_number.getText().toString().trim();

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

                if(strRegistration.isEmpty()){
                    registration_number.setError("Fill this form!");
                    registration_number.requestFocus();
                    return;
                }


                progressDialog.setMessage("Signing up...");
                progressDialog.show();

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for(final DataSnapshot snapshot: dataSnapshot.getChildren()){

                         object = snapshot.child("Reg").getValue().toString();

                            if(strRegistration.equalsIgnoreCase(object)){
                                register(strEmail,strPassword,strRegistration);
                            }




                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });

    }

    public void component (){

        email=(TextInputEditText)findViewById(R.id.input_email);
        password=(TextInputEditText)findViewById(R.id.input_password);
        registration_number=(TextInputEditText)findViewById(R.id.Registration_Number);
        SignUp=(Button) findViewById(R.id.Sign_up);
        progressDialog = new ProgressDialog(this);

        firebaseAuth= FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("registration_Number");

    }

    public void register(final String user_email, final String user_password, final String user_registrationNum ){

        progressDialog.setMessage("Signing up...");
        progressDialog.show();


        firebaseAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                String user = firebaseAuth.getCurrentUser().getUid();
                Ref= database.getReference().child("Student_Details").child(user);

                if (task.isSuccessful()){

                    HashMap<String, String> data= new HashMap<>();
                    data.put("Email", user_email);
                    data.put("Password", user_password);
                    data.put("Registration_Number", user_registrationNum);

                    Ref.setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                           startActivity(new Intent(getApplicationContext(),Login.class));
                        }
                    });
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"you are not", Toast.LENGTH_LONG).show();
                }

            }
        });




    }

    public void ResetField (){
        email.setText("");
        password.setText("");
        registration_number.setText("");
    }
}
