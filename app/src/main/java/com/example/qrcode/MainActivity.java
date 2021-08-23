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
   private FirebaseDatabase firebaseDatabase;
   Screening screening;
    private DatabaseReference databaseref ;
    //get the id of the current user
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
        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        databaseref=FirebaseDatabase.getInstance().getReference().child("screening").child(mCurrentUser.getEmail());
       // mDatabaseUsers=FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
        screening=new Screening();

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

Toast.makeText(MainActivity.this,"posting ....",Toast.LENGTH_LONG).show();

                 String drycough= dryCough.getText().toString();
                final String Fever= fever.getText().toString().trim();
                final String Fatigue= fatigue.getText().toString().trim();
                final String Headache= headache.getText().toString().trim();
                final String Sorethroat= soreThroat.getText().toString().trim();
                final String Taste= taste.getText().toString().trim();
                final String Aches= aches.getText().toString().trim();
                final String Contact= contact.getText().toString().trim();
                java.util.Calendar calendar = Calendar.getInstance();
                SimpleDateFormat currentData = new SimpleDateFormat("dd-MM-yyyy");
                final String saveCurrentDate = currentData.format(calendar.getTime());

                java.util.Calendar calendar1 = Calendar.getInstance();
                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");

                final String saveCurrentTime = currentTime.format(calendar1.getTime());


                if(TextUtils.isEmpty(drycough)||TextUtils.isEmpty(Fever)||TextUtils.isEmpty(Fatigue)||TextUtils.isEmpty(Headache)||TextUtils.isEmpty(Sorethroat)||TextUtils.isEmpty(Taste)||TextUtils.isEmpty(Aches)||TextUtils.isEmpty(Contact)){

                    Toast.makeText(MainActivity.this,"ALL FIELDS ARE REQUIRED",Toast.LENGTH_SHORT).show();
                }
                else{
                    addtoDb(drycough,Fever,Fatigue,Headache,Sorethroat,Taste,Aches,Contact,saveCurrentDate,saveCurrentDate);
                }




            }
        });
    }

    private void addtoDb(String drycough, String fever, String fatigue, String headache, String sorethroat, String taste, String aches, String contact, String saveCurrentDate, String saveCurrentTime) {
        screening.setDrycough(drycough);
        screening.setFever(fever);
        screening.setFatigue(fatigue);
        screening.setHeadache(headache);
        screening.setSorethroat(sorethroat);
        screening.setTaste(taste);
        screening.setAches(aches);
        screening.setContact(contact);
        screening.setDate(saveCurrentDate);
        screening.setTime(saveCurrentTime);

databaseref.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        databaseref.setValue(screening);

        Toast.makeText(MainActivity.this, "data added", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(MainActivity.this,Start2Activity.class);
        startActivity(intent);

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        Toast.makeText(MainActivity.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();


    }
});

    }

    private void notifyUser(String s) {
        Toast.makeText(MainActivity.this,s,
                Toast.LENGTH_SHORT).show();
    }

}


