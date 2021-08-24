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

import org.json.JSONException;
import org.json.JSONObject;


public class QRCodeGenerate extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private  UserData userData;
    private ProgressBar progressBar;
    TextInputLayout name,mobile,email;
    Button btGenerate;
    ImageView ivOutput;
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

