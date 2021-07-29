package com.example.qrcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {
    private FirebaseAuth mAuth;
    TextInputLayout fullname,emailaddress,phonenumber;
    private DatabaseReference reference;
    private  UserData userData;
    private ProgressBar progressBar;
String _fname,_email,_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String name=user.getUid();
        TextView welcome=findViewById(R.id.editprofile);
        welcome.setText("Edit Profile:"+name);
        //hooks
        fullname=findViewById(R.id.textInputName);
        emailaddress=findViewById(R.id.textInputEmail);
        phonenumber=findViewById(R.id.textInputMobile);
       reference= FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
       reference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               userData = snapshot.getValue(UserData.class);
               assert userData != null;
              fullname.getEditText().setText(userData.getFullname());
              emailaddress.getEditText().setText(userData.getEmail());
              phonenumber.getEditText().setText(userData.getPhonenumber());
           }

           @Override
           public void onCancelled(@NonNull  DatabaseError error) {
               Toast.makeText(EditProfile.this,error.getMessage(),Toast.LENGTH_SHORT).show();
           }
       });






    }

    public void Update(View view) {
if(isNameChanged()){
    Toast.makeText(this,"Successfully changed",Toast.LENGTH_SHORT).show();
}
else{
    Toast.makeText(this,"Data has not been changed",Toast.LENGTH_SHORT).show();
}
    }

    private boolean isNameChanged() {
        if(userData.getFullname().equals(fullname.getEditText().getText().toString())){
            reference.child(_fname).child(userData.getFullname()).setValue(fullname.getEditText().getText().toString());
            _fname=fullname.getEditText().getText().toString();
            return true;
        }
        else
            return false;
    }
}