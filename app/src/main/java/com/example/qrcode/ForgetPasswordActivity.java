package com.example.qrcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

public class ForgetPasswordActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private TextView resetstate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        mAuth = FirebaseAuth.getInstance();
        EditText email= findViewById(R.id.editTextEmail);
        resetstate=findViewById(R.id.resetText);
        Button resetpasswordbtn= findViewById(R.id.sendMessage);
        progressBar=findViewById(R.id.progressbar);

        resetpasswordbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                mAuth.fetchSignInMethodsForEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull  Task<SignInMethodQueryResult> task) {
                      if (task.getResult().getSignInMethods().isEmpty()) {
                          progressBar.setVisibility(View.GONE);
                          resetstate.setText("This is not a registered email,you may create an new account");
                      }else {
                          mAuth.sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                              @Override
                              public void onComplete(@NonNull Task<Void> task) {
                                  progressBar.setVisibility(View.GONE);
                                  if (task.isSuccessful()){
                                      resetstate.setText("A reset email has been sent to your email address");
                                  }else {
                                      resetstate.setText(task.getException().getMessage());
                                  }
                              }
                          });
                      }
                    }
                });
            }
        });
    }
}