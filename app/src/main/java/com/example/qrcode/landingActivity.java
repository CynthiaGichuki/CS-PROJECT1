package com.example.qrcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class landingActivity extends AppCompatActivity {
    private ImageButton studentbtn;
    private ImageButton operatorbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        studentbtn=findViewById(R.id.studentlogin);
        operatorbtn=findViewById(R.id.scanner);
        studentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent studentlogin=new Intent(landingActivity.this,LoginActivity.class);
                startActivity(studentlogin);

            }
        });
        operatorbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent scanner=new Intent(landingActivity.this, OperatorActivity.class);
                startActivity(scanner);
            }
        });

    }
}