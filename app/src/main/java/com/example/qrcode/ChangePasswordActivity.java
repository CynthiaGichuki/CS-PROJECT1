package com.example.qrcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {
private EditText oldpass,newpass,confirmpass;
private Button changepsw;
private FirebaseUser firebaseUser;
private ProgressBar progressBar;
private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        oldpass=findViewById(R.id.editTextoldPassword);
        newpass=findViewById(R.id.editTextnewPassword);
        confirmpass=findViewById(R.id.editconfirmTextPassword);
        progressBar=findViewById(R.id.progressbar);
        changepsw=findViewById(R.id.resetpassword);
        changepsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
String textoldpsw=oldpass.getText().toString();
String textnewpsw=newpass.getText().toString();
String textconfirmpsw=confirmpass.getText().toString();
if(textoldpsw.isEmpty()|| textnewpsw.isEmpty()||textconfirmpsw.isEmpty()){
    Toast.makeText(ChangePasswordActivity.this,"All fields are required",Toast.LENGTH_SHORT).show();
}else if(textoldpsw.length()<6){
    Toast.makeText(ChangePasswordActivity.this,"the new password length should be more than 6 characters",Toast.LENGTH_SHORT).show();
}else if(!textconfirmpsw.equals(textnewpsw)){
    Toast.makeText(ChangePasswordActivity.this,"Comfirm password doesnt match the new password",Toast.LENGTH_SHORT).show();
}else{
    changePassword(textoldpsw,textnewpsw);
}
            }
        });
    }

    private void changePassword(String textoldpsw, String textnewpsw) {
        progressBar.setVisibility(View.VISIBLE);
        AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(), textoldpsw);
        firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    firebaseUser.updatePassword(textnewpsw).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull  Task<Void> task) {
                            if (task.isSuccessful()) {
                                firebaseAuth.signOut();
                                Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } else {
                                Toast.makeText(ChangePasswordActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ChangePasswordActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}