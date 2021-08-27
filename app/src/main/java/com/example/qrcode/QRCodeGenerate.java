package com.example.qrcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class QRCodeGenerate extends AppCompatActivity implements AdapterView.OnItemSelectedListener, RangeTimePickerDialog.ISelectedTime {
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private  UserData userData;
    private ProgressBar progressBar;
    TextInputLayout name,mobile,email;
    Button btGenerate;
    ImageView ivOutput;
    Spinner spinner1, spinner2;
    List<String> subCategories = new ArrayList<>();

    private TimePicker timePicker1;
    int hour = timePicker1.getCurrentHour();

    private TextView time;
    private Calendar calendar;
    private String format = "";




    private  EditText editname,editmobile,editemail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_generate);
        timePicker1 = (TimePicker) findViewById(R.id.timePicker1);
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
        timePicker1 = (TimePicker) findViewById(R.id.timePicker1);
        time = (TextView) findViewById(R.id.tvtime);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        showTime(hour, min);
        calendar = Calendar.getInstance();
        List<String> categories =  new ArrayList<>();
        categories.add("MSB");
        categories.add("STMB");
        categories.add("Phase-1");

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
                JSONObject userdetails=new JSONObject();
                try {
                    userdetails.put("Name",editname.getText());
                    userdetails.put("Email",editemail.getText());
                    userdetails.put("Mobile",editmobile.getText());
                    userdetails.put("Course",spinner.getSelectedItem());
                    userdetails.put("Building",spinner1.getSelectedItem());
                    userdetails.put("Room",spinner2.getSelectedItem());
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


//                WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
//
//                // initializing a variable for default display.
//                Display display = manager.getDefaultDisplay();
//
//                // creating a variable for point which
//                // is to be displayed in QR Code.
//                Point point = new Point();
//                display.getSize(point);
//
//                // getting width and
//                // height of a point
//                int width = point.x;
//                int height = point.y;
//
//                // generating dimension from width and height.
//                int dimen = width < height ? width : height;
//                dimen = dimen * 3 / 4;
//
//                // setting this dimensions inside our qr code
//                // encoder to generate our qr code.
//                qrgEncoder = new QRGEncoder(dataEdt.getText().toString(), null, QRGContents.Type.TEXT, dimen);
//                try {
//                    // getting our qrcode in the form of bitmap.
//                    bitmap = qrgEncoder.encodeAsBitmap();
//                    // the bitmap is set inside our image
//                    // view using .setimagebitmap method.
//                    qrCodeIV.setImageBitmap(bitmap);
//                } catch (WriterException e) {
//                    // this method is called for
//                    // exception handling.
//                    Log.e("Tag", e.toString());
//                }

//                MultiFormatWriter writer = new MultiFormatWriter();
//
//                try {
//                    BitMatrix matrix = writer.encode(sText, BarcodeFormat.QR_CODE, 350, 350);
//                    //BarcodeEncoder encoder = new BarcodeEncoder();
//
//                    //Bitmap bitmap = encoder.createBitmap(matrix);
//
//                    //ivOutput.setImageBitmap(bitmap);
//                    InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//
//
//                } catch (WriterException e) {
//                    e.printStackTrace();
//                }

            }
        });
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.courses, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

    }
    public void setTime(View view) {
        int hour = timePicker1.getCurrentHour();
        int min = timePicker1.getCurrentMinute();
        showTime(hour, min);
    }
    private void showTime(int hour, int min) {
        if (hour == 0) {
            hour += 12;
            format = "AM";
        } else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }

        time.setText(new StringBuilder().append(hour).append(" : ").append(min)
                .append(" ").append(format));
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

    @Override
    public void onSelectedTime(int hourStart, int minuteStart, int hourEnd, int minuteEnd) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}

