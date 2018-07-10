package com.example.kon_boot.tour;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class Login extends AppCompatActivity {
      EditText edtname,edtphn,edtemail;
      Button btnsubmit;
      ImageView propic;
      DatabaseReference databaseReference;
      StorageReference storageReference;
    private StorageTask mUpload;
    private static  final int PICK_IMAGe_REQUEST=1;
    Uri muri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtname = findViewById(R.id.name);
        edtphn = findViewById(R.id.Phn);
        edtemail =findViewById(R.id.Email);
        propic = findViewById(R.id.Login);
        btnsubmit =findViewById(R.id.submit);
        storageReference = FirebaseStorage.getInstance().getReference("profile");
        databaseReference= FirebaseDatabase.getInstance().getReference("profile");


        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUpload!=null && mUpload.isInProgress())
                {
                    Toast.makeText(Login.this,"Upload in progress",Toast.LENGTH_SHORT).show();
                }
                else{
                    uploadfile();
                    Intent intent = new Intent(Login.this,MainActivity.class);
                    intent.putExtra("phnno",edtphn.getText().toString());
                    startActivity(intent);


                }
            }
        });
        propic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openfilechooser();
            }
        });

    }
    private  void openfilechooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGe_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_IMAGe_REQUEST&&resultCode==RESULT_OK&& data!=null &&data.getData()!=null)
        {
            muri=data.getData();
            propic.setImageURI(muri);
        }
    }
    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }
    private void uploadfile()
    {
        if(muri!=null && edtname!=null && edtemail!=null && edtphn!=null)
        {
            StorageReference fileRef = storageReference.child(System.currentTimeMillis()+ "."+getFileExtension(muri));
            mUpload=fileRef.putFile(muri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(Login.this,"Welcome",Toast.LENGTH_SHORT).show();
                            Upload upload = new Upload(edtname.getText().toString(),edtphn.getText().toString(),edtemail.getText().toString(), Objects.requireNonNull(taskSnapshot.getMetadata()).getReference().getDownloadUrl().toString());
                            String uploadId =databaseReference.push().getKey();
                            databaseReference.child(uploadId).setValue(upload);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Login.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else{
            Toast.makeText(this,"Please fill all the details",Toast.LENGTH_SHORT).show();
        }

    }


}

