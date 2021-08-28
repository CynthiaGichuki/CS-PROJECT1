package com.example.qrcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

  private EditText fullname,email,mobile,password;
  private Button registerbtn;
  private FirebaseAuth firebaseAuth;
  private DatabaseReference databaseReference;
  private ProgressBar progressBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);
    changeStatusBarColor();
    firebaseAuth=FirebaseAuth.getInstance();
    progressBar=findViewById(R.id.progressbar);
    fullname=findViewById(R.id.editTextName);
    email=findViewById(R.id.editTextEmail);
    mobile=findViewById(R.id.editTextMobile);
    password=findViewById(R.id.editTextPassword);
    registerbtn=findViewById(R.id.cirRegisterButton);

    Spinner spinner = findViewById(R.id.course);


    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
            R.array.courses, android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(adapter);
    spinner.setOnItemSelectedListener(this);
    registerbtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        final String fname= fullname.getText().toString();
        final String emailaddress= email.getText().toString();
        final String phnnumber= mobile.getText().toString();
        final String pass_word= password.getText().toString();
        final String strcourse=spinner.getSelectedItem().toString();
        final  String user_type="0";
        if(TextUtils.isEmpty(fname)||TextUtils.isEmpty(emailaddress)||TextUtils.isEmpty(phnnumber)||TextUtils.isEmpty(pass_word)){
          Toast.makeText(RegisterActivity.this,"ALL FIELDS ARE REQUIRED",Toast.LENGTH_SHORT).show();
        }
        else {
          register(fname,emailaddress,strcourse,phnnumber,pass_word,user_type);
        }
      }
    });

  }
  public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    String text = parent.getItemAtPosition(position).toString();
    Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
  }
  @Override
  public void onNothingSelected(AdapterView<?> parent) {

  }

  private void changeStatusBarColor() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      Window window = getWindow();
      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
      window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
    }
  }

  private void register(String fname, String emailaddress, String strcourse, String phnnumber, String pass_word,String user_type) {
    progressBar.setVisibility(View.VISIBLE);
    firebaseAuth.createUserWithEmailAndPassword(emailaddress,pass_word).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
      @Override
      public void onComplete(@NonNull  Task<AuthResult> task) {
        if (task.isSuccessful()){
          FirebaseUser ruser= firebaseAuth.getCurrentUser();
          String userId= ruser.getUid();
          databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
          HashMap<String,String> hashMap= new HashMap<>();
          hashMap.put("userId",userId);
          hashMap.put("fullname",fname);
          hashMap.put("course",strcourse);
          hashMap.put("email",emailaddress);
          hashMap.put("phonenumber",phnnumber);
          hashMap.put("usertype", user_type);
          hashMap.put("imgUrl","default");
          databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull  Task<Void> task) {
              if(task.isSuccessful()){
                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
              }
              else {
                Toast.makeText(RegisterActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
              }
            }
          });

        }else {
          Toast.makeText(RegisterActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
        }
      }
    });
  }

  public void onLoginClick(View view) {
    startActivity(new Intent(this,LoginActivity.class));
    overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);


  }
}