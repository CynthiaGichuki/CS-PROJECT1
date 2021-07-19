package com.example.qrcode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.UserHandle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Start2Activity extends AppCompatActivity {
    private TextView username;
    private CircleImageView circleImageView;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ImagesRecyclerAdapter imagesRecyclerAdapter;
    private List<ImagesList> imagesList;
    private  static final int IMAGE_REQUEST=1;
    private StorageTask storageTask;
    private Uri imageUri;
    private StorageReference storageReference;
    private  UserData userData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start2);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        imagesList =new ArrayList<>();
        username=findViewById(R.id.fullname);
        circleImageView=findViewById(R.id.profileimage);
        firebaseAuth=firebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        storageReference = FirebaseStorage.getInstance().getReference("profile_images");
        databaseReference.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull  DataSnapshot snapshot) {
        userData = snapshot.getValue(UserData.class);
        assert userData != null;
        username.setText(userData.getFullname());
        if (userData.getImgUrl().equals("default")) {
            circleImageView.setImageResource(R.drawable.ic_launcher_background);
        } else {
            Glide.with(getApplicationContext()).load(userData.getImgUrl()).into(circleImageView);
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        Toast.makeText(Start2Activity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
    }

});
circleImageView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder= new AlertDialog.Builder(Start2Activity.this);
        builder.setCancelable(true);

        View mView= LayoutInflater.from(Start2Activity.this).inflate(R.layout.select_image_layout,null);
        RecyclerView recyclerView=mView.findViewById(R.id.recyclerview);
        collectOldImages();
        recyclerView.setLayoutManager(new GridLayoutManager(Start2Activity.this,3));
        recyclerView.setHasFixedSize(true);
        imagesRecyclerAdapter = new ImagesRecyclerAdapter(imagesList,Start2Activity.this);
        recyclerView.setAdapter(imagesRecyclerAdapter);
        imagesRecyclerAdapter.notifyDataSetChanged();
        Button openImage= mView.findViewById(R.id.openimages);
        openImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });
        builder.setView(mView);
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
});

    }

    private void openImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMAGE_REQUEST && resultCode ==RESULT_OK && data !=null && data.getData() !=null ){
              imageUri=data.getData();
            if(storageTask !=null && storageTask.isInProgress()){
                 Toast.makeText(this,"uploading is in progress",Toast.LENGTH_SHORT).show();
             }
             else {
                 uploadeImage();
             }
        }
    }

    private void uploadeImage() {
        ProgressDialog progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("Uploading Image");
        progressDialog.show();
        if(imageUri !=null){
            Bitmap bitmap=null;
            try {
                bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
            }catch (IOException e){
                e.printStackTrace();
            }
            ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
            assert bitmap!=null;
            bitmap.compress(Bitmap.CompressFormat.JPEG,25,byteArrayOutputStream);
            byte[] imageFileToByte=byteArrayOutputStream.toByteArray();
            final StorageReference imageRef= storageReference.child(userData.getFullname()+System.currentTimeMillis()+".jpg");
            storageTask =imageRef.putBytes(imageFileToByte);
            storageTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot,Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return imageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull  Task<Uri> task) {
                    if (!task.isSuccessful()){
                      Uri downloadUri= task.getResult();
                      String sdownloadUri=downloadUri.toString();
                        Map<String,Object> hashmap =new HashMap<>();
                        databaseReference.updateChildren(hashmap);
                        final DatabaseReference profileImagesReference=FirebaseDatabase.getInstance().getReference("profile_images").child(firebaseUser.getUid());
                    profileImagesReference.push().setValue(hashmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull  Task<Void> task) {
                            if(task.isSuccessful()){
                                progressDialog.dismiss();
                            }else{
                                progressDialog.dismiss();
                                Toast.makeText(Start2Activity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                     }
                    else{
                        Toast.makeText(Start2Activity.this,"failed",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull  Exception e) {
                    Toast.makeText(Start2Activity.this,"failed",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();


                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       int id= item.getItemId();
       if(id==R.id.changepass){
           startActivity(new Intent(Start2Activity.this,ChangePasswordActivity.class));
       }else if(id==R.id.logout){
           firebaseAuth.signOut();
           startActivity(new Intent(Start2Activity.this,LoginActivity.class));
           finish();

       }
       return true;
    }

    private void collectOldImages() {
        DatabaseReference imagesListReference= FirebaseDatabase.getInstance().getReference("profile_images").child(firebaseUser.getUid());
        imagesListReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                imagesList.clear();
                for (DataSnapshot dataSnapshot :snapshot.getChildren()){
                    imagesList.add(dataSnapshot.getValue(ImagesList.class));

                }
                imagesRecyclerAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {
Toast.makeText(Start2Activity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}