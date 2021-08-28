package com.example.qrcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.mcsoft.timerangepickerdialog.RangeTimePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class QRCodeGenerate extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private  UserData userData;
    private ProgressBar progressBar;
    RadioButton mStatusOptions;
    RadioGroup mStatus;
    TextInputLayout name,mobile,email;
    Button btGenerate;
    ImageView ivOutput;
    Spinner spinner1, spinner2;
    Button btnDatePicker, btnTimePicker;
    EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    List<String> subCategories = new ArrayList<>();
String status;
    private  EditText editname,editmobile,editemail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_generate);

        name = findViewById(R.id.name);
        mobile=findViewById(R.id.mobile);
        email=findViewById(R.id.email);
        btGenerate = findViewById(R.id.idBtnGenerateQR);
        ivOutput = findViewById(R.id.idIVQrcode);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        editname=findViewById(R.id.editTextName);
        editmobile=findViewById(R.id.editTextMobile);
        editemail=findViewById(R.id.editTextEmail);
        spinner1 = findViewById(R.id.building);
        spinner2 = findViewById(R.id.room);
        mStatus=findViewById(R.id.rg_status);
        mStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mStatusOptions=mStatus.findViewById(checkedId);
                switch (checkedId){
                    case R.id.rb_status_yes:
                        status=mStatusOptions.getText().toString();
                        break;
                    case R.id.rb_status_no:
                        status=mStatusOptions.getText().toString();
                        break;
                    default:
                }

            }
        });

        List<String> categories =  new ArrayList<>();
        categories.add("MSB");
        categories.add("STMB");
        categories.add("Phase-1");
        btnDatePicker=(Button)findViewById(R.id.btn_date);
        btnTimePicker=(Button)findViewById(R.id.btn_time);
        txtDate=(EditText)findViewById(R.id.in_date);
        txtTime=(EditText)findViewById(R.id.in_time);

        ArrayAdapter<String> adapter_1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter_1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter_1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                if(adapterView.getItemAtPosition(position).equals("MSB")){
                    subCategories.clear();
                    subCategories.add("1"); subCategories.add("2"); subCategories.add("3");
                    subCategories.add("4"); subCategories.add("5"); subCategories.add("6");
                    subCategories.add("7"); subCategories.add("8"); subCategories.add("9");
                    subCategories.add("10"); subCategories.add("11"); subCategories.add("12");
                    subCategories.add("13"); subCategories.add("14"); subCategories.add("15");
                    fillSpinner();
                }
                else if (adapterView.getItemAtPosition(position).equals("STMB")){
                    subCategories.clear();
                    subCategories.add("F1-01"); subCategories.add("F1-02"); subCategories.add("F1-03");
                    subCategories.add("F2-01"); subCategories.add("F2-02"); subCategories.add("F2-03");
                    subCategories.add("F2-04"); subCategories.add("F2-05"); subCategories.add("F3-01");
                    subCategories.add("F3-02"); subCategories.add("F3-03"); subCategories.add("F3-04");
                    subCategories.add("F3-05"); subCategories.add("F4-01"); subCategories.add("F4-02");
                    subCategories.add("F4-03"); subCategories.add("F4-05"); subCategories.add("STMB 5TH FLOOR");
                    fillSpinner();


                }
                else  if(adapterView.getItemAtPosition(position).equals("Phase-1")){
                    subCategories.clear();
                    subCategories.add(" RM-01"); subCategories.add("lONGONOT LAB");
                    subCategories.add("RM-01"); subCategories.add("MENENGAI");
                    subCategories.add("RM-04"); subCategories.add("KINDARUMA");
                    subCategories.add("RM-02"); subCategories.add("JASIRI STAFFROOM");
                    subCategories.add("RM-05");  subCategories.add("F4-02");
                    subCategories.add("RM-03");
                    fillSpinner();

                }

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Spinner spinner = findViewById(R.id.course);
        reference= FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());



        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userData = snapshot.getValue(UserData.class);
                assert userData != null;

                name.getEditText().setText(userData.getFullname());
                email.getEditText().setText(userData.getEmail());
                mobile.getEditText().setText(userData.getPhonenumber());




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                java.util.Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                final String saveCurrentDate = sdf.format(calendar.getTime());

                java.util.Calendar calendar1 = Calendar.getInstance();
                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
                final String saveCurrentTime = currentTime.format(calendar1.getTime());
                JSONObject userdetails=new JSONObject();
                try {
                    userdetails.put("Name",editname.getText());
                    userdetails.put("Email",editemail.getText());
                    userdetails.put("Mobile",editmobile.getText());
                    userdetails.put("Course",spinner.getSelectedItem());
                    userdetails.put("Building",spinner1.getSelectedItem());
                    userdetails.put("Room",spinner2.getSelectedItem());
                    userdetails.put("Date",txtDate.getText());
                    userdetails.put("Time",txtTime.getText());
                    userdetails.put("Status",mStatusOptions.getText());
                    userdetails.put("Timestamp",saveCurrentTime);
                    JSONObject userObject = new JSONObject();
                    userObject.put("user", userdetails);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String line1 = "Name: " + editname.getText();
                String line2 = "Email: "  + editemail.getText();
                String line3="Mobile:"+editmobile.getText();

                String sText = line1 + "\n" + line2+"\n" + line3;
                QRCodeWriter qrCodeWriter = new QRCodeWriter();
                try {
                    BitMatrix bitMatrix = qrCodeWriter.encode(String.valueOf(userdetails), BarcodeFormat.QR_CODE, 200, 200);
                    Bitmap bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.RGB_565);
                    for (int x = 0; x<200; x++){
                        for (int y=0; y<200; y++){
                            bitmap.setPixel(x,y,bitMatrix.get(x,y)? Color.BLACK : Color.WHITE);
                        }
                    }
                    ivOutput.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }




            }
        });
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.courses, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(QRCodeGenerate.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();

            }
        });
        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(QRCodeGenerate.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                txtTime.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

    }


    private void fillSpinner() {


            ArrayAdapter<String> adapter_2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subCategories);
            adapter_2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter_2);

    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();

        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}

