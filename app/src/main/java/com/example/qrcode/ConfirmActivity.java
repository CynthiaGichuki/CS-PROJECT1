package com.example.qrcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class ConfirmActivity extends AppCompatActivity {
    private EditText name,mobile,email,temp;
    Button confirm;
    ProgressBar progressBar;

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
        name=findViewById(R.id.editTextName);
        mobile=findViewById(R.id.editTextMobile);
        email=findViewById(R.id.editTextEmail);
        confirm=findViewById(R.id.cirConfirmd);
        temp=findViewById(R.id.editTextTemp);
        progressBar=findViewById(R.id.progressbar);


        Intent intent = getIntent();

        String Name = intent.getStringExtra("name");
        String Email = intent.getStringExtra("email");
        String Mobile=intent.getStringExtra("mobile");


        name.setText(Name);
        mobile.setText(Mobile);
        email.setText(Email);


        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference("scanning");

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
        String tempa=temp.getText().toString().trim();
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
            adddatatodb(FName,EmailA,Mobileno,tempa,saveCurrentDate,saveCurrentTime);
        }
    }

    private void adddatatodb(String fName, String emailA, String mobileno,String tempa, String saveCurrentDate, String saveCurrentTime) {
        operatordetails.setName(fName);
        operatordetails.setEmail(emailA);
        operatordetails.setMobile(mobileno);
        operatordetails.setTemp(tempa);
        operatordetails.setDate(saveCurrentDate);
        operatordetails.setTime(saveCurrentTime);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.setValue(operatordetails);
               // Toast.makeText(ConfirmActivity.this, "data added", Toast.LENGTH_SHORT).show();
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