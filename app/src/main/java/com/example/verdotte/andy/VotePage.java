package com.example.verdotte.andy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class VotePage extends AppCompatActivity {

    CircleImageView circleImageView;
    TextView tvName, tvFaculty, tvCountry;
    Button btn_vote;
    ProgressDialog progressDialog;


    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference DataRef;
    DatabaseReference DaRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_page);

        circleImageView=(CircleImageView)findViewById(R.id.candidate_img);
        tvName=(TextView)findViewById(R.id.candidate_name);
        tvFaculty=(TextView)findViewById(R.id.candidate_faculty);
        tvCountry=(TextView)findViewById(R.id.candidate_country);
        btn_vote=(Button)findViewById(R.id.vote);


        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        progressDialog= new ProgressDialog(this);


        final String user_key = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DaRef= FirebaseDatabase.getInstance().getReference().child("Student_Details").child(user_key);

        final String data= getIntent().getExtras().get("Item").toString();
        DataRef =database.getReference().child("Candidate_Details").child(data);

        loading();

       btn_vote.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

            progressDialog.setMessage("voting...");
            progressDialog.show();

             ValueEventListener value=new ValueEventListener() {
                 @Override
                 public void onDataChange(DataSnapshot dataSnapshot) {

                  if(dataSnapshot.hasChild("state")){

                      progressDialog.dismiss();
                      Toast.makeText(getApplicationContext(),"Voted already",Toast.LENGTH_SHORT).show();

                  }else {

                      DataRef.child("votes").push().setValue(user_key).addOnCompleteListener(new OnCompleteListener<Void>() {
                          @Override
                          public void onComplete(@NonNull Task<Void> task) {

                              if (task.isSuccessful()){
                                  DaRef.child("state").setValue("voted");
                                  progressDialog.dismiss();
                                  Toast.makeText(getApplicationContext(),"Successfully voted",Toast.LENGTH_LONG).show();
                                  logout();
                              }

                          }
                      });
                  }

                 }

                 @Override
                 public void onCancelled(DatabaseError databaseError) {

                 }
             };

             DaRef.addValueEventListener(value);

           }
       });

    }

    public void loading () {

        DataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                tvName.setText(name);

                String Image = dataSnapshot.child("imageUrl").getValue().toString();
                Picasso.with(getApplicationContext()).load(Image).into(circleImageView);

                String faculty = dataSnapshot.child("faculty").getValue().toString();
                tvFaculty.setText(faculty);

                String country = dataSnapshot.child("country").getValue().toString();
                tvCountry.setText(country);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void logout (){
        auth.signOut();
        startActivity(new Intent(getApplicationContext(),Login.class));
    }
}
