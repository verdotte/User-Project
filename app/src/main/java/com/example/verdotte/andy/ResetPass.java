package com.example.verdotte.andy;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPass extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    TextInputEditText email;
    Button button;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);

        email=(TextInputEditText)findViewById(R.id.input_email);
        button=(Button)findViewById(R.id.btn_reset);
        progressDialog = new ProgressDialog(this);

        firebaseAuth=FirebaseAuth.getInstance();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String StrEamil = email.getText().toString().trim();

                if(StrEamil.isEmpty()){
                    email.setError("Fill!");
                    email.requestFocus();
                    return;
                }

                progressDialog.setMessage("We are sending you a link in your Email");
                progressDialog.show();

                firebaseAuth.sendPasswordResetEmail(StrEamil).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Verify the link in your Email", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(),"Error occurs", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}
