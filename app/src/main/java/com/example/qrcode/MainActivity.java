package com.example.qrcode;

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

import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {



//    private EditText course;
//    private EditText emailAddress;
//    private EditText phoneNumber;
    private EditText dryCough;
    private EditText fever;
    private EditText fatigue;
    private EditText headache;
    private EditText soreThroat;
    private EditText taste;
    private EditText aches;
    private EditText contact;



    private Button submitBTN;



    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference().child("Screening Details");




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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




        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(dryCough.getText().toString())){
                    Toast.makeText(MainActivity.this, "Kindly Enter Data Here", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MainActivity.this, dryCough.getText().toString(),Toast.LENGTH_SHORT).show();
                }
                if(TextUtils.isEmpty(fever.getText().toString())){
                    Toast.makeText(MainActivity.this, "Kindly Enter Data Here", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MainActivity.this, fever.getText().toString(),Toast.LENGTH_SHORT).show();
                }
                if(TextUtils.isEmpty(fatigue.getText().toString())){
                    Toast.makeText(MainActivity.this, "Kindly Enter Data Here", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MainActivity.this, fatigue.getText().toString(),Toast.LENGTH_SHORT).show();
                }
                if(TextUtils.isEmpty(headache.getText().toString())){
                    Toast.makeText(MainActivity.this, "Kindly Enter Data Here", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MainActivity.this, headache.getText().toString(),Toast.LENGTH_SHORT).show();
                }
                if(TextUtils.isEmpty(soreThroat.getText().toString())){
                    Toast.makeText(MainActivity.this, "Kindly Enter Data Here", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MainActivity.this, soreThroat.getText().toString(),Toast.LENGTH_SHORT).show();
                }
                if(TextUtils.isEmpty(taste.getText().toString())){
                    Toast.makeText(MainActivity.this, "Kindly Enter Data Here", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MainActivity.this, taste.getText().toString(),Toast.LENGTH_SHORT).show();
                }
                if(TextUtils.isEmpty(aches.getText().toString())){
                    Toast.makeText(MainActivity.this, "Kindly Enter Data Here", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MainActivity.this, aches.getText().toString(),Toast.LENGTH_SHORT).show();
                }
                if(TextUtils.isEmpty(contact.getText().toString())){
                    Toast.makeText(MainActivity.this, "Kindly Enter Data Here", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MainActivity.this, contact.getText().toString(),Toast.LENGTH_SHORT).show();
                }





                String dryCough1= dryCough.getText().toString();
                String fever1 = fever.getText().toString();
                String fatigue1 = fatigue.getText().toString();
                String headache1 = headache.getText().toString();
                String soreThroat1 = soreThroat.getText().toString();
                String taste1 = taste.getText().toString();
                String aches1 = aches.getText().toString();
                String contact1 = contact.getText().toString();




                HashMap<String, String> userMap = new HashMap<>();


                userMap.put("Dry Cough", dryCough1);
                userMap.put("Fever", fever1);
                userMap.put("Fatigue", fatigue1);
                userMap.put("Headache", headache1);
                userMap.put("SoreThroat", soreThroat1);
                userMap.put("Loss of Taste/Smell", taste1);
                userMap.put("Body Aches", aches1);
                userMap.put("Contact with confirmed case", contact1);

                //generate a unique key for each data using push method
                databaseReference.push().setValue(userMap);
                Toast.makeText(MainActivity.this, "Data inserted", Toast.LENGTH_SHORT).show();


            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(),text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}


