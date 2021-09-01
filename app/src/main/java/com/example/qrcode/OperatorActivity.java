package com.example.qrcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class OperatorActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView name,email,mobile,course,building,room,timein,datein,status,timestamp;
    private Button scanbtn,confirmbtn;
    private IntentIntegrator qrScan;
    private ProgressBar progressBar;
    //firebase

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator);

        //View objects
        name=findViewById(R.id.TextViewTextName);
        email=findViewById(R.id.TextViewEmail);
        mobile=findViewById(R.id.TextViewMobile);
        course=findViewById(R.id.TextViewCourse);
        room=findViewById(R.id.TextViewRoom);
        timein=findViewById(R.id.TextViewTime);
        datein=findViewById(R.id.TextViewDate);
        status=findViewById(R.id.TextViewStatus);
        timestamp=findViewById(R.id.TextViewtimestamp);
        building=findViewById(R.id.TextViewBuilding);
        scanbtn=findViewById(R.id.cirScan);

        confirmbtn=findViewById(R.id.cirConfirmd);


        qrScan = new IntentIntegrator(this);

        //attaching onclick listener
        scanbtn.setOnClickListener(this);
     //   confirmbtn.setOnClickListener(this);

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
scanbtn.setVisibility(View.VISIBLE);
//confirmbtn.setVisibility(View.INVISIBLE);
//String fname=name.getText().toString();
//                String Mobile=mobile.getText().toString();
//                String Email=email.getText().toString();


                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());
                    //setting values to textviews
                    name.setText(obj.getString("Name"));
                    email.setText(obj.getString("Email"));
                    mobile.setText(obj.getString("Mobile"));
                    course.setText(obj.getString("Course"));
                    timein.setText(obj.getString("Time"));
                    datein.setText(obj.getString("Date"));
                    status.setText(obj.getString("Status"));
                    timestamp.setText(obj.getString("Timestamp"));
                    building.setText(obj.getString("Building"));
                    room.setText(obj.getString("Room"));



                    String fname=obj.get("Name").toString();
                    String Email=obj.get("Email").toString();
                    String Mobile=obj.get("Mobile").toString();
                   String Course=obj.get("Course").toString();
                   String Status=obj.get("Status").toString();
                   String Building=obj.get("Building").toString();
                   String Room=obj.get("Room").toString();
                  String Date=obj.get("Date").toString();
                     String Time=obj.get("Time").toString();
                    String Timestamp=obj.get("Timestamp").toString();


                    Intent intent=new Intent(OperatorActivity.this,ConfirmActivity.class);
                    intent.putExtra("name",fname);
                    intent.putExtra("email",Email);
                    intent.putExtra("mobile",Mobile);
                    intent.putExtra("course",Course);
                    intent.putExtra("status",Status);
                    intent.putExtra("building",Building);
                    intent.putExtra("room",Room);
                    intent.putExtra("date",Date);
                    intent.putExtra("time",Time);
                   intent.putExtra("timestamp",Timestamp);
                    startActivity(intent);


                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();


                }


            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);

        }
    }

    private void check() {
        if(TextUtils.isEmpty(name.getText().toString())){
            Toast.makeText(this,"scan again",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(mobile.getText().toString())){
            Toast.makeText(this,"scan again",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(email.getText().toString())){
            Toast.makeText(this,"scan again",Toast.LENGTH_SHORT).show();
        }
        else {
            confirm();
        }

    }

    private void confirm() {

        java.util.Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String saveCurrentDate = sdf.format(calendar.getTime());

        java.util.Calendar calendar1 = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        final String saveCurrentTime = currentTime.format(calendar1.getTime());

databaseReference=FirebaseDatabase.getInstance().getReference().child("confrim");
        HashMap<String,Object>confirmMap=new HashMap<>();
        confirmMap.put("fullname",name.getText().toString());
        confirmMap.put("email",email.getText().toString());
        confirmMap.put("phonenumber",mobile.getText().toString());
        confirmMap.put("Date",saveCurrentDate);
        confirmMap.put("time",saveCurrentTime);
        databaseReference.updateChildren(confirmMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    Toast.makeText(OperatorActivity.this,"User has been confirmed",Toast.LENGTH_SHORT).show();
                    name.setText("");
                    email.setText("");
                    mobile.setText("");
                    scanbtn.setVisibility(View.VISIBLE);
                    confirmbtn.setVisibility(View.INVISIBLE);
                }
            }
        });



    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.cirScan:
                // Test code: text.setText("Button 1");
                qrScan.initiateScan();
                break;
            case R.id.cirConfirmd:
                // Test code: text.setText("Button 2");
                check();
                break;
            //...... continue for button3 - button 8
            default:
                Log.d(getApplication().getPackageName(), "Button click error!");
                break;
        }
    }



}