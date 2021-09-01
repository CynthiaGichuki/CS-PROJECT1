package com.example.qrcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
  private EditText email,password;
  private TextView forgetpassword;
  private FirebaseAuth firebaseAuth;
  private Button loginbtn;
  private ProgressBar progressBar;
  public static final String Email = "emailKey";
  public static final String MyPREFERENCES = "MyPrefs" ;

  SharedPreferences sharedpreferences;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
      getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }
    setContentView(R.layout.activity_login);
    progressBar=findViewById(R.id.progressbar);
    final Button loginbtn=this.findViewById(R.id.login);
    email=findViewById(R.id.editTextEmail);
    password=findViewById(R.id.editTextPassword);
    forgetpassword = findViewById(R.id.forget);
    firebaseAuth = FirebaseAuth.getInstance();
    sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


    loginbtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String tex_email = email.getText().toString();
        String tex_password = password.getText().toString();
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Email, tex_email);
        editor.commit();

        if (TextUtils.isEmpty(tex_email) || TextUtils.isEmpty(tex_password)){
          Toast.makeText(LoginActivity.this, "All Fields Required", Toast.LENGTH_SHORT).show();
        }
        else{

          login(tex_email,tex_password);
        }
      }
    });
    forgetpassword.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(LoginActivity.this,ForgetPasswordActivity.class ));
      }
    });
  }

  private void login(String tex_email, String tex_password) {

    progressBar.setVisibility(View.VISIBLE);
    firebaseAuth.signInWithEmailAndPassword(tex_email,tex_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
      @Override
      public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()){

          String uid=task.getResult().getUser().getUid();
          FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
          firebaseDatabase.getReference().child("Users").child(uid).child("usertype").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
              String usertype=snapshot.getValue(String.class);
              if(usertype.equals("0")){


                Intent intent=new Intent(LoginActivity.this,Start2Activity.class);
                startActivity(intent);
                Toast.makeText(LoginActivity.this,"welcome",Toast.LENGTH_SHORT).show();
              }
              if(usertype.equals("1")){
                Intent intent=new Intent(LoginActivity.this,OperatorActivity.class);
                startActivity(intent);
                Toast.makeText(LoginActivity.this,"operator side",Toast.LENGTH_SHORT).show();
              }
//              Intent intent = new Intent(LoginActivity.this,Start2Activity.class);
//              intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//              startActivity(intent);
//              finish();
//              Toast.makeText(LoginActivity.this,"STUDENT SIDE IS DONE",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

          });
//          Intent intent = new Intent(LoginActivity.this,Start2Activity.class);
//          intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//          startActivity(intent);
//          finish();
//          Toast.makeText(LoginActivity.this,"STUDENT SIDE IS DONE",Toast.LENGTH_SHORT).show();





        }else{
          Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
        }
      }
    });
  }

  public void onLoginClick(View view) {
    startActivity(new Intent(this,RegisterActivity.class));
    overridePendingTransition(R.anim.slide_in_right,R.anim.stay);

  }
}