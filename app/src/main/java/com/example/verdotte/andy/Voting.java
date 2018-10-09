 package com.example.verdotte.andy;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

 public class Voting extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseDatabase database;
    DatabaseReference dataRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting);

        recyclerView=(RecyclerView)findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));


        database= FirebaseDatabase.getInstance();
        dataRef= database.getReference().child("Candidate_Details");
        dataRef.keepSynced(true);


    }

     protected void onStart(){
        super.onStart();

         FirebaseRecyclerAdapter<Model, MyViewHolder> recyclerAdapter= new FirebaseRecyclerAdapter<Model,MyViewHolder>(
                 Model.class,
                 R.layout.card_view,
                 MyViewHolder.class,
                 dataRef
         ) {
             @Override
             protected void populateViewHolder(final MyViewHolder viewHolder, final Model model, int position) {

                 final String item = getRef(position).getKey();

                             viewHolder.bindName(model.getName());
                             viewHolder.bindFaculty(model.getFaculty());
                             viewHolder.bindCountry(model.getCountry());
                             viewHolder.bindImage(getApplication(),model.imageUrl);



                 viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {

                         Intent intent= new Intent(Voting.this,VotePage.class);
                         intent.putExtra("Item",item);
                         startActivity(intent);

                     }
                 });



             }


         };

         recyclerView.setAdapter(recyclerAdapter);
    }



public static class MyViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public MyViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void bindName (String name){
            TextView txtName= mView.findViewById(R.id.name);
            txtName.setText(name);
        }

        public void bindFaculty (String faculty){
            TextView txtFaculty= mView.findViewById(R.id.faculty);
            txtFaculty.setText(faculty);
        }

        public void bindCountry (String country){
            TextView txtCountry= mView.findViewById(R.id.country);
            txtCountry.setText(country);
        }

        public void bindImage (Context ctx, String image){
            CircleImageView Image= mView.findViewById(R.id.image);
            Picasso.with(ctx).load(image).into(Image);

        }
    }



 }
