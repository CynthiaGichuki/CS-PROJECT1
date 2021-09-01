package com.example.qrcode;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    //gender radio button
    RadioButton mFeverOptions;
    RadioGroup mFever;
    RadioButton mDryCoughOptions;
    RadioGroup mDryCough;
    RadioButton mFatigueOptions;
    RadioGroup mFatigue;
    RadioButton mHeadacheOptions;
    RadioGroup mHeadache;
    RadioButton mSoreThroatOptions;
    RadioGroup mSoreThroat;
    RadioButton mTasteOptions;
    RadioGroup mTaste;
    RadioButton mAchesOptions;
    RadioGroup mAches;
    RadioButton mContactOptions;
    RadioGroup mContact;




    private Button submitBTN;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth mAuth;
   private FirebaseDatabase firebaseDatabase;
   Screening screening;
    private DatabaseReference databaseref ;
    //get the id of the current user
    private DatabaseReference mDatabaseUsers;

String strdrycough,strfever,strfatigue,strheadache,strsorethroat,strtaste,straches,strcontact;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
     mDryCough=findViewById(R.id.rg_drycough);
     mFever=findViewById(R.id.rg_fever);
     mFatigue=findViewById(R.id.rg_fatigue);
     mHeadache=findViewById(R.id.rg_headache);
     mSoreThroat=findViewById(R.id.rg_sorethroat);
     mTaste=findViewById(R.id.rg_taste);
     mAches=findViewById(R.id.rg_aches);
     mContact=findViewById(R.id.rg_contact);

        submitBTN = findViewById(R.id.submitBTN);
        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        databaseref=FirebaseDatabase.getInstance().getReference().child("screening");
        mDatabaseUsers=FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
        screening=new Screening();

     mDryCough.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        mDryCoughOptions=mDryCough.findViewById(checkedId);
        switch (checkedId){
            case R.id.rb_drycough_yes:
                strdrycough=mDryCoughOptions.getText().toString();
                break;
            case R.id.rb_drycough_no:
                strdrycough=mDryCoughOptions.getText().toString();
                break;
            default:
        }

    }
});

        mFever.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mFeverOptions=mFever.findViewById(checkedId);
                switch (checkedId){
                    case R.id.rb_fever_yes:
                        strfever=mFeverOptions.getText().toString();
                        break;
                    case R.id.rb_fever_no:
                        strfever=mFeverOptions.getText().toString();
                        break;
                    default:
                }

            }
        });
        mFatigue.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mFatigueOptions=mFatigue.findViewById(checkedId);
                switch (checkedId){
                    case R.id.rb_fatigue_yes:
                        strfatigue=mFeverOptions.getText().toString();
                        break;
                    case R.id.rb_fatigue_no:
                        strfatigue=mFeverOptions.getText().toString();
                        break;
                    default:
                }

            }
        });
        mHeadache.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mHeadacheOptions=mHeadache.findViewById(checkedId);
                switch (checkedId){
                    case R.id.rb_headache_yes:
                        strheadache=mHeadacheOptions.getText().toString();
                        break;
                    case R.id.rb_headache_no:
                        strheadache=mHeadacheOptions.getText().toString();
                        break;
                    default:
                }

            }
        });
        mSoreThroat.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mSoreThroatOptions=mSoreThroat.findViewById(checkedId);
                switch (checkedId){
                    case R.id.rb_sorethroat_yes:
                        strsorethroat=mSoreThroatOptions.getText().toString();
                        break;
                    case R.id.rb_sorethroat_no:
                        strsorethroat=mSoreThroatOptions.getText().toString();
                        break;
                    default:
                }

            }
        });
        mAches.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mAchesOptions=mAches.findViewById(checkedId);
                switch (checkedId){
                    case R.id.rb_aches_yes:
                        straches=mAchesOptions.getText().toString();
                        break;
                    case R.id.rb_aches_no:
                        straches=mAchesOptions.getText().toString();
                        break;
                    default:
                }

            }
        });
        mTaste.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mTasteOptions=mTaste.findViewById(checkedId);
                switch (checkedId){
                    case R.id.rb_taste_yes:
                        strtaste=mTasteOptions.getText().toString();
                        break;
                    case R.id.rb_taste_no:
                        strtaste=mTasteOptions.getText().toString();
                        break;
                    default:
                }

            }
        });
        mFatigue.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mFatigueOptions=mFatigue.findViewById(checkedId);
                switch (checkedId){
                    case R.id.rb_fatigue_yes:
                        strfatigue=mFatigueOptions.getText().toString();
                        break;
                    case R.id.rb_fatigue_no:
                        strfatigue=mFatigueOptions.getText().toString();
                        break;
                    default:
                }

            }
        });
        mContact.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mContactOptions=mContact.findViewById(checkedId);
                switch (checkedId){
                    case R.id.rb_contact_yes:
                        strcontact=mContactOptions.getText().toString();
                        break;
                    case R.id.rb_contact_no:
                        strcontact=mContactOptions.getText().toString();
                        break;
                    default:
                }

            }
        });


        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

Toast.makeText(MainActivity.this,"posting ....",Toast.LENGTH_LONG).show();

                 String drycough= mDryCoughOptions.getText().toString();
                final String Fever= mFeverOptions.getText().toString().trim();
                final String Fatigue= mFatigueOptions.getText().toString().trim();
                final String Headache= mHeadacheOptions.getText().toString().trim();
                final String Sorethroat= mSoreThroatOptions.getText().toString().trim();
                final String Taste= mTasteOptions.getText().toString().trim();
                final String Aches= mAchesOptions.getText().toString().trim();
                final String Contact= mContactOptions.getText().toString().trim();
                java.util.Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                final String saveCurrentDate = sdf.format(calendar.getTime());

                java.util.Calendar calendar1 = Calendar.getInstance();
                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");

                final String saveCurrentTime = currentTime.format(calendar1.getTime());
String userId=mCurrentUser.getUid();

                if(TextUtils.isEmpty(drycough)||TextUtils.isEmpty(Fever)||TextUtils.isEmpty(Fatigue)||TextUtils.isEmpty(Headache)||TextUtils.isEmpty(Sorethroat)||TextUtils.isEmpty(Taste)||TextUtils.isEmpty(Aches)||TextUtils.isEmpty(Contact)){

                    Toast.makeText(MainActivity.this,"ALL FIELDS ARE REQUIRED",Toast.LENGTH_SHORT).show();
                }
                else{
                    addtoDb(drycough,Fever,Fatigue,Headache,Sorethroat,Taste,Aches,Contact,saveCurrentDate,saveCurrentDate,userId);
                }




            }
        });
    }

    private void addtoDb(String drycough, String fever, String fatigue, String headache, String sorethroat, String taste, String aches, String contact, String saveCurrentDate, String saveCurrentTime,String userId) {
        screening.setDrycough(drycough);
        screening.setFever(fever);
        screening.setFatigue(fatigue);
        screening.setHeadache(headache);
        screening.setSorethroat(sorethroat);
        screening.setTaste(taste);
        screening.setAches(aches);
        screening.setContact(contact);
        screening.setDate(saveCurrentDate);
        screening.setTime(saveCurrentTime);
        screening.setUserid(userId);
final DatabaseReference newpost=databaseref.push();

mDatabaseUsers.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        newpost.setValue(screening);

        Toast.makeText(MainActivity.this, "data added", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(MainActivity.this,QRCodeGenerate.class);
        startActivity(intent);

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        Toast.makeText(MainActivity.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();


    }
});

    }

    private void notifyUser(String s) {
        Toast.makeText(MainActivity.this,s,
                Toast.LENGTH_SHORT).show();
    }

}


