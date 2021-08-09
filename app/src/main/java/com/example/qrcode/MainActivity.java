package com.example.qrcode;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private EditText dryCough;
    private EditText fever;
    private EditText fatigue;
    private EditText headache;
    private EditText soreThroat;
    private EditText taste;
    private EditText aches;
    private EditText contact;
    private Button submitBTN;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth mAuth;
   // private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseref ;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseUsers;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());

//        Spinner spinner
//                =findViewById(R.id.course);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.courses, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
//        spinner.setOnItemSelectedListener(this);




        dryCough = findViewById(R.id.dryCough);
        fever = findViewById(R.id.fever);
        fatigue = findViewById(R.id.fatigue);
        headache = findViewById(R.id.headache);
        soreThroat = findViewById(R.id.soreThroat);
        taste = findViewById(R.id.taste);
        aches = findViewById(R.id.aches);
        contact = findViewById(R.id.contact);
        submitBTN = findViewById(R.id.submitBTN);
        mStorageRef=FirebaseStorage.getInstance().getReference();
        databaseref=FirebaseDatabase.getInstance().getReference().child("Screening");
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabaseUsers=FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
Toast.makeText(MainActivity.this,"posting ....",Toast.LENGTH_LONG).show();
                java.util.Calendar calendar = Calendar.getInstance();
                SimpleDateFormat currentData = new SimpleDateFormat("dd-MM-yyyy");
                final String saveCurrentDate = currentData.format(calendar.getTime());

                java.util.Calendar calendar1 = Calendar.getInstance();
                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");

                final String saveCurrentTime = currentTime.format(calendar1.getTime());

                final String drycough= dryCough.getText().toString().trim();
                final String Fever= fever.getText().toString().trim();
                final String Fatigue= fatigue.getText().toString().trim();
                final String Headache= headache.getText().toString().trim();
                final String Sorethroat= soreThroat.getText().toString().trim();
                final String Taste= taste.getText().toString().trim();
                final String Aches= aches.getText().toString().trim();
                final String Contact= contact.getText().toString().trim();
                if(TextUtils.isEmpty(drycough)||TextUtils.isEmpty(Fever)||TextUtils.isEmpty(Fatigue)||TextUtils.isEmpty(Headache)||TextUtils.isEmpty(Sorethroat)||TextUtils.isEmpty(Taste)||TextUtils.isEmpty(Aches)||TextUtils.isEmpty(Contact)){

                    Toast.makeText(MainActivity.this,"ALL FIELDS ARE REQUIRED",Toast.LENGTH_SHORT).show();
                }
                else{
                    final DatabaseReference newpost=databaseref.push();
                    mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            newpost.child("drycough").setValue(drycough);
                            newpost.child("fever").setValue(Fever);
                            newpost.child("fatigue").setValue(Fatigue);
                            newpost.child("headache").setValue(Headache);
                            newpost.child("soreThroat").setValue(Sorethroat);
                            newpost.child("taste").setValue(Taste);
                            newpost.child("aches").setValue(Aches);
                            newpost.child("contact").setValue(Contact);
                            newpost.child("uid").setValue(mCurrentUser.getUid());
                            newpost.child("time").setValue(saveCurrentTime);
                            newpost.child("date").setValue(saveCurrentDate);
                            newpost.child("email").setValue(snapshot.child("email").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Intent intent=new Intent(MainActivity.this,Start2Activity.class);
                                    startActivity(intent);

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull  DatabaseError error) {
                            notifyUser("Database error: " + error.toException());
                        }
                    });
                }




            }
        });
    }

    private void notifyUser(String s) {
        Toast.makeText(MainActivity.this,s,
                Toast.LENGTH_SHORT).show();
    }

}


