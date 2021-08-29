package com.example.qrcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private EditText name,mobile,email,course,building,status,date,time,room,timestamp;
    Spinner temp;
    Button confirm;
    ProgressBar progressBar;
    long maxid;

    //FirebaseDatabase databaseReference;
   // private FirebaseAuth mAuth;
   // private DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference;

    // creating a variable for
    // our object class
    Operatordetails operatordetails;
    String temperature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        status=findViewById(R.id.status);
        room=findViewById(R.id.room);
        date=findViewById(R.id.date);
        time=findViewById(R.id.time);
        building=findViewById(R.id.building);
        course=findViewById(R.id.course);
        timestamp=findViewById(R.id.timestamp);
        name=findViewById(R.id.editTextName);
        mobile=findViewById(R.id.editTextMobile);
        status=findViewById(R.id.status);
        email=findViewById(R.id.editTextEmail);
        confirm=findViewById(R.id.cirConfirmd);
       // temp=findViewById(R.id.editTextTemp);
        progressBar=findViewById(R.id.progressbar);
        temp = findViewById(R.id.SpinnerTemp);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.temp, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        temp.setAdapter(adapter);
        temp.setOnItemSelectedListener(this);

        Intent intent = getIntent();

        String Name = intent.getStringExtra("name");
        String Email = intent.getStringExtra("email");
        String Mobile=intent.getStringExtra("mobile");
        String Date=intent.getStringExtra("date");
        String Time=intent.getStringExtra("time");
        String Building=intent.getStringExtra("building");
        String Room=intent.getStringExtra("room");
        String Course=intent.getStringExtra("course");
        String Status=intent.getStringExtra("status");
        String TimeStamp=intent.getStringExtra("timestamp");




        name.setText(Name);
        mobile.setText(Mobile);
        email.setText(Email);
        course.setText(Course);
        time.setText(Time);
        building.setText(Building);
        room.setText(Room);
        date.setText(Date);
        status.setText(Status);
timestamp.setText(TimeStamp);




        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference("scanning").child(String.valueOf(maxid+1));

        // initializing our object
        // class variable.
      operatordetails=new Operatordetails();
        confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               progressBar.setVisibility(View.VISIBLE);
                check();
            }
        });



    }

    private void check() {
        String FName=name.getText().toString();
        String EmailA=email.getText().toString();
        String Mobileno=mobile.getText().toString();
        String tempa=temp.getSelectedItem().toString();
        String coursea=course.getText().toString();
        String rooma=room.getText().toString();
        String buildinga=building.getText().toString();
        String statusa=status.getText().toString();
        String datea=date.getText().toString();
        String timea=time.getText().toString();

        java.util.Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String saveCurrentDate = sdf.format(calendar.getTime());

        java.util.Calendar calendar1 = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        final String saveCurrentTime = currentTime.format(calendar1.getTime());




        if(TextUtils.isEmpty(name.getText().toString())){
            Intent intent=new Intent(ConfirmActivity.this,OperatorActivity.class);
            Toast.makeText(this,"scan again for name",Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }
        if(TextUtils.isEmpty(mobile.getText().toString())){
            Intent intent=new Intent(ConfirmActivity.this,OperatorActivity.class);
            Toast.makeText(this,"scan again for mobile",Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }
        if(TextUtils.isEmpty(email.getText().toString())){
            Intent intent=new Intent(ConfirmActivity.this,OperatorActivity.class);
            Toast.makeText(this,"scan again for email",Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }
        else {
            adddatatodb(FName,EmailA,Mobileno,tempa,rooma,buildinga,statusa,datea,timea,saveCurrentDate,saveCurrentTime);
        }
    }

    private void adddatatodb(String fName, String emailA, String mobileno, String tempa, String rooma, String buildinga, String statusa, String datea, String timea, String saveCurrentDate, String saveCurrentTime) {

        operatordetails.setName(fName);
        operatordetails.setEmail(emailA);
        operatordetails.setMobile(mobileno);
        operatordetails.setTemp(tempa);
        operatordetails.setRoom(rooma);
        operatordetails.setBuilding(buildinga);
        operatordetails.setStatus(statusa);
        operatordetails.setClassDate(datea);
        operatordetails.setClasstime(timea);
        operatordetails.setDate(saveCurrentDate);
        operatordetails.setTime(saveCurrentTime);
        //String dateid=operatordetails.getDate();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                maxid=(snapshot.getChildrenCount());
                //databaseReference.child(String.valueOf(maxid+1)).setValue(operatordetails);
                databaseReference.setValue(operatordetails);
               // Toast.mak eText(ConfirmActivity.this, "data added", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(ConfirmActivity.this,OperatorActivity.class);
                Toast.makeText(ConfirmActivity.this,"User has been confirmed",Toast.LENGTH_SHORT).show();
                startActivity(intent);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ConfirmActivity.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();


            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

//    private void adddatatodb(String fName, String emailA, String mobileno,String tempa, String datea,String timea, String building,String rooma, String statusa,String saveCurrentDate, String saveCurrentTime) {
//        operatordetails.setName(fName);
//        operatordetails.setEmail(emailA);
//        operatordetails.setMobile(mobileno);
//        operatordetails.setTemp(tempa);
//        operatordetails.setClassDate(datea);
//        operatordetails.setClasstime(timea);
//        operatordetails.setBuilding(building);
//        operatordetails.setRoom(rooma);
//        operatordetails.setStatus(statusa);
//        //operatordetails.setTime(timea);
//        operatordetails.setDate(saveCurrentDate);
//        operatordetails.setTime(saveCurrentTime);
//
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                databaseReference.setValue(operatordetails);
//               // Toast.makeText(ConfirmActivity.this, "data added", Toast.LENGTH_SHORT).show();
//                Intent intent=new Intent(ConfirmActivity.this,OperatorActivity.class);
//                Toast.makeText(ConfirmActivity.this,"User has been confirmed",Toast.LENGTH_SHORT).show();
//                startActivity(intent);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(ConfirmActivity.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
//
//
//            }
//        });
//
//    }

//    private void confirmdetails() {
//        Toast.makeText(ConfirmActivity.this,"wait a moment",Toast.LENGTH_SHORT).show();
//
//
//
//        databaseReference= FirebaseDatabase.getInstance().getReference().child("confirm");
//        HashMap<String,Object> confirmMap=new HashMap<>();
//        confirmMap.put("fullname",name.getText().toString());
//        confirmMap.put("email",email.getText().toString());
//        confirmMap.put("phonenumber",mobile.getText().toString());
//        confirmMap.put("Date",saveCurrentDate);
//        confirmMap.put("time",saveCurrentTime);
//        databaseReference.updateChildren(confirmMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if(task.isSuccessful()){
//Intent intent=new Intent(ConfirmActivity.this,OperatorActivity.class);
//                    Toast.makeText(ConfirmActivity.this,"User has been confirmed",Toast.LENGTH_SHORT).show();
//                    startActivity(intent);
//
//                }
//            }
//        });
//    }
}